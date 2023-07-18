package com.zml.search.controller;

import com.zml.result.Result;
import com.zml.search.service.SkuService;
import com.zml.ssyx.model.search.SkuEs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 16:23
 */
@RestController
@RequestMapping("/api/search/sku")
public class SkuApiController {

    @Autowired
    private SkuService skuService;

    //  上架
    @GetMapping("/inner/upperSku/{skuId}")
    public Result upperSku(@PathVariable Long skuId) {
        skuService.upperSku(skuId);
        return Result.ok();
    }

    //    下架
    @GetMapping("/inner/lowerSku/{skuId}")
    public Result lowerSku(@PathVariable Long skuId) {
        skuService.lowerSku(skuId);
        return Result.ok();
    }

    //    获取爆款商品
    @GetMapping("/inner/findHostSkuList")
    public List<SkuEs> findHostSkuList(){
        return skuService.findHostSkuList();
    }
}

