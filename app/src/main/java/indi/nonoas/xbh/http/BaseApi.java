package indi.nonoas.xbh.http;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import indi.nonoas.xbh.common.error.ErrorEnum;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 业务Api调用基类
 */
public class BaseApi {

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";
    private static final String BASE_ADDRESS = "txq63h.natappfree.cc";

    /**
     * 服务地址
     */
//    private static final String BASE_ADDRESS = "115.29.202.83:8090";

    /**
     * 用于存放 cookies
     */
    public static String cookies = "";


    // 请求返回码 beg

    /**
     * 请求数据异常
     */
    public static final int REQUEST_FAIL = -1;

    // 请求返回码 end

    /**
     * 异步get请求
     *
     * @param url      请求地址（相对于根地址的相对地址）
     * @param callback 回调对象
     */
    public static void asyncGet(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .addHeader("cookie", cookies)
                .get()
                .url(fullURL(url))
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步post请求
     *
     * @param url      请求地址（相对于根地址的相对地址）
     * @param body     请求体
     * @param callback 回调对象
     */
    public static void asyncPost(String url, RequestBody body, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .addHeader("cookie", cookies)
                .post(body)
                .url(fullURL(url))
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String fullURL(String url) {
        return PROTOCOL_HTTP + "://" + BASE_ADDRESS + "/" + url;
    }

    /**
     * 检查errorCode是否匹配
     *
     * @param errorEnum 错误信息枚举
     * @param errStr    错误码
     * @return true：相同
     */
    public static boolean checkErrorCode(ErrorEnum errorEnum, String errStr) {
        return errorEnum.getErrorCode().equals(errStr);
    }

    /**
     * 判断是否请求成功
     *
     * @param json json字符串
     * @return 判断请求码是否与 {@link ErrorEnum#SUCCESS} 相同
     */
    protected static boolean isRequestSuccess(JSONObject json) {
        return checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"));
    }

    /**
     * 从 Response 中解析出 body 到 json
     *
     * @param response 响应体
     * @return JSONObject
     */
    protected static JSONObject getRespBodyJson(Response response) throws IOException {
        ResponseBody body = response.body();
        if (null == body) {
            return new JSONObject();
        }
        return (JSONObject) JSONObject.parse(body.string());
    }


}
