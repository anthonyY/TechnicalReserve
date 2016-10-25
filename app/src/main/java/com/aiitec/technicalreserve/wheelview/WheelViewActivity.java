package com.aiitec.technicalreserve.wheelview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * 仿ios多级滚轮
 * @author yoyo
 * @since 2015-04-23
 * 
 * 快速集成说明：
 * 请看 WheelSeparateUtils.java 这个类
 * 
 */
public class WheelViewActivity extends BaseActivity {

	public static final String[] options = {"多级滚轮，单独滚动","多级滚轮，联动"};
	
	/** 单独滚动的工具类 */
	private WheelSeparateUtils wheelSeparateUtils;
	private int WHEEL_CNT = 3;	// 滚轮的个数
	private CityWheelUtils cityWheelUtils ;
	private TextView tv_city;
	
	/**	联动： 省市区的3级联动	*/
	
	@ContentView(R.layout.activity_wheelview)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("仿ios多级滚轮");
		
		findView();
	}
	
	private void findView(){
		((TextView)findViewById(R.id.title_content)).setText("仿ios多级滚动");
		((Button)findViewById(R.id.btn_select)).setText("多级滚轮，单独滚动");
		((Button)findViewById(R.id.btn_display)).setText("多级滚轮，联动");
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_city.setVisibility(View.VISIBLE); 
		cityWheelUtils = new CityWheelUtils(this, 340703);//默认0
		cityWheelUtils.setCityListener(cityListener);
		// 实例化，全局只有一个对象
		wheelSeparateUtils = new WheelSeparateUtils(this, WHEEL_CNT);
		wheelSeparateUtils.setOnButtonClickListener(new WheelSeparateUtils.OnButtonClickListener() {
			@Override
			public void onComfirm(View v, int[] ids) {
				if (ids.length == WHEEL_CNT) {
					StringBuffer sb = new StringBuffer();
					sb.append(ids[0]+"室 ");
					sb.append(ids[1]+"厅 ");
					sb.append(ids[2]+"卫 ");
					ToastUtil.show(WheelViewActivity.this, sb.toString());
				}
			}

			@Override
			public void onCancel() {

			}
		});
	}
	
	CityWheelUtils.CityListener cityListener = new CityWheelUtils.CityListener(){

		@Override
		public void getCity(City city, int index) {
			tv_city.setText(city.getProvince()+city.getCity()+city.getDistrict()+"\n regionId:"+city.getRegionId());
		}
		
	};
	
	public void onClick_Event(View view) {
		switch(view.getId()){
			case R.id.btn_select://单独滚动
				if (wheelSeparateUtils != null && wheelSeparateUtils.isShow()) {
					wheelSeparateUtils.dismiss();
				}
				wheelSeparateUtils.show();
				break;

			/**	联动 2个按钮的监听*/
			case R.id.btn_display://联动
				cityWheelUtils.show(findViewById(R.id.btn_select));
				break;
		}
	}
}
