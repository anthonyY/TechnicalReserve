package com.aiitec.technicalreserve.keybroad;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;


/**
 * 键盘回车的文字描述与监听类
 * 
 * @author Chalin
 * 
 * 快速集成说明:
 * 对应的技术点说明，在 activity_keybroad.xml 中已经有说明，请查看。
 * 
 */
public class KeybroadActivity extends BaseActivity {

	private EditText et_1, et_2, et_3, et_4, et_5, et_6, et_7;

	private void init() {
		setTitle("键盘回车的文字描述与监听");
		et_4 = (EditText) findViewById(R.id.et_4);
		et_4.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		
		/**	监听键盘回车的事件	*/
		et_4.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					Toast.makeText(getApplicationContext(), "搜索",
							Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});
		
	}

	@ContentView(R.layout.activity_keybroad)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		init();
	}

}
