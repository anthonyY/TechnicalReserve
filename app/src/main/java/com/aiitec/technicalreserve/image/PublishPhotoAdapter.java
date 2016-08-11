package com.aiitec.technicalreserve.image;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.aiitec.openapi.utils.AiiUtil;
import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorFragment;


/**
  *
  */
public class PublishPhotoAdapter extends BaseAdapter {   
    private Activity context;
	private LayoutInflater inflater;

	private boolean isInEdit = true;
	
//	private DisplayImageOptions options;
	
	private List<String> photoInfos ;
	

	private int width;
	private Handler handler;
	
    public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public final class ItemInfo{
        public ImageView img_icon_change;
        public ImageView img_photo;
 } 
       
    public PublishPhotoAdapter(Activity context, List<String> listItems) {
        this.context = context;            
        inflater = LayoutInflater.from(context);
        this.photoInfos = listItems;
		if(photoInfos == null){
			photoInfos = new ArrayList<>();
		}
//		options = new DisplayImageOptions.Builder()
//        .showImageOnLoading(me.nereo.multi_image_selector.R.drawable.default_error)
//		.showImageForEmptyUri(me.nereo.multi_image_selector.R.drawable.default_error)
//		.showImageOnFail(me.nereo.multi_image_selector.R.drawable.default_error)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//	    .bitmapConfig(Bitmap.Config.RGB_565)
//		.cacheInMemory(true)
//		.cacheOnDisk(true)
//		.considerExifParams(true)
//		.build();
		width = (AiiUtil.getScreenWidth(context)- AiiUtil.dip2px(context, 12)) / 4;
    }
	public PublishPhotoAdapter(Activity context) {
		this(context, new ArrayList<String>());
	}

	public int getCount() {
    	if(photoInfos.size()<=8) 
        	return photoInfos.size()+1;
    	else 
    		return 9;
    }   
  
    @Override
    public int getViewTypeCount() {
    	// TODO Auto-generated method stub
    	return 2;
    }
    @Override
    public int getItemViewType(int position) {
    	// TODO Auto-generated method stub
    	if(position!=8 && position==(getCount()-1))
    		return 1;
    	else if(position==8 && photoInfos.size()==8) {
    		return 1;
    	}else 
    		return 0;
    }
    public String getItem(int arg0) {
        if(photoInfos.size() > arg0){
            return photoInfos.get(arg0);   
        } else {
            return null;               
        }
//    	if(photoInfos.size()>=9) {
//    	    return photoInfos.get(arg0);   
//    	}
//    	else {
//    		if(arg0==getCount())
//        		return null;
//        	else 
//            return photoInfos.get(arg0); 
//    	}
    	  
    }   
  
    public long getItemId(int arg0) {   
        // TODO Auto-generated method stub   
        return arg0;   
    }   
          
    /**  
     * ListView Item设置  
     */  
	public View getView(final int position, View convertView, ViewGroup parent) {   
    	 ItemInfo  itemInfo = null;  
    	 if(convertView==null) {
    			itemInfo = new ItemInfo();   
                convertView = inflater.inflate(R.layout.item_publish_photo, null);
                itemInfo.img_icon_change = (ImageView) convertView.findViewById(R.id.img_icon_change);
                itemInfo.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
                convertView.setTag(itemInfo);
    	 }
    	 else {
    		 itemInfo = (ItemInfo) convertView.getTag();
    	 }
    	 
    	 
    	 final int what = itemInfo.img_icon_change.getId();
		if (getItemViewType(position) == 0) {
			// FrameLayout.LayoutParams params = new
			// FrameLayout.LayoutParams((int)(Util.getScreenWidth(context)/3),(int)((int)(Util.getScreenWidth(context)/3)/1.64));
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
			itemInfo.img_photo.setLayoutParams(params);
			itemInfo.img_photo.setScaleType(ScaleType.CENTER_CROP);
			if (isInEdit) {
				itemInfo.img_icon_change.setVisibility(View.VISIBLE);
				itemInfo.img_icon_change
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (handler != null) {
									Message message = new Message();
									// Bundle bundle = new Bundle();
									message.what = what;
									message.arg1 = position;
									handler.sendMessage(message);
								}
							}
						});
			} else {
				itemInfo.img_icon_change.setVisibility(View.GONE);
			}

			File imageFile = new File(getItem(position));
			if (imageFile.exists()) {
				Glide.with(context)
						.load(imageFile)
						.placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
						.error(me.nereo.multi_image_selector.R.drawable.default_error)
//						.tag(MultiImageSelectorFragment.TAG)
//							.resize(mGridWidth, mGridWidth)
//							.centerCrop()
						.into(itemInfo.img_photo);
			}else{
				itemInfo.img_photo.setImageResource(me.nereo.multi_image_selector.R.drawable.default_error);
			}
		} else if (getItemViewType(position) == 1) {

			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
			itemInfo.img_photo.setLayoutParams(params);
			itemInfo.img_photo.setScaleType(ScaleType.FIT_XY);
			itemInfo.img_photo.setImageResource(R.drawable.add_equipment);
			itemInfo.img_icon_change.setVisibility(View.GONE);
		}
        return convertView;   
    }

	public void setInEditMode(boolean isDelete){
		this.isInEdit = isDelete;
	}
    public boolean isInEditMode(){
    	return this.isInEdit;
    }
    public void updateList(List<String> photoInfos){
		this.photoInfos = photoInfos;
		notifyDataSetChanged();
	}
}  

