package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import indi.nonoas.xbh.common.log.ILogTag;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 可以使用handler返回响应信息的回调类
 */
public abstract class UICallback implements Callback {

    private final Handler handler;

    public UICallback(@NonNull Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Message msg = new Message();
        msg.what = BaseApi.REQUEST_FAIL;
        handler.sendMessage(msg);
        Log.e(ILogTag.DEV, e.getMessage());
    }

    @Override
    public final void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        Message msg = new Message();
        if (response.isSuccessful()) {
            // if the code is in [200..300)
            onResponseSuccess(call, response, msg);
        } else {
            onResponseFailure(call, response, msg);
        }
        handler.sendMessage(msg);
    }


    protected abstract void onResponseSuccess(Call call, Response response, Message msg) throws IOException;


    protected void onResponseFailure(Call call, Response response, Message msg) throws IOException {
        msg.what = BaseApi.REQUEST_FAIL;
    }

}
