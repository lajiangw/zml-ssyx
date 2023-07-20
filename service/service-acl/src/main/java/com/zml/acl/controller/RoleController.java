package com.zml.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zml.acl.service.RoleService;
import com.zml.result.Result;
import com.zml.ssyx.model.acl.Role;
import com.zml.ssyx.vo.acl.RoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 18:16
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
//@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("查看角色列表")
    @GetMapping("/{current}/{limit}")
    public Result pageList(@PathVariable Long current, @PathVariable Long limit, RoleQueryVo roleQueryVo) {
        Page<Role> rolePage = new Page<>(current,limit);
        IPage page = roleService.selectRolePage(rolePage, roleQueryVo);
        return Result.ok(page);
    }

    @ApiOperation("根据Id查询角色")
    @GetMapping("/get/{id}")
    public Result getId(@PathVariable("id") Long id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody Role role) {
        boolean save = roleService.save(role);
        return save ? Result.ok(null) : Result.fail(null);
    }

    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result update(@RequestBody Role role) {
        return roleService.updateById(role) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据ID删除角色")
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable Long id) {
        return roleService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除多个角色")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> list) {
        return roleService.removeByIds(list) ? Result.ok() : Result.fail();
    }
}
