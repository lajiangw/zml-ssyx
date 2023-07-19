package com.zml.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.mapper.ActivityInfoMapper;
import com.zml.activity.mapper.ActivityRuleMapper;
import com.zml.activity.mapper.ActivitySkuMapper;
import com.zml.activity.service.ActivityInfoService;
import com.zml.activity.service.ActivityRuleService;
import com.zml.activity.service.ActivitySkuService;
import com.zml.activity.service.CouponInfoService;
import com.zml.client.product.ProductFeignClient;
import com.zml.ssyx.enums.ActivityType;
import com.zml.ssyx.model.activity.ActivityInfo;
import com.zml.ssyx.model.activity.ActivityRule;
import com.zml.ssyx.model.activity.ActivitySku;
import com.zml.ssyx.model.activity.CouponInfo;
import com.zml.ssyx.model.base.BaseEntity;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.ActivityRuleVo;
import com.zml.ssyx.vo.order.CartInfoVo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZHANGMINLEI
 * @description 针对表【activity_info(活动表)】的数据库操作Service实现
 * @createDate 2023-07-14 10:28:34
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo>
        implements ActivityInfoService {

    @Resource
    private ActivityRuleMapper activityRuleMapper;

    @Resource
    private ActivitySkuMapper activitySkuMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ActivityRuleService activityRuleService;

    @Resource
    private ActivitySkuService activitySkuService;

    @Autowired
    private CouponInfoService couponInfoService;


    @Override
    public IPage<ActivityInfo> getPageList(Page<ActivityInfo> activityInfoPage) {
        Page<ActivityInfo> Page = baseMapper.selectPage(activityInfoPage, null);
        List<ActivityInfo> records = Page.getRecords();
        records.forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return Page;
    }

    @Override
    public Map<String, Object> findActivityRuleList(Long id) {
        Map<String, Object> map = new HashMap<>();
//        1通过活动id查询activityRule表
        LambdaQueryWrapper<ActivityRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRule::getActivityId, id);
        List<ActivityRule> activityRules = activityRuleMapper.selectList(wrapper);

//        2通过活动id查询Activity——sku表
        List<ActivitySku> activitySkus = activitySkuMapper.selectList(new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, id));
        List<Long> ids = activitySkus.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());

//      3通过skuid 查到商品信息表
        List<SkuInfo> skuInfos = productFeignClient.getSkuInfos(ids);


        map.put("activityRuleList", activityRules);
        map.put("skuInfoList", skuInfos);
        return map;
    }

    @Override
    @Transactional
    public void saveActivityRule(ActivityRuleVo vo) {
//     1  根据活动id删除之前的规则 涉及到两张表，所以都要删除
        Long activityId = vo.getActivityId();
        activityRuleMapper.delete(
                new LambdaQueryWrapper<ActivityRule>().eq(ActivityRule::getActivityId, activityId));

        activitySkuMapper.delete(new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, activityId));

// 2获取规则表的数据
        ActivityInfo activityInfo = baseMapper.selectById(activityId);
        List<ActivityRule> activityRuleList = vo.getActivityRuleList();
        activityRuleList.forEach(item -> {
            item.setActivityId(activityId);
            item.setActivityType(activityInfo.getActivityType());
        });
        activityRuleService.saveBatch(activityRuleList);

//        3 获取规则范围数据
        List<ActivitySku> activitySkuList = vo.getActivitySkuList();
        activitySkuList.forEach(item -> {
            item.setActivityId(activityId);
        });
        activitySkuService.saveBatch(activitySkuList);
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyWord(keyword);
        if (CollectionUtils.isEmpty(skuInfoList)) {
            return null;
        }

        List<Long> skuIdList = skuInfoList.stream().map(BaseEntity::getId).collect(Collectors.toList());

//        判断之前商品是否参加过活动
//        如果参加过，活动正在进行中，将商品排除
        List<Long> exitsSkuIdList = baseMapper.selectSkuIdListExits(skuIdList);

        List<SkuInfo> findSkuList = new ArrayList<>();
        skuInfoList.forEach(item -> {
            if (!exitsSkuIdList.contains(item.getId())) {
                findSkuList.add(item);
            }
        });
        return skuInfoList;
    }

    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> map = new HashMap<>();
        skuIdList.forEach(skuid -> {
            List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuid);
            if (!CollectionUtils.isEmpty(activityRuleList)) {
                ArrayList<String> list = new ArrayList<>();
                for (ActivityRule rule : activityRuleList) {
                    list.add(this.getRuleDesc(rule));
                }
                map.put(skuid, list);
            }
        });
        return map;
    }

    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
