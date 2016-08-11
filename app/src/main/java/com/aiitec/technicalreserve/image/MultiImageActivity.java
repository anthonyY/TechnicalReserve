package com.aiitec.technicalreserve.image;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.ViewUtils;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.openapi.view.annatation.event.OnItemClick;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.aiitec.widgets.NoScrollGridView;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 多图片选择类， 此类会调用多图片选择库multi-image-selector
 * 只需要跳转到那个类，再回来在onActivityResult 方法里处理就行了
 * 如：
 *      int maxNum = 9;
 *      Intent intent = new Intent(MultiImageActivity.this, MultiImageSelectorActivity.class);
 *      // 是否显示拍摄图片
 *      intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
 *      // 最大可选择图片数量
 *      intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
 *      // 选择模式
 *      intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
 *      // 默认选择
 *      if (mSelectPath != null && mSelectPath.size() > 0) {
 *          intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
 *      }
 *      startActivityForResult(intent, REQUEST_IMAGE);
 *
 *      回调再处理
 *       @Override
 *      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *      super.onActivityResult(requestCode, resultCode, data);
 *      if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
 *          ArrayList mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
 *          // adapter.updateList(mSelectPath);
 *      }
 *      这样就行了
 * @author  Anthony
 */
public class MultiImageActivity extends BaseActivity {

    /**选择图片跳转请求码*/
    private static final int REQUEST_IMAGE = 2;

    public static final String KEY_SELECTED_IMAGE = "selectPath";

    @Resource(R.id.gv_publish_image)
    private NoScrollGridView gv_publish_image;
    @Resource(R.id.et_publish_content)
    private EditText et_publish_content;

    /**图片路径列表*/
    private ArrayList<String> mSelectPath;
    /**视频路径*/
    private String videoPath;
    private PublishPhotoAdapter adapter ;

    @ContentView(R.layout.activity_muti_image_select)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("图片选择");
        adapter = new PublishPhotoAdapter(this, mSelectPath);
        adapter.setHandler(handler);
        gv_publish_image.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MultiImageActivity.this, ImageShowActivity.class);
                intent.putExtra(KEY_SELECTED_IMAGE, mSelectPath);
                startActivity(intent);
            }
        });
    }

    private android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case R.id.img_icon_change:
                {
                    mSelectPath.remove(msg.arg1);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            return false;
        }
    });


    @OnItemClick(R.id.gv_publish_image)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int last = parent.getAdapter().getCount() - 1;
        ToastUtil.show(this, "last:"+last +"------position:"+position);
        if (position == last) {
            int maxNum = 9;
            Intent intent = new Intent(MultiImageActivity.this, MultiImageSelectorActivity.class);
            // 是否显示拍摄图片
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大可选择图片数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
            // 选择模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            // 默认选择
            if (mSelectPath != null && mSelectPath.size() > 0) {
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
            }
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            for(String path: mSelectPath){
                Log.e("aiitec", "---" + path);
            }
            adapter.updateList(mSelectPath);

        }
    }



}
