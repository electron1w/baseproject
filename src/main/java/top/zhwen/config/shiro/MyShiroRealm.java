package top.zhwen.config.shiro;

import top.zhwen.domain.Permission;
import top.zhwen.domain.Role;
import top.zhwen.domain.User;
import top.zhwen.service.PermissionService;
import top.zhwen.service.RoleService;
import top.zhwen.service.UserService;
import top.zhwen.tools.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Zhw on 2017/8/11.
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    /**
     * 授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        List<Role> userRoleList = roleService.findRoleByUser(user.getUid());
        for (Role r : userRoleList) {
            //进行角色添加
            simpleAuthorizationInfo.addRole(r.getRole());
            List<Permission> permissions = permissionService.findByRoleId(r.getId());
            for (Permission p : permissions) {
                if(StringUtil.isNull(p.getPermission())){
                    continue;
                }
                //权限的添加
                simpleAuthorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 验证用户登录信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户输入的账号
        String username = (String) authenticationToken.getPrincipal();
        //通过username数据库查找user对象
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                getName()
        );
        return authenticationInfo;
    }
}
