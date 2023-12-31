package com.zml.activity.api;

import com.zml.activity.service.ActivityInfoService;
import com.zml.ssyx.model.order.CartInfo;
import com.zml.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 11:56
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {

    @Resource
    private ActivityInfoService activityInfoService;

    @PostMapping("/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList) {
        return activityInfoService.findActivity(skuIdList);
    }

    //    根据SkuId 查询营销数据和优惠卷
    @GetMapping("/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable Long skuId,
                                                     @PathVariable Long userId) {
        return activityInfoService.findActivityAndCoupon(skuId, userId);
    }

    @ApiOperation(value = "获取购物车满足条件的促销与优惠券信息")
    @PostMapping("inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList,
                                                    @PathVariable Long userId) {
        return activityInfoService.findCartActivityAndCoupon(cartInfoList,userId);
    }
}
