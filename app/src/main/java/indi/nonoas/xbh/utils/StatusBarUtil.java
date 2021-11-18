package indi.nonoas.xbh.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.TintContextWrapper;
import androidx.core.view.WindowInsetsControllerCompat;

import indi.nonoas.xbh.common.ColorTemplate;

/**
 * 状态栏工具
 *
 * @author Nonoas
 * @date 2021/11/17
 */
public class StatusBarUtil {
    /**
     * 切换状态栏样式
     *
     * @param type 状态栏样式枚举
     */
    @SuppressLint("ResourceType")
    public static void setStatusBar(Activity activity, StatusBarType type) {
        Window window = activity.getWindow();
        WindowInsetsControllerCompat winInsetCtrlCompat = new WindowInsetsControllerCompat(window, window.getDecorView());
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setBar(winInsetCtrlCompat, window, type);
    }

    private static void setBar(WindowInsetsControllerCompat winInsetCtrlCompat,
                               Window window,
                               StatusBarType type) {
        winInsetCtrlCompat.setAppearanceLightStatusBars(type.isLightStatus);
        window.setStatusBarColor(type.color);
    }


    public enum StatusBarType {

        DARK(Color.TRANSPARENT, false),
        LIGHT(Color.WHITE, true),
        GREEN(ColorTemplate.GREEN_SOFT, false);

        private final boolean isLightStatus;

        private final int color;

        StatusBarType(int color, boolean isLightStatus) {
            this.color = color;
            this.isLightStatus = isLightStatus;
        }
    }
}
