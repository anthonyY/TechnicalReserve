package com.aiitec.imdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aiitec.imdemo.R;
import com.aiitec.imdemo.presenter.FriendshipManagerPresenter;
import com.aiitec.imdemo.util.ToastUtil;
import com.aiitec.imdemo.viewinterface.FriendInfoView;
import com.aiitec.imdemo.viewinterface.FriendshipManageView;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SearchFriendActivity extends AppCompatActivity implements View.OnClickListener, FriendInfoView, FriendshipManageView {

    private EditText search_key;
    private Button search;
    private TextView searchResult, my_info;
    private FriendshipManagerPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        presenter = new FriendshipManagerPresenter(null, this, this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        search_key = (EditText) findViewById(R.id.id);
        search = (Button) findViewById(R.id.search);
        searchResult = (TextView) findViewById(R.id.result);

        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                if (TextUtils.isEmpty(search_key.getText().toString().trim())) {
                    search_key.setError("input ID");
                    return;
                }
                presenter.searchFriendById(search_key.getText().toString());
                break;
        }
    }

    /**
     * 查找好友回调
     *
     * @param users 资料列表
     */
    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        if (users.size() <= 0) {
            searchResult.setText("no result");
            return;
        }
        final TIMUserProfile user = users.get(0);
        searchResult.setText(user.getIdentifier() + "--" + user.getLocation() + "--" + user.getNickName() + "--" + user.getGender());
        searchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(SearchFriendActivity.this, "添加请求已发出!");
                presenter.addFriend(user.getIdentifier(), user.getIdentifier(), "默认分组", "加我吧");
            }
        });
    }

    /**
     * 添加好友回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
                ToastUtil.show(SearchFriendActivity.this, "add success");
                break;
            default:
                ToastUtil.show(SearchFriendActivity.this, status.name() + status.toString());
                break;
        }

    }

    /**
     * 删除好友回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {

    }

    /**
     * 改变分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {

    }
}
