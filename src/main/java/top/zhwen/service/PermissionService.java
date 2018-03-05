package top.zhwen.service;

import top.zhwen.domain.Permission;

import java.util.List;


public interface PermissionService {
    /**
     * 联表查询Role与Permission 查询Roleid = Permissionid
     *
     */
    List<Permission> findByRoleId(Long id);
    /**
     * 根据uid 获取一级菜单
     * */
    List<Permission> findMenuByUidAndIsParent(Long id,boolean isParent);
    /**
     * 查询全部
     * */
    List<Permission> findAll();

    /**
     * 条件筛选
     * */
    List<Permission> findByPermissions(Permission permission);
    /**
     * 获取二级菜单
     * */
    List<Permission> findByPermissionII(Permission permission);
    /**
     * 根据roleId筛选
     * */
    Permission findById(Long roleId);

    /**
     * add
     * */
    int addPermissions(Permission permission);

    /**
     * update
     * */
    int updatePermissions(Permission permission);

    /**
     * del
     * */
    int delPermission(Long id);
}