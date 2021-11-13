package indi.nonoas.xbh.http.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import indi.nonoas.xbh.utils.LogUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求日志拦截器
 *
 * @author Nonoas
 * @date 2021/11/14
 */
public class LogInterceptor implements Interceptor {

    private static final String TAG = LogInterceptor.class.getName();

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        LogUtil.d(TAG, " ");
        String reqLog = "\n发起网络请求\n================================================\n" +
                req.toString() +
                "\n=================================================\n\n";
        LogUtil.d(TAG, reqLog);
        Response resp = chain.proceed(req);
        String respLog = "\n获取网络响应: \n=================================================\n" +
                resp.toString() +
                "\n=================================================\n\n";
        LogUtil.d(TAG, respLog);
        LogUtil.d(TAG, " ");

        return resp;
    }
}
