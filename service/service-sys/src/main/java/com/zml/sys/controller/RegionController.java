package com.zml.sys.controller;

import com.zml.result.Result;
import com.zml.ssyx.model.sys.Region;
import com.zml.sys.service.RegionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-10 21:17
 */
@RequestMapping("/admin/sys/region")
@RestController
//@CrossOrigin
public class RegionController {

    @Resource
    private RegionService regionService;

    @ApiOperation("根据关键字查询")
    @GetMapping("/findRegionByKeyword/{keyword}")
    public Result findRegionByKeyword(@PathVariable String keyword) {
        List<Region> list = regionService.findRegionByKeyword(keyword);
        return Result.ok(list);
    }

}


