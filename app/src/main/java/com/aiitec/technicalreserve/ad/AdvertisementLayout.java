package com.aiitec.technicalreserve.ad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiitec.technicalreserve.R;

public class AdvertisementLayout extends LinearLayout{

	private ViewPager viewPager;
	private LinearLayout group;
	private ImageView[] images;
//	private int currentItem = 0;
	private Context context;
	private ScheduledExecutorService scheduledExecutorService;
	private int TIME_UNIT;
	int pageIndex = 1;
	private List<String> list;
	private double RATIO = (double)2/(double)4;
	
	private void initView(View view){
		viewPager = (ViewPager)view.findViewById(R.id.viewpager);
		group = (LinearLayout)view.findViewById(R.id.viewGroup);
	}
	
	public AdvertisementLayout(Context context) {
		super(context);
		this.context = context;
//		LayoutInflater mInflater = LayoutInflater.from(context);
//		View adview = mInflater.inflate(R.layout.layout_ad, null);
//		initView(adview);
//		addView(adview);
	}
	
	public AdvertisementLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray params = context.obtainStyledAttributes(attrs,
				R.styleable.ad);
        RATIO = (double)params.getFloat(R.styleable.ad_ratio, (float)0.5);
		int gravity = params.getInt(R.styleable.ad_gravity, Gravity.CENTER);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View adview = mInflater.inflate(R.layout.layout_ad, null);
		initView(adview);
		setCircleGravity(gravity);
		addView(adview);
		params.recycle();
    }


	public void setCircleGravity(int gravity) {
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)group.getLayoutParams();
		switch(gravity){
			case Gravity.CENTER :
				layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
				break;
			case Gravity.LEFT :
				layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
				break;
			case Gravity.RIGHT :
				layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
				break;
		}
		group.setLayoutParams(layoutParams);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        //高度和宽度一样
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec =  MeasureSpec.makeMeasureSpec((int)(childWidthSize*RATIO), MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@SuppressLint("NewApi")
	public AdvertisementLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	//实例化，动态创建图片下方的小点
	private void initListNavigation(int num) {
//		int num = im.length;
		images = new ImageView[num];
		for (int i = 0; i < num; i++) {
			images[i] = new ImageView(context);
			LayoutParams params = new LayoutParams(10,10);
			params.leftMargin=6;
			params.rightMargin = 6;
			images[i].setLayoutParams(params);
			if (i == 0) {
				images[i].setBackgroundResource(R.drawable.ad_img_circle01);
			} else {
				images[i].setBackgroundResource(R.drawable.ad_img_circle02);
			}
			group.addView(images[i]);
		}
	}
	
	//滑动监听
	private class pagerListener implements OnPageChangeListener {
		//滑动中
		@Override
		public void onPageScrollStateChanged(int arg0) {
			//0--->空闲，1--->是滑行中，2--->加载完毕
			switch(arg0){
			case 0:
				case 1:
//					if(scheduledExecutorService!=null){
//						scheduledExecutorService.shutdown();
//					}
//					scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//					scheduledExecutorService.scheduleAtFixedRate(new ViewPagerScrollTask(),TIME_UNIT, TIME_UNIT, TimeUnit.SECONDS);
				//当当前的页是第一张的时候就切成显示最后1张。
				if (pageIndex == 0) {
					pageIndex = list.size();
					viewPager.setCurrentItem(pageIndex, false);// 取消动画
					//当当前的页是最后一张的时候就切成显示的第一张。
				} else if (pageIndex == list.size()+1) {
					pageIndex = 1;
					viewPager.setCurrentItem(pageIndex, false);// 取消动画
				}
				else{
					viewPager.setCurrentItem(pageIndex);// 动画
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageSelected(int position) {
			if(scheduledExecutorService!=null){
				scheduledExecutorService.shutdown();
			}
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			scheduledExecutorService.scheduleAtFixedRate(new ViewPagerScrollTask(),TIME_UNIT, TIME_UNIT, TimeUnit.SECONDS);
			pageIndex = position;
			//当当前的页是第一张的时候就切成显示最后1张。
			if (position == 0) {
				for (int i = 0; i < images.length; i++) {
					if (i == list.size()-1) {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle01);
					} else {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle02);
					}
				
			}
				//当当前的页是最后一张的时候就切成显示的第一张。
			} else if (position == list.size()+1) {
				for (int i = 0; i < images.length; i++) {
					if (i == 1-1) {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle01);
					} else {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle02);
					}
				
			}
			}
			else{
				for (int i = 0; i < images.length; i++) {
					if (i == position-1) {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle01);
					} else {
						((View) images[i]).setBackgroundResource(R.drawable.ad_img_circle02);
					}
				
			}
			}
			
		}
	}
	
	
	
	private class ViewPagerScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
