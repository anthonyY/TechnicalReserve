package com.aiitec.technicalreserve.image;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDataList;
//	protected LayoutInflater inflater;
	protected final int mLayoutId;

	public CommonAdapter(Context context, List<T> list, int layoutId) {
		this.mContext = context;
		this.mDataList = list;
		this.mLayoutId = layoutId;
//		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDataList == null? 0 : mDataList.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获取 ViewHolder
		final ViewHolder holder = ViewHolder.get(mContext, convertView, parent,mLayoutId, position);
		// 暴露该方法给子类实现更多业务逻辑
		convert(holder, getItem(position),position);
		
		return holder.getConvertView();
	}


	public void updateList(List<T> list){
		this.mDataList = list;
		this.notifyDataSetChanged();
	}

	public void update(){
		this.notifyDataSetChanged();
	}

	/**
	 * 暴露 viewholder 给子类，绑定控件并设置数据的相关操作
	 * @param holder
	 * @param item data
	 */
	public abstract void convert(ViewHolder holder, T item, int position);


}

class ViewHolder {

	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mPosition;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position){
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		//setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个 ViewHolder 对象
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}


	/**
	 * 通过控件的Id获取对应的控件，如果没有则加入 Map中
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 返回该 item 的 view
	 * @return
	 */
	public View getConvertView(){
		return mConvertView;
	}

	/**
	 * 返回该item的 position
	 * @return
	 */
	public int getPosition(){
		return mPosition;
	}
}





