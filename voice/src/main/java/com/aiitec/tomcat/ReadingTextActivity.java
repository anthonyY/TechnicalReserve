package com.aiitec.tomcat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

public class ReadingTextActivity extends Activity implements View.OnClickListener {
    private static String TAG = "TAG";
    private EditText iat_text;
    private Button tts_btn_person_select,tts_play,tts_cancel,tts_pause,tts_resume;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue ;

    // 默认发音人
    private String voicer = "xiaoyan";

    private SharedPreferences mSharedPreferences;

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 引擎类型  在线合成
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private int selectedNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_text);

        initView();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(ReadingTextActivity.this, mTtsInitListener);

        // 云端发音人名称列表
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);

    }

    private void initView() {
        iat_text = (EditText) findViewById(R.id.iat_text);
        tts_btn_person_select = (Button) findViewById(R.id.tts_btn_person_select);
        tts_play = (Button) findViewById(R.id.tts_play);
        tts_cancel = (Button) findViewById(R.id.tts_cancel);
        tts_pause = (Button) findViewById(R.id.tts_pause);
        tts_resume = (Button) findViewById(R.id.tts_resume);
        tts_btn_person_select.setOnClickListener(this);
        tts_play.setOnClickListener(this);
        tts_cancel.setOnClickListener(this);
        tts_pause.setOnClickListener(this);
        tts_resume.setOnClickListener(this);

    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(ReadingTextActivity.this,"初始化失败,错误码：" + code,Toast.LENGTH_SHORT).show();
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //选择发音人
            case R.id.tts_btn_person_select:
                showPresonSelectDialog();

                break;
            //开始合成
            // 收到onCompleted 回调时，合成结束、生成合成音频
            // 合成的音频格式：只支持pcm格式
            case R.id.tts_play:
                // 移动数据分析，收集开始合成事件
                FlowerCollector.onEvent(ReadingTextActivity.this, "tts_play");

                String text = iat_text.getText().toString();
                // 设置参数
                setParam();
                int code = mTts.startSpeaking(text, mTtsListener);
                //			/**
                //			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
                //			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
                //			*/
                //			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
                //			int code = mTts.synthesizeToUri(text, path, mTtsListener);

                if (code != ErrorCode.SUCCESS) {
                    if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                        //未安装则跳转到提示安装页面
//                        mInstaller.install();
                    }else {
                        Toast.makeText(ReadingTextActivity.this,"语音合成失败,错误码: " + code,Toast.LENGTH_LONG).show();
                    }
                }

                break;
            //取消
            case R.id.tts_cancel:
                mTts.stopSpeaking();
                break;
            //暂停播放
            case R.id.tts_pause:
                mTts.pauseSpeaking();
                break;
            //继续播放
            case R.id.tts_resume:
                mTts.resumeSpeaking();
                break;

        }
    }

    private void showPresonSelectDialog() {
        new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
                .setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项,各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                voicer = mCloudVoicersValue[which];
                                if ("catherine".equals(voicer) || "henry".equals(voicer) || "vimary".equals(voicer)) {
                                    iat_text.setText(R.string.text_tts_source_en);
                                } else {
                                    iat_text.setText(R.string.text_tts_source);
                                }
                                selectedNum = which;
                                dialog.dismiss();
                            }
                        }).show();


    }

    /**
     * 参数设置
     * @param
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Toast.makeText(ReadingTextActivity.this,"开始播放",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakPaused() {
            Toast.makeText(ReadingTextActivity.this,"暂停播放",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakResumed() {
            Toast.makeText(ReadingTextActivity.this,"继续播放",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            Toast.makeText(ReadingTextActivity.this,String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            Toast.makeText(ReadingTextActivity.this,String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Toast.makeText(ReadingTextActivity.this,"播放完成",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(ReadingTextActivity.this,error.getPlainDescription(true),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(ReadingTextActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }
    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(ReadingTextActivity.this);
        super.onPause();
    }

}
