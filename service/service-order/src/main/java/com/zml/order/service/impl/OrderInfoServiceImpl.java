package com.zml.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.client.ActivityFeignClient;
import com.zml.cart.client.CartFeignClient;
import com.zml.client.user.UserFeignClient;
import com.zml.constant.RedisConst;
import com.zml.order.mapper.OrderInfoMapper;
import com.zml.order.service.OrderInfoService;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.order.OrderInfo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import com.zml.ssyx.vo.order.OrderSubmitVo;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.utils.JwtHelper;
import com.zml.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ZHANGMINLEI
 * @description 针对表【order_info(订单)】的数据库操作Service实现
 * @createDate 2023-07-19 21:22:14
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    //    确认订单
    @Override
    public OrderConfirmVo confirmOrder(HttpServletRequest request) {
//        获取用户Id
        Long userId = JwtHelper.getUserId(request.getHeader("token"));

//        获取用户对应的团长信息
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);

//        获取购物车中选中的商品
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);

//           生成唯一标识 这里我们使用雪花算法
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1L);
        Long oderNo1 = snowflakeIdGenerator.nextId();
        String oderNo = oderNo1.toString();

        redisTemplate.opsForValue().set(RedisConst.ORDER_REPEAT + oderNo, oderNo, 24, TimeUnit.HOURS);

//        TODO 获取购物车优惠卷信息
//        OrderConfirmVo cartActivityAndCoupon = activityFeignClient.findCartActivityAndCoupon(cartCheckedList, userId);
        OrderConfirmVo cartActivityAndCoupon = new OrderConfirmVo();
        cartActivityAndCoupon.setLeaderAddressVo(leaderAddressVo);
        cartActivityAndCoupon.setOrderNo(oderNo);
        return cartActivityAndCoupon;
    }

    //    生成订单
    @Override
    public Long submitOrder(OrderSubmitVo orderParamVo) {
        return null;
    }

    //    获取订单详情
    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        return null;
    }
}




