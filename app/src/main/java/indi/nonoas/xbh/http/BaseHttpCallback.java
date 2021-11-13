package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 可以使用handler返回响应信息的回调类，该类默认实现了 {@link Callback#onFailure} 方法，
 * 并且处理了 {@code (!response.isSuccessful())} 的情况
 * 将 {@code (response.isSuccessful())} 的情况交由子类去实现。
 *
 * @author Nonoas
 */
public abstract class BaseHttpCallback implements Callback {

    private final Handler handler;

    public BaseHttpCallback(@NonNull Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Message msg = handler.obtainMessage();
        msg.what = BaseApi.REQUEST_FAIL;
        handler.sendMessage(msg);
    }

    @Override
    public final void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        Message msg = handler.obtainMessage();
        if (response.isSuccessful()) {
            // if the code is in [200..300)
            onResponseSuccess(call, response, msg);
        } else {
            onResponseFailure(call, response, msg);
        }
        handler.sendMessage(msg);
    }


    /**
     * 在 {@code response.isSuccessful()} 时会调用这个方法
     */
    protected abstract void onResponseSuccess(Call call, Response response, Message msg) throws IOException;


    protected void onResponseFailure(Call call, Response response, Message msg) throws IOException {
        msg.what = BaseApi.REQUEST_FAIL;
    }

}
