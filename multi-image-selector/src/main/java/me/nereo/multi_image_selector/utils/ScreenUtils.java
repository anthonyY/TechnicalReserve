package me.nereo.multi_image_selector.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * ScreenUtils Created by hanj on 14-9-25.
 */
public class ScreenUtils {
	public static int getScreenW(Context context) {
		if (screenW <= 0)
			initScreen(context);
		return screenW;
	}

	public static void setScreenW(int screenW) {
		ScreenUtils.screenW = screenW;
	}

	public static int getScreenH(Context context) {
		if (screenH <= 0)
			initScreen(context);
		return screenH;
	}

	public static void setScreenH(int screenH) {
		ScreenUtils.screenH = screenH;
	}

	private static int screenW;
	private static int screenH;
	private static float screenDensity;

	public static void initScreen(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;
		screenH = dm.heightPixels;
		screenDensity = dm.density;
	}

	public static float getScreenDensity(Context context) {
		if (screenDensity <= 0)
			initScreen(context);
		return screenDensity;
	}

	/**
	 * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewTopHeight(View view) {
		return view.getTop();
	}

	/**
	 * 获取屏幕中控件底部位置的高度--即控件底部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewBottomHeight(View view) {
		return view.getBottom();
	}

	/**
	 * 获取屏幕中控件左侧的位置--即控件左侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewLeftHeight(View view) {
		return view.getLeft();
	}

	/**
	 * 获取屏幕中控件右侧的位置--即控件右侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewRightHeight(View view) {
		return view.getRight();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context
	 * @param dpValue dp值
	 * @return 像素值
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param context
	 * @param pxValue 像素值
	 * @return dp值
	 */
	public static int px2dip(Context context, float pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
