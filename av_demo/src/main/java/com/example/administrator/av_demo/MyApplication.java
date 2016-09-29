package com.example.administrator.avdemo;

import android.app.Application;
import android.content.Context;

import com.example.administrator.avdemo.helper.InitBusinessHelper;
import com.example.administrator.avdemo.log.SxbLogImpl;

/**
 * Created by Administrator on 2016/8/5.
 */
public class MyApplication extends Application {

    private static MyApplication app;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        context = getApplicationContext();

        //初始化日志输出
        SxbLogImpl.init(getApplicationContext());

        //初始化app，avsdk imsdk
        InitBusinessHelper.initApp(context);
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return app;
    }
}
