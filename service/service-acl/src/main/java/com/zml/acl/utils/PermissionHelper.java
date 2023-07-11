package com.zml.acl.utils;

import com.zml.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-10 18:19
 */
public class PermissionHelper {

    public static List<Permission> buildPermission(List<Permission> allList) {
        List<Permission> list = new ArrayList<>();
        for (Permission permission : allList) {
            if (permission.getPid() == 0) {
                permission.setLevel(1);
                list.add(findChildren(permission, allList));
            }
        }
        return list;
    }

    private static Permission findChildren(Permission permission, List<Permission> list) {
        permission.setChildren(new ArrayList<>());

        for (Permission it : list) {
            if (permission.getId().longValue() == it.getPid().longValue()) {
                int level = permission.getLevel() + 1;
                it.setLevel(level);
                if (Objects.isNull(permission.getChildren())) {
                    permission.setChildren(new ArrayList<>());
                }
                permission.getChildren().add(findChildren(it, list));
            }
        }
        return permission;
    }
}
