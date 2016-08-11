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
public class XRecyclerAdapter extends RecyclerView.Adapter<XRecyclerAdapter.ViewHolder>{
    private List<String> mDataset;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tv_item_content;
        public ViewHolder(View v) {
            super(v);
            view = v;
            tv_item_content = (TextView) v.findViewById(R.id.tv_item_content);
        }
    }

    public XRecyclerAdapter(Context context, List<String> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }
    public XRecyclerAdapter(Context context) {
        mDataset = new ArrayList<>();
        this.context = context;
    }
    @Override
    public XRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_xrecycler, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String data = mDataset.get(position);
        holder.tv_item_content.setText(data);
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

    public void updateList(List<String> orgens){
        this.mDataset = orgens;
        notifyDataSetChanged();
    }
    public void addList(List<String> orgens){
        this.mDataset.addAll(orgens);
        notifyDataSetChanged();
    }

    public String getItem(int position){
        return mDataset.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
