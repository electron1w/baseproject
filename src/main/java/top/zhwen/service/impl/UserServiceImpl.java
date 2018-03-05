package top.zhwen.service.impl;

import com.github.pagehelper.PageHelper;
import top.zhwen.dao.UserMapper;
import top.zhwen.domain.User;
import top.zhwen.domain.UserExample;
import top.zhwen.domain.UserExample.Criteria;
import top.zhwen.service.UserService;
import top.zhwen.tools.StringUtil;
import top.zhwen.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public User findByUid(Long uid) {
        if (uid != null) {
            return this.userMapper.selectByPrimaryKey(uid);
        }else{
            return null;
        }
    }

    @Override
    public User findByUsername(String userName) {
        UserExample example = new UserExample();
        Criteria criteria = example.createCriteria();
        if (!StringUtil.isNull(userName)) {
            criteria.andUsernameEqualTo(userName);
        } else {
            return null;
        }
        List<User> users = this.userMapper.selectByExample(example);
        if (users != null && users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> queryUserList(User user, Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        UserExample example = new UserExample();
        Criteria criteria = example.createCriteria();
        proSearchParam(user, criteria);
        return this.userMapper.selectByExample(example);
    }

    @Override
    public List<UserVO> queryUserRole(User user, Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        UserExample example = new UserExample();
        Criteria criteria = example.createCriteria();
        proSearchParam(user, criteria);
        return this.userMapper.queryUserRole(example);
    }

    @Override
    public int addUser(User user) {
        return this.userMapper.insert(user);
    }

    @Override
    public int updateUser(User user) {
        return this.userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return this.userMapper.deleteByIds(ids);
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(User user, Criteria criteria) {
        if (user != null) {
            if (user.getUid() != null) {
                criteria.andUidEqualTo(user.getUid());
            }
            if (user.getName() != null && user.getName().trim().length() > 0) {
                criteria.andNameLike("%" + user.getName().trim() + "%");
            }
            if (user.getState() != null) {
                criteria.andStateEqualTo(user.getState());
            }
            if (user.getUsername() != null && user.getUsername().trim().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername().trim() + "%");
            }
        }
    }

}