package com.zml.acl.controller;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.acl.service.AdminService;
import com.zml.acl.service.RoleService;
import com.zml.result.Result;
import com.zml.ssyx.model.acl.Admin;
import com.zml.ssyx.vo.acl.AdminQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 21:19
 */
@RestController
@Api(tags = "用户接口")
//TODO 用户接口前端无法显示！疑似是跨域问题
@CrossOrigin(origins = "http://localhost:9528")
@RequestMapping("/admin/acl/user")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //    给用户分配角色
    @ApiOperation("用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId, @RequestParam List<Long> roleId) {
        return roleService.savaAdminRole(adminId, roleId) ? Result.ok() : Result.fail();
    }

    @ApiOperation("获取用户角色")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId) {
        Map<String, Object> map = roleService.getRoleByAdmin(adminId);
        return Result.ok(map);
    }

    //    获取后台用户分页列表(带搜索)
    @GetMapping("/{page}/{limit}}")
    @ApiOperation("获取后台用户分页列表")
    public Result<IPage<Admin>> getPageList(@PathVariable Long page, @PathVariable Long limit, AdminQueryVo vo) {
        Page<Admin> adminPage = new Page<>(page, limit);
        IPage<Admin> pageList = adminService.getPageList(adminPage, vo);
        return Result.ok(pageList);
    }

    //    根据ID获取某个后台用户
    @ApiOperation("根据IP查询用户")
    @GetMapping("/get/{id}")
    public Result getId(@PathVariable Long id) {
        return Result.ok(adminService.getById(id));
    }

    //    保存一个新的后台用户
    @ApiOperation("新增用户")
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin) {
        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        return adminService.save(admin) ? Result.ok() : Result.fail();
    }

    //    更新一个后台用户
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
//        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        return adminService.updateById(admin) ? Result.ok() : Result.fail();
    }

    //    删除某个用户
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result removeId(@PathVariable Long id) {
        return adminService.removeById(id) ? Result.ok() : Result.fail();
    }

    //    批量删除多个用户
    @ApiOperation("批量删除用户")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> list) {
        return adminService.removeByIds(list) ? Result.ok() : Result.fail();
    }
}

