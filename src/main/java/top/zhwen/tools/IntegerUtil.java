package top.zhwen.tools;

/**
 * Created by Zhw on 2017/8/15.
 */
public class IntegerUtil {

    /**
     * Integer.parseInt("x")
     *
     * @param intStr
     * */
    public static Integer parseInt(String intStr) {
        if (!StringUtil.isNull(intStr)) {
            return Integer.parseInt(intStr);
        } else {
            return null;
        }
    }
}
