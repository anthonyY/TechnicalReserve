package com.aiitec.imdemo.presenter;

import android.os.Handler;
import android.util.Log;

import com.aiitec.imdemo.viewinterface.SplashView;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;


/**
 * 闪屏界面逻辑
 */
public class SplashPresenter {
    SplashView view;
    private static final String TAG = SplashPresenter.class.getSimpleName();

    public SplashPresenter(SplashView view) {
        this.view = view;
    }


    /**
     * 加载页面逻辑
     */
    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.isUserLogin()) {
                    view.navToHome();
                } else {
                    view.navToLogin();
                }
            }
        }, 1000);
    }


}
