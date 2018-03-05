package top.zhwen.service.impl;

import top.zhwen.dao.PermissionMapper;
import top.zhwen.domain.Permission;
import top.zhwen.domain.PermissionExample;
import top.zhwen.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        if (roleId != null) {
            return permissionMapper.selectByRoleId(roleId);
        }
        return null;
    }

    @Override
    public List<Permission> findMenuByUidAndIsParent(Long uid,boolean isParent) {
        if (uid != null) {
            return permissionMapper.queryParentMenuByUserId(uid,isParent);
        }
        return null;
    }

    @Override
    public List<Permission> findAll() {
        PermissionExample example = new PermissionExample();
        //Criteria criteria=example.createCriteria();
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByPermissions(Permission permission) {
        PermissionExample example = new PermissionExample();
        PermissionExample.Criteria criteria = example.createCriteria();
        proSearchParam(permission, criteria);
        example.setOrderByClause(" sort");
        return permissionMapper.selectByExample(example);
    }

    @Override
    public List<Permission> findByPermissionII(Permission permission) {
        PermissionExample example = new PermissionExample();
        PermissionExample.Criteria criteria = example.createCriteria();
        proSearchParam(permission, criteria);
        criteria.andParentidNotEqualTo(0L);
        example.setOrderByClause(" sort");
        return permissionMapper.selectByExample(example);
    }

    @Override
    public Permission findById(Long id) {
        if(id==null){
            return null;
        }
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addPermissions(Permission permission) {
        return permissionMapper.insertSelective(permission);
    }

    @Override
    public int updatePermissions(Permission permission) {
        return permissionMapper.updateByPrimaryKeySelective(permission);
    }

    @Override
    public int delPermission(Long id) {
        if(id==null){
            return 0;
        }
        return permissionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(Permission permission, PermissionExample.Criteria criteria) {
        if (permission != null) {
            if (permission.getId()!=null){
                criteria.andIdEqualTo(permission.getId());
            }
            if (permission.getParentid() != null) {
                criteria.andParentidEqualTo(permission.getParentid());
            }
            if (permission.getName() != null && permission.getName().trim().length() > 0) {
                criteria.andNameEqualTo(permission.getName().trim());
            }
            if (permission.getResourcetype() != null && permission.getResourcetype().trim().length() > 0) {
                criteria.andResourcetypeEqualTo(permission.getResourcetype());
            }
        }
    }
}