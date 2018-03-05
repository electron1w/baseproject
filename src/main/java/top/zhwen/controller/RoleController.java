package top.zhwen.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import top.zhwen.constants.UserConstants;
import top.zhwen.domain.Permission;
import top.zhwen.domain.Role;
import top.zhwen.domain.RolePermission;
import top.zhwen.service.PermissionService;
import top.zhwen.service.RolePermissionService;
import top.zhwen.service.RoleService;
import top.zhwen.service.UserRoleService;
import top.zhwen.tools.IntegerUtil;
import top.zhwen.tools.LongUtil;
import top.zhwen.tools.ServletUtil;
import top.zhwen.tools.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zhw on 2017/8/17.
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @RequiresPermissions("role:all")//权限管理;
    public String user() {
        return "role-list";
    }

    @RequestMapping(value = "/queryRoleList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //@RequiresPermissions("role:queryRoleList")//权限管理;
    public void roleList(HttpServletRequest request, HttpServletResponse response) {
        String pageStr = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rowsStr = request.getParameter("rows"); // 取得每页显示行数，,注意这是jqgrid自身的参数
        Integer page = IntegerUtil.parseInt(pageStr);
        Integer rows = IntegerUtil.parseInt(rowsStr);
        String description = request.getParameter("description");
        String roleStr = request.getParameter("role");
        Role role = new Role();
        role.setDescription(description);
        role.setRole(roleStr);

        List<Role> roleList = roleService.queryRoleList(role, page, rows);
        PageInfo<Role> pageInfo = new PageInfo<Role>(roleList);
        JSONObject jo = new JSONObject();
        jo.put("rows", roleList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records", pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    /**
     * 新增角色
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("role:add")//权限管理;
    public void addRole(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String description = request.getParameter("description");
        String roleStr = request.getParameter("role");
        if (StringUtil.isNull(description)) {
            result.put("message", "角色名不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        if (StringUtil.isNull(roleStr)) {
            result.put("message", "角色简称不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Role role = new Role();
        role.setDescription(description);
        role.setRole(roleStr);
        role.setAvailable(UserConstants.roleAvailable);//默认可用
        int index = roleService.addUser(role);
        if (index > 0) {
            result.put("message", "角色添加成功!");
            result.put("flag", true);
        } else {
            result.put("message", "角色添加失败");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 修改角色
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@RequiresPermissions("role:update")//权限管理;
    public void updateUser(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String id = request.getParameter("id");
        Role role = roleService.findById(LongUtil.parseLong(id));
        if (role == null) {
            result.put("message", "系统异常请稍后重试!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String description = request.getParameter("description");
        String roleStr = request.getParameter("role");
        if (StringUtil.isNull(description)) {
            result.put("message", "角色名称不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        if (StringUtil.isNull(roleStr)) {
            result.put("message", "角色简称不能为空!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        role.setDescription(description);
        role.setRole(roleStr);
        int index = roleService.updateRole(role);
        if (index > 0) {
            result.put("message", "角色修改成功!");
            result.put("flag", true);
        } else {
            result.put("message", "角色修改失败!");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 删除标题
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    //@RequiresPermissions("role:delete")//权限管理;
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids");
        JSONObject result = new JSONObject();
        userRoleService.deleteByRoleIds(ids.split(","));
        int index = roleService.deleteByIds(ids.split(","));
        if (index > 0) {
            result.put("message", "角色删除成功!");
            result.put("flag", true);
        } else {
            result.put("message", "角色删除失败!");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    @RequestMapping(value = "/queryPermissionList", method = RequestMethod.POST)
    //@RequiresPermissions("role:queryPermissionList")
    public void permissionList(HttpServletRequest request, HttpServletResponse response) {
        String roleidStr = request.getParameter("roleid");
        Long roleId = LongUtil.parseLong(roleidStr);
        List<RolePermission> rolePermissions = rolePermissionService.findByRoleId(roleId);
        Permission permission = new Permission();
        List<Permission> permissions = permissionService.findByPermissions(permission);
        JSONObject jo = new JSONObject();
        jo.put("rolePermissions", rolePermissions);
        jo.put("permissions", permissions);
        ServletUtil.createSuccessResponse(200, jo, response);
    }


    @RequestMapping(value = "/savePermissions",method = RequestMethod.POST)
    //@RequiresPermissions("role:savePermissions")
    public void savePermissions(HttpServletRequest request, HttpServletResponse response){
        JSONObject result = new JSONObject();
        String idStr=request.getParameter("id");
        String permissionsIdStr=request.getParameter("permissionsId");
        Long id=LongUtil.parseLong(idStr);
        if (id == null) {
            result.put("message", "用户获取失败!");
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String[] permissionsId = permissionsIdStr.split(",");
        List<String> permissionsIdList = Arrays.asList(permissionsId);
        List<String> permissionsIdCopy=new LinkedList<String>(permissionsIdList);
        List<RolePermission> rolePermissions=rolePermissionService.findByRoleId(id);
        for(RolePermission rp:rolePermissions){
            boolean success = true;
            for (String s:permissionsIdList){
                if(rp.getPermissionid()==LongUtil.parseLong(s)){
                    permissionsIdCopy.remove(s);
                    success = false;
                    break;
                }
            }
            if(success){
                rolePermissionService.delRolePermission(rp);
            }
        }
        RolePermission rp=new RolePermission();
        rp.setRoleid(id);
        for (String s:permissionsIdCopy){
            Long pi = LongUtil.parseLong(s);
            if (pi == null) {
                continue;
            }
            rp.setPermissionid(pi);
            rolePermissionService.addRolePermission(rp);
        }
        result.put("message", "信息修改成功!");
        result.put("flag", true);
        logger.info("角色:" + id + "授权修改");
        ServletUtil.createSuccessResponse(200, result, response);
    }
}
