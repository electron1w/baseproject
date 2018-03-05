package top.zhwen.controller;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 * Created by Zhw on 2017/8/3.
 */
@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    // 登录提交地址和applicationontext-shiro.xml配置的loginurl一致。 (配置文件方式的说法)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, Model model) throws Exception {
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = " ";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                msg = "账号或密码错误";//UnknownAccountException 账号不存在
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                msg = "账号或密码错误";//IncorrectCredentialsException -- > 密码不正确
            } else if ("kaptchaValidateFailed".equals(exception)) {
                msg = "验证码错误";//kaptchaValidateFailed -- > 验证码错误
            } else {
                msg = exception;
            }
            model.addAttribute("msg",msg);
            return "login";
        } else {
            return "redirect:index";
        }
    }
}
