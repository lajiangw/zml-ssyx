package com.zml.cart.service;

import com.zml.ssyx.model.order.CartInfo;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 21:00
 */
public interface CartService {
    void addCart(Long userId, Long skuId, Integer skuNum);

    void deelteCart(Long skuId, Long userId);

    void deleteAllCart(Long userId);

    void bathDeleteCart(List<Long> skuIdList, Long userId);

    List<CartInfo> getCartList(Long userId);

    List<CartInfo> getCartCheckedList(Long userId);
}
