package indi.nonoas.xbh.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowInsetsControllerCompat;

import indi.nonoas.xbh.R;
import indi.nonoas.xbh.common.ColorTemplate;

/**
 * @author Nonoas
 * @date 2021-09-20 20:09
 */
public class SystemUtil {

    private SystemUtil() {

    }

    /**
     * 切换状态栏样式
     *
     * @param type 状态栏样式枚举
     */
    @SuppressLint("ResourceType")
    public static void toggleStatusBarColor(Activity activity, StatusBarType type) {
        Window window = activity.getWindow();
        WindowInsetsControllerCompat winInsetCtrlCompat = new WindowInsetsControllerCompat(window, window.getDecorView());
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        switch (type) {
            case DARK:
                winInsetCtrlCompat.setAppearanceLightStatusBars(false);
                window.setStatusBarColor(Color.TRANSPARENT);
                break;
            case GREEN:
                winInsetCtrlCompat.setAppearanceLightStatusBars(false);
                window.setStatusBarColor(ColorTemplate.GREEN_SOFT);
                break;
            default:
                winInsetCtrlCompat.setAppearanceLightStatusBars(true);
                window.setStatusBarColor(Color.WHITE);
                break;
        }
    }

    public enum StatusBarType {
        /**
         * 状态栏颜色：透明<br/>
         * 字体颜色：亮色
         */
        DARK,
        /**
         * 状态栏颜色：白色<br/>
         * 字体颜色：黑色
         */
        LIGHT,
        GREEN
    }

}
