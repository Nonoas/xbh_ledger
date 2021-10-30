package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import indi.nonoas.xbh.common.log.ILogTag;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class UICallback implements Callback {

    private final Handler handler;

    public UICallback(Handler handler) {
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
    public abstract void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
}
