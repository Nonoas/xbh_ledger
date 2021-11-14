package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

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
     * 获取余额列表api
     */
    public static void qryBalance(String userId, long date, Handler handler) {
        String url = StringUtils.format("acc/qryBalance?userId=%s&date=%d", userId, date);
        BaseApi.asyncGet(url, handler);
    }

    /**
     * 添加账户
     *
     * @param accBalance 账户余额信息
     * @param handler    UI处理器
     */
    public static void addAccBalance(AccBalance accBalance, Handler handler) {
        String json = JSON.toJSONString(accBalance);
        BaseApi.asyncPost("acc/addAccBalance",
                RequestBody.create(MediaType.parse("application/json"), json),
                new HttpCallback(handler)
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
        asyncGet(StringUtils.format("acc/qryBalanceRecord?userId=%s&unit=%d&span=%d", userId, unit, span),
                handler);
    }

    /**
     * 删除某个账户
     *
     * @param userId  用户id
     * @param accId   账户id
     * @param handler UI处理器
     */
    public static void delAccBalance(String userId, String accId, Handler handler) {
        asyncPost("acc/delAccBalance",
                new FormBody.Builder()
                        .add("userId", userId)
                        .add("accId", accId)
                        .build(),
                handler);
    }


}
