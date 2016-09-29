package an.com.alipaytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aiitec.alipay.AliPayUtils;
import com.aiitec.alipay.Keys;

/**
 * 支付宝支付，直接引用alipay的library, 在本工程AbdroidManifast加权限和 H5PayActivity H5AuthActivity 就行了
 */
public class AlipayActivity extends AppCompatActivity {

    private EditText et_amount, et_content, et_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Keys.DEFAULT_PARTNER = getString(R.string.alipay_merchant_id);
        Keys.DEFAULT_SELLER = getString(R.string.alipay_merchant_account);
        Keys.PUBLIC = getString(R.string.alipay_public);
        Keys.PRIVATE = getString(R.string.alipay_private);

        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        et_amount = (EditText) findViewById(R.id.et_amount);


    }
    public void onClick(View v){
        double amount = Double.parseDouble(et_amount.getText().toString());
        AliPayUtils.pay(this, "标题", et_content.getText().toString(), amount, AliPayUtils.getOutTradeNo(), new AliPayUtils.PayCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                Log.e("aiitec", "支付成功");
            }

            @Override
            public void onFail() {
                showToast("支付失败");
                Log.e("aiitec", "支付失败");
            }

            @Override
            public void onCancel() {
                showToast("支付取消");
                Log.e("aiitec", "支付取消");
            }

            @Override
            public void onNetErr() {
                showToast("网络异常");
                Log.e("aiitec", "网络异常");
            }
        });
    }
    void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
