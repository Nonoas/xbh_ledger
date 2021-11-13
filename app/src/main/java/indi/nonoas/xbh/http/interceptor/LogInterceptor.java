package indi.nonoas.xbh.http.interceptor;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

import indi.nonoas.xbh.common.AppStore;
import indi.nonoas.xbh.common.error.ErrorEnum;
import indi.nonoas.xbh.http.BaseApi;
import indi.nonoas.xbh.pojo.User;
import indi.nonoas.xbh.utils.CookieUtil;
import indi.nonoas.xbh.utils.HttpUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 登录拦截器，用于登录失效时重连
 */
public class LogInterceptor implements Interceptor {

    public static String cookie = "";

    @SuppressWarnings("all")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalReq = chain.request();
        Response originalResp = chain.proceed(originalReq);
        // 如果请求失败，则直接返回响应
        if (!originalResp.isSuccessful()) {
            return originalResp;
        }
        ResponseBody respBody = originalResp.body();
        MediaType mediaType = respBody.contentType();
        String bodyStr = respBody.string();
        respBody.close();
        // 判断是否登录过期
        JSONObject json = JSONObject.parseObject(bodyStr);
        if (HttpUtil.checkErrorCode(ErrorEnum.LOGIN_EXPIRED, json)) {
            String currCookie = CookieUtil.getCookie(originalReq.url().toString(), originalReq.url().host());
            synchronized (LogInterceptor.class) {
                // 如果cookie已经更新，则携带cookie重新请求
                if (!cookie.equals(currCookie)) {
                    Request.Builder builder = originalReq.newBuilder().header("Cookie", cookie);
                    return chain.proceed(builder.build());
                }

                // 发起登录
                Request loginRequest = getLoginRequest();
                Response loginResp = chain.proceed(loginRequest);
                // 如果登录成功再重新发送原来的请求
                if (loginResp.isSuccessful()) {
                    dealCookie(loginRequest, loginResp);
                    loginResp.body().close();
                    Request.Builder builder = originalReq.newBuilder().header("Cookie", cookie);
                    return chain.proceed(builder.build());
                }
            }
        }
        // 重建原来的请求
        return originalResp.newBuilder()
                .body(ResponseBody.create(mediaType, bodyStr))
                .build();
    }

    /**
     * 处理返回的cookie
     *
     * @param loginReq  登录请求
     * @param loginResp 登录响应
     */
    private void dealCookie(Request loginReq, Response loginResp) {
        List<String> cookies = loginResp.headers("Set-cookie");
        if (!cookies.isEmpty()) {
            cookie = CookieUtil.encodeCookie(cookies);
            CookieUtil.saveCookie(loginReq.url().toString(), loginReq.url().host(), cookie);
        }
    }

    // 获取登录的request
    private Request getLoginRequest() {
        User user = AppStore.getUser();
        String url = String.format("login/login?userId=%s&password=%s", user.getUserId(), user.getPassword());
        return new Request.Builder()
                .url(BaseApi.fullURL(url))
                .get()
                .build();
    }
}
