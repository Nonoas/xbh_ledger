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
 * okHttp通用拦截
 */
public class LogInterceptor implements Interceptor {

    @SuppressWarnings("all")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalReq = chain.request();
        Response originalResp = chain.proceed(originalReq);
        ResponseBody body = originalResp.body();
        MediaType mediaType = body.contentType();
        String str = body.string();
        // 判断是否登录过期
        JSONObject json = JSONObject.parseObject(str);
        if (HttpUtil.checkErrorCode(ErrorEnum.LOGIN_EXPIRED, json.getString("errorCode"))) {
            originalResp.body().close();
            Request loginRequest = getLoginRequest();
            Response loginResponse = chain.proceed(loginRequest);
            // 如果登录成功再重新发送原来的请求
            if (loginResponse.isSuccessful()) {
                List<String> cookies = loginResponse.headers("set-cookie");
                if (!cookies.isEmpty()) {
                    String cookie = CookieUtil.encodeCookie(cookies);
                    CookieUtil.saveCookie(loginRequest.url().toString(), loginRequest.url().host(), cookie);
                }
                loginResponse.body().close();
                return chain.proceed(originalReq);
            }
        }
        // 重建原来的响应
        return originalResp.newBuilder()
                .body(ResponseBody.create(mediaType, str))
                .build();
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
