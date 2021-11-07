package indi.nonoas.xbh.http;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public abstract class HttpUICallback implements Handler.Callback {

    protected HttpUICallback() {
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        int w = msg.what;
        Object obj = msg.obj;
        if (BaseApi.REQUEST_FAIL == w) {
            handleError(w, obj);
        }
        if (BaseApi.MSG_SUCCESS==w) {
            onMsgSuccess(obj);
        }else {
            onMsgError(w, obj);
        }
        return false;
    }

    protected abstract void onMsgSuccess(Object obj);

    protected abstract void onMsgError(int w, Object obj);

    protected abstract void handleError(int w, Object obj);
}
