package com.aiitec.imdemo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.aiitec.imdemo.R;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendshipManager;

public class MainActivity extends FragmentActivity {

    private RadioGroup radioGroup;
    private TabHost tHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        initTab(savedInstanceState);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_conversation_page:
                        tHost.setCurrentTab(0);
                        break;
                    case R.id.rb_friends_page:
                        tHost.setCurrentTab(1);
                        break;
                }
            }
        });

        //设置好友验证方式 目前没有方法设置默认的好友验证方式（腾讯文档这么写的(=@__@=)），所以每次进来都要设置一次
        //允许任何人添加好友
        //  TIM_FRIEND_ALLOW_ANY
        //拒绝任何人添加好友
        //  TIM_FRIEND_DENY_ANY
        //非法的选项类型
        //  TIM_FRIEND_INVALID
        //添加好友需要验证
        //  TIM_FRIEND_NEED_CONFIRM
        TIMFriendshipManager.getInstance().setAllowType(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e("setAllowType", "setAllowType failed: " + i + " desc" + s);
            }

            @Override
            public void onSuccess() {
                Log.e("setAllowType", "setAllowType success");
            }
        });
    }

    private void initTab(Bundle savedInstanceState) {
        tHost.setup();

        tHost.addTab(tHost.newTabSpec("conversation_fragment")
                .setIndicator("conversation_fragment")
                .setContent(R.id.conversaton_fragment));
        tHost.addTab(tHost.newTabSpec("friends_fragment")
                .setIndicator("friends_fragment")
                .setContent(R.id.friends_fragment));
    }
}
