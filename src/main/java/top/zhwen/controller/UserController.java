package top.zhwen.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import top.zhwen.constants.UserConstants;
import top.zhwen.domain.Role;
import top.zhwen.domain.User;
import top.zhwen.domain.UserRole;
import top.zhwen.service.OperLogsService;
import top.zhwen.service.RoleService;
import top.zhwen.service.UserRoleService;
import top.zhwen.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.zhwen.tools.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zhw on 2017/8/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private OperLogsService operLogsService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @RequiresPermissions("user:all")//权限管理;
    public String user(Model model) {
        if(UserUtil.isAdmin()){
            model.addAttribute("admin","admin");
        }
        return "user-list";
    }

    @RequestMapping(value = "/queryUserList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //@RequiresPermissions("user:queryUserList")//权限管理;
    public void userList(HttpServletRequest request, HttpServletResponse response) {
        String pageStr = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rowsStr = request.getParameter("rows"); // 取得每页显示行数，,注意这是jqgrid自身的参数
        Integer page = IntegerUtil.parseInt(pageStr);
        Integer rows = IntegerUtil.parseInt(rowsStr);
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        List<User> userList = userService.queryUserList(user, page, rows);
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        JSONObject jo = new JSONObject();
        jo.put("rows", userList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records", pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/queryRoleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //@RequiresPermissions("user:queryRoleList")//权限管理;
    public void roleList(HttpServletRequest request, HttpServletResponse response) {
        String uidStr = request.getParameter("uid");
        Long uid = LongUtil.parseLong(uidStr);
        List<UserRole> userRoleList = userRoleService.findUserRoleById(uid);
        List<Role> roleAllList = roleService.finaAllRole();
        JSONObject jo = new JSONObject();
        jo.put("userRoleList", userRoleList);
        jo.put("roleAllList", roleAllList);
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    //@RequiresPermissions("user:saveRole")//权限管理;
    public void addUserRole(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String uidStr = request.getParameter("uid");
        String roleIdStr = request.getParameter("roleid");
        Long uid = LongUtil.parseLong(uidStr);
        if (uid == null) {
            result.put("message", "指定用户错误!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String[] roleId = roleIdStr.split(",");
        List<String> roleIdList = Arrays.asList(roleId);
        List<String> roleIdListCopy = new ArrayList<>(roleIdList);
        List<UserRole> userRoleList = userRoleService.findUserRoleById(uid);
        Long adminId = userService.findByUsername("admin").getUid();//admin id
        Role role = roleService.findByrole("admin");
        for (UserRole ur : userRoleList) {
            boolean success = true;
            for (String r : roleIdList) {
                if (ur.getRoleid() .equals(LongUtil.parseLong(r)) || (adminId .equals(uid)  && role.getId().equals(ur.getRoleid()))) {
                    roleIdListCopy.remove(r);
                    success = false;
                    break;
                }
            }
            if (success) {
                userRoleService.delUserRole(ur);
            }
        }
        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        for (String r : roleIdListCopy) {
            Long rl = LongUtil.parseLong(r);
            if (rl == null) {
                continue;
            }
            userRole.setRoleid(rl);
            userRoleService.addUserRole(userRole);
        }
        /*if (index > 0) {
            result.put("message", "信息修改成功!");
            result.put("flag", true);
        } else {*/
        result.put("message", "信息修改成功!");
        result.put("flag", true);
        //}
        logger.info(UserUtil.getLoginUserName()+":" + uid + "角色修改");
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 新添账号
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("user:add")//权限管理;
    public void addUser(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject result = UserUtil.userValidityDetection(name, username, password);
        if (result != null) {
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        } else {
            result = new JSONObject();
        }
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setSalt(CalculateUtil.generateMixRandomCode(UserConstants.saltLength));
        Object obj = new SimpleHash(UserConstants.algorithmName, password, user.getCredentialsSalt(), UserConstants.hashIterations);
        user.setPassword(obj.toString());
        user.setState(UserConstants.state);
        int index = userService.addUser(user);
        if (index > 0) {
            logger.info("操作人:"+UserUtil.getLoginUserName()+" "+"添加"+user.getUsername()+"账号");
            operLogsService.addLog(UserUtil.getLoginUserName(), "添加"+user.getUsername()+"账号", UserUtil.getUserIp(request));
        }
        MessageUtil.uppOrAddMessage(index, "账号添加成功!","账号添加失败!",response);
    }

    /**
     * 修改用户
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@RequiresPermissions("user:update")//权限管理;
    public void updateUser(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String uidStr = request.getParameter("uid");
        Long uid=LongUtil.parseLong(uidStr);
        if(uid==null){
            uid=UserUtil.getLoginUserId();
        }
        User user = userService.findByUid(uid);
        if (user == null) {
            result.put("message", "系统异常请重试!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String name = request.getParameter("name");
        if (StringUtil.isNull(name)) {
            result.put("message", "名称不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String oldpassword = request.getParameter("oldpassword");
        if (StringUtil.isNull(oldpassword)) {
            result.put("message", "请输入旧密码!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Object obj = new SimpleHash(UserConstants.algorithmName, oldpassword, user.getCredentialsSalt(), UserConstants.hashIterations);
        if (!obj.toString().equals(user.getPassword())) {
            result.put("message", "旧密码错误!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String password = request.getParameter("password");
        if (StringUtil.isNull(password)) {
            result.put("message", "新密码不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        if (!ValidatorUtil.isPasswd(password)) {
            result.put("message", "新密码只能是数字或者字母，并且长度为6-12位!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        user.setSalt(CalculateUtil.generateMixRandomCode(UserConstants.saltLength));
        Object obj2 = new SimpleHash(UserConstants.algorithmName, password, user.getCredentialsSalt(), UserConstants.hashIterations);
        user.setPassword(obj2.toString());
        user.setState(UserConstants.state);
        user.setUsername(user.getUsername());
        user.setName(name);
        int index = userService.updateUser(user);
        if (index > 0) {
            logger.info("操作人:"+UserUtil.getLoginUserName()+" "+"修改"+user.getUsername()+"账号");
            operLogsService.addLog(UserUtil.getLoginUserName(), "修改"+user.getUsername()+"账号", UserUtil.getUserIp(request));
        }
        MessageUtil.uppOrAddMessage(index, "账号修改成功!","账号修改失败!",response);
    }

    /**
     * 删除
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    //@RequiresPermissions("user:delete")//权限管理;
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids");
        Long adminId = userService.findByUsername("admin").getUid();
        userRoleService.deleteByIds(ids.split(","), adminId);
        int index = userService.deleteByIds(ids.split(","));
        if (index > 0) {
            logger.info("操作人:"+UserUtil.getLoginUserName()+" "+"删除"+ids+"账号");
            operLogsService.addLog(UserUtil.getLoginUserName(), "删除"+ids+"账号", UserUtil.getUserIp(request));
        }
        MessageUtil.uppOrAddMessage(index, "账号删除成功!","账号删除失败!",response);
    }
    /**
     * 重置密码
     * */
    @PostMapping("reset")
    public void resetPwd(Long id,HttpServletRequest request,HttpServletResponse response){
        if(!UserUtil.isAdmin()){
            MessageUtil.failMessageFlag("您无权进行此操作",response);
            return;
        }
        if(id==null){
            MessageUtil.failMessageFlag("无法获取重置员工",response);
            return;
        }
        User user=userService.findByUid(id);
        user.setSalt(CalculateUtil.generateMixRandomCode(UserConstants.saltLength));
        String password=CalculateUtil.generateDigitRandomCode(UserConstants.restPwdDigit);
        Object obj2 = new SimpleHash(UserConstants.algorithmName, password, user.getCredentialsSalt(), UserConstants.hashIterations);
        user.setPassword(obj2.toString());
        int index=userService.updateUser(user);
        if(index>0){
            logger.info("操作人:"+UserUtil.getLoginUserName()+" "+"重置"+user.getUsername()+"密码为:"+password);
            operLogsService.addLog(UserUtil.getLoginUserName(), "重置"+user.getUsername()+"密码为:"+password, UserUtil.getUserIp(request));
        }
        MessageUtil.uppOrAddMessage(index,"密码重置为:"+password,"重置失败,请重试",response);
    }
}
