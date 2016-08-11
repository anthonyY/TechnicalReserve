
package com.aiitec.technicalreserve.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.aiitec.technicalreserve.image.ImagePagerActivity;
import com.aiitec.widgets.ShareDialog;
import com.aiitec.widgets.ShareDialog1;

/**
 * 客户端与网页javascript交互类
 * @author Chalin
 * 
 * 快速集成说明：
 * 在代码中已经有详细说明，请按照1、2、3、4点来看，如下概要顺序
  	android 中的java代码调用webview里面的js脚本
	webview中的js脚本调用本地的java代码
	java调用js并传递参数
	调用 WebViewClient 监听网页加载情况
 *
 */
public class WebActivity extends BaseActivity implements OnClickListener{
	
	private WebView webView;
	private ShareDialog shareDialog;
	private Button btn_share,btn_buy;
	private Handler handler = new Handler();

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void init(){
		webView = (WebView)findViewById(R.id.webview);
		btn_share = (Button)findViewById(R.id.btn_share);
        btn_buy = (Button)findViewById(R.id.btn_buy);
        setTitle("客户端与网页javascript交互");
        btn_share.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        
        Random random  = new Random();
        int rand = random.nextInt(100);
        // 1、android中调用webview中的js脚本
		webView.getSettings().setJavaScriptEnabled(true);	// 启用javascript
		webView.loadUrl("http://192.168.1.14/AiiUtility_web/quanshiT/pro_detail.html?id="+rand);
		// 2、网页webview中js调用本地java方法
		// 	  "Js_Interactive" 是这个对象在js中的别名
		webView.addJavascriptInterface(new JavascriptInterface(this), "Js_Interactive");
		// 
		webView.setWebViewClient(new MywebViewClient());  
		
		popInit();
	}
	
	// js通信接口  
    public class JavascriptInterface {  
  
        private Context context;  
  
        public JavascriptInterface(Context context) {  
            this.context = context;  
        }  
        
