package com.zml.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.order.mapper.OrderItemMapper;
import com.zml.order.service.OrderInfoService;
import com.zml.result.Result;
import com.zml.ssyx.model.order.OrderInfo;
import com.zml.ssyx.vo.order.OrderItemMqVo;
import com.zml.ssyx.vo.order.OrderSubmitVo;
import com.zml.ssyx.vo.order.OrderUserQueryVo;
import com.zml.utils.JwtHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-19 21:24
 */
@Api(value = "Order管理", tags = "Order管理")
@RestController
@RequestMapping(value = "/api/order")
public class OrderInfoController {

    @Resource
    private OrderInfoService orderService;



    @ApiOperation("确认订单")
    @GetMapping("auth/confirmOrder")
    public Result confirm(HttpServletRequest request) {
        return Result.ok(orderService.confirmOrder(request));
    }

    @ApiOperation("生成订单")
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderParamVo, HttpServletRequest request) {
        // 获取到用户Id
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        Long orderId = orderService.submitOrder(orderParamVo, userId);
        return Result.ok();
    }

    @ApiOperation("获取订单详情")
    @GetMapping("auth/getOrderInfoById/{orderId}")
    public Result getOrderInfoById(@PathVariable("orderId") Long orderId) {
        OrderInfo orderInfo = orderService.getOrderInfoById(orderId);
        return Result.ok(orderInfo);
    }

    @ApiOperation(value = "获取用户订单分页列表")
    @GetMapping("auth/findUserOrderPage/{page}/{limit}")
    public Result findUserOrderPage(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "orderVo", value = "查询对象", required = false)
            OrderUserQueryVo orderUserQueryVo, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        orderUserQueryVo.setUserId(userId);

        Page<OrderInfo> orderInfoPage = new Page<>(page, limit);
        IPage<OrderInfo> infoIPage = orderService.selectPageList(orderInfoPage, orderUserQueryVo);
        return Result.ok(infoIPage);
    }
}
