package com.zml.activity.mapper;

import com.zml.ssyx.model.activity.ActivityInfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zml.ssyx.model.activity.ActivityRule;
import com.zml.ssyx.model.activity.ActivitySku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【activity_info(活动表)】的数据库操作Mapper
* @createDate 2023-07-14 10:28:34
* @Entity generator.domain.ActivityInfo
*/
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    List<Long> selectSkuIdListExits(@Param("skuIdList") List<Long> skuIdList);


    List<ActivityRule> findActivityRule(@Param("skuId") Long skuid);

    List<ActivitySku> selectCarActivity(@Param("skuIdList") List<Long> skuIdList);
}




