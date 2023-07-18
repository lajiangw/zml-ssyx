package com.zml.activity.controller;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.activity.service.CouponInfoService;
import com.zml.result.Result;
import com.zml.ssyx.model.activity.CouponInfo;
import com.zml.ssyx.vo.activity.CouponRuleVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-14 15:32
 */
//@CrossOrigin
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Resource
    private CouponInfoService couponInfoService;


    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable Long page, @PathVariable Long limit) {
        Page<CouponInfo> couponInfoPage = new Page<>(page, limit);
        return Result.ok(couponInfoService.getPageList(couponInfoPage));
    }

    @PostMapping("/save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok();
    }

    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(couponInfoService.selectById(id));
    }

//    url: `${api_name}/update`,
//    method: 'put',

    @PutMapping("/update")
    public Result update(@RequestBody CouponInfo couponInfo) {
        couponInfoService.updateById(couponInfo);
        return Result.ok();
    }

    /*  url: `${api_name}/remove/${id}`,
      method: 'delete'
      */
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        return couponInfoService.removeById(id) ? Result.ok() : Result.fail();
    }

//    url: `${api_name}/batchRemove`,
//    method: 'delete',

    @DeleteMapping("/batchRemove")
    public Result batchRemove(List<Long> idList) {
        return couponInfoService.removeByIds(idList) ? Result.ok() : Result.fail();
    }

    @GetMapping("/findCouponRuleList/{id}")
    public Result findCouponRuleList(@PathVariable Long id) {
        couponInfoService.findCouponRuleList(id);
        return Result.ok();
    }

    @PostMapping("/saveCouponRule")
    public Result saveCouponRule(@RequestBody CouponRuleVo ruleVo) {
         couponInfoService.saveCouponRule(ruleVo);
         return Result.ok();
    }
}
