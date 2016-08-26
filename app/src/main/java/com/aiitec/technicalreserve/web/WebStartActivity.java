package com.aiitec.technicalreserve.web;

import android.os.Bundle;
import android.webkit.WebView;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;

/**
 * 新的开的网页类
 * @author Chalin
 *
 */
public class WebStartActivity extends BaseActivity {

	private String url;
	@Resource(R.id.webview)
	WebView webView;
	@ContentView(R.layout.activity_webstart)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getIntent().hasExtra("url")){
			url = getIntent().getStringExtra("url");
		}
		webView.loadUrl(url);
		setTitle("新网页");
	}
}
