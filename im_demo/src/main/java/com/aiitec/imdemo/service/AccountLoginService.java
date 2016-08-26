package com.aiitec.imdemo.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aiitec.imdemo.common.Constant;
import com.aiitec.imdemo.ui.ImgCodeActivity;
import com.aiitec.imdemo.util.ToastUtil;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by dgy on 15/8/12.
 */
public class AccountLoginService {

    private final static String TAG = "AccountLoginService";

    private Context context;
    private EditText txt_username;
    private EditText txt_password;

    private String username;
    private String password;

    private TLSService tlsService;
    public static PwdLoginListener pwdLoginListener;


    public AccountLoginService(Context context,
                               EditText txt_username,
                               EditText txt_password,
                               Button btn_login) {
        this.context = context;
        this.txt_username = txt_username;
        this.txt_password = txt_password;

        tlsService = TLSService.getInstance();
        pwdLoginListener = new PwdLoginListener();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = AccountLoginService.this.txt_username.getText().toString();
                password = AccountLoginService.this.txt_password.getText().toString();

                // 验证用户名和密码的有效性
                if (username.length() == 0 || password.length() == 0) {
                    ToastUtil.show(AccountLoginService.this.context, "用户名密码不能为空");
                    return;
                }

                tlsService.TLSPwdLogin(username, password, pwdLoginListener);
            }
        });
    }

    class PwdLoginListener implements TLSPwdLoginListener {
        @Override
        public void OnPwdLoginSuccess(TLSUserInfo userInfo) {
            ToastUtil.show(context, "登录 OK");
            TLSService.getInstance().setLastErrno(0);
            AccountLoginService.this.jumpToSuccActivity();
        }

        @Override
        public void OnPwdLoginReaskImgcodeSuccess(byte[] picData) {
            ImgCodeActivity.fillImageview(picData);
        }

        @Override
        public void OnPwdLoginNeedImgcode(byte[] picData, TLSErrInfo errInfo) {
            Intent intent = new Intent(context, ImgCodeActivity.class);
            intent.putExtra(Constant.EXTRA_IMG_CHECKCODE, picData);
            intent.putExtra(Constant.EXTRA_LOGIN_WAY, Constant.USRPWD_LOGIN);
            context.startActivity(intent);
        }

        @Override
        public void OnPwdLoginFail(TLSErrInfo errInfo) {
            TLSService.getInstance().setLastErrno(-1);
            ToastUtil.show(context, errInfo.ExtraMsg);
        }

        @Override
        public void OnPwdLoginTimeout(TLSErrInfo errInfo) {
            TLSService.getInstance().setLastErrno(-1);
            ToastUtil.show(context, errInfo.ExtraMsg);
        }
    }

    void jumpToSuccActivity() {
        String thirdappPackageNameSucc = Constant.thirdappPackageNameSucc;
        String thirdappClassNameSucc = Constant.thirdappClassNameSucc;

        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_LOGIN_WAY, Constant.USRPWD_LOGIN);
        intent.putExtra(Constant.EXTRA_USRPWD_LOGIN, Constant.USRPWD_LOGIN_SUCCESS);
        if (thirdappPackageNameSucc != null && thirdappClassNameSucc != null) {
            intent.setClassName(thirdappPackageNameSucc, thirdappClassNameSucc);
            context.startActivity(intent);
        } else {
            ((Activity) context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();
        }
    }
}