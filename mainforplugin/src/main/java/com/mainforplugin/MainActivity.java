package com.mainforplugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aiitec.openapi.net.AIIRequest;
import com.aiitec.openapi.utils.LogUtil;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLBasePluginFragmentActivity;
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
public class MainActivity extends Activity {
    private ArrayList<PluginAdapter.PluginItem> mPluginItems = new ArrayList<PluginAdapter.PluginItem>();
    private PluginAdapter mPluginAdapter;

    private ListView mListView;
    private TextView mNoPluginTextView;
    private LinearLayout background;

    AIIRequest aiiRequest;


    //没有服务器测试的话把插件放在   手机跟目录/DynamicLoadHost 下
    /**
     * 插件路径
     */
    public static String pluginFolder;

    /**保存当前主题包路径的key*/
    public static final String KEY_THEME_PATH = "THEME_PACKAGE_PATH";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.showLog = true;
        initView();
        initData();
    }

    private void initView() {
        background = (LinearLayout) findViewById(R.id.background);
//        btnSkin1 = (Button) findViewById(R.id.btnSkin1);
//        btnSkin2 = (Button) findViewById(R.id.btnSkin2);
        mNoPluginTextView = (TextView) findViewById(R.id.mNoPluginTextView);
        mListView = (ListView) findViewById(R.id.mListView);

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

        if(!AiiUtil.isSDCardEnable()){
            return;
        }
        File file = getExternalCacheDir();
        if(file != null){
            pluginFolder = file.getAbsolutePath()+"/DynamicLoadHost/";
        } else {
            pluginFolder = Environment.getExternalStorageDirectory() + "/DynamicLoadHost/";
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
        }
        PluginAdapter.PluginItem item = new PluginAdapter.PluginItem();
        item.id = -1;
        mPluginItems.add(item);
        mPluginAdapter.update(mPluginItems);
    }

    public void doDownload(View view) {
        if(view.getId() ==  R.id.btnSkin2){
            startActivityForResult(new Intent(this, OnlineSkinActivity.class), 1);
        }
    }
    public void deletePlugin(View view) {
        if(view.getId() == R.id.deleteAllPlugin){
            deleteAllPlugin();
        }
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
        AiiUtil.putString(MainActivity.this, KEY_THEME_PATH, null);
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadData();
    }
}
