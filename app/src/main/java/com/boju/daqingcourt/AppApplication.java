package com.boju.daqingcourt;

import android.app.Application;


/**
 * Created by Administrator on 2017/8/18.
 * AppApplication
 */

public class AppApplication extends Application {
    private static AppApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
