package com.aiitec.imdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aiitec.imdemo.R;
import com.aiitec.imdemo.adapter.FriendAdapter;
import com.aiitec.imdemo.util.ToastUtil;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class FriendsFragment extends Fragment {

    private ListView friends;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, true);
        friends = (ListView) view.findViewById(R.id.friends_list);
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.show(getActivity(), "获取朋友列表失败");
                Log.e("FriendsFragment", "错误码：" + i + "错误信息：" + s);
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> timUserProfiles) {
                FriendAdapter friendAdapter = new FriendAdapter(getActivity(), timUserProfiles);
                friends.setAdapter(friendAdapter);
                friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("identify", timUserProfiles.get(position).getIdentifier());
                        intent.putExtra("type", TIMConversationType.C2C);
                        startActivity(intent);
                    }
                });
            }
        });
        return view;
    }
}
