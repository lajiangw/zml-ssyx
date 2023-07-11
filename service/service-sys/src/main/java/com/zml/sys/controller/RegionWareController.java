package com.zml.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.result.Result;
import com.zml.ssyx.model.sys.RegionWare;
import com.zml.ssyx.vo.sys.RegionWareQueryVo;
import com.zml.sys.service.RegionWareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.valves.rewrite.RewriteValve;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-10 21:20
 */
@Api(tags = "开通区域接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/sys/regionWare")
public class RegionWareController {

    @Autowired
    private RegionWareService regionWareService;

    @ApiOperation("开通区域列表")
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit, RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> regionWarePage = new Page<>(page, limit);
        return Result.ok(regionWareService.selectPage(regionWarePage, regionWareQueryVo));
    }

    @ApiOperation("添加开通区域")
    @PostMapping("/save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.insert(regionWare);
        return Result.ok();
    }

    @ApiOperation("删除开通区域")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        return regionWareService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("修改开通区域")
    @PostMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        regionWareService.updateStatus(id, status);
        return Result.ok();
    }
}
