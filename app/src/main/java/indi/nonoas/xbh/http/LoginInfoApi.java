package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import indi.nonoas.xbh.activity.RegistryActivity;
import indi.nonoas.xbh.common.error.ErrorEnum;
import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.pojo.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录相关api
 */
public class LoginInfoApi extends BaseApi {

    /**
     * 登录成功
     */
    public static final int LOGIN_SUCCESS = 0;

    public static final int WRONG_LOGIN_INFO = 1;

    /**
     * 发送验证码成功
     */
    public static final int SEND_VERIFY_CODE_SUCCESS = 2;

    /**
     * 发送验证码失败
     */
    public static final int SEND_VERIFY_CODE_FAIL = 3;

    /**
     * 注册成功
     */
    public static final int REGISTRY_SUCCESS = 4;

    /**
     * 注册失败
     */
    public static final int REGISTRY_FAILURE = 5;

    /**
     * cookie
     */
    private static String cookie = "";

    /**
     * 登录请求 api
     *
     * @param handler ui处理器
     * @param user    用户信息
     */
    public static void login(Handler handler, User user) {
        BaseApi.asyncGet(String.format("login/login?userId=%s&password=%s", user.getUserId(), user.getPassword()),
                new UICallback(handler) {
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
                }
        );
    }

    /**
     * 注册api
     *
     * @param userId     用户名
     * @param password   密码
     * @param verifyCode 验证码
     * @param handler    事件处理器
     */
    public static void registry(String userId, String password, String verifyCode, Handler handler) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("cookie", cookie)
                .post(new FormBody.Builder()
                        .add("userId", userId)
                        .add("password", password)
                        .add("verifyCode", verifyCode)
                        .build())
                .url(fullURL("login/registry"))
                .build();
        client.newCall(request).enqueue(new UICallback(handler) {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Message msg = new Message();
                if (response.isSuccessful()) {
                    JSONObject json = (JSONObject) JSONObject.parse(response.body().string());
                    if (checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"))) {
                        msg.what = REGISTRY_SUCCESS;
                    } else {
                        msg.what = REGISTRY_FAILURE;
                    }
                    msg.obj = json;
                } else {
                    msg.what = REQUEST_FAIL;
                }
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 发送注册验证码
     *
     * @param handler 事件处理器 {@link RegistryActivity#sendVerifyCode()}
     */
    public static void sendVerityCode(String email, Handler handler) {
        BaseApi.asyncGet(
                String.format("mail/sendVerifyMail?email=%s", email),
                new UICallback(handler) {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Message msg = new Message();
                        if (response.isSuccessful()) {
                            JSONObject json = (JSONObject) JSONObject.parse(Objects.requireNonNull(response.body()).string());
                            // 发送成功
                            if (checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"))) {
                                msg.what = SEND_VERIFY_CODE_SUCCESS;
                            } else {
                                msg.what = SEND_VERIFY_CODE_FAIL;
                            }
                            msg.obj = json;
                            // 保存sessionId
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0);
                            LoginInfoApi.cookie = session.substring(0, session.indexOf(";"));
                        } else {
                            msg.what = REQUEST_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                }
        );
    }
}
