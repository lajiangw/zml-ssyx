package com.zml.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.order.OrderInfo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import com.zml.ssyx.vo.order.OrderSubmitVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author ZHANGMINLEI
* @description 针对表【order_info(订单)】的数据库操作Service
* @createDate 2023-07-19 21:22:14
*/
public interface OrderInfoService extends IService<OrderInfo> {

    OrderConfirmVo confirmOrder(HttpServletRequest request);

    OrderInfo getOrderInfoById(Long orderId);

    Long submitOrder(OrderSubmitVo orderParamVo);
}
