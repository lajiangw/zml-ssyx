package com.zml.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.mapper.CouponInfoMapper;
import com.zml.activity.mapper.CouponRangeMapper;
import com.zml.activity.service.CouponInfoService;
import com.zml.client.product.ProductFeignClient;
import com.zml.ssyx.enums.CouponRangeType;
import com.zml.ssyx.model.activity.CouponInfo;
import com.zml.ssyx.model.activity.CouponRange;
import com.zml.ssyx.model.base.BaseEntity;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.CouponRuleVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZHANGMINLEI
 * @description 针对表【coupon_info(优惠券信息)】的数据库操作Service实现
 * @createDate 2023-07-14 10:28:46
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo>
        implements CouponInfoService {

    @Resource
    private CouponRangeMapper couponRangeMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public IPage<CouponInfo> getPageList(Page<CouponInfo> couponInfoPage) {
        Page<CouponInfo> couponInfoPage1 = baseMapper.selectPage(couponInfoPage, null);
        List<CouponInfo> records = couponInfoPage1.getRecords();
        records.forEach(item -> {
            item.setCouponName(item.getCouponType().getComment());
            CouponRangeType rangeType = item.getRangeType();
            if (rangeType != null) {
                item.setRangeTypeString(rangeType.getComment());
            }
        });
        return couponInfoPage1;
    }

    @Override
    public CouponInfo selectById(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if (couponInfo.getRangeType() != null) {
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        Map<String, Object> map = new HashMap<>();
        CouponInfo couponInfo = baseMapper.selectById(id);

        List<CouponRange> couponRanges = couponRangeMapper.
                selectList(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id));

        List<Long> rangeIds = couponRanges.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rangeIds)) {
            if (couponInfo.getRangeType() == CouponRangeType.SKU) {
                List<SkuInfo> skuInfos = productFeignClient.getSkuInfos(rangeIds);
                map.put("skuInfoList", skuInfos);
            } else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                List<Category> categories = productFeignClient.findCateGoryList(rangeIds);
                map.put("categoryList", categories);
            }
        }
        return map;
    }

    @Override
    @Transactional
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
//        删除之前的信息
        couponRangeMapper.delete(new LambdaQueryWrapper<CouponRange>().
                eq(CouponRange::getCouponId, couponRuleVo.getCouponId()));
//更新优惠卷信息
        CouponInfo couponInfo = baseMapper.selectById(couponRuleVo.getCouponId());
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        baseMapper.updateById(couponInfo);
//添加优惠卷新规则
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        couponRangeList.forEach(item -> {
            item.setCouponId(couponRuleVo.getCouponId());
            couponRangeMapper.insert(item);
        });
    }

    //    根据skuid  和userid查询优惠卷信息
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        return baseMapper.selectCouponList(skuInfo.getId(), skuInfo.getCategoryId(), userId);
    }

    //获取可以使用的优惠卷列表
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        List<CouponInfo> userAllCouponInfo = baseMapper.selectCouponInfoList(userId);
        if (CollectionUtils.isEmpty(userAllCouponInfo)) {
            return null;
        }
//        2从第一步的list集合中，获取所有优惠卷信息
        List<Long> couponInfoIdList = userAllCouponInfo.stream().map(BaseEntity::getId).collect(Collectors.toList());
//        3 查询优惠卷使用范围
        LambdaQueryWrapper<CouponRange> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CouponRange::getCouponId, couponInfoIdList);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(wrapper);
//        4 获取优惠卷nid 优惠卷id进行分组，得到map集合
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangeList);
//        5 遍历全部优惠卷集合，判断优惠类型
//        全场通过  sku和 分类
        CouponInfo optimalCouponInfo = null;
        BigDecimal reduceAmount = new BigDecimal(0);
        for (CouponInfo couponInfo : userAllCouponInfo) {
            if (CouponRangeType.ALL == couponInfo.getRangeType()) {
                //全场通用
                //判断是否满足优惠使用门槛
                //计算购物车商品的总价
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if (totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0) {
                    couponInfo.setIsSelect(1);
                }
            } else {
//                    优惠卷id获取想对应的skuid列表
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
//                    满足使用范围的购物项
                List<CartInfo> currentCartInfoList = cartInfoList.stream().filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId())).collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if (totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0) {
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect().intValue() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
            if (null != optimalCouponInfo) {
                optimalCouponInfo.setIsOptimal(1);
            }

        }
//        6 返回数据
        return userAllCouponInfo;
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

    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList) {
        Map<Long, List<Long>> couponIdToSkuIdMap = new HashMap<>();

//        根据优惠卷id进行分组
        Map<Long, List<CouponRange>> couponRangeToRangeListMap = couponRangeList.stream().collect(Collectors.groupingBy(CouponRange::getCouponId));
        for (Map.Entry<Long, List<CouponRange>> entry : couponRangeToRangeListMap.entrySet()) {
            Long couponId = entry.getKey();
            List<CouponRange> rangeList = entry.getValue();
//            创建set集合，因为一个商品只能对一个优惠卷
            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : rangeList) {
                    if (couponRange.getRangeType() == CouponRangeType.SKU && couponRange.getRangeId() == cartInfo.getSkuId().longValue()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    } else if (couponRange.getRangeType() == CouponRangeType.CATEGORY &&
                            couponRange.getRangeId().longValue() == cartInfo.getCategoryId().longValue()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    } else {

                    }
                }
            }
            couponIdToSkuIdMap.put(couponId, new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;

    }
}




