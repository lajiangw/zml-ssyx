package com.zml.activity.service;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.ssyx.model.activity.CouponInfo;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;

/**
* @author ZHANGMINLEI
* @description 针对表【coupon_info(优惠券信息)】的数据库操作Service
* @createDate 2023-07-14 10:28:46
*/
public interface CouponInfoService extends IService<CouponInfo> {

    IPage<CouponInfo> getPageList(Page<CouponInfo> couponInfoPage);

    CouponInfo selectById(Long id);

    Map<String,Object> findCouponRuleList(Long id);

    void saveCouponRule(CouponRuleVo ruleVo);

    List<CouponInfo> findCouponInfoList(Long skuId, Long userId);

    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);
}
