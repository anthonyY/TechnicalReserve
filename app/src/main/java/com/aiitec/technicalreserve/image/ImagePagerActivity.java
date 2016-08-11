/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.aiitec.technicalreserve.image;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiitec.openapi.utils.AiiUtil;
import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import uk.co.senab.photoview.viewpager.HackyViewPager;
import uk.co.senab.photoview.viewpager.PageIndicator;

/**
 * 图片展示类， 可以手势放大缩小，双击放大缩小，单击返回， 保存到本地
 * @author shc
 * @version 1.0
 */
public class ImagePagerActivity extends BaseActivity {
	
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	DisplayImageOptions options;
	public static final String STATE_POSITION = "position";
	public static final String IMAGES = "images";

	private ArrayList<String> pathList;
	private int position;
	HackyViewPager pager;
	PageIndicator mIndicator;

	
	private TextView tv_order;
	@ContentView(R.layout.activity_image_pager)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pathList = getIntent().getStringArrayListExtra(IMAGES);
		position = getIntent().getIntExtra(STATE_POSITION, 0);

		tv_order = (TextView) findViewById(R.id.tv_order);
		pager = (HackyViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(pathList));
		pager.setCurrentItem(position);
		pager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tv_order.setText((arg0 + 1) + "/" + pathList.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		tv_order.setText((position + 1) + "/" + pathList.size());
	}

	/** 保存方法 */
	public void saveBitmap(Bitmap loadedImage, String path) {
		if(!AiiUtil.isSDCardEnable()){
			ToastUtil.show(this,"sd卡不存在或者连接不上，请检查sdcard");
			return;
		}
		int index = path.lastIndexOf("/");
		String fileName = "";
		if(index != -1){
			fileName = path.substring(index+1, path.length());
		} else {
			fileName = new Random().nextInt(100)+".jpg";
		}
		File dir = new File(AiiUtil.getSDCardPath()+"/image/");
		if(!dir.exists()){
			dir.mkdir();
		}
		File f = new File(dir.getAbsolutePath(), fileName);
		if (f.exists()) {
			ToastUtil.show(this,"文件已存在");
			return ;
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			loadedImage.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			ToastUtil.show(this,"图片保存成功，保存路径："+f.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class ImagePagerAdapter extends PagerAdapter {

		private List<String> imagepaths;
		private LayoutInflater inflater;
		private Bitmap loadedImage;
		ImagePagerAdapter(List<String> list) {
			this.imagepaths = list;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return imagepaths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			imageView.setOnViewTapListener(new OnViewTapListener() {

				@Override
				public void onViewTap(View view, float x, float y) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			imageLayout.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					saveBitmap(loadedImage, imagepaths.get(position));
				}
			});
			Glide.with(ImagePagerActivity.this)
					.load(imagepaths.get(position))
					.error(me.nereo.multi_image_selector.R.drawable.default_error)
					.into(imageView);

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}
}