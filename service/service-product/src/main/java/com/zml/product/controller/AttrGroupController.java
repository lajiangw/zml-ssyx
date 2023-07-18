package com.zml.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.product.service.AttrGroupService;
import com.zml.result.Result;
import com.zml.ssyx.model.product.AttrGroup;
import com.zml.ssyx.vo.product.AttrGroupQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 14:38
 */
@Api(value = "AttrGroup管理", tags = "平台属性分组管理")
@RestController
@RequestMapping(value = "/admin/product/attrGroup")
//@CrossOrigin
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @ApiOperation("分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageList(@PathVariable Long page, @PathVariable Long limit, AttrGroupQueryVo vo) {
        Page<AttrGroup> attrGroupPage = new Page<>(page, limit);
        IPage<AttrGroup> iPage = attrGroupService.pageList(attrGroupPage, vo);
        return Result.ok(iPage);
    }

    @ApiOperation("查询全部分组")
    @GetMapping("/findAllList")
    public Result findAllList() {
        List<AttrGroup> list = attrGroupService.findAllList();
        return Result.ok(list);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(attrGroupService.getById(id));
    }

    @ApiOperation("保存")
    @PostMapping ("/save")
    public Result save(@RequestBody AttrGroup attrGroup) {
        return attrGroupService.save(attrGroup) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据ID修改")
    @PutMapping("/update")
    public Result update(@RequestBody AttrGroup attrGroup) {
        return attrGroupService.updateById(attrGroup) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据ID删除")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        return attrGroupService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据ID批量删除")
    @DeleteMapping("/batchRemove")
    public Result removeByIds(@RequestBody List<Long> list) {
        return attrGroupService.removeByIds(list) ? Result.ok() : Result.fail();
    }

}
