package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

/**
 * 通用UI处理类，用于根据http返回结果，对UI进行操作
 */
public abstract class HttpUICallback implements Handler.Callback {

    protected HttpUICallback() {
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        int w = msg.what;
        Object obj = msg.obj;
        switch (w) {
            // 请求失败
            case BaseApi.REQUEST_FAIL:
                handleError(w, obj);
                break;
            // 消息类型为成功
            case BaseApi.MSG_SUCCESS:
                onMsgSuccess(obj);
                break;
            default:
                onMsgError(w, obj);
        }
        return false;
    }

    /**
     * 返回消息类型为 “成功”
     *
     * @param obj 一般为 JSONObject
     */
    protected abstract void onMsgSuccess(Object obj);

    /**
     * 返回消息类型为 “错误”
     *
     * @param msgWhat 消息id
     * @param obj     消息内容，一般为 JSONObject
     */
    protected abstract void onMsgError(int msgWhat, Object obj);

    /**
     * 响应错误，一般为服务器异常
     *
     * @param msgWhat 消息id
     * @param obj     消息内容，一般为 JSONObject
     */
    protected abstract void handleError(int msgWhat, Object obj);
}
