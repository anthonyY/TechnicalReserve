package com.aiitec.technicalreserve;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.ViewUtils;
import com.aiitec.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Anthony
 *         CreateTime 2016/4/7.
 * @since 1.0
 */
public class BaseActivity extends AppCompatActivity{
//    TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        setStatusBarTranslucent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }
    @Override
    public void setTitle(int title) {
        getSupportActionBar().setTitle(title);
    }
    private SystemBarTintManager tintManager;
    protected void setStatusBarBackgroundRes(int res){
        tintManager.setStatusBarTintResource(res);
    }
    /**
     * 设置沉浸式布局状态栏背景状态
     * 设置了沉浸式布局后，xml布局的跟布局的padding将不起作用，如需padding，需嵌套一个布局来padding
     */
    public void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);

        /**
         * 如果是自定义标题栏，去掉了ActionBar的话，需在 Activity的布局中的根布局，加上以下的2句属性
         * */
        // 如果不在 xml 布局中设置以下2个属性，可在代码中动态设置(建议使用该种方式)
        // android:fitsSystemWindows="true"
        // android:clipToPadding="true"
        // 动态设置：获取整个根布局的id并设置top和bottom的padding
        // SystemBarConfig config = tintManager.getConfig();
        // RelativeLayout rl = (RelativeLayout) findViewById(R.id.rrll);
        // rl.setPadding(0, config.getPixelInsetTop(false), 0,
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        // if (on) {
        // winParams.flags |= bits;
        // } else {
        // winParams.flags &= ~bits;
        // }
        winParams.flags |= bits;
        win.setAttributes(winParams);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
