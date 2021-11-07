package indi.nonoas.xbh.http.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import indi.nonoas.xbh.common.log.ILogTag;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okHttp通用拦截
 */
public class LogInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(ILogTag.DEV, "===================================================");
        Log.i(ILogTag.DEV, "拦截器REQUEST:" + request.toString());
        Response response = chain.proceed(request);
        Log.i(ILogTag.DEV, "拦截器RESPONSE:" + response.toString());
        Log.i(ILogTag.DEV, "===================================================");
        return response;
    }
}
