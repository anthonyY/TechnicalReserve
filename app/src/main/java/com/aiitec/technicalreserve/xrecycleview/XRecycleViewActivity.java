package com.aiitec.technicalreserve.xrecycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.MainAdapter;
import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * 键盘回车的文字描述与监听类
 * 
 * @author Chalin
 * 
 * 快速集成说明:
 * 对应的技术点说明，在 activity_keybroad.xml 中已经有说明，请查看。
 * 
 */
public class XRecycleViewActivity extends BaseActivity implements XRecyclerView.LoadingListener, XRecyclerAdapter.OnItemClickListener {

	@Resource(R.id.btn_recycler_switch_style)
	private Button btn_recycler_switch_style;
	@Resource(R.id.recycler_view)
	private XRecyclerView recycler_view;
	private XRecyclerAdapter adapter;
	@Resource(stringArrayId = R.array.image_show_datas)
	private List<String> data;
	private List<String> data2 = new ArrayList<>();

	private void init() {
		setTitle("XRecycleView");
		btn_recycler_switch_style.setText("网格");
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recycler_view.setLayoutManager(mLayoutManager);
		recycler_view.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
//		recycler_view.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotate);
//		recycler_view.setArrowImageView(R.drawable.iconfont_downgrey);

		recycler_view.setLoadingListener(this);
		data2.addAll(data);
		adapter = new XRecyclerAdapter(this,data2);
		adapter.setOnItemClickListener(this);
		recycler_view.setAdapter(adapter);


	}

	@ContentView(R.layout.activity_xrecycle)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		init();
	}

	@Override
	public void onItemClick(View v, int position) {
		ToastUtil.show(XRecycleViewActivity.this, "点击了"+adapter.getItem(position));
	}

	@Override
	public void onRefresh() {
		data2.clear();
		data2.addAll(data);
		adapter.updateList(data2);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				recycler_view.refreshComplete();
				ToastUtil.show(XRecycleViewActivity.this, "刷新完成");
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				recycler_view.loadMoreComplete();
				for(String str: data){
					data2.add(str);
				}
				adapter.updateList(data2);
//				ToastUtil.show(XRecycleViewActivity.this, "加载更多完成");
			}
		}, 1000);
	}

	public void onClick(View v){
		switch(v.getId()){
			case R.id.btn_recycler_switch_style:
				startActivity(new Intent(this, GridActivity.class));
				break;
		}
	}
}