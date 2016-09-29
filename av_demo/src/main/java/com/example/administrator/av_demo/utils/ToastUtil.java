//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.administrator.avdemo.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast;
    private static String lastText = "";

    public ToastUtil() {
    }

    public static void show(Context context, String info) {
        if (mToast == null) {
            mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(info);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        if (!lastText.equals(info)) {
            mToast.show();
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    ToastUtil.lastText = "";
                }
            }, 1000L);
        }

        lastText = info;
    }

    public static void show(Context context, int info) {
        show(context, context.getResources().getString(info));
    }
}
