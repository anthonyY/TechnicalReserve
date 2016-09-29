package an.com.unionpay;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class UnionpayUtil {

	/**00正式环境    01 测试环境*/
	private Mode mode = Mode.ONLINE;
//	private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
	private static final String LOG_TAG = "aiitec";
	public static final int TYPE_JAR = 0x01;
	public static final int TYPE_APK= 0x02;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 0x02;
	private String R_SUCCESS = "success";
	private String R_FAIL = "fail";
	private String R_CANCEL = "cancel";
	private Activity activity;
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public UnionpayUtil(Activity activity) {
		this.activity = activity;
	}
	public void onActivityResult( int requestCode, int resultCode, Intent data) 
	{
		if( data == null ){
			return;
		}
		
		String str =  data.getExtras().getString("pay_result").trim();
		Log.e("aiitec", "pay_result:"+str);
		if( str.equalsIgnoreCase(R_SUCCESS) ){
			Toast.makeText(activity, " 支付成功！ ", Toast.LENGTH_SHORT).show();
		}else if( str.equalsIgnoreCase(R_FAIL) ){
			Toast.makeText(activity, " 支付失败！ ", Toast.LENGTH_SHORT).show();
		}else if( str.equalsIgnoreCase(R_CANCEL) ){
			Toast.makeText(activity, " 你已取消了本次订单的支付！ ", Toast.LENGTH_SHORT).show();
		}	
	}
	
//	private Handler mHandler = new Handler(new Handler.Callback() {
//		
//		@Override
//		public boolean handleMessage(Message msg) {
////			Log.e(LOG_TAG, " " + "" + msg.obj);
////	        if (mLoadingDialog.isShowing()) {
////	            mLoadingDialog.dismiss();
////	        }
//
//	        String tn = "";
//	        if (msg.obj == null || ((String) msg.obj).length() == 0) {
//	            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//	            builder.setTitle("错误提示");
//	            builder.setMessage("网络连接失败,请重试!");
//	            builder.setNegativeButton("确定",
//	                    new DialogInterface.OnClickListener() {
//	                        @Override
//	                        public void onClick(DialogInterface dialog, int which) {
//	                            dialog.dismiss();
//	                        }
//	                    });
//	            builder.create().show();
//	        } else {
//	            tn = (String) msg.obj;
//	            Log.e("aiitec", "tn:"+tn);
//	            /*************************************************
//	             * 步骤2：通过银联工具类启动支付插件
//	             ************************************************/
//	            if(msg.arg1 == TYPE_JAR){
//	            	doStartUnionPayPluginJar(activity, tn, mMode);
//	            } else if(msg.arg1 == TYPE_APK){
//	            	doStartUnionPayPluginAPK(activity, tn, mMode);
//	            }
//	        }
//
//			return false;
//		}
//	});
	
	public static void doStartUnionPayPluginJar(Activity activity, String tn, Mode mode) {
		UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null, tn, mode.getValue());
	}
	

    public static void doStartUnionPayPluginAPK(final Activity activity, String tn, Mode mode) {
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        int ret = UPPayAssistEx.startPay(activity, null, null, tn, mode.getValue());
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            Log.e(LOG_TAG, " plugin not found or need upgrade!!!");

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(activity);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
        Log.e(LOG_TAG, "" + ret);
    }
    

    
    public void pay(String tn, int type){
    	
//    	new PayThread(type).start();
         /*************************************************
          * 步骤2：通过银联工具类启动支付插件
          ************************************************/
    	Log.e("aiitec", "tn:"+tn);
         if(type == TYPE_JAR){
         	doStartUnionPayPluginJar(activity, tn, mode);
         } else if(type == TYPE_APK){
         	doStartUnionPayPluginAPK(activity, tn, mode);
         }
    }
    public void pay(String tn){
//    	new PayThread(TYPE_JAR).start();
         Log.e("aiitec", "tn:"+tn);
         /*************************************************
          * 步骤2：通过银联工具类启动支付插件
          ************************************************/
         doStartUnionPayPluginJar(activity, tn, mode);
    }
}
