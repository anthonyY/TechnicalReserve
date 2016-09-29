package com.example.administrator.avdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.avdemo.helper.LoginHelper;
import com.example.administrator.avdemo.utils.ToastUtil;
import com.example.administrator.avdemo.viewinterface.LoginView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class LoginActivity extends Activity implements LoginView {

    private EditText name, password;
    private Button login;
    private LoginHelper mLoginHelper;
    private Animation shakeanimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        shakeanimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        mLoginHelper = new LoginHelper(this, this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    name.startAnimation(shakeanimation);
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    password.startAnimation(shakeanimation);
                    return;
                }
                mLoginHelper.tlsLogin(name.getText().toString(), password.getText().toString());
//                mLoginHelper.imLogin(name.getText().toString(),password.getText().toString());
            }
        });
    }


    /**
     * 登陆成功回调
     */
    @Override
    public void loginSucc() {
        ToastUtil.show(this, "login success");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 登陆失败回调
     */
    @Override
    public void loginFail() {
        ToastUtil.show(this, "login fail");
    }
}
