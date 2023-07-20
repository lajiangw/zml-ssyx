package com.zml.payment.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.payment.mapper.PaymentInfoMapper;
import com.zml.payment.service.PaymentInfoService;

import com.zml.ssyx.model.order.PaymentInfo;
import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
* @createDate 2023-07-20 12:11:45
*/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
    implements PaymentInfoService {

}