//        根据skuId 查询sku营销活动 一个活动有多个规则
        List<ActivityRule> activityRuleList = this.findActivityRule(skuId);
//        2 根据skuId+userId查询优惠卷信息
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfoList(skuId, userId);
//        3 封装到Map中 返回
        Map<String, Object> map = new HashMap<>();
        map.put("couponInfoList", couponInfoList);
        map.put("activityRuleList", activityRuleList);

        return null;
    }

    //    根据skuid查询活动规则
    @Override
    public List<ActivityRule> findActivityRule(Long skuId) {
        List<ActivityRule> activityRule = baseMapper.findActivityRule(skuId);
        for (ActivityRule rule : activityRule) {
            String ruleDesc = this.getRuleDesc(rule);
            rule.setRuleDesc(ruleDesc);
        }

        return activityRule;

    }

    @Override
    public OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId) {
//        获取购物车，每个购物车参与活动，根据互动规则分组
//        一个规则对应多种商品
        List<CartInfoVo> cartActivityList = this.findCartActivityList(cartInfoList);
//        2 计算商品参与互动之后的优惠金额
        BigDecimal reduceAmount = cartActivityList.stream().filter(cartInfoVo ->
                        cartInfoVo.getActivityRule() != null
                ).map(cartInfoVo -> cartInfoVo.getActivityRule().getReduceAmount()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
//        3 获取购物车中可用的优惠卷
        List<CouponInfo> couponInfoList = couponInfoService.findCartCouponInfo(cartInfoList, userId);


//        4 计算商品使用优惠卷的金额，一次只能使用一张优惠卷
        BigDecimal couponReduceAmount = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(couponInfoList)) {
            couponInfoList.stream().filter(couponInfo -> couponInfo.getIsOptimal() == 1)
                    .map(CouponInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
//        5 计算没有参与活动的金额
        BigDecimal originalTotalAmount = cartInfoList.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getCartPrice()
                        .multiply(new BigDecimal(cartInfo.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        6 使用优惠卷的金额
        BigDecimal totalAmount = originalTotalAmount.subtract(reduceAmount).subtract(couponReduceAmount);
//        7 封装数据进行返回
        OrderConfirmVo orderTradeVo = new OrderConfirmVo();
        orderTradeVo.setCarInfoVoList(cartActivityList);
        orderTradeVo.setActivityReduceAmount(reduceAmount);
        orderTradeVo.setCouponInfoList(couponInfoList);
        orderTradeVo.setCouponReduceAmount(couponReduceAmount);
        orderTradeVo.setOriginalTotalAmount(originalTotalAmount);
        orderTradeVo.setTotalAmount(totalAmount);
        return orderTradeVo;
    }

    //    获取购物享相对应规则
    @Override
    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList) {
        List<CartInfoVo> cartInfoVos = new ArrayList<>();
        List<Long> skuIdList = cartInfoList.stream().map(CartInfo::getSkuId).collect(Collectors.toList());
//        根据skuid 获取参加活动id
        List<ActivitySku> activitySkuList = baseMapper.selectCarActivity(skuIdList);

//      根据活动进行分组，每个活动有哪些skuid
//        map 的key就是分组字段的活动id value 是每组里面sku列表的数据，set集合
        Map<Long, Set<Long>> map = activitySkuList.stream().collect(
                Collectors.groupingBy(
                        ActivitySku::getActivityId,
                        Collectors.mapping(ActivitySku::getSkuId, Collectors.toSet())
                )
        );
        Map<Long, List<ActivityRule>> activityRuleListMap = new HashMap<>();
//        所有活动id
        Set<Long> activityIdSet = activitySkuList.stream().map(ActivitySku::getActivityId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(activityIdSet)) {
            LambdaQueryWrapper<ActivityRule> wrapper = new LambdaQueryWrapper<>();
//            wrapper.orderByAsc(ActivityRule::getConditionAmount, ActivityRule::getConditionNum);
//            wrapper.in(ActivityRule::getActivityId, activityIdSet);
            List<ActivityRule> activityRuleList = activityRuleMapper.selectList(wrapper);
//            根据互动id进行分组  封装到map中去
            activityRuleListMap = activityRuleList.stream().collect(Collectors.groupingBy(ActivityRule::getActivityId));
        }

//        有活动的购物项 skuid 使用set集合存储
        Set<Long> activitySkuIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(map)) {
//            遍历map集合
            for (Map.Entry<Long, Set<Long>> entry : map.entrySet()) {
                Long actvityId = entry.getKey();
                Set<Long> value = entry.getValue();
                List<CartInfo> infoList = cartInfoList.stream().filter(cartInfo -> value.contains(cartInfo.getSkuId())).collect(Collectors.toList());
//                计算购物总金额
                BigDecimal bigDecimal = this.computeTotalAmount(infoList);
                int i = this.computeCartNum(infoList);
                List<ActivityRule> activityRuleList = activityRuleListMap.get(actvityId);
                ActivityType activityType = activityRuleList.get(0).getActivityType();
//                判断活动的类型\
                ActivityRule activityRule = null;
                if (activityType == ActivityType.FULL_REDUCTION) {
                    activityRule = this.computeFullReduction(bigDecimal, activityRuleList);
                } else {
                    activityRule
                            = this.computeFullDiscount(i, bigDecimal, activityRuleList);
                }

                CartInfoVo cartInfoVo = new CartInfoVo();
                cartInfoVo.setActivityRule(activityRule);
                cartInfoVo.setCartInfoList(infoList);
                cartInfoVos.add(cartInfoVo);
                activitySkuIdSet.addAll(value);
            }
        }
//        没有互动购物项的skuId
//        获取那些SkuId没有参加活动
        skuIdList.removeAll(activitySkuIdSet);
        if (!CollectionUtils.isEmpty(skuIdList)) {
            Map<Long, CartInfo> skuIdCartInfoMap = cartInfoList.stream().collect(Collectors.toMap(CartInfo::getSkuId, cartInfo -> cartInfo));
            for (Long s : skuIdList) {
                CartInfoVo cartInfoVo = new CartInfoVo();
                cartInfoVo.setActivityRule(null);
                List<CartInfo> list = new ArrayList<>();

                list.add(skuIdCartInfoMap.get(s));
                cartInfoVo.setCartInfoList(list);
            }
        }

        return cartInfoVos;
    }

    /**
     * 计算满量打折最优规则
     *
     * @param totalNum
     * @param activityRuleList //该活动规则skuActivityRuleList数据，已经按照优惠折扣从大到小排序了
     */
    private ActivityRule computeFullDiscount(Integer totalNum, BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项购买个数大于等于满减件数，则优化打折
            if (totalNum.intValue() >= activityRule.getConditionNum()) {
                BigDecimal skuDiscountTotalAmount = totalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                BigDecimal reduceAmount = totalAmount.subtract(skuDiscountTotalAmount);
                activityRule.setReduceAmount(reduceAmount);
                optimalActivityRule = activityRule;
                break;
            }
        }
        if (null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size() - 1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，还差")
                    .append(totalNum - optimalActivityRule.getConditionNum())
                    .append("件");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }


    /**
     * 计算满减最优规则
     *
     * @param totalAmount
     * @param activityRuleList //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
     */
    private ActivityRule computeFullReduction(BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项金额大于等于满减金额，则优惠金额
            if (totalAmount.compareTo(activityRule.getConditionAmount()) > -1) {
                //优惠后减少金额
                activityRule.setReduceAmount(activityRule.getBenefitAmount());
                optimalActivityRule = activityRule;
                break;
            }
        }
        if (null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size() - 1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，还差")
                    .append(totalAmount.subtract(optimalActivityRule.getConditionAmount()))
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }


    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if (cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    private int computeCartNum(List<CartInfo> cartInfoList) {
        int total = 0;
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if (cartInfo.getIsChecked().intValue() == 1) {
                total += cartInfo.getSkuNum();
            }
        }
        return total;
    }


    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuffer ruleDesc = new StringBuffer();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }
}




