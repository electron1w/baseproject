package top.zhwen.tools;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Zhw on 2017/10/19.
 */
public class MessageUtil {
    public static void uppOrAddMessage(long index, String sucessMessage, String failMessage, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        if (index > 0) {
            result.put("message", sucessMessage);
            result.put("flag", true);
        } else {
            result.put("message", failMessage);
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    public static void failMessageFlag(String failMessage, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        result.put("message", failMessage);
        result.put("flag", false);
        ServletUtil.createSuccessResponse(200, result, response);
    }

    public static void successMessageFlag(String failMessage, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        result.put("message", failMessage);
        result.put("flag", true);
        ServletUtil.createSuccessResponse(200, result, response);
    }
}
