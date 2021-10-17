package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;

import indi.nonoas.xbh.common.error.ErrorEnum;
import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录相关api
 */
public class LoginInfoApi extends BaseApi {

    /**
     * 请求数据异常
     */
    public static final int REQUEST_FAIL = -1;

    /**
     * 登录成功
     */
    public static final int LOGIN_SUCCESS = 0;

    public static final int WRONG_LOGIN_INFO = 1;

    /**
     * 登录请求 api
     *
     * @param handler ui处理器
     * @param user    用户信息
     */
    public static void login(Handler handler, User user) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(fullURL(MessageFormat.format("login/login?userId={0}&password={1}", user.getUserId(), user.getPassword())))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message msg = new Message();
                msg.what = REQUEST_FAIL;
                handler.sendMessage(msg);
                Log.e(ILogTag.DEV, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Message msg = new Message();
                if (response.isSuccessful()) {
                    JSONObject json = (JSONObject) JSONObject.parse(Objects.requireNonNull(response.body()).string());
                    if (checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"))) {
                        msg.what = LOGIN_SUCCESS;
                    } else {
                        msg.what = WRONG_LOGIN_INFO;
                    }
                    msg.obj = json;
                } else {
                    msg.what = REQUEST_FAIL;
                }
                handler.sendMessage(msg);
            }
        });
    }
}
