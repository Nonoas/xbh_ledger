package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import indi.nonoas.xbh.common.error.ErrorEnum;
import indi.nonoas.xbh.common.log.ILogTag;
import indi.nonoas.xbh.pojo.AccBalance;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 账户余额api
 */
public class AccBalanceApi extends BaseApi {
    /**
     * 请求数据异常
     */
    public static final int REQUEST_FAIL = -1;

    /**
     * 查询成功
     */
    public static final int QRY_SUCCESS = 0;

    public static final int ADD_SUCCESS = 1;
    public static final int ADD_FAIL = 2;

    /**
     * 获取余额列表api
     */
    public static void qryBalance(String userId, long date, Handler handler) {
        String url = String.format(Locale.CHINA, "/acc/qryBalance?userId=%s&date=%d", userId, date);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(fullURL(url))
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
                    JSONObject json = (JSONObject) JSONObject.parse(response.body().string());
                    if (checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"))) {
                        msg.what = QRY_SUCCESS;
                    } else {
                        msg.what = ADD_FAIL;
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
     * 添加账户
     *
     * @param accBalance 账户余额信息
     * @param handler    UI处理器
     */
    public static void addAccBalance(AccBalance accBalance, Handler handler) {
        String url = "/acc/addAccBalance";
        String json = JSON.toJSONString(accBalance);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(RequestBody.Companion.create(json, MediaType.parse("application/json")))
                .url(fullURL(url))
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
                    JSONObject json = (JSONObject) JSONObject.parse(response.body().string());
                    if (checkErrorCode(ErrorEnum.SUCCESS, json.getString("errorCode"))) {
                        msg.what = ADD_SUCCESS;
                    } else {
                        msg.what = ADD_FAIL;
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
