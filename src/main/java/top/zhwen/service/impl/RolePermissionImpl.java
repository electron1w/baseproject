package top.zhwen.service.impl;

import top.zhwen.dao.RolePermissionMapper;
import top.zhwen.domain.RolePermission;
import top.zhwen.domain.RolePermissionExample;
import top.zhwen.domain.RolePermissionExample.Criteria;
import top.zhwen.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolePermissionImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RolePermission> findByRoleId(Long roleId) {
        RolePermissionExample example = new RolePermissionExample();
        Criteria criteria = example.createCriteria();
        if (roleId != null) {
            criteria.andRoleidEqualTo(roleId);
        }
        return rolePermissionMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public int delRolePermission(RolePermission rp) {
        RolePermissionExample example = new RolePermissionExample();
        Criteria criteria = example.createCriteria();
        proSearchParam(rp,criteria);
        return this.rolePermissionMapper.deleteByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public int addRolePermission(RolePermission rp) {
        return this.rolePermissionMapper.insert(rp);
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(RolePermission rp, Criteria criteria) {
        if (rp != null) {
            if (rp.getRoleid()!= null) {
                criteria.andRoleidEqualTo(rp.getRoleid());
            }
            if (rp.getPermissionid() != null) {
                criteria.andPermissionidEqualTo(rp.getPermissionid());
            }
        }
    }
}