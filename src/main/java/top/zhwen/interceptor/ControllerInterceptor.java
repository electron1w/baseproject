/*
package top.zhwen.interceptor;

import ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

*/
/**
 * 拦截器：记录用户操作日志，检查用户token
 * Created by Zhw on 2017/10/13.
 *//*

@Aspect
@Component
public class ControllerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);
    @Value("${spring.profiles}")//配置文件中属性信息
    private String env;

    */
/**
     * 定义拦截规则：拦截top.zhwen.controller.app包下面的所有类中，有@RequestMapping注解的方法。
     *//*

    @Pointcut("execution(* top.zhwen.controller.api..*(..)) and @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerMethodPointcut() {
    }

    */
/**
     * 拦截器具体实现
     *
     * @param pjp
     * @return ErrorCode（被拦截方法的执行结果，或需要登录的错误提示。）
     *//*

    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* top.zhwen.........)”写进这里
    public Object interceptor(ProceedingJoinPoint pjp) {
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        Set<Object> allParams = new LinkedHashSet<>(); //保存所有请求参数，用于输出到日志中
        logger.info("请求开始，方法：{}", methodName);
        Object result = null;
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            //logger.debug("arg: {}", arg);
            if (arg instanceof Map<?, ?>) {
                //提取方法中的MAP参数，用于记录进日志中
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) arg;

                allParams.add(map);
            } else if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                if (isLoginRequired(method)) {
                    if (!isLogin(request)) {
                        //result = new JsonResult(ResultCode.NOT_LOGIN, "请登录"）", null);
                    }
                }
                //获取query string 或 posted form data参数
                Map<String, String[]> paramMap = request.getParameterMap();
                if (paramMap != null && paramMap.size() > 0) {
                    allParams.add(paramMap);
                }
            } else if (arg instanceof HttpServletResponse) {
                //do nothing...
            } else {
                //allParams.add(arg);
            }
        }
        try {
            if (result == null) {
                // 一切正常的情况下，继续执行被拦截的方法
                result = pjp.proceed();
            }
        } catch (Throwable e) {
            logger.info("exception: ", e);
            //result = new JsonResult(ResultCode.EXCEPTION, "发生异常："+e.getMessage());
        }
        if (result instanceof ErrorCode) {
            long costMs = System.currentTimeMillis() - beginTime;
            logger.info("{}请求结束，耗时：{}ms", methodName, costMs);
        }
        return result;
    }

    */
/**
     * 判断一个方法是否需要登录
     *
     * @param method
     * @return
     *//*

    private boolean isLoginRequired(Method method) {
        if (!env.equals("prod")) { //只有生产环境才需要登录
            return false;
        }
        boolean result = true;
        */
/*if(method.isAnnotationPresent(Permission.class)){
            result = method.getAnnotation(Permission.class).loginReqired();
        }*//*

        return result;
    }

    //判断是否已经登录
    private boolean isLogin(HttpServletRequest request) {
        return true;
        */
/*String token = XWebUtils.getCookieByName(request, WebConstants.CookieName.AdminToken);
        if("1".equals(redisOperator.get(RedisConstants.Prefix.ADMIN_TOKEN+token))){
            return true;
        }else {
            return false;
        }*//*

    }
}
*/
