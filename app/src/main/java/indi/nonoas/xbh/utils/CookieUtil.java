package indi.nonoas.xbh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import indi.nonoas.xbh.MyApplication;
import indi.nonoas.xbh.http.BaseApi;

public class CookieUtil {

    private static final String COOKIE_PREF = "cookies_prefs";

    private CookieUtil() {
    }

    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    public static void saveCookie(String url, String domain, String cookies) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        } else {
            editor.putString(url, cookies);
        }

        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies);
        }
        BaseApi.cookies = cookies;
        editor.apply();
    }

    /**
     * 整合cookie为唯一字符串
     */
    public static String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        // cookie去重
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            Collections.addAll(set, arr);
        }

        for (String cookie : set) {
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (last > 0 && sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }

    /**
     * 从SharedPreferences中读取cookie
     *
     * @param url    请求url
     * @param domain 主机
     * @return cookie字符串
     */
    public static String getCookie(String url, String domain) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        return "";
    }
}
