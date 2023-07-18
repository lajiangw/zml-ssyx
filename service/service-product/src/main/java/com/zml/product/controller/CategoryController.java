package com.zml.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.product.service.CategoryService;
import com.zml.result.Result;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.vo.product.CategoryQueryVo;
import com.zml.ssyx.vo.product.CategoryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 14:11
 */
@RestController
//@CrossOrigin
@RequestMapping("/admin/product/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @ApiOperation("分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageList(@PathVariable Long page, @PathVariable Long limit, CategoryQueryVo vo) {
        Page<Category> categoryPage = new Page<>(page, limit);
        IPage<Category> categoryIPage = categoryService.pageList(categoryPage, vo);
        return Result.ok(categoryIPage);
    }

    @ApiOperation(value = "获取商品分类信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    @ApiOperation(value = "新增商品分类")
    @PostMapping("save")
    public Result save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok();
    }

    @ApiOperation(value = "修改商品分类")
    @PutMapping("update")
    public Result updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok();
    }

    @ApiOperation(value = "删除商品分类")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除商品分类")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        categoryService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(value = "获取全部商品分类")
    @GetMapping("findAllList")
    public Result findAllList() {
        return Result.ok(categoryService.list());
    }
}
