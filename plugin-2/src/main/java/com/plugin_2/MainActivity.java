package com.plugin_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ryg.dynamicload.DLBasePluginActivity;

import java.lang.reflect.Method;
/**
 * 插件2， run成apk,放到服务器，主工程就可以使用了
 * 插件工程不要使用this, 要使用that代替this
 */
public class MainActivity extends DLBasePluginActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        that.setContentView(generateContentView(that));
    }

    private View generateContentView(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        Button button = new Button(context);
        button.setText("Invoke host method");
        layout.addView(button, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Class clazz = Class.forName("com.mainforplugin.TestHostClass");
                    Object t = clazz.newInstance();
                    Method method = clazz.getMethod("testMethod", Context.class);
                    method.invoke(t, that);
                } catch (Exception e){
                    e.printStackTrace();
                }

//                TestHostClass testHostClass = new TestHostClass();
//                testHostClass.testMethod(that);
            }
        });

        TextView textView = new TextView(context);
        textView.setText("Hello, I'm Plugin B.");
        textView.setTextSize(30);
        layout.addView(textView, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult resultCode=" + resultCode);
        if (resultCode == RESULT_FIRST_USER) {
            that.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
