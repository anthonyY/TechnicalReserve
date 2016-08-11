package com.aiitec.technicalreserve.web;

import android.os.Bundle;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;

/**
 * 新的开的网页类
 * @author Chalin
 *
 */
public class WebStartActivity extends BaseActivity {
	@ContentView(R.layout.activity_webstart)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("新网页");
	}
}
