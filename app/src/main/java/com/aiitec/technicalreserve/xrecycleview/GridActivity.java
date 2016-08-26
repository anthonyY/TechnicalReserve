package com.aiitec.technicalreserve.xrecycleview;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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

public class GridActivity extends BaseActivity implements CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener {

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

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;
        private int num;
        public SpaceItemDecoration(int space, int num) {
            this.space = space;
            this.num = num;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


            outRect.bottom = space;
//            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
//            if (parent.getChildLayoutPosition(view) %num==0) {
//                outRect.left = 0;
//            } else {
//                outRect.left = space;
//            }
        }
    }
    private void init() {
        setTitle("XRecycleView");
        btn_recycler_switch_style.setText("列表");//AiiUtil.dip2px(this, 8)
        recycler_view.addItemDecoration(new SpaceItemDecoration(AiiUtil.dip2px(this, 8), 3));

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.recycle_grid_in);
        recycler_view.setLayoutAnimation(new LayoutAnimationController(anim, 0.5f));
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        recycler_view.setLoadingMoreEnabled(false);
        recycler_view.setPullRefreshEnabled(false);
        for (int i = 0; i < 10; i++) {
            GridEntity entity = new GridEntity();
            entity.setContent("内容啦啦阿斯顿" + i);
            entity.setId(i);
            entity.setImagePath(ImageShowActivity.imagePaths[i % ImageShowActivity.imagePaths.length]);
            data.add(entity);
        }
        adapter = new XRecyclerGridAdapter(this, data);
        adapter.setOnRecyclerViewItemClickListener(this);
        adapter.setOnViewInItemClickListener(new CommonRecyclerViewAdapter.OnViewInItemClickListener() {
            @Override
            public void onViewInItemClick(View v, int position) {
                if(v.getId() == R.id.tv_item_grid_content){
                    ToastUtil.show(GridActivity.this, "点击了item文字："+adapter.getItem(position).getContent());

                }
            }
        }, R.id.tv_item_grid_content );
        recycler_view.setAdapter(adapter);


    }


    @Override
    public void onItemClick(View v, int position) {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recycler_switch_style:
                finish();
                break;
        }
    }

}
