package com.aiitec.technicalreserve.xrecycleview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;

import com.aiitec.openapi.utils.AiiUtil;
import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.aiitec.technicalreserve.image.ImageShowActivity;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GridActivity extends BaseActivity implements  XRecyclerGridAdapter.OnItemClickListener{

    @Resource(R.id.btn_recycler_switch_style)
    private Button btn_recycler_switch_style;
    @ContentView(R.layout.activity_xrecycle)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    @Resource(R.id.recycler_view)
    private XRecyclerView recycler_view;
    private XRecyclerGridAdapter adapter;
    private List<GridEntity> data = new ArrayList<>();

    private void init() {
        setTitle("XRecycleView");
        btn_recycler_switch_style.setText("列表");
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.offsetChildrenHorizontal(AiiUtil.dip2px(this, 8));
        mLayoutManager.offsetChildrenVertical(AiiUtil.dip2px(this,16));
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.recycle_grid_in);
        recycler_view.setLayoutAnimation(new LayoutAnimationController(anim, 0.5f));
        recycler_view.setLayoutManager(mLayoutManager);

        recycler_view.setLoadingMoreEnabled(false);
        recycler_view.setPullRefreshEnabled(false);
        for(int i=0; i<10; i++){
            GridEntity entity = new GridEntity();
            entity.setContent("内容啦啦阿斯顿"+i);
            entity.setId(i);
            entity.setImagePath(ImageShowActivity.imagePaths[i%ImageShowActivity.imagePaths.length]);
            data.add(entity);
        }
        adapter = new XRecyclerGridAdapter(this,data);
        adapter.setOnItemClickListener(this);
        recycler_view.setAdapter(adapter);


    }


    @Override
    public void onItemClick(View v, int position) {

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_recycler_switch_style:
                finish();
                break;
        }
    }
}
