package com.zml.cart.controller;

import com.zml.activity.client.ActivityFeignClient;
import com.zml.auth.AuthContextHolder;
import com.zml.cart.service.CartService;
import com.zml.result.Result;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import com.zml.utils.JwtHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 20:59
 */

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Resource
    private CartService cartService;

    @Resource
    private ActivityFeignClient activityFeignClient;

    //    将商品添加到购物车
    @GetMapping("/addToCart/{skuId}/{skuNum}")
    public Result addCart(@PathVariable("skuId") Long skuId,
                          @PathVariable Integer skuNum, HttpServletRequest request) {
        Long userId = JwtHelper.getUserId(request.getHeader("token"));
        AuthContextHolder.setUserId(userId);
        cartService.addCart(userId, skuId, skuNum);
        return Result.ok();
    }

    //    根据skuid 删除购物车
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable Long skuId) {
        Long userId = AuthContextHolder.getUserId();
        cartService.deelteCart(skuId, userId);
        return Result.ok();
    }

    //    清空购物车
    @DeleteMapping("/deleteAllCart")
    public Result deleteAllCart() {
        Long userId = AuthContextHolder.getUserId();
        cartService.deleteAllCart(userId);
        return Result.ok();
    }

    //    批量删除
    @DeleteMapping("/bathDeleteCart")
    public Result bathDeleteCart(@RequestBody List<Long> skuIdList) {
        Long userId = AuthContextHolder.getUserId();
        cartService.bathDeleteCart(skuIdList, userId);
        return Result.ok();
    }

    @GetMapping("/cartList")
    public Result cartList() {
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartService.getCartList(userId);
        return Result.ok();
    }


    /**
     * 查询带优惠卷的购物车
     *
     * @param request
     * @return
     */
    @GetMapping("activityCartList")
    public Result activityCartList(HttpServletRequest request) {
        // 获取用户Id
        Long userId = JwtHelper.getUserId(request.getHeader("token"));
        List<CartInfo> cartInfoList = cartService.getCartList(userId);
        OrderConfirmVo orderTradeVo = activityFeignClient.findCartActivityAndCoupon(cartInfoList, userId);
        return Result.ok(orderTradeVo);
    }

    /**
     * 根据用户Id 查询购物车列表
     *
     * @param userId
     * @return
     */
    @GetMapping("inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId) {
        return cartService.getCartCheckedList(userId);
    }
}
