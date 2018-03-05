package top.zhwen.service.impl;

import top.zhwen.dao.UserRoleMapper;
import top.zhwen.domain.UserRole;
import top.zhwen.domain.UserRoleExample;
import top.zhwen.domain.UserRoleExample.Criteria;
import top.zhwen.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> findUserRoleById(Long uid) {
        UserRoleExample example = new UserRoleExample();
        Criteria criteria = example.createCriteria();
        if (uid != null) {
            criteria.andUidEqualTo(uid);
        }
        return userRoleMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public int addUserRole(UserRole ur) {
        return userRoleMapper.insert(ur);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public int delUserRole(UserRole ur) {
        UserRoleExample example = new UserRoleExample();
        Criteria criteria = example.createCriteria();
        proSearchParam(ur,criteria);
        return userRoleMapper.deleteByExample(example);
    }

    @Override
    public int deleteByIds(String[] ids,Long adminId) {
        return this.userRoleMapper.deleteByIds(ids,adminId);
    }

    @Override
    public int deleteByRoleIds(String[] Roleids) {
        return this.userRoleMapper.deleteByRoleIds(Roleids);
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(UserRole ur, Criteria criteria) {
        if (ur != null) {
            if (ur.getUid() != null) {
                criteria.andUidEqualTo(ur.getUid());
            }
            if (ur.getRoleid() != null) {
                criteria.andRoleidEqualTo(ur.getRoleid());
            }
        }
    }
}