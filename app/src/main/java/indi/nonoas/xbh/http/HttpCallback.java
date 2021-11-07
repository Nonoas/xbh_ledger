package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class HttpCallback extends BaseHttpCallback {

    private final Handler handler;

    public HttpCallback(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    protected void onResponseSuccess(Call call, Response response, Message msg) throws IOException {
        JSONObject json = BaseApi.getRespBodyJson(response);
        if (BaseApi.isRequestSuccess(json)) {
            msg.what = BaseApi.MSG_SUCCESS;
        } else {
            msg.what = BaseApi.MSG_ERROR;
        }
        msg.obj = json;
    }
}
