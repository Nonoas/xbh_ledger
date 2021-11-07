package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Locale;

import indi.nonoas.xbh.pojo.AccBalance;
import indi.nonoas.xbh.utils.StringUtils;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
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

    public static final int QRY_TOT_BALANCE_SUCCESS = 3;
    public static final int QRY_TOT_BALANCE_FAIL = 4;

    public static final int DEL_ACC_SUCCESS = 5;
    public static final int DEL_ACC_FAIL = 6;

    /**
     * 获取余额列表api
     */
    public static void qryBalance(String userId, long date, Handler handler) {
        String url = String.format(Locale.CHINA, "/acc/qryBalance?userId=%s&date=%d", userId, date);
        BaseApi.asyncGet(url, new BaseHttpCallback(handler) {
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
                RequestBody.create(MediaType.parse("application/json"), json),
                new BaseHttpCallback(handler) {
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

    /**
     * 查询周期余额
     *
     * @param userId  用户id
     * @param unit    周期单位
     * @param span    周期数
     * @param handler UI处理器
     */
    public static void qryPeriodTotBalance(String userId, int unit, int span, Handler handler) {
        BaseApi.asyncGet(
                StringUtils.format("/acc/qryBalanceRecord?userId=%s&unit=%d&span=%d", userId, unit, span),
                new BaseHttpCallback(handler) {
                    @Override
                    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
                        JSONObject json = getRespBodyJson(response);
                        if (isRequestSuccess(json)) {
                            msg.what = QRY_TOT_BALANCE_SUCCESS;
                        } else {
                            msg.what = QRY_TOT_BALANCE_FAIL;
                        }
                        msg.obj = json;
                    }
                });
    }

    /**
     * 删除某个账户
     *
     * @param userId  用户id
     * @param accId   账户id
     * @param handler UI处理器
     */
    public static void delAccBalance(String userId, String accId, Handler handler) {
        asyncPost("/acc/delAccBalance",
                new FormBody.Builder()
                        .add("userId", userId)
                        .add("accId", accId)
                        .build(),
                handler);
    }


}
