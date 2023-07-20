package com.zml.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.client.ActivityFeignClient;
import com.zml.cart.client.CartFeignClient;
import com.zml.client.product.ProductFeignClient;
import com.zml.client.user.UserFeignClient;
import com.zml.constant.RedisConst;
import com.zml.exception.SsyxException;
import com.zml.order.mapper.OrderInfoMapper;
import com.zml.order.service.OrderInfoService;
import com.zml.result.ResultCodeEnum;
import com.zml.ssyx.enums.SkuType;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.order.OrderInfo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import com.zml.ssyx.vo.order.OrderSubmitVo;
import com.zml.ssyx.vo.product.SkuStockLockVo;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.utils.JwtHelper;
import com.zml.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Resource
    private ProductFeignClient productFeignClient;

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
    public Long submitOrder(OrderSubmitVo orderParamVo, Long userId) {
//        设置给那个用户的订单

//        订单不可以重复提交
//        通过redis和lua脚本判断 lua脚本是保证原子性的。
//        通过传递oderNO判断，如果有相同的说明正常提交，如果没有相同的说明重复提交
        String orderNo = orderParamVo.getOrderNo();
        if (StringUtils.isEmpty(orderNo)) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }

        String script = "if(redis.call('get', KEYS[1]) == ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end";
        Boolean flag = (Boolean) redisTemplate.execute(
                new DefaultRedisScript(script, Boolean.class),
                Arrays.asList(RedisConst.ORDER_REPEAT + orderNo), orderNo);

        if (Boolean.FALSE.equals(flag)) {
            throw new SsyxException(ResultCodeEnum.REPEAT_SUBMIT);
        }

//        3 验证库存，锁定库存，不是真正的减去库存，而是锁定库
//         假如现在仓库有5个西红柿，我想买两个，首先验证仓库，是否有两个库存，然后将这个连个西红柿进行锁定。
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);
        List<CartInfo> commonSkuList = cartCheckedList.stream().
                filter(cartInfo -> Objects.equals(cartInfo.getSkuType(), SkuType.COMMON.getCode())).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(commonSkuList)) {
            List<SkuStockLockVo> skuStockLockVoList = commonSkuList.stream().map(cartInfo -> {
                SkuStockLockVo skuStockLockVo = new SkuStockLockVo();
                skuStockLockVo.setSkuId(cartInfo.getSkuId());
                skuStockLockVo.setSkuNum(cartInfo.getSkuNum());
                return skuStockLockVo;
            }).collect(Collectors.toList());
            Boolean isLockSuss = productFeignClient.checkAndLock(skuStockLockVoList, orderNo);
            if (!isLockSuss) {
                throw new SsyxException(ResultCodeEnum.ORDER_PRICE_ERROR);
            }
        }

//        5 进行下单的操作
//        向两张表进行操作oderinfo 和 orderItem TODO 未完成，生成订单125-129 尚上优选
        Long orderId = this.saveOrder(orderParamVo, cartCheckedList);
//        6返回订单id
        return null;
    }

    private Long saveOrder(OrderSubmitVo orderParamVo, List<CartInfo> cartCheckedList) {
        return null;
    }

    //    获取订单详情
    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        return null;
    }
}




