package com.example.administrator.avdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.avdemo.adapter.CommentsAdapter;
import com.example.administrator.avdemo.common.Constants;
import com.example.administrator.avdemo.control.QavsdkControl;
import com.example.administrator.avdemo.log.SxbLog;
import com.example.administrator.avdemo.model.MySelfInfo;
import com.example.administrator.avdemo.utils.ToastUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVEndpoint;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.av.sdk.AVView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 * 观众观看直播Activity
 */
public class LiveActivity extends Activity implements View.OnClickListener {

    private GLRootView avView;
    private TextView room_num, live_time, watch_count;
    private Button send_message, check_host_info, click_like, send_comments;
    private EditText comments;
    private LinearLayout comment_layout;
    private ListView live_comments;
    private CommentsAdapter adapter;
    private List<String> commentList = new ArrayList<>();

    //群消息
    private TIMConversation mGroupConversation;
    private TIMConversation mC2CConversation;

    private AVView mRequestViewList[] = new AVView[4];
    private String mRequestIdentifierList[] = new String[4];

    private int roomNum;
    private boolean isInAVRoom = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_live);
        initView();

        roomNum = getIntent().getIntExtra("roomNum", 0);
        joinIMChatRoom();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        avView = (GLRootView) findViewById(R.id.AVUIControl);
        room_num = (TextView) findViewById(R.id.live_room_num);
        live_time = (TextView) findViewById(R.id.live_time);
        watch_count = (TextView) findViewById(R.id.live_people);
        send_message = (Button) findViewById(R.id.send_message);
        check_host_info = (Button) findViewById(R.id.host_info);
        click_like = (Button) findViewById(R.id.click_like);
        comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
        send_comments = (Button) findViewById(R.id.send);
        comments = (EditText) findViewById(R.id.comments);
        live_comments = (ListView) findViewById(R.id.live_comments);
        live_comments.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        send_message.setOnClickListener(this);
        click_like.setOnClickListener(this);
        send_comments.setOnClickListener(this);
        adapter = new CommentsAdapter(LiveActivity.this, commentList);
        live_comments.setAdapter(adapter);

    }

    /**
     * 首先加入聊天室
     */
    private void joinIMChatRoom() {
        TIMGroupManager.getInstance().applyJoinGroup("" + roomNum, "申请加入" + roomNum, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (i == 10013) {
                    ToastUtil.show(LiveActivity.this, "已经是成员");
                    joinAVRoom();
                }
                ToastUtil.show(LiveActivity.this, "加入聊天室失败" + "错误码:" + i + "-错误信息:" + s);
            }

            @Override
            public void onSuccess() {
                ToastUtil.show(LiveActivity.this, "加入聊天室成功");
                joinAVRoom();
            }
        });
    }

    /**
     * 加入音视频房间
     */
    private void joinAVRoom() {


        AVContext avContext = QavsdkControl.getInstance().getAVContext();

        AVRoomMulti.EnterRoomParam enterRoomParam = new AVRoomMulti.EnterRoomParam();
        enterRoomParam.authBits = Constants.NORMAL_MEMBER_AUTH;//；TODO：普通成员权限
        enterRoomParam.avControlRole = Constants.NORMAL_MEMBER_ROLE;//；TODO：普通成员角色
        enterRoomParam.autoCreateRoom = false;//;TODO：是否自动创建房间
        enterRoomParam.appRoomId = roomNum; //；TODO：房间号
        enterRoomParam.authBuffer = null;//；TODO：密钥
        enterRoomParam.audioCategory = Constants.AUDIO_VOICE_CHAT_MODE;//；TODO：音频场景策略
        enterRoomParam.videoRecvMode = AVRoom.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO;//；TODO：半自动模式，加速成员进房间获取视频速度

        if (avContext != null) {
            avContext.enterRoom(AVRoom.AV_ROOM_MULTI, delegate, enterRoomParam);
        }
    }

    /**
     * 房间回调
     */
    private AVRoomMulti.Delegate delegate = new AVRoomMulti.Delegate() {
        @Override
        public void onEnterRoomComplete(int i) {
        }

        @Override
        public void onExitRoomComplete(int i) {
            //成员退出群
            TIMGroupManager.getInstance().quitGroup("" + roomNum, new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    ToastUtil.show(LiveActivity.this, "退出聊天群失败" + "错误码" + i + "-错误信息" + s);
                }

                @Override
                public void onSuccess() {
                    ToastUtil.show(LiveActivity.this, "退出聊天群成功");
                    if ((QavsdkControl.getInstance() != null) && (QavsdkControl.getInstance().getAVContext() != null) && (QavsdkControl.getInstance().getAVContext().getAudioCtrl() != null)) {
                        QavsdkControl.getInstance().getAVContext().getAudioCtrl().stopTRAEService();
                    }
                    finish();
                }
            });
        }

        @Override
        public void onEndpointsUpdateInfo(int i, String[] strings) {
            ToastUtil.show(LiveActivity.this, "onEndpointsUpdateInfo");
            ToastUtil.show(LiveActivity.this, "加入房间成功");
            //房间成功创建之后可以开始初始化音频 视频等
            QavsdkControl.getInstance().getAVContext().getAudioCtrl().startTRAEService();
            QavsdkControl.getInstance().initAvUILayer(LiveActivity.this, avView);

            //修正摄像头镜像
            AVVideoCtrl avVideoCtrl = QavsdkControl.getInstance().getAVContext().getVideoCtrl();
            if (avVideoCtrl != null) {
                avVideoCtrl.setCameraPreviewChangeCallback(mCameraPreviewChangeCallback);
            }

            initTIMListener();
            //请求主播端数据
            ArrayList<String> list = new ArrayList<>();
            for (String s : strings) {
                list.add(s);
            }
            requestViewList(list);
            if (!isInAVRoom) {
                //构造一条消息
                TIMMessage msg = new TIMMessage();

                //添加文本内容
                TIMTextElem elem = new TIMTextElem();
                elem.setText(MySelfInfo.getInstance().getId() + "进入直播间");

                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    Log.d("ChatActivity", "addElement failed");
                    return;
                }
                mGroupConversation.sendMessage(msg, timValueCallBack);
                isInAVRoom = true;
            }
        }

        @Override
        public void OnPrivilegeDiffNotify(int i) {
        }

        @Override
        public void OnSemiAutoRecvCameraVideo(String[] strings) {
        }
    };

    private TIMValueCallBack timValueCallBack = new TIMValueCallBack() {
        @Override
        public void onError(int i, String s) {
            ToastUtil.show(LiveActivity.this, "发送失败");

        }

        @Override
        public void onSuccess(Object o) {
            ToastUtil.show(LiveActivity.this, "发送成功");
        }
    };

    /**
     * 初始化聊天室监听
     */
    private void initTIMListener() {
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, roomNum + "");
        TIMManager.getInstance().addMessageListener(messageListener);
        mC2CConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, roomNum + "");
    }

    private TIMMessageListener messageListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            for (TIMMessage t : list) {
                for (int i = 0; i < t.getElementCount(); i++) {
                    TIMElem elem = t.getElement(i);
                    if (elem.getType() == TIMElemType.Text) {
                        TIMTextElem ww = (TIMTextElem) elem;
                        String s = ww.getText().toString();
                        adapter.addMessage(s);
                    }
                }
            }
            return false;
        }
    };

    /**
     * 摄像头转换回调
     */
    private AVVideoCtrl.CameraPreviewChangeCallback mCameraPreviewChangeCallback = new AVVideoCtrl.CameraPreviewChangeCallback() {

        @Override
        public void onCameraPreviewChangeCallback(int cameraId) {
            SxbLog.d("PublishLiveActivity", "WL_DEBUG mCameraPreviewChangeCallback.onCameraPreviewChangeCallback cameraId = " + cameraId);
            //在前置摄像头的情况下才设置镜像
            QavsdkControl.getInstance().setMirror(0 == cameraId);
        }
    };

    /**
     * AVSDK 请求主播数据
     *
     * @param identifiers 主播ID
     */
    public void requestViewList(ArrayList<String> identifiers) {
        if (identifiers.size() == 0) return;
        AVEndpoint endpoint = ((AVRoomMulti) QavsdkControl.getInstance().getAVContext().getRoom()).getEndpointById(identifiers.get(0));
        if (endpoint != null) {
            ArrayList<String> alreadyIds = QavsdkControl.getInstance().getRemoteVideoIds();//已经存在的IDs

            for (String id : identifiers) {//把新加入的添加到后面
                if (!alreadyIds.contains(id)) {
                    alreadyIds.add(id);
                }
            }
            int viewindex = 0;
            for (String id : alreadyIds) {//一并请求
                if (viewindex >= 4) break;
                AVView view = new AVView();
                view.videoSrcType = AVView.VIDEO_SRC_TYPE_CAMERA;
                view.viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
                //界面数
                mRequestViewList[viewindex] = view;
                mRequestIdentifierList[viewindex] = id;
                viewindex++;
            }
            int ret = AVEndpoint.requestViewList(mRequestIdentifierList, mRequestViewList, viewindex, requestViewListCompleteCallback);

        } else {
            Toast.makeText(LiveActivity.this, "Wrong Room!!!! Live maybe close already!", Toast.LENGTH_SHORT).show();
        }


    }

    private AVEndpoint.RequestViewListCompleteCallback requestViewListCompleteCallback = new AVEndpoint.RequestViewListCompleteCallback() {
        @Override
        protected void OnComplete(String[] strings, AVView[] avViews, int i, int i1) {
            for (String id : strings) {
                showVideoView(true, id);
            }
        }
    };

    private void showVideoView(boolean isHost, String id) {
        QavsdkControl.getInstance().setLocalHasVideo(true, id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                comment_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.click_like:
                //构造一条消息
                TIMMessage msg = new TIMMessage();

                //添加文本内容
                TIMTextElem elem = new TIMTextElem();
                elem.setText(MySelfInfo.getInstance().getId() + "为你点了一个赞");

                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    Log.d("ChatActivity", "addElement failed");
                    return;
                }
                mGroupConversation.sendMessage(msg, timValueCallBack);
                break;
            case R.id.send:
                if (TextUtils.isEmpty(comments.getText().toString().trim())) {
                    ToastUtil.show(LiveActivity.this, "请说点什么吧");
                    break;
                }
                //构造一条消息
                TIMMessage msg1 = new TIMMessage();

                //添加文本内容
                TIMTextElem elem1 = new TIMTextElem();
                elem1.setText(MySelfInfo.getInstance().getId() + ":" + comments.getText().toString());

                //将elem添加到消息
                if (msg1.addElement(elem1) != 0) {
                    Log.d("ChatActivity", "addElement failed");
                    return;
                }
                mGroupConversation.sendMessage(msg1, new TIMValueCallBack<TIMMessage>() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtil.show(LiveActivity.this, "发送失败，请重试");

                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage) {
                        ToastUtil.show(LiveActivity.this, "发送成功");
                        adapter.addMessage(MySelfInfo.getInstance().getId() + ":" + comments.getText().toString());
                        comments.setText("");
                        comment_layout.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        TIMManager.getInstance().removeMessageListener(messageListener);
        AVContext avContext = QavsdkControl.getInstance().getAVContext();
        avContext.exitRoom();
    }
}
