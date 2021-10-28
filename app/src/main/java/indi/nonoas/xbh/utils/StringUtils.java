package indi.nonoas.xbh.utils;

import java.util.Locale;

/**
 * 字符串工具
 *
 * @author Nonoas
 * @date 2021-09-21 13:34
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 判断字符串是否为 null 或 空白字符串
     *
     * @param str 源字符串
     * @return null 或 " " 返回 true
     */
    public static boolean isEmpty(String str) {
        return null == str || str.trim().isEmpty();
    }

    /**
     * 带 Local format 方法
     *
     * @param str  字符串模板
     * @param args 参数
     * @return 格式化后的字符串
     */
    public static String format(String str, Object... args) {
        return String.format(Locale.getDefault(), str, args);
    }
}
