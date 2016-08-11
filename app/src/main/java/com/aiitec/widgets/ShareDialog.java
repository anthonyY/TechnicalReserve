package com.aiitec.widgets;



import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.aiitec.technicalreserve.R;


public class ShareDialog extends Dialog {

	private Animation animation;
	private LinearLayout ll_share;
	private Context context ;
	private View view;

	
	public ShareDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.share1, null);
		ll_share = (LinearLayout) view.findViewById(R.id.ll_share);

		setContentView(view);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}



	public ShareDialog(Context context) {
		super(context);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.share1, null);
		ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
		
		setContentView(view);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(params);
	}
	/**
	 * 
	 * @param context
	 * @param layoutId
	 * @param theme
	 */
	public ShareDialog(Context context, int layoutId, int theme) {
		super(context, theme);
		this.context = context;
		view = LayoutInflater.from(context).inflate(layoutId, null);
		ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
		
		setContentView(view);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}

	@Override
	public void show() {
		super.show();
		if(ll_share != null){
			animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in1);
			ll_share.startAnimation(animation);		
		}
	}
	
	/**
	 * 隐藏部分控件
	 * @param id
	 */
	public void goneView(int id) {
		if(view != null){
			View goneView = view.findViewById(id);
			if(goneView != null){
				goneView.setVisibility(View.GONE);
			}
		}
	}
	public void visibilityView(int id) {
		if(view != null){
			View goneView = view.findViewById(id);
			if(goneView != null){
				goneView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public View getView() {
		return view;
	}
	
	@Override
	public void dismiss() {
		
		if(ll_share != null){
			ll_share.clearAnimation();		
		}
		if(ll_share != null){
			animation = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out1);
			ll_share.startAnimation(animation);		
		}
		super.dismiss();
	}


}