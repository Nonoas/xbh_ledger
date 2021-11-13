package indi.nonoas.xbh.utils;

import android.util.Log;

import indi.nonoas.xbh.common.log.ILogTag;

/**
 * @author Nonoas
 * @date 2021/11/14
 */
public class LogUtil {

    public static final String PREFIX = ILogTag.DEV;

    private LogUtil() {
    }

    public static void i(String tag, String msg) {
        Log.i(PREFIX + "_" + tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(PREFIX + "_" + tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(PREFIX + "_" + tag, msg);
    }
}
