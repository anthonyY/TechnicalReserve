package com.aiitec.imdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aiitec.imdemo.R;
import com.aiitec.imdemo.common.Constant;
import com.aiitec.imdemo.model.UserInfo;
import com.aiitec.imdemo.service.TLSService;
import com.aiitec.imdemo.util.ToastUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.open.utils.Util;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by huangkai on 2016/8/19.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText name, password;
    private Button login;
    private TLSService tlsService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_password);
        login = (Button) findViewById(R.id.login);
        tlsService = TLSService.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("input name");
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("input password");
            return;
        }
        //两层登陆，，，，，，，
        TLSService.getInstance().TLSPwdLogin(name.getText().toString(), password.getText().toString(), new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                TLSService.getInstance().refreshUserSig(tlsUserInfo.identifier, new TLSService.RefreshUserSigListener() {
                    @Override
                    public void onUserSigNotExist() {
                        ToastUtil.show(LoginActivity.this, "onUserSigNotExist");
                    }

                    @Override
                    public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {
                        ToastUtil.show(LoginActivity.this, "OnRefreshUserSigSuccess" + tlsUserInfo.identifier);
                        UserInfo.getInstance().setId(tlsUserInfo.identifier);
                        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(tlsUserInfo.identifier));
                        TIMUser t = new TIMUser();
                        t.setAccountType(Constant.ACCOUNT_TYPE + "");
                        t.setIdentifier(name.getText().toString());
                        t.setAppIdAt3rd(Constant.SDK_APPID + "");
                        TIMManager.getInstance().login(Constant.SDK_APPID, t, UserInfo.getInstance().getUserSig(), new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                ToastUtil.show(LoginActivity.this, "code：" + i + "msg:" + s);
                            }

                            @Override
                            public void onSuccess() {
                                ToastUtil.show(LoginActivity.this, "onSuccess");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {
                        ToastUtil.show(LoginActivity.this, "OnRefreshUserSigFail");
                    }

                    @Override
                    public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {
                        ToastUtil.show(LoginActivity.this, "OnRefreshUserSigTimeout");
                    }
                });


            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
                ToastUtil.show(LoginActivity.this, "OnPwdLoginReaskImgcodeSuccess");
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                ToastUtil.show(LoginActivity.this, "OnPwdLoginNeedImgcode");
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                ToastUtil.show(LoginActivity.this, "OnPwdLoginFail");
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                ToastUtil.show(LoginActivity.this, "OnPwdLoginTimeout");
            }
        });

    }
}
