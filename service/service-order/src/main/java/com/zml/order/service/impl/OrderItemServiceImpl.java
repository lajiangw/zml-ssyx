package com.zml.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.order.mapper.OrderItemMapper;
import com.zml.order.service.OrderItemService;
import com.zml.ssyx.model.order.OrderItem;
import org.springframework.stereotype.Service;

/**
 * @author ZHANGMINLEI
 * @description 针对表【order_item(订单项信息)】的数据库操作Service实现
 * @createDate 2023-07-19 21:22:22
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

}




