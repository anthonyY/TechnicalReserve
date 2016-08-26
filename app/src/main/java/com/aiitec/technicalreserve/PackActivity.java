package com.aiitec.technicalreserve;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.web.WebStartActivity;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/8/25.
 * 多渠道打包介绍
 */
public class PackActivity extends BaseActivity {

    @Resource(R.id.iv_gif)
    private ImageView iv_gif;
    @Resource(R.id.tv_info)
    private TextView tv_info;
    @ContentView(R.layout.activity_pack)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("多渠道打包");

        String text = tv_info.getText().toString();
        int index = text.lastIndexOf("http");
        String url = "";
        if(index > 0){
            url = text.substring(index, text.length());
        }

//        String url = ;
        SpannableStringBuilder ssb = new SpannableStringBuilder(tv_info.getText());
        ssb.setSpan(new URLSpan(url), index, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new UnderlineSpan(), index, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_info.setText(ssb);
// 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        tv_info.setMovementMethod(LinkMovementMethod.getInstance());
// 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        tv_info.setHighlightColor(0xff8FABCC);

        Glide.with(this).load(R.drawable.dabao).into(iv_gif);
    }
//    public void onClick(View v){
//        if(v.getId() == R.id.tv_url){
//            Intent intent = new Intent(this, WebStartActivity.class);
//            intent.putExtra("url", )
//            startActivity();
//        }
//    }
}
