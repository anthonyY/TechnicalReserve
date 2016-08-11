
package com.aiitec.technicalreserve.ad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;

public class BannerAdapter extends PagerAdapter {
	private View rowView;
	private Context context;
//	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private SparseArray<View> rowViews = new SparseArray<>();
	private List<HashMap<String, String>> list;
	public static final String KEY_IMG = "img";
	public static final String KEY_URL = "url";
	public static final String KEY_NAME = "name";
//	private int[] images = new int[]{R.drawable.t0,R.drawable.t1,R.drawable.t2,R.drawable.t3};
//	private DisplayImageOptions options;
	public BannerAdapter(Context context,List<String> list) {
		this.context = context;
		this.list = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_IMG, list.get(i));
			this.list.add(map);
		}
//	 	options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.default_error)
//		.showImageForEmptyUri(R.drawable.default_error)
//		.showImageOnFail(R.drawable.default_error)
//		.cacheInMemory(true)
//		.cacheOnDisk(true)
//		.considerExifParams(true)
//		.build();
	}
	public BannerAdapter(Context context,ArrayList<HashMap<String,String>> list) {
		this.context = context;
		this.list = list;
//		options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.default_error)
//		.showImageForEmptyUri(R.drawable.default_error)
//		.showImageOnFail(R.drawable.default_error)
//		.cacheInMemory(true)
//		.cacheOnDisk(true)
//		.considerExifParams(true)
//		.build();
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		BannerHolder holder = null;
		rowView = rowViews.get(arg1);
		if (rowView == null) {
			rowView = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
			holder = new BannerHolder(rowView);
			rowView.setTag(holder);
		} else {
			holder = (BannerHolder) rowView.getTag();
		}
		String adImg = list.get(arg1).get(KEY_IMG);
		final String adLink = list.get(arg1).get(KEY_URL);
		final String name = list.get(arg1).get(KEY_NAME);
		if(!TextUtils.isEmpty(adImg)){
//			ImageLoader.getInstance().displayImage(adImg, holder.getImageView(), options);
			Glide.with(context)
					.load(adImg)
					.placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
					.error(me.nereo.multi_image_selector.R.drawable.default_error)
					.centerCrop()
					.into(holder.getImageView());
		} else {
			holder.getImageView().setBackgroundColor(context.getResources().getColor(R.color.gray_line));
		}
		
		/**	监听点击广告图片后的操作，一般只做网页跳转	*/
		rowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!TextUtils.isEmpty(adLink)){
					Intent in = new Intent("com.aiitec.ad");
					Bundle bundle = new Bundle();
					bundle.putString("url", adLink);
					bundle.putString("title", name);
					in.putExtras(bundle);
//					in.putExtra(Extras.INTENT_BUNDLE, bundle);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);		
				} 
				
			}
		});
		rowViews.put(arg1, rowView);
		((ViewPager) arg0).addView(rowView, 0);
		return rowView;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	private class BannerHolder {
		private View view;
		private ImageView imageview;

		private BannerHolder(View v) {
			view = v;
		}

		ImageView getImageView() {
			if (imageview == null) {
				imageview = (ImageView) view.findViewById(R.id.banner);
			}
			return imageview;
		}
	}

}
