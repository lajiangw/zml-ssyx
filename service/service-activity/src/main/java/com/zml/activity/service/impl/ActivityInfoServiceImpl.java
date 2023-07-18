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
import com.zml.client.product.ProductFeignClient;
import com.zml.ssyx.enums.ActivityType;
import com.zml.ssyx.model.activity.ActivityInfo;
import com.zml.ssyx.model.activity.ActivityRule;
import com.zml.ssyx.model.activity.ActivitySku;
import com.zml.ssyx.model.base.BaseEntity;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.ActivityRuleVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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




