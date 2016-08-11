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
public class XRecyclerGridAdapter extends RecyclerView.Adapter<XRecyclerGridAdapter.ViewHolder>{
    private List<GridEntity> mDataset;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tv_item_grid_content;
        ImageView iv_item_grid_img;
        public ViewHolder(View v) {
            super(v);
            view = v;
            tv_item_grid_content = (TextView) v.findViewById(R.id.tv_item_grid_content);
            iv_item_grid_img = (ImageView) v.findViewById(R.id.iv_item_grid_img);
        }
    }

    public XRecyclerGridAdapter(Context context, List<GridEntity> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }
    public XRecyclerGridAdapter(Context context) {
        mDataset = new ArrayList<>();
        this.context = context;
    }
    @Override
    public XRecyclerGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        int width =(AiiUtil.getScreenWidth(context)-AiiUtil.dip2px(context, 32))/3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        view.setLayoutParams(params);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        GridEntity data = mDataset.get(position);
        holder.tv_item_grid_content.setText(data.getContent());
        Glide.with(context).load(data.getImagePath()).into(holder.iv_item_grid_img);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateList(List<GridEntity> orgens){
        this.mDataset = orgens;
        notifyDataSetChanged();
    }
    public void addList(List<GridEntity> orgens){
        this.mDataset.addAll(orgens);
        notifyDataSetChanged();
    }

    public GridEntity getItem(int position){
        return mDataset.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
