package top.zhwen.controller;

import top.zhwen.domain.Permission;
import top.zhwen.service.PermissionService;
import top.zhwen.tools.UserUtil;
import top.zhwen.vo.PermissionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * 主页
 * Created by zhw on 2017/8/1.
 */


@Controller
public class IndexController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/main")
    public String main(Model model) {
        model.addAttribute("name", UserUtil.getLoginUserName());
        return "main";
    }

    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        List<PermissionVO> permissionVOS = getPermissionVOList();
        model.addAttribute("permissions", permissionVOS);
        model.addAttribute("userName", UserUtil.getLoginUserName());

        return "index";
    }


    private List<PermissionVO> getPermissionVOList() {
        List<Permission> permissions = permissionService.findMenuByUidAndIsParent(UserUtil.getLoginUserId(), true);
        List<PermissionVO> permissionVOS = new LinkedList<>();
        List<Permission> permissionSon = permissionService.findMenuByUidAndIsParent(UserUtil.getLoginUserId(), false);
        for (Permission p : permissions) {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(p, permissionVO);
            List<Permission> permissionSonCopy = new LinkedList<>();
            for (Permission ps : permissionSon) {
                if (ps.getParentid() == p.getId()) {
                    permissionSonCopy.add(ps);
                }
            }
            permissionVO.setChildPermission(permissionSonCopy);
            permissionVOS.add(permissionVO);
        }
        return permissionVOS;
    }

    @RequestMapping("/403")
    public String error() {
        return "error";
    }
}
