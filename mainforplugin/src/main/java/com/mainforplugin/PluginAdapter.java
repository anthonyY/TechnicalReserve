package com.mainforplugin;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryg.utils.DLUtils;

public class PluginAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();
	private Context context;
	public PluginAdapter(Context context, ArrayList<PluginItem> mPluginItems) {
		this.context = context;
		this.mPluginItems = mPluginItems;
		mInflater = LayoutInflater.from(context);
	}
	public PluginAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void update(ArrayList<PluginItem> mPluginItems) {
		this.mPluginItems = mPluginItems;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mPluginItems.size();
	}

	@Override
	public PluginItem getItem(int position) {
		return mPluginItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.plugin_item, parent,
					false);
			holder = new ViewHolder();
			holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.app_name);
			holder.apkName = (TextView) convertView.findViewById(R.id.apk_name);
			holder.packageName = (TextView) convertView.findViewById(R.id.package_name);

			holder.switch_skin = (Button) convertView.findViewById(R.id.switch_skin);
			holder.goto_plugin = (Button) convertView.findViewById(R.id.goto_plugin);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.goto_plugin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onButtonClickListener != null){
					onButtonClickListener.gotoPlugin(position);
				}
			}
		});
		holder.switch_skin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onButtonClickListener != null){
					onButtonClickListener.switchSkin(position);
				}
			}
		});
		PluginItem item = mPluginItems.get(position);
		if(item.id == PluginItem.DEFAULT_THEME){//-1 代表默认主题
			holder.appIcon.setImageResource(R.mipmap.ic_launcher);
			holder.appName.setText(R.string.app_name);
			holder.apkName.setText("默认主题");
			holder.packageName.setText(context.getPackageName()
					+ "\n" + MainActivity.class );
			holder.goto_plugin.setVisibility(View.GONE);
		} else {
			holder.goto_plugin.setVisibility(View.VISIBLE);
			PackageInfo packageInfo = item.packageInfo;
			holder.appIcon.setImageDrawable(DLUtils.getAppIcon(context, item.pluginPath));
			holder.appName.setText(DLUtils.getAppLabel(context,item.pluginPath));
			holder.apkName.setText(item.pluginPath.substring(item.pluginPath.lastIndexOf(File.separatorChar) + 1));
			holder.packageName.setText(packageInfo.applicationInfo.packageName
					+ "\n" + item.launcherActivityName + "\n"
					+ item.launcherServiceName);
			
		}
		return convertView;
	}



	private static class ViewHolder {
		public ImageView appIcon;
		public TextView appName;
		public TextView apkName;
		public TextView packageName;
		public Button switch_skin, goto_plugin;
	}
	
	
	
	
	public static class PluginItem {
		public PackageInfo packageInfo;
		public String pluginPath;
		public String launcherActivityName;
		public String launcherServiceName;
		public long id;
		public static final int DEFAULT_THEME = -1;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\"packageInfo\":").append("\"").append(packageInfo).append("\"")
					.append("\"pluginPath\":").append("\"").append(pluginPath).append("\"")
					.append("\"launcherActivityName\":").append("\"").append(launcherActivityName).append("\"")
					.append("\"launcherServiceName\":").append("\"").append(launcherServiceName).append("\"")
					.append("\"id\":").append("\"").append(id).append("\"")
					.append("\"DEFAULT_THEME\":").append("\"").append(DEFAULT_THEME).append("\"");
			return sb.toString();
		}
	}

	private OnButtonClickListener onButtonClickListener;

	public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
		this.onButtonClickListener = onButtonClickListener;
	}

	interface OnButtonClickListener{
		void switchSkin(int position);
		void gotoPlugin(int position);
	}
}