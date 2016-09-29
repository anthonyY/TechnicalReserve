package com.aiitec.technicalreserve;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.achartengine.ChartActivity;
import com.aiitec.technicalreserve.ad.AdActivity;
import com.aiitec.technicalreserve.emoji.EmojiActivity;
import com.aiitec.technicalreserve.fastupload.FastUploadActivity;
import com.aiitec.technicalreserve.image.ImageShowActivity;
import com.aiitec.technicalreserve.image.MultiImageActivity;
import com.aiitec.technicalreserve.keybroad.KeybroadActivity;
import com.aiitec.technicalreserve.web.WebActivity;
import com.aiitec.technicalreserve.wheelview.WheelViewActivity;
import com.aiitec.technicalreserve.xrecycleview.CommonRecyclerViewAdapter;
import com.aiitec.technicalreserve.xrecycleview.XRecycleViewActivity;
import com.aiitec.technicalreserve.zxing.ZxingActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Android 技术储备工程主页类
 *
 * @author Anthony
 *         createTime 2016-04-06
 * @since 1.0
 * <p/>
 * 插件化开发做成库有问题，所以要单独run成一个apk，可以单独启动，也可以从本apk可以跳转过去
 */
public class MainActivity extends BaseActivity {

    @Resource(R.id.main_recycler_view)
    private RecyclerView main_recycler_view;
    private MainAdapter mAdapter;
    @Resource(stringArrayId = R.array.main_datas)
    private List<String> datas;

    @ContentView(R.layout.activity_main)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("首页");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        main_recycler_view.setHasFixedSize(true);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        main_recycler_view.setLayoutManager(mLayoutManager);

        mAdapter = new MainAdapter(this, datas);
        mAdapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, MultiImageActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ImageShowActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, EmojiActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, AdActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, KeybroadActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, WheelViewActivity.class));
                        break;

                    case 6:
                        startActivity(new Intent(MainActivity.this, ZxingActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, XRecycleViewActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, WebActivity.class));
                        break;
                    case 9:
                        try {//广播启动另一个项目mainforplugin
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.mainforplugin", "com.mainforplugin.MainActivity");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(MainActivity.this, "请安装mainforplugin APK");
                        }
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this, ChartActivity.class));
                        break;
                    case 11://文件秒传
                        startActivity(new Intent(MainActivity.this, FastUploadActivity.class));
                        break;
                    case 12:
                        try {//广播启动另一个项目aiitec.tomcat
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.aiitec.tomcat", "com.aiitec.tomcat.MainActivity");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(MainActivity.this, "请安装aiitec.tomcat APK");
                        }
                        break;
                    case 13:
                        try {//广播启动另一个项目腾讯IM
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.aiitec.imdemo", "com.aiitec.imdemo.ui.SplashActivity");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(MainActivity.this, "请安装com.aiitec.imdemo APK");
                        }
                        break;
                    case 14:
                        startActivity(new Intent(MainActivity.this, PackActivity.class));
                        break;
                    case 15:
                        try {//广播启动另一个项目aiitec.tomcat
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.example.administrator.avdemo", "com.example.administrator.avdemo.LoginActivity");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(MainActivity.this, "com.example.administrator.avdemo APK");
                        }
                        break;
                    case 16:
                        try {//广播启动另一个项目aiitec.tomcat
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ComponentName comp = new ComponentName("com.aiitec.fmzh", "an.com.alipaytest.MainActivity");
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.show(MainActivity.this, "alipaytest APK");
                        }

                        break;
                }
            }
        });
        main_recycler_view.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
