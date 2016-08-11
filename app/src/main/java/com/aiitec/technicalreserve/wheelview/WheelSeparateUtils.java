package com.aiitec.technicalreserve.wheelview;



import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.PopupWindow;

import com.aiitec.technicalreserve.R;

/**
 * @author yy
 * @since 2015-05-28
 * 
 * 实现 3 个滚轮单独滚动
 * 该储备使用了 PopupWindow 来弹出 滚轮
 * 
 * 1、首先在布局文件中，父布局使用 LinearLayout 装载 "kankan.wheel.widget.WheelView" 控件
 * 	    设置每个 WheelView 控件的权重都为 1
 * 2、LayoutInflater 加载布局，绑定 WheelView 控件，加载数据( 数据一般为 String[] )
 * 3、设置 Adapter
 * 		WheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, String[]));
 * 
 * 4、设置监听器与条目可见数量
 * 		WheelView.setVisibleItems(7);	// 条目可见数量
 * 		WheelView.addChangingListener(listener);	// 监听器,一般只用该监听器. PS:注意点请看最后
 * 		不常用的监听器：
 * 			leftWheelView.addScrollingListener(listener)
			leftWheelView.addClickingListener(listener)
 *
 */


public class WheelSeparateUtils {

	private WheelView leftWheelView;
	private WheelView midWheelView;
	private WheelView rightWheelView;
//	private Button btn_OK, btn_cancel;
	private PopupWindow popupWindow;
	
	/**
	 * 	获取选中的值，及序号
	 */
//	private String mCurrentLeftValue ;
//	private String mCurrentMidValue;
//	private String mCurrentRightValue;
	private int mCurrentLeftCnt=1;
	private int mCurrentMidCnt=1;
	private int mCurrentRighttCnt=1;
	
	/**
	 * 户型的数据
	 */
	private String[] roomList;
	private String[] hallList;
	private String[] toiletList;
	/**
	 * 楼层的数据
	 */
	private String[] floorList;
	private String[] totalFloorList;
	
	private Context context;
	private LayoutInflater layoutInflater;
	
	private int wheelCnt = 0;
	
	
	public WheelSeparateUtils(Context context, int wheelCnt) {
		this.context = context;
		this.wheelCnt = wheelCnt;
		layoutInflater = LayoutInflater.from(context);
		
		findView();
		initData();
	}

	private void findView(){
		View view = layoutInflater.inflate(R.layout.wheel, null);
		/**	弹出框*/
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);			// 锁定 popupWindow 的焦点，外面不可点击
		popupWindow.setAnimationStyle(R.style.popupAnim);	// 弹入与弹出的动画效果
		
		leftWheelView = (WheelView) view.findViewById(R.id.left_wheel);
    	rightWheelView = (WheelView) view.findViewById(R.id.right_wheel);
    	leftWheelView.addChangingListener(onWheelChangedListener);
    	rightWheelView.addChangingListener(onWheelChangedListener);
    	if (wheelCnt == 3) {
    		midWheelView = (WheelView) view.findViewById(R.id.mid_wheel);
    		midWheelView.setVisibility(View.VISIBLE);
    		midWheelView.addChangingListener(onWheelChangedListener);
		}
		view.findViewById(R.id.btn_ok_room).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if(onButtonClickListener != null){
					onButtonClickListener.onComfirm(v, getCntData());
				}
			}
		});
		view.findViewById(R.id.btn_cancel_room).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if(onButtonClickListener != null){
					onButtonClickListener.onCancel();
				}
			}
		});
