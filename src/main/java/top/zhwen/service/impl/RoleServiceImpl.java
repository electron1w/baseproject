package top.zhwen.service.impl;

import com.github.pagehelper.PageHelper;
import top.zhwen.dao.RoleMapper;
import top.zhwen.domain.Role;
import top.zhwen.domain.RoleExample;
import top.zhwen.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoleByUser(Long uid) {
        if (uid != null) {
            return roleMapper.selectByUserRoleId(uid);
        }
        return null;
    }

    @Override
    public List<Role> finaAllRole() {
        RoleExample example = new RoleExample();
        return roleMapper.selectByExample(example);
    }


    @Override
    public List<Role> queryRoleList(Role role, Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria=example.createCriteria();
        proSearchParam(role, criteria);
        return this.roleMapper.selectByExample(example);
    }

    @Override
    public int addUser(Role role) {
        return this.roleMapper.insert(role);
    }

    @Override
    public Role findById(Long id) {
        if (id!=null){
            return this.roleMapper.selectByPrimaryKey(id);
        }else {
            return null;
        }
    }

    @Override
    public int updateRole(Role role) {
        return this.roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return this.roleMapper.deleteByIds(ids);
    }

    @Override
    public Role findByrole(String role) {
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria=example.createCriteria();
        criteria.andRoleEqualTo(role);
        List<Role> roles=roleMapper.selectByExample(example);
        if (roles!=null&&roles.size()>0){
            return roles.get(0);
        }
        return null;
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(Role role, RoleExample.Criteria criteria) {
        if (role != null) {
            if (role.getId() != null) {
                criteria.andIdEqualTo(role.getId());
            }
            if (role.getAvailable() != null ) {
                criteria.andAvailableEqualTo(role.getAvailable());
            }
            if (role.getDescription() != null&& role.getDescription().trim().length() > 0) {
                criteria.andDescriptionLike("%" +role.getDescription()+ "%");
            }
            if (role.getRole() != null && role.getRole().trim().length() > 0) {
                criteria.andRoleLike("%" + role.getRole().trim() + "%");
            }
        }
    }
}