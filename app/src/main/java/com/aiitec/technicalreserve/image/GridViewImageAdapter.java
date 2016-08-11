package com.aiitec.technicalreserve.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiitec.openapi.utils.AiiUtil;
import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/3/25.
 */
public class GridViewImageAdapter extends CommonAdapter<String> {
    public GridViewImageAdapter(Context context, List<String> list){
        super(context,list, R.layout.item_image_show_gridview);
    }
    @Override
    public void convert(ViewHolder holder, String item, int position) {

        ImageView iv_item_circle_gridview = holder.getView(R.id.iv_item_circle_gridview);
        ViewGroup.LayoutParams params =iv_item_circle_gridview.getLayoutParams();
//        new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, AiiUtil.dip2px(mContext, 100));
        int size = getCount();
        if(size == 2 || size == 4){
            params.width = (AiiUtil.getScreenWidth(mContext)- AiiUtil.dip2px(mContext, 4))>>1;
            params.height = params.width;

            iv_item_circle_gridview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if(size == 1){
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height =  AiiUtil.dip2px(mContext, 200);

            iv_item_circle_gridview.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            params.width = (AiiUtil.getScreenWidth(mContext)- AiiUtil.dip2px(mContext, 8))/3;
            params.height = params.width;
            iv_item_circle_gridview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        iv_item_circle_gridview.setLayoutParams(params);

        String imagePath = "";
        if(item.startsWith("http")){
            imagePath = item ;
        } else {
            imagePath = "file://"+item;
        }
//        ImageLoader.getInstance().displayImage(imagePath, iv_item_circle_gridview, options);
        Glide.with(mContext)
                .load(imagePath)
                .error(me.nereo.multi_image_selector.R.drawable.default_error)
                .into(iv_item_circle_gridview);
    }
}
