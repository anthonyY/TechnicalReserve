package an.com.alipaytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aiitec.alipay.AliPayUtils;
import com.aiitec.alipay.Keys;

public class MainActivity extends AppCompatActivity {

    private EditText et_amount, et_content, et_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv = new ListView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        lv.setLayoutParams(params);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("支付宝支付");
        adapter.add("银联支付");
        adapter.add("微信支付");
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        startActivity(new Intent(MainActivity.this, AlipayActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, UnionpayActivity.class));
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "有空再研究，有官方例子，看官方的也还算好理解", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        setContentView(lv);



    }

}
