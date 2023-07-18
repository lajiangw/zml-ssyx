package com.zml.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.ssyx.model.user.UserDelivery;
import com.zml.user.mapper.UserDeliveryMapper;
import com.zml.user.service.UserDeliveryService;

import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【user_delivery(会员提货记录表)】的数据库操作Service实现
* @createDate 2023-07-17 13:28:17
*/
@Service
public class UserDeliveryServiceImpl extends ServiceImpl<UserDeliveryMapper, UserDelivery>
    implements UserDeliveryService {

}




