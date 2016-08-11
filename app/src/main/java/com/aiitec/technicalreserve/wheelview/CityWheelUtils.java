package com.aiitec.technicalreserve.wheelview;


import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.aiitec.technicalreserve.R;


/**
 * @author Authony
 * @since 2015-05-28
 * 
 * 
 *
 */
public class CityWheelUtils implements OnClickListener {

	private Button btn_cancel, btn_ok;
	
	private WheelView leftWheelView;
	private WheelView midWheelView;
	private WheelView rightWheelView;
	private PopupWindow popupWindow;
	private CityListener cityListener;
	private City mCity;
	private Context context;
	private LayoutInflater layoutInflater;
	private CityDBUtils cityDBUtils;
	
	private CityWheelAdapter adapter2;
	private CityWheelAdapter adapter3;
	private CityWheelAdapter adapter1;
	private Region[] provinceRegions, cityRegions, districtsRegions;
	private int index ;
	
	
	
	public CityWheelUtils(Context context, final int regionId, int index) {
		this.context = context;
		this.index = index;
		layoutInflater = LayoutInflater.from(context);
		cityDBUtils = new CityDBUtils(context);
		mCity = new City();
		findView();
		cityDBUtils.setCityInitListener(cityInitListener);
		cityDBUtils.initProvince();			    
		if(regionId > 0){
		    new Handler().postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    cityDBUtils.initRegionId(regionId);					    
                }
            }, 1000);
		}
	}
	public CityWheelUtils(Context context, int regionId) {
	    this(context, regionId, 0);
	}

	private void findView(){
		View view = layoutInflater.inflate(R.layout.city_wheel , null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.popupAnim);
		view.findViewById(R.id.left_wheel).setVisibility(View.GONE);
    	rightWheelView = (WheelView) view.findViewById(R.id.right_wheel);
    	btn_ok =  (Button) view.findViewById(R.id.btn_city_wheel_ok);
    	btn_cancel = (Button) view.findViewById(R.id.btn_city_wheel_cancel);
    	rightWheelView.addChangingListener(onWheelChangedListener);
    	midWheelView = (WheelView) view.findViewById(R.id.mid_wheel);
    	midWheelView.setVisibility(View.VISIBLE);
    	midWheelView.addChangingListener(onWheelChangedListener);
    	
    	adapter2 = new CityWheelAdapter(context);
    	adapter3 = new CityWheelAdapter(context);
    	adapter1 = new CityWheelAdapter(context);
    	leftWheelView = (WheelView) view.findViewById(R.id.left_wheel);
    		
    	leftWheelView.addChangingListener(onWheelChangedListener);
    	leftWheelView.setViewAdapter(adapter1);
    	leftWheelView.setVisibility(View.VISIBLE);
    	leftWheelView.setVisibleItems(7);
    	midWheelView.setViewAdapter(adapter2);
    	rightWheelView.setViewAdapter(adapter3);
    	
    	midWheelView.setVisibleItems(7);
		rightWheelView.setVisibleItems(7);
		
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	private CityDBUtils.CityInitListener cityInitListener = new CityDBUtils.CityInitListener(){

		@Override
		public void getCitys(List<Region> citys, int index) {
			switch (index) {
			case CityDBUtils.PROVICE:
				provinceRegions = new Region[citys.size()];
				for (int i = 0; i < citys.size(); i++) {
				    provinceRegions[i] = citys.get(i);
				}
				adapter1.update(provinceRegions);
				leftWheelView.setCurrentItem(0);
				if(mCity == null){
				    mCity = new City();
				}
				if(citys.size() > 0){
    				mCity.setProvince(citys.get(0).getName());
    				mCity.setProvinceCode(citys.get(0).getId());
    				if(provinceRegions.length > 0){
    				    cityDBUtils.initCities((int)provinceRegions[0].getId(), CityDBUtils.CITY);							
    				}
				}
				break;
			case CityDBUtils.CITY:
			    cityRegions = new Region[citys.size()];
			    for (int i = 0; i < citys.size(); i++) {
			        cityRegions[i] = citys.get(i);
			    }
			    adapter2.update(cityRegions);
			    midWheelView.setCurrentItem(0);
			    if(mCity == null){
                    mCity = new City();
                }
			    if(citys.size() > 0){
                    mCity.setCity(citys.get(0).getName());
                    mCity.setCityCode(citys.get(0).getId());
                    mCity.setRegionId(citys.get(0).getId());
    			    if(cityRegions.length > 0){
    			        cityDBUtils.initCities((int)cityRegions[0].getId(), CityDBUtils.DISTRICTS);							
    			    }
			    }
				break;
			case CityDBUtils.DISTRICTS:
			    if(mCity == null){
                    mCity = new City();
                }
			    districtsRegions = new Region[citys.size()+1];
			    Region allDistrictsRegion = new Region();
			    allDistrictsRegion.setName("全区");
			    allDistrictsRegion.setId(mCity.getCityCode());
			    districtsRegions[0] = allDistrictsRegion;
			    mCity.setDistrict("");//显示全区，实际去出来可能全区去掉比较好
			    mCity.setDistrictCode(allDistrictsRegion.getId());
			    mCity.setRegionId(allDistrictsRegion.getId());
			    if(citys.size() > 0){
			        for (int i = 1; i < citys.size()+1; i++) {
			            districtsRegions[i] = citys.get(i-1);
			        }
			    } 
				adapter3.update(districtsRegions);
				rightWheelView.setCurrentItem(0);
				break;

			default:
				break;
			}
		}

		@Override
		public void getCity(final City city, int index) {
			mCity = city.clone();
			
			for (int i = 0; i < provinceRegions.length; i++) {
                if(mCity.getProvinceCode() == provinceRegions[i].getId()){
                    leftWheelView.setCurrentItem(i);
                    break;
                }
            }
			new Handler().postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    for (int i = 0; i < cityRegions.length; i++) {
                        if(city.getCityCode() == cityRegions[i].getId()){
                            midWheelView.setCurrentItem(i);
                            handler.postDelayed(new Runnable() {
                                
                                @Override
                                public void run() {
                                    for (int i = 0; i < districtsRegions.length; i++) {
                                        if(city.getDistrictCode() == districtsRegions[i].getId()){
                                            rightWheelView.setCurrentItem(i);
                                            break;
                                        }
                                    }
                                }
                            }, 100);
                            break;
                        }
                    }
                }
            }, 100);
			
		}
		
	};

	private Handler handler = new Handler();
	
	public boolean isShow(){
		if (popupWindow.isShowing()) {
			return true;
		}
		
		return false;
	}
	
	public void show(View v){
		Rect rect = new Rect();
		((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int winHeight =((Activity)context).getWindow().getDecorView().getHeight();
		popupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.BOTTOM, 0,winHeight-rect.bottom);
	}
	
	public void dismiss(){
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
	
	private OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
		    if (wheel == leftWheelView) {
		        int current = leftWheelView.getCurrentItem();
		        int provinceCode = (int) provinceRegions[current].getId();
		        String priovince =  provinceRegions[current].getName();
		        mCity.setProvince(priovince);
		        mCity.setProvinceCode(provinceCode);
		        midWheelView.setCurrentItem(0);
		        cityDBUtils.initCities(provinceCode, CityDBUtils.CITY);					
		    } else if (wheel == midWheelView) {
		        int current = midWheelView.getCurrentItem();
		        int cityCode = (int) cityRegions[current].getId();
		        String city =  cityRegions[current].getName();
		        mCity.setCity(city);
		        mCity.setCityCode(cityCode);
		        rightWheelView.setCurrentItem(0);
		        cityDBUtils.initCities(cityCode, CityDBUtils.DISTRICTS);					
		        
		    } else if (wheel == rightWheelView) {
		        int current = rightWheelView.getCurrentItem();
		        int currentRegionId = (int) districtsRegions[current].getId();
		        String currentBussiness =  districtsRegions[current].getName();
		        mCity.setDistrict(currentBussiness);
		        mCity.setDistrictCode(currentRegionId);
		        mCity.setRegionId(currentRegionId);
		    } 
		}
	};
	class CityWheelAdapter extends AbstractWheelTextAdapter{

	    private Region items[];
	    public CityWheelAdapter(Context context) {
	        super(context);
	        items = new Region[0];
	    }
	    public CityWheelAdapter(Context context, Region items[]) {
	    	super(context);
	    	this.items = items;
	    }
	    public void update(Region items[]) {
	    	this.items = items;
	    	notifyDataInvalidatedEvent();
		}
	    @Override
	    public CharSequence getItemText(int index) {
	        if (index >= 0 && index < items.length) {
	        	Region item = items[index];
	            return item.getName();
	        }
	        return null;
	    }
	    @Override
	    public int getItemsCount() {
	        return items.length;
	    }
		
	}
	public void setCityListener(CityListener cityListener) {
		this.cityListener = cityListener;
	}
	public interface CityListener {
		void getCity(City city, int index) ;
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_city_wheel_cancel:
			dismiss();
			break;
		case R.id.btn_city_wheel_ok:
			if(cityListener != null){
				cityListener.getCity(mCity, index);
			}
			dismiss();
			break;

		default:
			break;
		}
	}
}



