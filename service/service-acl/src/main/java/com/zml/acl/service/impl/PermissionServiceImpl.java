package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.acl.mapper.PermissionMapper;
import com.zml.acl.service.PermissionService;

import com.zml.acl.service.RolePermissionService;
import com.zml.acl.utils.PermissionHelper;
import com.zml.ssyx.model.acl.Permission;
import com.zml.ssyx.model.acl.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZHANGMINLEI
 * @description 针对表【permission(权限)】的数据库操作Service实现
 * @createDate 2023-07-10 16:45:50
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public List<Permission> queryAllPermission() {
        List<Permission> permissions = baseMapper.selectList(null);
        return PermissionHelper.buildPermission(permissions);
    }

    @Override
    public void removeChildById(Long id) {
        ArrayList<Long> idslist = new ArrayList<>();
        this.getAllPermissionId(id, idslist);
        idslist.add(id);
        baseMapper.deleteBatchIds(idslist);
    }

    @Override
    public List<Permission> toAssign(Long roleId) {
        /*
         * 1 查询角色所有菜单
         * 2 根据id查询这个角色对应的权限
         * */
        ArrayList<Permission> permissions = new ArrayList<>();
//          菜单表
        List<Permission> permissionList = baseMapper.selectList(null);
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
//        查询该角色对应的菜单权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);
        List<Long> PermissionIdList = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        permissionList.forEach(item -> {
            if (PermissionIdList.contains(item.getPid())) {
                permissions.add(item);
            }
        });
        return permissions;
    }

    @Override
    public void doAssign(Long roleId, List<Long> permissionId) {
        /*
         * 1 删掉这个角色所有的权限，
         * 2 重新为他分配权限
         * */
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionService.remove(wrapper);


        List<RolePermission> list = permissionId.stream().map(item ->
        {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(item);
            return rolePermission;
        }).collect(Collectors.toList());
        rolePermissionService.saveBatch(list);
    }


    private void getAllPermissionId(Long id, ArrayList<Long> idslist) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid, id);
        List<Permission> childList = baseMapper.selectList(wrapper);
//        递归查询是否还有子菜单
        childList.stream().forEach(item -> {
            idslist.add(item.getId());
            this.getAllPermissionId(item.getId(), idslist);
        });
    }
}




