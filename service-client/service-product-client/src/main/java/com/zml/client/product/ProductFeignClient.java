package com.zml.client.product;

import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 17:23
 */
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getCategory/{categoryId}")
    Category getCategory(@PathVariable Long categoryId);

    //    根据skuid 获取信息
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable Long skuId);

    @GetMapping("/api/product/inner/get/SkuInfo/{ids}")
    public List<SkuInfo> getSkuInfos(@PathVariable List<Long> ids);

    @GetMapping("/api/product/inner/findSkuInfoByKeyWord/{keyWord}")
    List<SkuInfo> findSkuInfoByKeyWord(@PathVariable String keyWord);

    @PostMapping("/api/product/inner/findCateGoryList")
    public List<Category> findCateGoryList(@RequestBody List<Long> ids);

    @GetMapping("/api/product/inner/findAllCategoryList")
    public List<Category> findAllCategoryList();

    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList();

    @GetMapping("/api/product/inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable Long skuId);

}
