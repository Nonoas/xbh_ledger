package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import indi.nonoas.xbh.activity.RegistryActivity;
import indi.nonoas.xbh.pojo.User;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
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
     * 登录请求 api
     *
     * @param handler ui处理器
     * @param user    用户信息
     */
    public static void login(Handler handler, User user) {
        BaseApi.asyncGet(String.format("login/login?userId=%s&password=%s", user.getUserId(), user.getPassword()),
                new UICallback(handler) {
                    @Override
                    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                        JSONObject json = (JSONObject) JSONObject.parse(Objects.requireNonNull(response.body()).string());
                        if (BaseApi.isRequestSuccess(json)) {
                            msg.what = LOGIN_SUCCESS;
                            // 保存sessionId
                            saveCookies(response);
                        } else {
                            msg.what = WRONG_LOGIN_INFO;
                        }
                        msg.obj = json;
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

        BaseApi.asyncPost("login/registry",
                new FormBody.Builder()
                        .add("userId", userId)
                        .add("password", password)
                        .add("verifyCode", verifyCode)
                        .build(),
                new UICallback(handler) {
                    @Override
                    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                        JSONObject json = getRespBodyJson(response);
                        msg.what = isRequestSuccess(json) ? REGISTRY_SUCCESS : REGISTRY_FAILURE;
                        msg.obj = json;
                    }
                }
        );
    }

    /**
     * 发送注册验证码
     *
     * @param handler 事件处理器 {@link RegistryActivity#sendVerifyCode()}
     */
    @SuppressWarnings("all")
    public static void sendVerityCode(String email, Handler handler) {
        BaseApi.asyncGet(
                String.format("mail/sendVerifyMail?email=%s", email),
                new UICallback(handler) {
                    @Override
                    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                        JSONObject json = getRespBodyJson(response);
                        // 发送成功
                        msg.what = isRequestSuccess(json)
                                ? SEND_VERIFY_CODE_SUCCESS
                                : SEND_VERIFY_CODE_FAIL;
                        msg.obj = json;
                        // 保存sessionId
                        BaseApi.saveCookies(response);
                    }
                }
        );
    }
}
