package com.aiitec.technicalreserve.xrecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiitec.technicalreserve.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony
 *         createTime 2016/8/10.
 * @version 1.0
 */
public class XRecyclerAdapter extends CommonRecyclerViewAdapter<String>{
    @Override
    public void convert(CommonRecyclerViewHolder h, String entity, int position) {
        int itemViewType = getItemType(position);
        h.setText(R.id.tv_item_content, entity);
    }
    //返回item布局的id
    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_xrecycler;
    }

   public XRecyclerAdapter(Context context, List<String> myDataset) {
       super(context, myDataset);
    }

    public XRecyclerAdapter(Context context) {
        this(context, new ArrayList<String>());
    }

}
