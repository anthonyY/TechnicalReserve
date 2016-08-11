package com.aiitec.technicalreserve.emoji;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aiitec.openapi.utils.ToastUtil;
import com.aiitec.openapi.view.ViewUtils;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.openapi.view.annatation.event.OnClick;
import com.aiitec.openapi.view.annatation.event.OnCompoundButtonCheckedChange;
import com.aiitec.openapi.view.annatation.event.OnTextChanged;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

/**
 * emoji表情类
 */
public class EmojiActivity extends BaseActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @Resource(R.id.editEmojicon)
    EmojiconEditText mEditEmojicon;

    @Resource(R.id.txtEmojicon)
    EmojiconTextView mTxtEmojicon;
    @Resource(R.id.use_system_default)
    CheckBox mCheckBox;
    @ContentView(R.layout.activity_emoji)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_emoji);

        setEmojiconFragment(false);
    }
    @OnCompoundButtonCheckedChange(R.id.use_system_default)
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mEditEmojicon.setUseSystemDefault(b);
        mTxtEmojicon.setUseSystemDefault(b);
        setEmojiconFragment(b);
    }
    @OnTextChanged(R.id.editEmojicon)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTxtEmojicon.setText(s);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        EmojiconsFragment emojiconsFragment = EmojiconsFragment.newInstance(useSystemDefault);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, emojiconsFragment)
                .commit();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }


    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }
}
