package indi.nonoas.xbh.view.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import indi.nonoas.xbh.R;

public class CoverableToast {

    private static Toast toast;

    public static final ToastType SUCCESS = ToastType.SUCCESS;

    public static final ToastType FAIL = ToastType.FAIL;

    public static void showToast(Context context, String msg, int length) {
        showToast(context, msg, null, length);
    }

    public static void showToast(Context context, String msg, ToastType type, int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.coverabletoast, null);
        ImageView iv = view.findViewById(R.id.toast_icon);
        TextView tv = view.findViewById(R.id.toast_text);

        if (type == ToastType.FAIL) {
            iv.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_failure));
        } else {
            iv.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_success));
        }

        tv.setText(msg);
        toast.setView(view);
        toast.setDuration(length);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();
    }

    /**
     * 显示 “长时间” 的 Toast
     *
     * @param context 上下文对象
     * @param msg     消息文本
     */
    public static void showLongToast(Context context, String msg, ToastType type) {
        showToast(context, msg, type, Toast.LENGTH_LONG);
    }

    /**
     * 显示 “短时间” 的 Toast
     *
     * @param context 上下文对象
     * @param msg     消息文本
     */
    public static void showShortToast(Context context, String msg, ToastType type) {
        showToast(context, msg, type, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, String msg) {
        showToast(context, msg, null, Toast.LENGTH_SHORT);
    }
}
