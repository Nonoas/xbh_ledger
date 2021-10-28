package indi.nonoas.xbh.http;

import java.util.concurrent.TimeUnit;

import indi.nonoas.xbh.common.error.ErrorEnum;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BaseApi {

    private static final String PROTOCOL_HTTP = "http";
    private static final String PROTOCOL_HTTPS = "https";
    private static final String BASE_ADDRESS = "u6c9db.natappfree.cc";
//    private static final String BASE_ADDRESS = "115.29.202.83:8090";


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
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
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
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
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


}
