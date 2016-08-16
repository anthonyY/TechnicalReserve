package com.aiitec.technicalreserve;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiitec.technicalreserve.xrecycleview.CommonRecyclerViewAdapter;
import com.aiitec.technicalreserve.xrecycleview.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页的adapter
 * createTime 2016/4/1.
 * @author Anthony
 * @since 1.0
 */
public class MainAdapter extends CommonRecyclerViewAdapter<String>{


    @Override
    public void convert(CommonRecyclerViewHolder h, String entity, int position) {
        h.setText(R.id.textView, entity);
    }
    //返回item布局的id
    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_main;
    }

    public MainAdapter(Context context, List<String> myDataset) {
        super(context, myDataset);
    }

    public MainAdapter(Context context) {
        this(context, new ArrayList<String>());
    }


}