package top.zhwen.controller;

import com.alibaba.fastjson.JSONObject;
import top.zhwen.constants.UserConstants;
import top.zhwen.domain.Permission;
import top.zhwen.domain.RolePermission;
import top.zhwen.service.PermissionService;
import top.zhwen.service.RolePermissionService;
import top.zhwen.tools.MessageUtil;
import top.zhwen.tools.ServletUtil;
import top.zhwen.tools.StringUtil;
import top.zhwen.tools.UserUtil;
import top.zhwen.vo.PermissionVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zhw on 2017/9/5.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @RequiresPermissions("menu:all")//权限管理;
    public String menuShow() {
        return "menu-list";
    }

    /**
     * ajax fuelux.tree-sampledata.js  getTreeData() 获取菜单数据
     */
    @RequestMapping(value = "/queryMenu", method = RequestMethod.POST)
    //@RequiresPermissions("menu:queryMenu")//权限管理;
    public void menuList(HttpServletRequest request, HttpServletResponse response) {
        Permission permission = new Permission();
        permission.setParentid(UserConstants.first_order_menu);
        List<Permission> permissionParent = permissionService.findByPermissions(permission);
        List<PermissionVO> permissionVOS = new LinkedList<PermissionVO>();
        for (Permission p : permissionParent) {
            PermissionVO permissionVO = new PermissionVO();
            permission.setParentid(p.getId());
            BeanUtils.copyProperties(p, permissionVO);
            List<Permission> permissionSon = permissionService.findByPermissions(permission);
            permissionVO.setChildPermission(permissionSon);
            permissionVOS.add(permissionVO);
        }
        JSONObject jo = new JSONObject();
        jo.put("permissionList", permissionVOS);
        jo.put("permissionParent", permissionParent);
        ServletUtil.createSuccessResponse(200, jo, response);
    }
    /**
     * 获取菜单选择值
     * */
    @PostMapping("/getMenuSelect")
    public void getMenuSelect(Integer type,HttpServletResponse response){
        Permission permission = new Permission();
        List<Permission> permissionList=null;
        if(type==0) {
            permission.setParentid(UserConstants.first_order_menu);
            permissionList = permissionService.findByPermissions(permission);
        }else{
            permission.setResourcetype(UserConstants.the_menu_type_menu);
            permissionList = permissionService.findByPermissionII(permission);
        }
        JSONObject jo = new JSONObject();
        jo.put("permissionList", permissionList);
        ServletUtil.createSuccessResponse(200, jo, response);
    }
    /**
     * ajax menu-list.js  menuInfo() 根据name获取当前菜单信息返回
     */
    @RequestMapping(value = "/queryByName", method = RequestMethod.POST)
    //@RequiresPermissions("menu:queryByName")//权限管理;
    public void queryByName(String name, HttpServletResponse response) {
        if (name != null) {
            Permission permission = new Permission();
            name = name.trim();
            permission.setName(name);
            permission.setResourcetype(UserConstants.the_menu_type_menu);
            List<Permission> permissions = permissionService.findByPermissions(permission);
            if (permissions != null && permissions.size() > 0) {
                permission = new Permission();
                permission.setParentid(permissions.get(0).getId());
                permission.setResourcetype(UserConstants.the_menu_type_button);
                permissions.addAll(permissionService.findByPermissions(permission));
            }
            JSONObject json = new JSONObject();
            json.put("permissions", permissions);
            ServletUtil.createSuccessResponse(200, permissions, response);
        }
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public void queryById(Long id, HttpServletResponse response) {
        if (id != null) {
            Permission permission = new Permission();
            permission.setId(id);
            List<Permission> permissions = permissionService.findByPermissions(permission);
            permission = new Permission();
            permission.setResourcetype(UserConstants.the_menu_type_menu);
            List<Permission> permissionList = permissionService.findByPermissionII(permission);
            permissions.addAll(permissionList);
            JSONObject json = new JSONObject();
            json.put("permissions", permissions);
            ServletUtil.createSuccessResponse(200, permissions, response);
        }
    }

    @RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
    //@RequiresPermissions("menu:saveMenu")//权限管理;
    public void saveMenu(Permission permission, String checkType, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String message = checkInfo(permission, checkType);
        if (!StringUtil.isNull(message)) {
            MessageUtil.failMessageFlag(message, response);
            return;
        }
        if ("1".equals(checkType)) {//判断当前更新
            int index = permissionService.updatePermissions(permission);
            if (index > 0) {
                result.put("message", "菜单修改成功!");
                result.put("flag", true);
                logger.info("修改菜单,操作人:" + UserUtil.getLoginUserName());
            } else {
                result.put("message", "菜单修改失败!");
                result.put("flag", false);
            }
        } else if ("2".equals(checkType)) {
            int index = 0;
            if (permission.getId() != null && permission.getId() > 7) {//默认菜单
                //TODO
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermissionid(permission.getId());
                rolePermissionService.delRolePermission(rolePermission);
                index = permissionService.delPermission(permission.getId());
            }
            if (index > 0) {
                result.put("message", "菜单删除成功!");
                result.put("flag", true);
                logger.info("删除菜单,操作人:" + UserUtil.getLoginUserName());
            } else {
                result.put("message", "菜单删除失败!");
                result.put("flag", false);
            }
        } else {
            int index = permissionService.addPermissions(permission);
            if (index > 0) {
                result.put("message", "菜单添加成功!");
                result.put("flag", true);
                logger.info("添加一级菜单,操作人:" + UserUtil.getLoginUserName());
            } else {
                result.put("message", "菜单添加失败!");
                result.put("flag", false);
            }
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    private String checkInfo(Permission permission, String checkType) {
        if (permission == null) {
            return "系统异常,请检查参数!";
        }
        if (!"0".equals(checkType) && permission.getId() == null) {
            return "无法获取当前菜单信息,请刷新页面重新提交!";
        }
        if (StringUtil.isNull(permission.getName())) {
            return "无法获取当前菜单信息,请刷新页面重新提交!";
        } else {
            if(!permission.getResourcetype().equals(UserConstants.the_menu_type_button)) {
                Permission permission1 = new Permission();
                permission1.setName(permission.getName());
                List<Permission> permissions = permissionService.findByPermissions(permission1);
                if (permissions != null && permissions.size() > 0 && permission.getId() != permissions.get(0).getId()) {
                    return "已存在对应菜单,请重新输入!";
                }
            }
        }
        if (permission.getParentid() == null) {
            return "无法获取当前菜单信息,请刷新页面重新提交!";
        }
        return null;
    }
}
