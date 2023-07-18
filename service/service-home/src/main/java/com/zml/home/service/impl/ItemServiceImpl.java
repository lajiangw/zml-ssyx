package com.zml.home.service.impl;

import cn.hutool.core.map.MapUtil;
import com.zml.activity.client.ActivityFeignClient;
import com.zml.client.product.ProductFeignClient;
import com.zml.client.search.SearchFeignClient;
import com.zml.home.service.ItemService;
import com.zml.ssyx.vo.product.SkuInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 17:12
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private SearchFeignClient searchFeignClient;

    //    查看商品详情接口
    @Override
    public Map<String, Object> item(Long skuId, Long userId) {
        Map<String, Object> map = new HashMap<>();

        CompletableFuture<Void> skuInfoVo1 = CompletableFuture.runAsync(() -> {
//            远程调用获取sku对应的数据
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(skuId);
            map.put("skuInfoVo", skuInfoVo);
        }, threadPoolExecutor);

//        得到优惠卷信息
        CompletableFuture<Void> activityFuture = CompletableFuture.runAsync(() -> {
            Map<String, Object> activityMap = activityFeignClient.findActivityAndCoupon(skuId, userId);
            if (MapUtil.isNotEmpty(activityMap)) {
                map.putAll(activityMap);
            }

        }, threadPoolExecutor);

//        3 更新商品热度
        CompletableFuture<Void> hotFuture = CompletableFuture.runAsync(() -> {
            searchFeignClient.incrHotScore(skuId);
        }, threadPoolExecutor);

//        任务组合
        CompletableFuture.allOf(skuInfoVo1, activityFuture, hotFuture).join();
        return map;
    }
}
