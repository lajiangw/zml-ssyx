package com.zml.activity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.mapper.ActivitySkuMapper;
import com.zml.activity.service.ActivitySkuService;

import com.zml.ssyx.model.activity.ActivitySku;
import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【activity_sku(活动参与商品)】的数据库操作Service实现
* @createDate 2023-07-14 12:27:23
*/
@Service
public class ActivitySkuServiceImpl extends ServiceImpl<ActivitySkuMapper, ActivitySku>
    implements ActivitySkuService {

}




