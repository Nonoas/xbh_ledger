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
        BaseApi.asyncGet(url, new UICallback(handler) {
            @Override
            protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                JSONObject json = getRespBodyJson(response);
                msg.what = isRequestSuccess(json) ? QRY_SUCCESS : ADD_FAIL;
                msg.obj = json;
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
        String json = JSON.toJSONString(accBalance);
        BaseApi.asyncPost(
                "/acc/addAccBalance",
                RequestBody.Companion.create(json, MediaType.parse("application/json")),
                new UICallback(handler) {
                    @Override
                    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                        JSONObject json = getRespBodyJson(response);
                        if (isRequestSuccess(json)) {
                            msg.what = ADD_SUCCESS;
                        } else {
                            msg.what = ADD_FAIL;
                        }
                        msg.obj = json;
                    }
                }
        );
    }


}
