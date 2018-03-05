package top.zhwen.service;

import top.zhwen.domain.Role;

import java.util.List;

public interface RoleService {

    /**
     * 根据User获取Role与UserRole相应的List
     * */
     List<Role> findRoleByUser(Long uid);
    /**
     * 获取角色表
     * */
     List<Role> finaAllRole();

    /**
     * 分页查询
     *
     * @param role
     * @param page
     * @param rows
     */
     List<Role> queryRoleList(Role role, Integer page, Integer rows);

    /**
     * 添加账号
     * */
     int addUser(Role role);

    /**
     * 根据 id 查询用户信息
     * */
     Role findById(Long id);

    /**
     * 账号更新
     * */
     int updateRole(Role role);

    /**
     * 批量删除
     * */
     int deleteByIds(String[] ids);
    /**
     * 根据 role 查询
     * */
    Role findByrole(String role);
}