package com.example.administrator.avdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.avdemo.helper.OKhttpHelper;
import com.example.administrator.avdemo.log.SxbLog;
import com.example.administrator.avdemo.model.LiveInfoJson;
import com.example.administrator.avdemo.model.MySelfInfo;
import com.example.administrator.avdemo.utils.ToastUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.av.sdk.AVView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/8/10.
 * <p/>
 * 主播直播间
 */
public class PublishLiveActivity extends Activity implements View.OnClickListener {
    //视频view
    private GLRootView avView;
    private TextView time, watch_num, room_num;
    private ListView commentList;
    private Button switch_camera, switch_mic, adjust_beauty, quit, beauty_commit;
    private EditText beauty_edit;
    private LinearLayout beauty_layout;
    private List<String> commentLists = new ArrayList<>();
    private CommentsAdapter adapter;
    //随机的房号
    private int roomNum;
    //摄像头与麦克风
    private static final int FRONT_CAMERA = 0;
    private static final int BACK_CAMERA = 1;
    private static final int MIC_MUTE = 2;
    private static final int MIC_OPEN = 3;
    private int cameraStatus = FRONT_CAMERA;//摄像头状态，默认是前置摄像头
    private int MICStatus = MIC_OPEN;//麦克的状态（默认为open）
    //群消息
    private TIMConversation mGroupConversation;
    private TIMConversation mC2CConversation;
    //播放计时
    private long mSecond = 0;
    private Timer videoTimer;
    private VideoTimerTask videoTimerTask;
    private String formatTime;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 4;
    //心跳机制（服务端需要确认房间存活）
    private Timer heartBeatTimer;
    private HeartBeatTimerTask heartBeatTimerTask;
    private static final int HEART_BEAT_TIME_TIMER_TASK = 5;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDAT_WALL_TIME_TIMER_TASK:
                    updateWallTime();
                    break;
                case HEART_BEAT_TIME_TIMER_TASK:
                    ToastUtil.show(PublishLiveActivity.this, "心跳");
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_publish_live);
        roomNum = (int) ((Math.random() * 9 + 1) * 10000);
        registerReceiver();
        initView();
        //创建一个直播 先创建一个聊天室，再创建一个avRoom
        createChatroom();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //AvSurfaceView 初始化成功
            if (action.equals(Constants.ACTION_SURFACE_CREATED)) {
                //打开摄像头
                AVVideoCtrl avVideoCtrl1 = QavsdkControl.getInstance().getAVContext().getVideoCtrl();
                avVideoCtrl1.enableCamera(FRONT_CAMERA, true, new AVVideoCtrl.EnableCameraCompleteCallback() {
                    protected void onComplete(boolean enable, int result) {//开启摄像头回调
                        ToastUtil.show(PublishLiveActivity.this, result + "");
                        if (result == AVError.AV_OK) {//开启成功
                            ToastUtil.show(PublishLiveActivity.this, "开启摄像头成功");
                        } else {
                            ToastUtil.show(PublishLiveActivity.this, "开启摄像头失败");
                        }
                    }
                });
                //打开麦克风
                AVAudioCtrl avAudioCtrl = QavsdkControl.getInstance().getAVContext().getAudioCtrl();
                boolean b = avAudioCtrl.enableMic(true);

                //渲染本地画面
                QavsdkControl.getInstance().setSelfId(MySelfInfo.getInstance().getId());
                QavsdkControl.getInstance().setLocalHasVideo(true, MySelfInfo.getInstance().getId());
                //开启心跳
                heartBeatTimer = new Timer(true);
                heartBeatTimerTask = new HeartBeatTimerTask();
                heartBeatTimer.schedule(heartBeatTimerTask, 1000, 3 * 1000);
                //开始计时（这里可能还需要告诉服务器房间创建成功）
                videoTimer = new Timer(true);
                videoTimerTask = new VideoTimerTask();
                videoTimer.schedule(videoTimerTask, 1000, 1000);
            }
        }
    };


    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_SURFACE_CREATED);
        intentFilter.addAction(Constants.ACTION_HOST_ENTER);
        intentFilter.addAction(Constants.ACTION_CAMERA_OPEN_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_CAMERA_CLOSE_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_SWITCH_VIDEO);
        intentFilter.addAction(Constants.ACTION_HOST_LEAVE);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    /**
     * 创建一个聊天室
     */
    private void createChatroom() {
        List<String> list = new ArrayList<>();//直播群成员的集合
        String roomName = MySelfInfo.getInstance().getId() + "的直播间";//房间名
        TIMGroupManager.getInstance().createGroup("AVChatRoom", list, roomName, roomNum + "", new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                //已在房间中
                if (i == 10025) {
                    createAVRoom();
                    return;
                }
                ToastUtil.show(PublishLiveActivity.this, "创建聊天室失败");
                Log.e("PublishLiveActivity", "错误码：" + i + "错误信息：" + s);
            }

            @Override
            public void onSuccess(String s) {
                ToastUtil.show(PublishLiveActivity.this, "创建聊天室成功");
                createAVRoom();
            }
        });
    }

    /**
     * 创建一个音频直播室
     */
    private void createAVRoom() {
        AVContext avConetxt = QavsdkControl.getInstance().getAVContext();

        AVRoomMulti.EnterRoomParam enter_room = new AVRoomMulti.EnterRoomParam();
        enter_room.authBits = Constants.HOST_AUTH;// 主播权限 所有权限
        enter_room.avControlRole = Constants.HOST_ROLE;//主播角色
        enter_room.autoCreateRoom = true;//主播自动创建房间

        enter_room.appRoomId = roomNum; //房间号
        enter_room.authBuffer = null;//密钥
        enter_room.audioCategory = Constants.AUDIO_VOICE_CHAT_MODE;//音频场景策略
        enter_room.videoRecvMode = AVRoom.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO;//视频场景策略 半自动模式，加速成员进房间获取视频速度

        avConetxt.enterRoom(AVRoom.AV_ROOM_MULTI, delegate, enter_room);
    }

    /**
     * 摄像头转换回调
     */
    private AVVideoCtrl.CameraPreviewChangeCallback mCameraPreviewChangeCallback = new AVVideoCtrl.CameraPreviewChangeCallback() {

        @Override
        public void onCameraPreviewChangeCallback(int cameraId) {
            SxbLog.d("PublishLiveActivity", "WL_DEBUG mCameraPreviewChangeCallback.onCameraPreviewChangeCallback cameraId = " + cameraId);
            //在前置摄像头的情况下才设置镜像
            QavsdkControl.getInstance().setMirror(FRONT_CAMERA == cameraId);
        }
    };

    //房间回调
    private AVRoomMulti.Delegate delegate = new AVRoomMulti.Delegate() {
        @Override
        public void onEnterRoomComplete(int i) {
            if (i == 0) {
//                ToastUtil.show(PublishLiveActivity.this, "创建房间成功");
                //房间成功创建之后
                QavsdkControl.getInstance().getAVContext().getAudioCtrl().startTRAEService();
                QavsdkControl.getInstance().initAvUILayer(PublishLiveActivity.this, avView);//这一步会在成功之后发送广播，再来做余下操作，，，
                //修正摄像头镜像
                AVVideoCtrl avVideoCtrl = QavsdkControl.getInstance().getAVContext().getVideoCtrl();
                if (avVideoCtrl != null) {
                    avVideoCtrl.setCameraPreviewChangeCallback(mCameraPreviewChangeCallback);
                }
                initTIMListener();
            }
        }

        @Override
        public void onExitRoomComplete(int i) {
            //离开音视频房间后,首先退出聊天室
            quitIMChatRoom();
            //终止音频服务
            if ((QavsdkControl.getInstance() != null) && (QavsdkControl.getInstance().getAVContext() != null) && (QavsdkControl.getInstance().getAVContext().getAudioCtrl() != null)) {
                QavsdkControl.getInstance().getAVContext().getAudioCtrl().stopTRAEService();
            }
            //告诉用户服务器，直播已经结束了
            NotifyServerLiveEnd liveEndTask = new NotifyServerLiveEnd();
            liveEndTask.execute(MySelfInfo.getInstance().getId());

            finish();
        }

        /**
         * 不要在这里做房间成员变化监听，在IMSdk里面做
         * @param i
         * @param strings
         */
        @Override
        public void onEndpointsUpdateInfo(int i, String[] strings) {
//            ToastUtil.show(PublishLiveActivity.this, "状态码:" + i);
//            ToastUtil.show(PublishLiveActivity.this, " 房间成员变化");
        }

        @Override
        public void OnPrivilegeDiffNotify(int i) {
        }

        @Override
        public void OnSemiAutoRecvCameraVideo(String[] strings) {
        }
    };

    /**
     * 初始化聊天室监听
     */
    private void initTIMListener() {
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, roomNum + "");
        TIMManager.getInstance().addMessageListener(messageListener);
        TIMGroupManager.getInstance().getGroupMembers(roomNum + "", new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {

            }
        });
        mC2CConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, roomNum + "");
    }

    /**
     * 群消息回调
     */
    private TIMMessageListener messageListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            parseIMMEssage(list);
            return false;
        }
    };

    /**
     * 解析消息
     */
    private void parseIMMEssage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;

        for (TIMMessage t : tlist) {
            for (int i = 0; i < t.getElementCount(); i++) {
                TIMElem elem = t.getElement(i);
                if (elem.getType() == TIMElemType.Text) {
                    TIMTextElem ww = (TIMTextElem) elem;
                    String s = ww.getText().toString();
                    adapter.addMessage(s);
                }
            }
        }
    }

    /**
     * 初始化控件
     */

    private void initView() {
        room_num = (TextView) findViewById(R.id.room_num);
        room_num.setText("房间号：" + roomNum);
        avView = (GLRootView) findViewById(R.id.AVUIControl);
        time = (TextView) findViewById(R.id.time);
        watch_num = (TextView) findViewById(R.id.watch_num);
        commentList = (ListView) findViewById(R.id.comments);
        commentList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        switch_camera = (Button) findViewById(R.id.switch_camera);
        switch_mic = (Button) findViewById(R.id.switch_mic);
        adjust_beauty = (Button) findViewById(R.id.adjust_beauty);
        quit = (Button) findViewById(R.id.quit);
        beauty_layout = (LinearLayout) findViewById(R.id.beauty_layout);
        beauty_edit = (EditText) findViewById(R.id.beauty_progress);
        beauty_commit = (Button) findViewById(R.id.beauty_commit);

        switch_camera.setOnClickListener(this);
        switch_mic.setOnClickListener(this);
        adjust_beauty.setOnClickListener(this);
        quit.setOnClickListener(this);
        beauty_commit.setOnClickListener(this);
        adapter = new CommentsAdapter(PublishLiveActivity.this, commentLists);
        commentList.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_camera://转换摄像头
                AVVideoCtrl videoCtrl = QavsdkControl.getInstance().getAVContext().getVideoCtrl();
                final int toChange;
                if (cameraStatus == FRONT_CAMERA) {
                    toChange = BACK_CAMERA;
                } else {
                    toChange = FRONT_CAMERA;
                }
                videoCtrl.switchCamera(toChange, new AVVideoCtrl.SwitchCameraCompleteCallback() {
                    @Override
                    protected void onComplete(int i, int i1) {
                        if (i1 == AVError.AV_OK) {
                            ToastUtil.show(PublishLiveActivity.this, "转换摄像头成功");
                            cameraStatus = toChange;
                        }
                    }
                });
                break;
            case R.id.switch_mic://开启/关闭麦克风
                AVAudioCtrl audioCtrl = QavsdkControl.getInstance().getAVContext().getAudioCtrl();
                if (MICStatus == MIC_OPEN) {
                    audioCtrl.enableMic(false);
                    switch_mic.setText("开启麦克风");
                    MICStatus = MIC_MUTE;
                } else {
                    audioCtrl.enableMic(true);
                    switch_mic.setText("关闭麦克风");
                    MICStatus = MIC_OPEN;
                }
                break;
            case R.id.adjust_beauty://调整美颜效果
                beauty_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.quit://退出直播
                onBackPressed();
                break;
            case R.id.beauty_commit://美颜调整
                if (TextUtils.isEmpty(beauty_edit.getText().toString().trim())) {
                    ToastUtil.show(PublishLiveActivity.this, "请输入一个数值");
                    return;
                }
                if (beauty_edit.getText().toString().trim().equals("0")) {
                    ToastUtil.show(PublishLiveActivity.this, "不能为0");
                    return;
                }
                int progress = Integer.valueOf(beauty_edit.getText().toString().trim() + "0");
                QavsdkControl.getInstance().getAVContext().getVideoCtrl().inputBeautyParam(9.0f * progress / 100.0f);//美颜
                beauty_layout.setVisibility(View.GONE);
                ToastUtil.show(PublishLiveActivity.this, "又美了许多！");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //首先退出音视频房间
        AVContext avContext = QavsdkControl.getInstance().getAVContext();
        avContext.exitRoom();
        //然后走到onExitRoomComplete中（退出房间成功回调），再做余下的操作
    }

    /**
     * 解散聊天室（主播）
     */
    private void quitIMChatRoom() {
        //解散聊天室
        TIMGroupManager.getInstance().deleteGroup(roomNum + "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.show(PublishLiveActivity.this, "解散群失败");
            }

            @Override
            public void onSuccess() {
                ToastUtil.show(PublishLiveActivity.this, "解散群成功");
            }
        });
        //删除会话
        TIMManager.getInstance().deleteConversation(TIMConversationType.Group, "" + roomNum);
    }

    class NotifyServerLiveEnd extends AsyncTask<String, Integer, LiveInfoJson> {

        @Override
        protected LiveInfoJson doInBackground(String... strings) {
            return OKhttpHelper.getInstance().notifyServerLiveStop(strings[0]);
        }

        @Override
        protected void onPostExecute(LiveInfoJson result) {
        }
    }

    /**
     * 记时器任务
     */
    private class VideoTimerTask extends TimerTask {
        public void run() {
            ++mSecond;
            handler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    /**
     * 心跳任务
     */
    private class HeartBeatTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(HEART_BEAT_TIME_TIMER_TASK);
        }
    }

    //格式化时间
    private void updateWallTime() {
        String hs, ms, ss;

        long h, m, s;
        h = mSecond / 3600;
        m = (mSecond % 3600) / 60;
        s = (mSecond % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }
        time.setText(formatTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != heartBeatTimer) {
            heartBeatTimer.cancel();
            heartBeatTimer = null;
        }
        if (null != videoTimer) {
            videoTimer.cancel();
            videoTimer = null;
        }
        QavsdkControl.getInstance().clearVideoMembers();
        QavsdkControl.getInstance().onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}

