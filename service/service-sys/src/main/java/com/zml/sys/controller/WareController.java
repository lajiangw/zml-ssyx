package com.zml.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.result.Result;
import com.zml.ssyx.model.sys.Ware;
import com.zml.ssyx.vo.product.WareQueryVo;
import com.zml.sys.service.WareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 11:26
 */
@RequestMapping("/admin/sys/ware")
@CrossOrigin
@RestController
@Api(tags = "仓库模块")
public class WareController {

    @Autowired
    private WareService wareService;

    @ApiOperation("查询所有仓库列表")
    @GetMapping("/findAllList")
    public Result findAllList() {
        return Result.ok(wareService.list(null));
    }

    @ApiOperation("仓库分页查询")
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit, WareQueryVo vo) {
        Page<Ware> warePage = new Page<>(page, limit);
        return Result.ok(wareService.getPageList(warePage, vo));
    }

    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(wareService.getById(id));
    }

    @ApiOperation("保存仓库")
    @PostMapping("/save")
    public Result save(@RequestBody Ware ware) {
        return Result.ok(wareService.save(ware));
    }


    @ApiOperation("根据id修改仓库")
    @PutMapping("/update")
    public Result updateById(@RequestBody Ware ware) {
        return Result.ok(wareService.updateById(ware));
    }

    @ApiOperation("根据id删除仓库")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        return wareService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(List<Long> idList) {
        return wareService.removeByIds(idList) ? Result.ok() : Result.fail();
    }
}

