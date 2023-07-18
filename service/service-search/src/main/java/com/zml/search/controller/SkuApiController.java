package com.zml.search.controller;

import com.zml.result.Result;
import com.zml.search.service.SkuService;
import com.zml.ssyx.model.search.SkuEs;
import com.zml.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<SkuEs> findHostSkuList() {
        return skuService.findHostSkuList();
    }

    //    查询所有商品
    @GetMapping("/{page}/{limit}")
    public Result listSku(@PathVariable Integer page,
                          @PathVariable Integer limit,
                          SkuEsQueryVo skuEsQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<SkuEs> page1 = skuService.search(pageable, skuEsQueryVo);
        return Result.ok(page1);
    }

    //    更新商品热度
    @GetMapping("/inner/incrHotScore/{skuId}")
    public boolean incrHotScore(@PathVariable Long skuId) {
        skuService.incrHotScore(skuId);
        return true;
    }
}



