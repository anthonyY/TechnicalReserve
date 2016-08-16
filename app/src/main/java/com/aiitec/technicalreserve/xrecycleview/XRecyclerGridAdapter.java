package com.aiitec.technicalreserve.xrecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiitec.openapi.utils.AiiUtil;
import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony
 *         createTime 2016/8/10.
 * @version 1.0
 */
public class XRecyclerGridAdapter extends CommonRecyclerViewAdapter<GridEntity>{

    @Override
    public void convert(CommonRecyclerViewHolder h, GridEntity entity, int position) {
        h.setText(R.id.tv_item_grid_content, entity.getContent());
        ImageView iv = (ImageView) h.getView(R.id.iv_item_grid_img);
        Glide.with(context).load(entity.getImagePath()).placeholder(R.drawable.default_error).into(iv);
        int width =(AiiUtil.getScreenWidth(context)-AiiUtil.dip2px(context, 32))/3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        ((RelativeLayout)h.getView(R.id.rl_item)).setLayoutParams(params);
    }
    //返回item布局的id
    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_grid;
    }


    public XRecyclerGridAdapter(Context context, List<GridEntity> myDataset) {
        super(context, myDataset);
    }
    public XRecyclerGridAdapter(Context context) {
        this(context, new ArrayList<GridEntity>());
    }


}
