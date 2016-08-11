package com.aiitec.technicalreserve;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 主页的adapter
 * createTime 2016/4/1.
 * @author Anthony
 * @since 1.0
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<String> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            textView = (TextView)cardView.findViewById(R.id.textView);
        }
    }

    public MainAdapter( List<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main, parent, false);
        // set the view's size, margins, paddings and layout parameters
//            ...
        ViewHolder vh = new ViewHolder(cardView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClicklistener != null){
                    onItemClicklistener.onItemClick(v, position);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    private OnItemClicklistener onItemClicklistener;

    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    public interface OnItemClicklistener{
        void onItemClick(View v, int position);
    }
}