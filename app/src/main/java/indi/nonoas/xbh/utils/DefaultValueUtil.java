package indi.nonoas.xbh.utils;

/**
 * @author Nonoas
 * @date 2021-09-21 13:45
 */
public class DefaultValueUtil {

    private DefaultValueUtil() {
    }

    /**
     * @param str 参数
     * @return 相当于 StringUtils.isEmpty(str) ? str : defVal;
     */
    public static String getValue(String str, String defVal) {
        return StringUtils.isEmpty(str) ? defVal : str;
    }

    /**
     * @param str 参数
     * @return 相当于 StringUtils.isEmpty(str) ? str : "";
     */
    public static String getValOrNullStr(String str) {
        return getValue(str, "");
    }

    /**
     * @param str 参数
     * @return 相当于 StringUtils.isEmpty(str) ? str : "0";
     */
    public static String getValOrZero(String str) {
        return getValue(str, "0");
    }
}
