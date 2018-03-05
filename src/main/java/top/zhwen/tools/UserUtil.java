package top.zhwen.tools;

import com.alibaba.fastjson.JSONObject;
import top.zhwen.constants.UserConstants;
import top.zhwen.domain.User;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zhw on 2017/9/7.
 */
public class UserUtil {
    /**
     * 获取用户ip
     * */
    public static String getUserIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getLoginUserName() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            return user.getName();
        }
        return null;
    }
    public static String getLoginName() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }
    public static Long getLoginUserId() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }
    /**
     * 判断当前用户是否为超级管理员 admin
     * */
    public static boolean isAdmin(){
        return UserConstants.adminUserName.equals(getLoginName());
    }

    public static JSONObject userValidityDetection(String name, String username, String password) {
        JSONObject result = new JSONObject();
        if (StringUtil.isNull(name)) {
            result.put("message", "昵称不能为空!");
            result.put("flag", false);
            return result;
        }
        if (StringUtil.isNull(username)) {
            result.put("message", "账户号不能为空!");
            result.put("flag", false);
            return result;
        }
        if (!ValidatorUtil.isUsername(username)) {
            result.put("message", "用户名格式只能是数字、字母或者下划线!");
            result.put("flag", false);
            return result;
        }
        if (StringUtil.isNull(password)) {
            result.put("message", "密码不能为空!");
            result.put("flag", false);
            return result;
        }
        if (!ValidatorUtil.isPasswd(password)) {
            result.put("message", "密码只能是数字或者字母，并长度为6-12位!");
            result.put("flag", false);
            return result;
        }
        return null;
    }
}