        @android.webkit.JavascriptInterface
        public void dialog_selectType(){
			//javascript 从子线程调用
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "javascript调用了popwindow", Toast.LENGTH_SHORT).show();
					//接到web里面的javascript传来的命令，调用显示dialog
					shareDialog.show();
				}
			});

        }
        //这个方法获取从网页得到的imagepath
        @android.webkit.JavascriptInterface
        public void openImage(String img) { 
        	Toast.makeText(context, "javascript传来的图片地址："+img, Toast.LENGTH_SHORT).show();
//        	Intent intent = new Intent(WebActivity.this,ImageActivity.class);
//        	intent.putExtra("imageurl", img);
        	ArrayList<String> imagePathLists = new ArrayList<String>();
        	for (int i = 0; i < 3; i++) {
        		imagePathLists.add(img);
			}
        	Intent intent = new Intent(context,ImagePagerActivity.class);
			intent.putExtra("position", 1);
			intent.putStringArrayListExtra("images", imagePathLists);
            startActivity(intent);
        }
        //这个方法获取从网页得到的imagepath
        @android.webkit.JavascriptInterface
        public void Imageshow(String img,int postion) { 
        	Toast.makeText(context, "javascript传来的图片地址："+img, Toast.LENGTH_SHORT).show();
//        	Intent intent = new Intent(WebActivity.this,ImageActivity.class);
//        	intent.putExtra("imageurl", img);
        	System.out.println("imgpath:"+img+" postion:"+postion);
        	
        	Intent intent = new Intent(context,ImagePagerActivity.class);
        	intent.putExtra("position", postion);
        	intent.putStringArrayListExtra("images", imagepath(img));
        	startActivity(intent);
        }
        
        @android.webkit.JavascriptInterface
        public void collection(long id){
        	Toast.makeText(context, "javascript传来的商品id："+id, Toast.LENGTH_SHORT).show();
        }
        @android.webkit.JavascriptInterface
        public void collection(String id){
        	Toast.makeText(context, "javascript传来的商品id："+id, Toast.LENGTH_SHORT).show();
        }
        //这是网页控制跳去一个新的Activity
        @android.webkit.JavascriptInterface
	    public void comment(String id){
	    	Toast.makeText(context, "javascript传来的商品id："+id, Toast.LENGTH_SHORT).show();  
	    	Intent intent = new Intent(WebActivity.this,WebStartActivity.class);
	    	startActivity(intent);
	    }
        @android.webkit.JavascriptInterface
	    public void business_go(String bussID){
        	Toast.makeText(context, "javascript传来的商家id："+bussID, Toast.LENGTH_SHORT).show();  
	    }
	    
    } 
	@ContentView(R.layout.activity_web)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	public void popInit() {
		shareDialog = new ShareDialog(this,R.layout.share,R.style.LoadingDialog);
		shareDialog.setCancelable(true);
		shareDialog.setCanceledOnTouchOutside(true);
		ListView listView = (ListView) shareDialog.getView().findViewById(R.id.listView);
		listView.setAdapter(new MyAdapter(getList(), WebActivity.this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 3、 java代码调用js并传递参数,注意str类型在传递的时候参数要用单引号括起来
				webView.loadUrl("javascript:selectODM('傻逼海阔',"+arg2+")");
				shareDialog.dismiss();
			}
		});
		
	}

	// 动态注入js函数监听  
	private void addImageClickListner() {  
        // 这段js函数的功能就是，遍历所有的img几点，并添加 函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		StringBuilder sb = new StringBuilder();
		sb
				.append("javascript:(function(){")
				.append("var objs = document.getElementsByTagName(\"img\"); " )
				.append("for(var i=0;i<objs.length;i++)  ")
				.append( "{")
				.append("    objs[i].onclick=function()  ")
				.append("    {  ")
				.append("        window.Js_Interactive.openImage(this.src);  ")
				.append("    }  ")
				.append("}")
				.append("})");
        webView.loadUrl(sb.toString());
    }
    
    // 4、监听网页加载后的操作
    private class MywebViewClient extends WebViewClient{
    	@Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
  
            return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @SuppressLint("SetJavaScriptEnabled") 
        @Override  
        public void onPageFinished(WebView view, String url) {  
  
            view.getSettings().setJavaScriptEnabled(true);  
  
            super.onPageFinished(view, url);  
            // html加载完成之后，添加监听图片的点击js函数  
//            addImageClickListner();  
        }  
  
        @SuppressLint("SetJavaScriptEnabled") 
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  
            view.getSettings().setJavaScriptEnabled(true);  
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
  
            super.onReceivedError(view, errorCode, description, failingUrl);  
  
        }  
    }
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.btn_buy:
			shareDialog.show();
			break;
		case R.id.btn_share:
			Toast.makeText(WebActivity.this, "改变收藏按钮的背景", Toast.LENGTH_SHORT).show();  
			//collectDOM
			// 3、 java代码调用js并传递参数,注意str类型在传递的时候参数要用单引号括起来
			webView.loadUrl("javascript:collectDOM()");
			break;
		}
		
	}
	
	private class MyAdapter extends BaseAdapter{
		public List<String> list;
		private LayoutInflater mInflater;// 动态布局映射

		public MyAdapter(List<String> ll,Context context){
			this.list = ll;
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1 = mInflater.inflate(android.R.layout.simple_list_item_1, null);
			TextView tv_name = (TextView) arg1;
			tv_name.setText(list.get(arg0));
			return arg1;
		}
		
	}
	
	private List<String> getList(){
		List<String> ll = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			ll.add("分类"+i);
		}
		return ll;
	}
	
	private ArrayList<String> imagepath(String json){
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonarray = jsonObject.getJSONArray("imgpath");
			for (int i = 0; i < jsonarray.length(); i++) {
				list.add(jsonarray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
