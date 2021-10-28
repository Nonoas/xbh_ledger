package indi.nonoas.xbh.view;

import android.content.Context;
import android.widget.Toast;

public class CoverableToast {

    private static Toast toast;

    public static void showToast(Context context, String msg, int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, length);
        toast.show();
    }

    /**
     * 显示 “长时间” 的 Toast
     * @param context 上下文对象
     * @param msg 消息文本
     */
    public static void showLongToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示 “短时间” 的 Toast
     * @param context 上下文对象
     * @param msg 消息文本
     */
    public static void showShortToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }
}
