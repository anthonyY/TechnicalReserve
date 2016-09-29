package com.example.administrator.avdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.haha.perflib.Main;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button start_live, enter_room;
    private EditText roomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        start_live = (Button) findViewById(R.id.start_live);
        enter_room = (Button) findViewById(R.id.enter_room);
        roomNum = (EditText) findViewById(R.id.room_num);

        start_live.setOnClickListener(this);
        enter_room.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_live://开始一个直播
                Intent intent = new Intent(MainActivity.this, PublishLiveActivity.class);
                startActivity(intent);
                break;
            case R.id.enter_room://进入一个直播间
                if (TextUtils.isEmpty(roomNum.getText().toString().trim())) {
                    roomNum.setError("请输入房间号");
                    return;
                }
                if (roomNum.getText().toString().length() != 5) {
                    roomNum.setError("房间号为5位数");
                    roomNum.setText("");
                    return;
                }
                Intent intent2 = new Intent(MainActivity.this, LiveActivity.class);
                intent2.putExtra("roomNum", Integer.valueOf(roomNum.getText().toString().trim()));
                startActivity(intent2);
                break;
        }
    }
}
