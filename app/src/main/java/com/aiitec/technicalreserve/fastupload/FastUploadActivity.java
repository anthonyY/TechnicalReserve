package com.aiitec.technicalreserve.fastupload;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.aiitec.openapi.enums.ServiceType;
import com.aiitec.openapi.net.AIIRequest;
import com.aiitec.openapi.net.AIIResponse;
import com.aiitec.openapi.net.UploadFileUtils;
import com.aiitec.openapi.packet.UploadFilesRequest;
import com.aiitec.openapi.packet.UploadFilesResponse;
import com.aiitec.openapi.utils.LogUtil;
import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.openapi.view.annatation.event.OnClick;
import com.aiitec.openapi.view.annatation.event.OnItemClick;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.aiitec.technicalreserve.image.ImageShowActivity;
import com.aiitec.technicalreserve.image.PublishPhotoAdapter;
import com.aiitec.widgets.NoScrollGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * 秒传文件, 需要服务端支持，目前老麦已经做了支持，但是还没有应用到项目中
 */
public class FastUploadActivity extends BaseActivity {

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

	private AIIRequest aiiRequest;
	public static final String API = "http://192.168.1.67/newImageApi/public/api";
	private boolean isOpenFastUpload = true;
	private UploadFileUtils uploadFileUtils;
	@Resource(R.id.fab)
	private FloatingActionButton fab;
	@ContentView(R.layout.activity_muti_image_select)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		et_publish_content.setText(API);
		setTitle("图片选择");
		adapter = new PublishPhotoAdapter(this, mSelectPath);
		adapter.setHandler(handler);
		gv_publish_image.setAdapter(adapter);
		aiiRequest = new AIIRequest(this);
		aiiRequest.setServiceType(ServiceType.PHP);
		aiiRequest.setUrl(API);
		uploadFileUtils = new UploadFileUtils(this, aiiRequest);
		uploadFileUtils.setOpenFastUpload(isOpenFastUpload);

		LogUtil.showLog = true;

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				aiiRequest.setUrl(et_publish_content.getText().toString());
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				for(String path: mSelectPath){
					File file = new File(path);
					map.put(file.getName(), file);
				}
				LogUtil.e("api:"+API);
				uploadFileUtils.requestUpload(map, aiiResponse, 1);
			}
		});

		et_publish_content.setImeOptions(EditorInfo.IME_ACTION_DONE);


	}
	AIIResponse aiiResponse = new AIIResponse(FastUploadActivity.this){
		@Override
		public void onStart(int index) {
			super.onStart(index);

		}

		@Override
		public void onFailure(String content, int index) {
			super.onFailure(content, index);
			ToastUtil.show(FastUploadActivity.this, "网络异常");
		}

		@Override
		public void onSuccess(String content, int index) {
			super.onSuccess(content, index);
//			UploadFilesResponse response = new UploadFilesResponse();
			Log.i("aiitec", content);
			ToastUtil.show(FastUploadActivity.this, "上传成功");

		}
	};
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:// 点击返回图标事件
				this.finish();
				break;
			case R.id.action_more:
				ToastUtil.show(getApplicationContext(), "点击了右上角按钮");
				if(isOpenFastUpload){
					item.setTitle("开启秒传");
				} else {
					item.setTitle("关闭秒传");
				}
				uploadFileUtils.setOpenFastUpload(!isOpenFastUpload);
				isOpenFastUpload = !isOpenFastUpload;
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
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
			Intent intent = new Intent(FastUploadActivity.this, MultiImageSelectorActivity.class);
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
