package indi.nonoas.xbh;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import indi.nonoas.xbh.common.GlobalExceptionHandler;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
//        GlobalExceptionHandler.getInstance().init(this);
    }

    public static Context getContext() {
        return mContext;
    }
}

