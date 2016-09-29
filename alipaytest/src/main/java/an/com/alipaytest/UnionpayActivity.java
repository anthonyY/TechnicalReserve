package an.com.alipaytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aiitec.alipay.AliPayUtils;

import an.com.unionpay.Mode;
import an.com.unionpay.UnionpayUtil;

/**
 * 银联支付，直接引用unionpay的library, 在本工程AbdroidManifast加权限和com.unionpay.uppay.PayActivity 就行了
 * <p>
 * 需要注意的是assets目录，需要用Android Studio生成，复制粘贴可能不可用
 * 文档地址 https://open.unionpay.com/ajweb/help/file/techFile?productId=3
 * <p>
 * 一、开发前的准备工作
 * 1. 打开https://open.unionpay.com/，后续说的文档下载、FAQ查询等都在这个平台操作。
 * 2. 下载规范和开发包。帮助中心-下载-产品接口规范-手机控件支付产品接口规范，帮助中心-下载-产品接口规范-手机控件支付产品技术开发包。
 * 3. 开发人员都请先看下6.2的消费的交易流程。
 * 4. 相关测试参数：
 * 如果已签约，有自己的测试商户，则直接用自己的商户号测试，测试证书开发包里都有。* 尽量用真实商户号测试，防止有时候参数配错能尽早发现。
 * 如果没有签约，或者商户号尚未分配，请在平台里自行获取商户并且开交易权限，方法为：
 * a) 左上角注册；
 * b) 登陆后右上角我的测试-测试参数
 * c) 我的测试-产品-选下自己集成的。
 * <p>
 * 测试卡号信息：
 * 借记卡：6226090000000048
 * 手机号：18100000000
 * 密码：111101
 * 短信验证码：123456
 * （短信验证码记得点下获取验证码之后再输入）
 * <p>
 * 贷记卡：6226388000000095；
 * 手机号：18100000000；
 * cvn2：248；
 * 有效期：1219；
 * 短信验证码：123456
 * （短信验证码记得点下获取验证码之后再输入）
 *
 * @author Anthony
 *         createTime 2016/9/1.
 * @version 1.0
 */

public class UnionpayActivity extends Activity {
    private EditText et_amount, et_content, et_title;
    //	private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn"; 测试可以通过这个地址获取tn, 正式时，服务端会提供
    private UnionpayUtil unionpayUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        et_title = (EditText) findViewById(R.id.et_title);
        et_title.setText("201609011714458495538");
        et_content = (EditText) findViewById(R.id.et_content);
        et_amount = (EditText) findViewById(R.id.et_amount);
        unionpayUtil = new UnionpayUtil(this);
        unionpayUtil.setMode(Mode.TEST);
    }

    public void onClick(View v) {

//        unionpayUtil.pay( et_title.getText().toString(), UnionpayUtil.TYPE_APK);//apk 的方式支付
        unionpayUtil.pay(et_title.getText().toString(), UnionpayUtil.TYPE_JAR);//jar的方式支付，都可以，我们用jar的方式吧
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //这个回调不能忘记
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        unionpayUtil.onActivityResult(requestCode, resultCode, data);
    }
}