//    	btn_OK = (Button) view.findViewById(R.id.btn_ok_room);
//    	btn_cancel = (Button) view.findViewById(R.id.btn_cancel_room);
//    	btn_OK.setOnClickListener(buttonOnClickListener);
//    	btn_cancel.setOnClickListener(buttonOnClickListener);
	}
	
	private void initData(){
		if (wheelCnt == 2) {
			setFloorData();
		}else if (wheelCnt == 3) {
			setRoomData();
		}
	}
	
	private void setRoomData(){
		roomList = new String[10];
		hallList = new String[10];
		toiletList = new String[10];
		
		for (int i = 0; i < 10; i++) {
			int cnt = i + 1;
			roomList[i] = cnt+" 室";
			hallList[i] = cnt+" 厅";
			toiletList[i] = cnt+" 卫";
		}
		
		leftWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, roomList));
		midWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, hallList));
		rightWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, toiletList));
		leftWheelView.setVisibleItems(7);
		midWheelView.setVisibleItems(7);
		rightWheelView.setVisibleItems(7);
	}
	
	private void setFloorData(){
		floorList = new String[99];
		totalFloorList = new String[99];
		
		for (int i = 0; i < 99; i++) {
			int cnt = i + 1;
			floorList[i] = "第"+cnt+"层";
			totalFloorList[i] = "共"+cnt+"层";
		}
		
		leftWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, floorList));
		rightWheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, totalFloorList));
		leftWheelView.setVisibleItems(6);
		rightWheelView.setVisibleItems(6);
	}
	
	public boolean isShow(){
		if (popupWindow !=null && popupWindow.isShowing()) {
			return true;
		}
		return false;
	}
	
	public void show(View v){
		if (popupWindow != null) {
			Rect rect = new Rect();
			((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			int winHeight =((Activity)context).getWindow().getDecorView().getHeight();
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, winHeight-rect.bottom);
		}
	}
	
	public void dismiss(){
		if (popupWindow !=null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

//	public String[] getValueData(){
//		String[] value = null;
//		if (wheelCnt == 2) {
//			value = new String[2];
//			value[0] = mCurrentLeftValue;
//			value[1] = mCurrentRightValue;
//		}else if (wheelCnt == 3) {
//			value = new String[3];
//			value[0] = mCurrentLeftValue;
//			value[1] = mCurrentMidValue;
//			value[2] = mCurrentRightValue;
//		}
//		
//		return value;
//	}
	
	
	/**
	 * 获取选择了哪项数据的 position，
	 * 用 int 数组装载并返回
	 * 
	 * @return int[]
	 */
	public int[] getCntData(){
		int[] cnt = null;
		if (wheelCnt == 2) {
			cnt = new int[2];
			cnt[0] = mCurrentLeftCnt;
			cnt[1] = mCurrentRighttCnt;
		}else if (wheelCnt == 3) {
			cnt = new int[3];
			cnt[0] = mCurrentLeftCnt;
			cnt[1] = mCurrentMidCnt;
			cnt[2] = mCurrentRighttCnt;
		}
		
		return cnt;
	}
	
	
	private OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			switch (wheelCnt) {
				case 2:	// 2级滚轮
					if (wheel == leftWheelView) {
						/**	这里需要注意，当弹出 WheelView 时，默认3个滚轮都是显示第一项时，如果没选择，
						 * 	直接点击确认返回数据----> newValue = 0
						 * */
						if (newValue == 0) {		
							mCurrentLeftCnt = 1;
						}else {
							mCurrentLeftCnt = newValue + 1;
						}
					}else if (wheel == rightWheelView) {
						if (newValue == 0) {
							mCurrentRighttCnt = 1;
						}else {
							mCurrentRighttCnt = newValue + 1;
						}
					}
					break;
	
				case 3: // 3级滚轮
					if (wheel == leftWheelView) {
						if (newValue == 0) {
							mCurrentLeftCnt = 1;
						}else {
							mCurrentLeftCnt = newValue + 1;
						}
					}else if (wheel == midWheelView) {
						if (newValue == 0) {
							mCurrentMidCnt = 1;
						}else {
							mCurrentMidCnt = newValue + 1;
						}
					}else if (wheel == rightWheelView) {
						if (newValue == 0) {
							mCurrentRighttCnt = 1;
						}else {
							mCurrentRighttCnt = newValue + 1;
						}
					}
					break;
			}
		}
	};
	private OnButtonClickListener onButtonClickListener;

	public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
		this.onButtonClickListener = onButtonClickListener;
	}

	interface OnButtonClickListener{
		void onComfirm(View v, int[] data);
		void onCancel();
	}
}



