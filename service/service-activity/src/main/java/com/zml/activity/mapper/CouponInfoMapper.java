package com.zml.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zml.ssyx.model.activity.CouponInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【coupon_info(优惠券信息)】的数据库操作Mapper
 * @createDate 2023-07-14 10:28:46
 * @Entity generator.domain.CouponInfo
 */
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    List<CouponInfo> selectCouponList(@Param("skuId") Long skuId,
                                      @Param("categoryId") Long categoryId,
                                      @Param("userId") Long userId);
}




