package com.zml.cart.service.impl;

import com.zml.cart.service.CartService;
import com.zml.client.product.ProductFeignClient;
import com.zml.constant.RedisConst;
import com.zml.exception.SsyxException;
import com.zml.result.ResultCodeEnum;
import com.zml.ssyx.enums.SkuType;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.model.product.SkuInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 21:00
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ProductFeignClient productFeignClient;

    //    添加商品到购物车
    @Override
    public void addCart(Long userId, Long skuId, Integer skuNum) {
        //        因为购物车数据存储到redis里面。
//        从redis中根据key获取数据，这个key包含userId
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
//        根据第一步查询出来的结果，得到的是skuid和skuNum关系
//        判断是否为第一次添加这个商品,如果有这个key说明是第一次添加
        CartInfo cartInfo = new CartInfo();
        if (boundHashOperations.hasKey(skuId.toString())) {
//      有这个skuid。将数量＋1
            cartInfo = boundHashOperations.get(skuId.toString());
            Integer CurrentskuNum = cartInfo.getSkuNum() + skuNum;
            if (CurrentskuNum < 1) {
                return;
            }
//            更新购物车信息
            cartInfo.setSkuNum(CurrentskuNum);
            cartInfo.setCurrentBuyNum(CurrentskuNum);
//          购买的数量不能大于限购数量
            if (CurrentskuNum > cartInfo.getPerLimit()) {
                throw new SsyxException(ResultCodeEnum.SKU_LIMIT_ERROR);
            }
//            更新其他值  是否被选中 默认添加到购物车就是选中的，所以设置为true
            cartInfo.setIsChecked(1);
        } else {
//      如果结果里面灭有skuId就是第一次添加
//            直接添加
            skuNum = 1;

//            封装cartinfo对象
            cartInfo = new CartInfo();
//            通过远程调用获得skuinfo信息
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            if (skuInfo == null) {
                throw new SsyxException(ResultCodeEnum.DATA_ERROR);
            }
            BeanUtils.copyProperties(skuInfo, cartInfo);
            cartInfo.setUserId(userId);
            cartInfo.setSkuNum(skuNum);
            cartInfo.setCurrentBuyNum(skuNum);
            cartInfo.setSkuType(SkuType.COMMON.getCode());
            cartInfo.setIsChecked(1);
            cartInfo.setStatus(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
//        更新redis缓存
        boundHashOperations.put(skuId.toString(), cartInfo);
//        设置过期时间
        this.setCartKeyExpire(cartKey);
    }

    //    根据skuid 删除购物车
    @Override
    public void deelteCart(Long skuId, Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> hashOps = redisTemplate.boundHashOps(cartKey);
        if (hashOps.hasKey(skuId.toString())) {
            hashOps.delete(skuId.toString());
        }
    }

    //    清空购物车
    @Override
    public void deleteAllCart(Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> operations = redisTemplate.boundHashOps(cartKey);
//        直接删除大key 清空购物车信息
        operations.delete(cartKey);
    }

    //    批量删除
    @Override
    public void bathDeleteCart(List<Long> skuIdList, Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(cartKey);
        skuIdList.stream().forEach(item -> {
            boundHashOperations.delete(item.toString());
        });
    }

    //    购物车列表
    @Override
    public List<CartInfo> getCartList(Long userId) {
        List<CartInfo> cartInfoList = new ArrayList<>();
        if (StringUtils.isEmpty(userId)) {
            return cartInfoList;
        }
//        从redis 获取购物车信息
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        cartInfoList = boundHashOperations.values();
//            不为空则根据时间降序
        Objects.requireNonNull(cartInfoList).sort(new Comparator<CartInfo>() {
            @Override
            public int compare(CartInfo o1, CartInfo o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
        return cartInfoList;
    }

    @Override
    public List<CartInfo> getCartCheckedList(Long userId) {
        String cartKey = this.getCartKey(userId);
        BoundHashOperations<String, String, CartInfo> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> cartInfoList = boundHashOperations.values();
        return Objects.requireNonNull(cartInfoList).stream().filter(cartInfo -> cartInfo.getIsChecked() == 1).collect(Collectors.toList());
    }

    //设置key过期时间
    private void setCartKeyExpire(String key) {
        redisTemplate.expire(key, RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
    }

    private String getCartKey(Long userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }


}
