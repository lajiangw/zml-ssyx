package com.zml.home.controller;

import com.zml.client.product.ProductFeignClient;
import com.zml.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 10:10
 */
@RestController
@Api(tags = "分类显示")
@RequestMapping("/home")
public class CatGoryApiController {

    @Resource
    private ProductFeignClient productFeignClient;

    /**
     * 通过feign调用product接口实现查询所有分类
     *
     * @return 所有分类
     */
    @GetMapping("/category")
    public Result categoryList() {
        return Result.ok(productFeignClient.findAllCategoryList());


    }
}
