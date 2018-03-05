package top.zhwen.service;

import top.zhwen.domain.User;
import top.zhwen.vo.UserVO;

import java.util.List;

public interface UserService {
    /**
     * 根据 uid 查询用户信息
     * */
     User findByUid(Long uid);
    /**
     * 根据userName来查询用户信息
     */
     User findByUsername(String userName);
    /**
     * 分页查询
     *
     * @param user
     * @param page
     * @param rows
     */
     List<User> queryUserList(User user, Integer page, Integer rows);

    /**
     * 根据User获取角色表
     * */
     List<UserVO> queryUserRole(User user, Integer page, Integer rows);

    /**
     * 添加账号
     * */
     int addUser(User user);

    /**
     * 账号更新
     * */
     int updateUser(User user);

    /**
     * 批量删除
     * */
     int deleteByIds(String[] ids);
}