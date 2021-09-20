package indi.nonoas.xbh.utils;

import android.graphics.Color;
import android.view.Window;

import androidx.core.view.WindowInsetsControllerCompat;

import indi.nonoas.xbh.common.AppStore;

/**
 * @author Nonoas
 * @date 2021-09-20 20:09
 */
public class SystemUtil {

	private SystemUtil() {

	}

	public static void toggleStatusBarColor(StatusBarType type) {
		WindowInsetsControllerCompat winInsetCtrlCompat = AppStore.getWinInsetCtrlCompat();
		Window currWindow = AppStore.getCurrWindow();
		if (null == winInsetCtrlCompat || null == currWindow) {
			return;
		}
		if (StatusBarType.DARK == type) {
			AppStore.getWinInsetCtrlCompat().setAppearanceLightStatusBars(false);
			AppStore.getCurrWindow().setStatusBarColor(Color.TRANSPARENT);
		} else {
			AppStore.getWinInsetCtrlCompat().setAppearanceLightStatusBars(true);
			AppStore.getCurrWindow().setStatusBarColor(Color.WHITE);
		}
	}


	public enum StatusBarType {
		DARK, LIGHT
	}

}
