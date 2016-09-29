package com.example.administrator.avdemo.viewinterface;


/**
 * 登录回调
 */
public interface LoginView extends MvpView{

    void loginSucc();

    void loginFail();
}
