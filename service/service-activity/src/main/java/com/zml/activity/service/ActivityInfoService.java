package com.zml.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.ssyx.model.activity.ActivityInfo;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.ActivityRuleVo;

import java.util.List;
import java.util.Map;

/**
* @author ZHANGMINLEI
* @description 针对表【activity_info(活动表)】的数据库操作Service
* @createDate 2023-07-14 10:28:34
*/
public interface ActivityInfoService extends IService<ActivityInfo> {

    IPage<ActivityInfo> getPageList(Page<ActivityInfo> activityInfoPage);

    Map<String,Object> findActivityRuleList(Long id);

    void saveActivityRule(ActivityRuleVo activityRule);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

}
