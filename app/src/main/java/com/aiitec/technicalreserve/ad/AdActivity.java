package com.aiitec.technicalreserve.ad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.openapi.view.annatation.event.OnCompoundButtonCheckedChange;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdActivity extends BaseActivity {

    @ContentView(R.layout.activity_ad)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("广告");
        init();
    }
    @Resource(R.id.et_time)
    private EditText et_time;
    @Resource(R.id.et_number)
    private EditText et_number;
    private boolean is_open = false;
    @Resource(R.id.tg_open)
    private ToggleButton tg_open;
    @Resource(R.id.ad_layout)
    private AdvertisementLayout advertisementLayout;
    private int num = 3, time = 2;
    //随便从网上获取的图片资源
    private String[] img = {
            "http://pic.wenwen.soso.com/p/20110404/20110404194638-296085280.jpg",
            "http://pic.wenwen.soso.com/p/20110404/20110404194729-542361848.jpg",
            "http://ichip.chjhs.ntpc.edu.tw/dyna/data/user/admin/files/20090316111610124.jpg",
            "http://www.th7.cn/d/file/p/2011/07/13/e563f6271025b9a9e424bac309cad389.jpg",
            "http://wenwen.soso.com/p/20110402/20110402213246-1002474835.jpg",
            "http://pic.nipic.com/2007-11-09/200711916636774_2.jpg",
            "http://img.popoho.com/allimg/120613/1A14332B-3.jpg" };

    private void init(){
        tg_open.setChecked(true);
        advertisementLayout.startAD(num, time, true,getImagePath(num),-1);
        et_number.setText(num + "");
        et_time.setText(time + "");
    }



//    @Override
    @OnCompoundButtonCheckedChange(R.id.tg_open)
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        if (arg1) {
            is_open = true;
        } else {
            is_open = false;
        }
    }

    public  void onClick_Event(View v){
        switch(v.getId()){
            //确定按钮
            case R.id.btn_sure:
                String str_num = et_number.getText().toString().trim();
                String str_time = et_time.getText().toString().trim();
                int num,time;
                if(str_num.equals("")){
                    num = 1;
                }else{
                    num = Integer.valueOf(str_num);
                }

                if(str_time.equals("")){
                    time = 5;
                }else{
                    time = Integer.valueOf(str_time);
                }

                advertisementLayout.startAD(num, time, is_open,getImagePath(num),-1);
                break;
        }
    }

    //这个随机选择图片
    private List<String> getImagePath(int number){
        List<String> list = new ArrayList<String>();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            int index = random.nextInt(img.length);
            list.add(img[index]);
        }
        return list;
    }
}
