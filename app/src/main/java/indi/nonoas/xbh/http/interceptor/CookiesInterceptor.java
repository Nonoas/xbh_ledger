package indi.nonoas.xbh.http.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import indi.nonoas.xbh.MyApplication;
import indi.nonoas.xbh.http.BaseApi;
import indi.nonoas.xbh.utils.CookieUtil;
import indi.nonoas.xbh.utils.StringUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CookiesInterceptor implements Interceptor {

    private static final String COOKIE_PREF = "cookies_prefs";

    public CookiesInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 从内存读取cookie
        String cookie = BaseApi.cookies;
        if (StringUtils.isEmpty(cookie)) {
            cookie = getCookie(request.url().toString(), request.url().host());
        }
        // 从本地读取cookie，如果有则直接使用
        if (!TextUtils.isEmpty(cookie)) {
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Cookie", cookie);
            return chain.proceed(builder.build());
        }
        // 否则从response读取使用
        Response response = chain.proceed(request);
        List<String> cookies = response.headers("set-cookie");
        if (!cookies.isEmpty()) {
            cookie = CookieUtil.encodeCookie(cookies);
            CookieUtil.saveCookie(request.url().toString(), request.url().host(), cookie);
        }
        return response;
    }

    /**
     * 从SharedPreferences中读取cookie
     *
     * @param url    请求url
     * @param domain 主机
     * @return cookie字符串
     */
    private String getCookie(String url, String domain) {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }
        return null;
    }

    /**
     * 清除本地Cookie
     *
     * @param context Context
     */
    public static void clearCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
