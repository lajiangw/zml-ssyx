package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.acl.mapper.RoleMapper;
import com.zml.acl.service.AdminRoleService;
import com.zml.acl.service.RoleService;
import com.zml.ssyx.model.acl.AdminRole;
import com.zml.ssyx.model.acl.Role;
import com.zml.ssyx.vo.acl.RoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 18:14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public IPage<Role> selectRolePage(Page<Role> rolePage, RoleQueryVo roleQueryVo) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        String roleName = roleQueryVo.getRoleName();
//        构造条件，只有name不为空，才进行条件匹配
        wrapper.like(!StringUtils.isEmpty(roleName), Role::getRoleName, roleName);
//        将分页条件传入，和条件 进行查询返回。
        return baseMapper.selectPage(rolePage, wrapper);

    }

    @Override
//    根据id查询用户的角色
    public Map<String, Object> getRoleByAdmin(Long adminId) {
        /*
         * 1 查询所有角色
         * 2 根据id查询这个用户对应的角色
         * 3 遍历这个用户对应的角色信息
         * 4 将这个角色存入集合
         * 5 将所有角色 存入集合
         * */
        List<Role> assignRoles = new ArrayList<>();
        List<Role> allRolesList = baseMapper.selectList(null);
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        List<AdminRole> allRolesAdminList = adminRoleService.list(wrapper);
        List<Long> roleIdList = allRolesAdminList.stream().map(AdminRole::getRoleId).collect(Collectors.toList());
        allRolesList.forEach(role -> {
            if (roleIdList.contains(role.getId())) {
                assignRoles.add(role);
            }
        });
        Map<String, Object> map = new HashMap<>(10);
        map.put("assignRoles", assignRoles);
        map.put("allRolesList", allRolesList);
        return map;
    }

    @Override
    public Boolean savaAdminRole(Long adminId, List<Long> roleIds) {
        /*
         * 1 删除用户所有角色
         * 2 重新为用户分配角色
         * */
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleService.remove(wrapper);

        List<AdminRole> AdminRole = roleIds.stream().map(roleId -> {
            AdminRole adminRole = new AdminRole();
            adminRole.setRoleId(roleId);
            adminRole.setAdminId(adminId);
            return adminRole;
        }).collect(Collectors.toList());
        return adminRoleService.saveBatch(AdminRole);
    }
}
