package com.zml.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.activity.ActivityInfo;
import com.zml.ssyx.model.activity.ActivityRule;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.ActivityRuleVo;
import com.zml.ssyx.vo.order.CartInfoVo;
import com.zml.ssyx.vo.order.OrderConfirmVo;

import java.util.List;
import java.util.Map;

/**
 * @author ZHANGMINLEI
 * @description 针对表【activity_info(活动表)】的数据库操作Service
 * @createDate 2023-07-14 10:28:34
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    IPage<ActivityInfo> getPageList(Page<ActivityInfo> activityInfoPage);

    Map<String, Object> findActivityRuleList(Long activityId);

    void saveActivityRule(ActivityRuleVo activityRule);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    List<ActivityRule> findActivityRule(Long skuId);

    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);

//    获取购物项相对应的规则
    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);
}
