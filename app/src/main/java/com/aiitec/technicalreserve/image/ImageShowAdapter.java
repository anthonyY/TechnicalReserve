package com.aiitec.technicalreserve.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aiitec.technicalreserve.R;
import com.aiitec.widgets.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
 public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.ViewHolder> {

    private List<Message> messages;
    private Context context;
//    private static final int HORIZONTAL_SPACING = 4; // gridview横向间距

    public ImageShowAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_show, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


        final Message message = messages.get(position);


/* 9宫格图片展示TAG_AII_NET
        *//** ================================================================ */
        viewHolder.tv_item_circle_content.setText(message.getContent()!=null?message.getContent():"");
        if (message.getImages() != null && message.getImages().size() > 0) {

            viewHolder.gv_item_circle_content.setVisibility(View.VISIBLE);
            if (message.getImages().size() == 1 ) {
                viewHolder.gv_item_circle_content.setNumColumns(1);
            }
            else if (message.getImages().size() == 4 || message.getImages().size() == 2) {
                 viewHolder.gv_item_circle_content.setNumColumns(2);
             } else {
                 viewHolder.gv_item_circle_content.setNumColumns(3);
            }
            viewHolder.gv_item_circle_content.setVisibility(View.VISIBLE);

            final ArrayList<String> paths = new ArrayList<String>();

            for (int i = 0; i < message.getImages().size(); i++) {
                paths.add(message.getImages().get(i).getPath());

            }

            GridViewImageAdapter imageAdapter = new GridViewImageAdapter( context, paths);
            viewHolder.gv_item_circle_content.setAdapter(imageAdapter);
            viewHolder.gv_item_circle_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    Intent intent = new Intent(context, ImagePagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ImagePagerActivity.IMAGES, paths);
                    intent.putExtra(ImagePagerActivity.STATE_POSITION, arg2);
                    context.startActivity(intent);
                }
            });
        }


/////////////////////////////////////////////


        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

//		viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				if(mItemLongClickListener!=null) {
//					mItemLongClickListener.onItemLongClick(v,position);
//				}
//				return false;
//			}
//		});


    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return messages.size();
    }




    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_circle_content;//内容

        private NoScrollGridView gv_item_circle_content;

        public View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            tv_item_circle_content = (TextView) view.findViewById(R.id.tv_item_circle_content);

            gv_item_circle_content = (NoScrollGridView) view.findViewById(R.id.gv_item_circle_content);

        }
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

//	public void setOnItemLongClickListener(MyItemLongClickListener listener){
//		this.mItemLongClickListener = listener;
//	}



    private MyItemClickListener mItemClickListener;

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

//	public interface  MyItemLongClickListener{
//		void onItemLongClick(View view,int postion);
//	}

    public void updateList(List<Message> list) {
        this.messages = list;
        this.notifyDataSetChanged();
    }
}