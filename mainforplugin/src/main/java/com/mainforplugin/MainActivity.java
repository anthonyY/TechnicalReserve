package com.mainforplugin;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.utils.DLUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 这个是插件化开发的注工程，Plugin-1 和 Plugin-2作为他的插件，单独run成apk, 放到服务器，然后主工程下载就可以用了，后面会设计成协议，让有新插件时主工程提示下载
 * github https://github.com/singwhatiwanna/dynamic-load-apk
 * CSDN 博客相关内容 http://blog.csdn.net/t12x3456/article/details/39958755/
 */
public class MainActivity extends DLBasePluginActivity {
    private ArrayList<PluginAdapter.PluginItem> mPluginItems = new ArrayList<PluginAdapter.PluginItem>();
    private PluginAdapter mPluginAdapter;

    private ListView mListView;
    private TextView mNoPluginTextView;
    private LinearLayout background;
    private Button btnSkin1;
    private Button btnSkin2;

    private ProgressDialog progressDialog;

    //没有服务器测试的话把插件放在   手机跟目录/DynamicLoadHost 下
    /**
     * 插件路径
     */
    private String pluginFolder;
    /**
     * 服务器地址
     */
    private String url = "http://192.168.1.81:8080/plugin/";

    /**保存当前主题包路径的key*/
    public static final String KEY_THEME_PATH = "THEME_PACKAGE_PATH";

    private HttpUtils httpUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        background = (LinearLayout) findViewById(R.id.background);
        btnSkin1 = (Button) findViewById(R.id.btnSkin1);
        btnSkin2 = (Button) findViewById(R.id.btnSkin2);
        mNoPluginTextView = (TextView) findViewById(R.id.mNoPluginTextView);
        mListView = (ListView) findViewById(R.id.mListView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在下载......");
        mPluginAdapter = new PluginAdapter(this,mPluginItems);
        mPluginAdapter.setOnButtonClickListener(new PluginAdapter.OnButtonClickListener() {
            @Override
            public void switchSkin(int position) {
                PluginAdapter.PluginItem item = mPluginAdapter.getItem(position);
                String path = null;
                if(item.id != PluginAdapter.PluginItem.DEFAULT_THEME){//-1 代表默认主题
                    path = item.pluginPath;
                }
                AiiUtil.putString(MainActivity.this, KEY_THEME_PATH, path);
                changeSkin(path);
            }

            @Override
            public void gotoPlugin(int position) {
                PluginAdapter.PluginItem item = mPluginItems.get(position);
                DLPluginManager pluginManager = DLPluginManager.getInstance(MainActivity.this);
                pluginManager.startPluginActivity(MainActivity.this, new DLIntent(item.packageInfo.packageName, item.launcherActivityName));
            }
        });
        mListView.setAdapter(mPluginAdapter);
    }
    private void initData() {
        httpUtils = new HttpUtils();
        if(!AiiUtil.isSDCardEnable()){
            return;
        }
        File file = getExternalCacheDir();
        if(file != null){
            pluginFolder = file.getAbsolutePath()+"/DynamicLoadHost";
        } else {
            pluginFolder = Environment.getExternalStorageDirectory() + "/DynamicLoadHost";
        }


        String path = AiiUtil.getString(this, KEY_THEME_PATH);
        changeSkin(path);

        loadData();
    }

    private void loadData() {
        File file = new File(pluginFolder);
        if(!file.exists()){
            file.mkdir();
        }

        File[] plugins = file.listFiles();
        if (plugins == null || plugins.length == 0) {
            mNoPluginTextView.setVisibility(View.VISIBLE);
            btnSkin1.setVisibility(View.VISIBLE);
            btnSkin2.setVisibility(View.VISIBLE);
            mPluginItems.clear();
            mPluginAdapter.update(mPluginItems);
            return;
        }

        mPluginItems.clear();
        //遍历该目录下的所有文件（插件）
        for (File plugin : plugins) {


            PluginAdapter.PluginItem item = new PluginAdapter.PluginItem();
            item.pluginPath = plugin.getAbsolutePath();
            item.packageInfo = DLUtils.getPackageInfo(this, item.pluginPath);
            if (item.packageInfo.activities != null
                    && item.packageInfo.activities.length > 0) {
                item.launcherActivityName = item.packageInfo.activities[0].name;
            }
            if (item.packageInfo.services != null
                    && item.packageInfo.services.length > 0) {
                item.launcherServiceName = item.packageInfo.services[0].name;
            }

            mPluginItems.add(item);
            if(item.launcherActivityName.equals("com.ryg.dynamicload.sample.mainplugin.MainActivity")){
                btnSkin1.setVisibility(View.GONE);
            } else if(item.launcherActivityName.equals("com.plugin_2.MainActivity")){
                btnSkin2.setVisibility(View.GONE);
            }
        }
        PluginAdapter.PluginItem item = new PluginAdapter.PluginItem();
        item.id = -1;
        mPluginItems.add(item);
        mPluginAdapter.update(mPluginItems);
    }

    public void doDownload(View view) {
        if(view.getId() ==  R.id.btnSkin1) {
            download("plugin-1-debug.apk");
        } else if(view.getId() ==  R.id.btnSkin2){
            download("plugin-2-debug.apk");
        }
    }
    public void deletePlugin(View view) {
        if(view.getId() == R.id.deleteAllPlugin){
            deleteAllPlugin();
        }
    }


    private void download(String fileName) {

        httpUtils.download(url+fileName, pluginFolder+"/"+fileName, true, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(), "下载完成！", Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载完成！");
                loadData();
                progressDialog.dismiss();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                progressDialog.setMessage("正在下载  "+(current*100/total)+"%");

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getApplicationContext(), "下载失败！"+msg, Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载失败！"+msg);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }
    @SuppressWarnings("deprecation")
    private void setBackgroundLowVersion(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setBackgroundHeightVersion(View view, Drawable drawable) {
        view.setBackground(drawable);
    }
    /**
     * 更改皮肤 （目前只有背景）
     */
    public void changeSkin(String path){

        if(!TextUtils.isEmpty(path)){
            DLPluginPackage p = DLPluginManager.getInstance(MainActivity.this).loadApk(path);
            //背景   插件的R.drawable.background
            //根据需要获取其他资源
            int backgroundRec = p.resources.getIdentifier("background", "drawable", p.packageName);// ppmm.jpg
            if (backgroundRec > 0) {
                Drawable drawable = p.resources.getDrawable(backgroundRec);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackgroundHeightVersion(background, drawable);
                } else {
                    setBackgroundLowVersion(background, drawable);
                }
            }

        } else {//路径为空就是默认主题
            background.setBackgroundResource(R.drawable.background);
        }
    }

    private void deleteAllPlugin(){

        File file = new File(pluginFolder);
        if(file.exists()){
            for(File f: file.listFiles()){
                if(!f.isDirectory()){
                    f.delete();
                }
            }
        }
        loadData();
    }
}
