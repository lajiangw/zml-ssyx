package com.zml.product.api;

import com.zml.product.service.CategoryService;
import com.zml.product.service.SkuInfoService;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 17:09
 */
@RestController
@RequestMapping("/api/product")
public class ProductInnerController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private SkuInfoService skuInfoService;

    //    根据分类ID 获得分类信息
    @GetMapping("/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }

    //    根据skuid 获取信息
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId) {
        return skuInfoService.getById(skuId);
    }

    @GetMapping("/inner/get/SkuInfo/{ids}")
    public List<SkuInfo> getSkuInfos(@PathVariable List<Long> ids) {
        return skuInfoService.listByIds(ids);
    }

    //    根据关键字查询商品信息
    @GetMapping("/inner/findSkuInfoByKeyWord/{keyWord}")
    public List<SkuInfo> findSkuInfoByKeyWord(@PathVariable String keyWord) {
        return skuInfoService.findSkuInfoByKeyWord(keyWord);
    }

    @PostMapping("/inner/findCateGoryList")
    public List<Category> findCateGoryList(@RequestBody List<Long> ids) {
        return categoryService.listByIds(ids);
    }

//获得所有分类
    @GetMapping("/inner/findAllCategoryList")
    public List<Category> findAllCategoryList() {
        return categoryService.list();
    }

//    获得所有新人专项商品
    @GetMapping("/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList(){
     return   skuInfoService.findNewPersonSkuInfoList();
    }

//    根据skuId获得Sku信息
    @GetMapping("/inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable Long skuId){
        return skuInfoService.getSkuInfoVo(skuId);
    }

}
