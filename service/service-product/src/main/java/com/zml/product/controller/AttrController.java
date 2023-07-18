package com.zml.product.controller;

import com.zml.product.service.AttrService;
import com.zml.result.Result;
import com.zml.ssyx.model.product.Attr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 16:29
 */
@Api(value = "Attr管理", tags = "平台属性管理")
@RestController
@RequestMapping(value = "/admin/product/attr")
//@CrossOrigin
public class AttrController {

    @Autowired
    private AttrService attrService;

    @GetMapping("/{groupId}")
    @ApiOperation("组id查询")
    public Result getList(@PathVariable Long groupId) {
        List<Attr> list = attrService.getList(groupId);
        return Result.ok(list);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Attr attr = attrService.getById(id);
        return Result.ok(attr);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Attr attr) {
        attrService.save(attr);
        return Result.ok();
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Attr attr) {
        attrService.updateById(attr);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        attrService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrService.removeByIds(idList);
        return Result.ok();
    }

}
