package com.zml.client.search;

import com.zml.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 19:03
 */
@FeignClient("service-search")
public interface SearchFeignClient {

    @GetMapping("/api/search/sku/inner/findHostSkuList")
     List<SkuEs> findHostSkuList();


    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    public boolean incrHotScore(@PathVariable Long skuId);
}
