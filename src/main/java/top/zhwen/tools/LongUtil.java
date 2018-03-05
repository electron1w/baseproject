package top.zhwen.tools;

/**
 * Created by Zhw on 2017/8/15.
 */
public class LongUtil {

    /**
     * Integer.parseInt("x")
     *
     * @param intStr
     * */
    public static Long parseLong(String intStr) {
        if (!StringUtil.isNull(intStr)) {
            return Long.parseLong(intStr);
        } else {
            return null;
        }
    }
}
