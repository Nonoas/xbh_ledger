package indi.nonoas.xbh.common;

import android.view.Window;

import androidx.core.view.WindowInsetsControllerCompat;

import indi.nonoas.xbh.pojo.User;

/**
 * 储存应用程序的全局数据
 */
public class AppStore {

	/**
	 * 登录用户
	 */
	private static User user;

	/**
	 * 当前程序窗口
	 */
	private static Window currWindow;

	private static WindowInsetsControllerCompat winInsetCtrlCompat;

	private AppStore() {
	}


	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		AppStore.user = user;
	}

	public static Window getCurrWindow() {
		return currWindow;
	}

	public static void setCurrWindow(Window currWindow) {
		AppStore.currWindow = currWindow;
	}

	public static WindowInsetsControllerCompat getWinInsetCtrlCompat() {
		return winInsetCtrlCompat;
	}

	public static void setWinInsetCtrlCompat(WindowInsetsControllerCompat winInsetCtrlCompat) {
		AppStore.winInsetCtrlCompat = winInsetCtrlCompat;
	}
}
