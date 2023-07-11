package com.zml.acl.controller;

import com.zml.acl.service.PermissionService;
import com.zml.result.Result;
import com.zml.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-10 16:44
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("显示所有菜单")
    @GetMapping()
    public Result list() {
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);
    }

    @ApiOperation("添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody Permission permission) {
        return permissionService.save(permission) ? Result.ok() : Result.fail();
    }

    @ApiOperation("修改接口")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission) {
        return permissionService.updateById(permission) ? Result.ok() : Result.fail();
    }

    //    递归删除菜单
    @ApiOperation("删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok();
    }

    @ApiOperation("查看角色权限")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        return Result.ok(permissionService.toAssign(roleId));
    }

    @ApiOperation("给某个角色授权")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long roleId, @RequestParam List<Long> permissionId) {
        permissionService.doAssign(roleId, permissionId);
        return Result.ok();
    }
}