//				currentItem = (currentItem + 1) % images.length;
				pageIndex++;
				if(pageIndex>=list.size()+1) pageIndex=1;
				handler.obtainMessage().sendToTarget(); // 通
			}
		}

	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(pageIndex);//
//			setCurrentItem() ;
		};
	};
	/**
	 * 广告调用接口
	 * @param num 集合的size
	 * @param time_unit 自动滑动的时间（秒）
	 * @param is_open 是否自动滑动
	 * @param list 图片集合
	 * @param ratio 这个是广告的宽高的比例(传-1就是默认0.5比例)
	 */
	public void startAD(int num,int time_unit,boolean is_open,List<String> list,double ratio){
		if(ratio!=-1){
			RATIO = ratio;
		}
		this.list = list;
		group.removeAllViews();
		if(is_open){
			if(time_unit<=0){
				TIME_UNIT = 1;
			}else{
				TIME_UNIT = time_unit;
			}
		}
		initListNavigation(num);
		
		if(num<=0){
			findViewById(R.id.linear_ad).setVisibility(View.GONE);
		}else{
			findViewById(R.id.linear_ad).setVisibility(View.VISIBLE);
			viewPager.setBackgroundResource(R.color.gray_line);
			viewPager.setAdapter(new BannerAdapter(context,getList(list)));
			viewPager.setOnPageChangeListener(new pagerListener());
			viewPager.setCurrentItem(pageIndex);
			if(is_open){
				if(scheduledExecutorService!=null){
					scheduledExecutorService.shutdown();
				}
				scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
				scheduledExecutorService.scheduleAtFixedRate(new ViewPagerScrollTask(),TIME_UNIT, TIME_UNIT, TimeUnit.SECONDS);
				
			}else{
				if(scheduledExecutorService!=null){
					scheduledExecutorService.shutdown();
				}
			}
		}
	}
	
	/**
	 * 广告调用接口
	 * @param num 集合的size
	 * @param time_unit 自动滑动的时间（秒）
	 * @param is_open 是否自动滑动
	 * @param list 图片集合
	 */
	public void startAD(int num,int time_unit,boolean is_open,List<String> list){
		this.list = list;
		group.removeAllViews();
		if(is_open){
			if(time_unit<=0){
				TIME_UNIT = 1;
			}else{
				TIME_UNIT = time_unit;
			}
		}
		initListNavigation(num);
		
		if(num<=0){
			findViewById(R.id.linear_ad).setVisibility(View.GONE);
		}else{
			findViewById(R.id.linear_ad).setVisibility(View.VISIBLE);
			viewPager.setBackgroundResource(R.color.gray_line);
			viewPager.setAdapter(new BannerAdapter(context,getList(list)));
			viewPager.setOnPageChangeListener(new pagerListener());
			viewPager.setCurrentItem(pageIndex);
			if(is_open){
				if(scheduledExecutorService!=null){
					scheduledExecutorService.shutdown();
				}
				scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
				scheduledExecutorService.scheduleAtFixedRate(new ViewPagerScrollTask(),TIME_UNIT, TIME_UNIT, TimeUnit.SECONDS);
				
			}else{
				if(scheduledExecutorService!=null){
					scheduledExecutorService.shutdown();
				}
			}
		}
	}
	
	//最前面加一张最后一张，最后面再加一张第一张。
	private List<String> getList(List<String> list){
		List<String> ll = new ArrayList<String>();
		if(list.size()>1){
			group.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				if(i==0){
					ll.add(list.get(list.size()-1));
				}
				ll.add(list.get(i));
				if(i == list.size()-1){
					ll.add(list.get(0));
				}
			}
		}else{
			ll.addAll(list);
			group.setVisibility(View.GONE);
		}
		return ll;
	} 
	
}
