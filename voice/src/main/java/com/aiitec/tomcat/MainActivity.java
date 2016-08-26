package com.aiitec.tomcat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends Activity implements View.OnClickListener{
    private static String TAG = "TAG";

    private EditText  iat_text;
    private Button iat_recognize,iat_stop,iat_cancel,iat_recognize_stream,tv_text,btn_choose;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String language = "zh_cn"; //翻译语言，默认中文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);


    }

    private void initView() {
        iat_text = (EditText) findViewById(R.id.iat_text);
        iat_recognize = (Button) findViewById(R.id.iat_recognize);
        iat_stop = (Button) findViewById(R.id.iat_stop);
        iat_cancel = (Button) findViewById(R.id.iat_cancel);
        iat_recognize_stream = (Button) findViewById(R.id.iat_recognize_stream);
        tv_text = (Button) findViewById(R.id.tv_text);
        btn_choose = (Button) findViewById(R.id.btn_choose);

        iat_recognize.setOnClickListener(this);
        iat_stop.setOnClickListener(this);
        iat_cancel.setOnClickListener(this);
        iat_recognize_stream.setOnClickListener(this);
        tv_text.setOnClickListener(this);
        btn_choose.setOnClickListener(this);

        initDialog();

    }

    private AlertDialog.Builder builder;

    private void initDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择语种");
        final String[] sex = {"中文", "英文"};
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'中文' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name =  sex[which];
                if(name.equals("中文")){
                    language = "zh_cn";
                }else if (name.equals("英文")){
                    language = "en_us";
                }
                Toast.makeText(MainActivity.this, "选择了：" + sex[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(MainActivity.this,"初始化失败，错误码：" + code,Toast.LENGTH_SHORT).show();
            }
        }
    };

    int ret = 0; // 函数调用返回值
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iat_recognize:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(MainActivity.this, "iat_recognize");

                iat_text.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    Toast.makeText(MainActivity.this,getString(R.string.text_begin),Toast.LENGTH_LONG).show();
                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        Toast.makeText(MainActivity.this,"听写失败,错误码：" + ret,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,getString(R.string.text_begin),Toast.LENGTH_LONG).show();
                    }
                }

                break;
            //停止听写
            case R.id.iat_stop:
                mIat.stopListening();
                Toast.makeText(MainActivity.this,"停止听写",Toast.LENGTH_LONG).show();
                break;
            // 取消听写
            case R.id.iat_cancel:
                mIat.cancel();
                Toast.makeText(MainActivity.this,"取消听写",Toast.LENGTH_LONG).show();
                break;
            // 音频流识别
            case R.id.iat_recognize_stream:
                iat_text.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                // 设置音频来源为外部文件
                mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
                // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
                // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
                // mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    Toast.makeText(MainActivity.this,"识别失败,错误码：" + ret,Toast.LENGTH_LONG).show();
                } else {
                    byte[] audioData = FucUtil.readAudioFile(MainActivity.this, "iattest.wav");

                    if (null != audioData) {
                        Toast.makeText(MainActivity.this,getString(R.string.text_begin_recognizer),Toast.LENGTH_LONG).show();
                        // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                        // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                        // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
                        // 音频切分方法：FucUtil.splitBuffer(byte[] buffer,int length,int spsize);
                        mIat.writeAudio(audioData, 0, audioData.length);
                        mIat.stopListening();
                    } else {
                        mIat.cancel();
                        Toast.makeText(MainActivity.this,"读取音频流失败",Toast.LENGTH_LONG).show();
                    }
                }

                break;

            case R.id.tv_text:
                Intent intent = new Intent(this,ReadingTextActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_choose:
                language = "zh_cn";

                builder.show();

                break;
        }
    }


    private void setParam() {
// 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
//        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, language);
//        } else {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
//        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");



    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(MainActivity.this,error.getPlainDescription(true),Toast.LENGTH_SHORT).show();
        }

    };


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        iat_text.setText(resultBuffer.toString());
        iat_text.setSelection(iat_text.length());
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Toast.makeText(MainActivity.this,"开始说话",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            Toast.makeText(MainActivity.this,error.getPlainDescription(true),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Toast.makeText(MainActivity.this,"结束说话",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Toast.makeText(MainActivity.this,"当前正在说话，音量大小：" + volume,Toast.LENGTH_SHORT).show();
            Log.d(TAG, "返回音频数据："+data.length);
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
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(MainActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(MainActivity.this);
        super.onPause();
    }

}
