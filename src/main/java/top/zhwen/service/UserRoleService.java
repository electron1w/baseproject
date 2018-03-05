package top.zhwen.service;

import top.zhwen.domain.UserRole;

import java.util.List;


public interface UserRoleService {

    /**
     * 根据用户ID获取用户权限
     * */
     List<UserRole> findUserRoleById(Long uid);

    /**
     * add
     * */

     int addUserRole(UserRole ur);

    /**
     * del
     * */
     int delUserRole(UserRole ur);

    /**
     * 批量删除
     * */
     int deleteByIds(String[] ids,Long adminId);

    /**
     * 批量删除
     * */
     int deleteByRoleIds(String[] Roleids);
}