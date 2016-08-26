package com.aiitec.imdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiitec.imdemo.R;
import com.tencent.TIMUserProfile;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class FriendAdapter extends BaseAdapter {

    private Context context;
    private List<TIMUserProfile> list;

    public FriendAdapter(Context context, List<TIMUserProfile> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_friend,null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.friend_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getIdentifier());
        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
