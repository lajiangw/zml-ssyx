package com.zml.activity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.activity.service.ActivityInfoService;
import com.zml.result.Result;
import com.zml.ssyx.model.activity.ActivityInfo;
import com.zml.ssyx.model.activity.ActivityRule;
import com.zml.ssyx.vo.activity.ActivityRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-14 10:40
 */
@Api(tags = "活动接口")
@RestController
@RequestMapping("/admin/activity/activityInfo")
//@CrossOrigin
//TODO 跨域问题无法显示。
public class ActivityInfoController {

    @Resource
    private ActivityInfoService activityInfoService;

    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable Long page, @PathVariable Long limit) {
        Page<ActivityInfo> activityInfoPage = new Page<>(page, limit);
        return Result.ok(activityInfoService.getPageList(activityInfoPage));
    }

    @ApiOperation("添加活动")
    @PostMapping("/save")
    public Result save(@RequestBody ActivityInfo activityInfo) {
        return activityInfoService.save(activityInfo) ? Result.ok() : Result.fail();
    }

    @ApiOperation("通过id查询")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(activityInfoService.getById(id));
    }

    @ApiOperation("通过id删除")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        return activityInfoService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("通过id修改")
    @PutMapping("/update")
    public Result updateById(@RequestBody ActivityInfo info) {
        return activityInfoService.updateById(info) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result removeRows(List<Long> idList) {
        return activityInfoService.removeByIds(idList) ? Result.ok() : Result.fail();
    }

    @ApiOperation("通过id查看活动规则")
    @GetMapping("/findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable Long id) {
        return Result.ok(activityInfoService.findActivityRuleList(id));
    }

    @ApiOperation("保存活动规则")
    @PostMapping("/saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo) {
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok();
    }

    @GetMapping("/findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable String keyword) {
        return Result.ok(activityInfoService.findSkuInfoByKeyword(keyword));
    }


}
