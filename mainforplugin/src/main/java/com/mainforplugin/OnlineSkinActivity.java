package com.mainforplugin;

import android.app.Activity;
import android.app.ProgressDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aiitec.openapi.net.AIIRequest;
import com.aiitec.openapi.net.AIIResponse;
import com.aiitec.openapi.net.ProgressResponseBody;
import com.aiitec.openapi.utils.LogUtil;
import com.aiitec.openapi.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * 在线皮肤类
 */
public class OnlineSkinActivity extends Activity {
    private ProgressDialog progressDialog;
//    private HttpUtils httpUtils;
    private String url = "http://192.168.1.101:8080/plugin/";
    private String listUrl = "http://192.168.1.101:8080/AndroidPlugin/servlet/PluginServlet";
    private ListView lv;
    private ArrayAdapter<String> adapter;

    AIIRequest aiiRequest;
    File[] plugins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_skin);
        aiiRequest = new AIIRequest(this);
        LogUtil.showLog = true;
        lv = (ListView) findViewById(R.id.lv);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在下载......");

//        httpUtils = new HttpUtils();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    download(adapter.getItem(position));
            }
        });

        File file = new File(MainActivity.pluginFolder);
        if(!file.exists()){
            file.mkdir();
        }

        plugins = file.listFiles();

        requestList();
    }

    private void requestList(){

        aiiRequest.sendOthers(listUrl, null, new AIIResponse(this){
            @Override
            public void onSuccess(String content, int index) {
                super.onSuccess(content, index);
                try{
                    JSONObject obj = new JSONObject(content);
                    JSONArray array = obj.getJSONArray("plugins");


                    for(int i=0; i<array.length(); i++){
                        JSONObject obj2 = array.getJSONObject(i);
                        String name = obj2.getString("path");

                        boolean isDownload = false;
                        if (plugins == null || plugins.length == 0) {
                            for(File fileName: plugins){
                                if(fileName.getName().equalsIgnoreCase(name)){//已经下载了
                                    isDownload = true;
                                    break;
                                }
                            }
                        }
                        if(!isDownload){
                            adapter.add(name);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( String content, int index) {
                super.onFailure(content, index);
                ToastUtil.show(OnlineSkinActivity.this, "网络异常");
            }
        }, 1);
    }

    private long total;
    private void download(String fileName) {


        aiiRequest.download(url + fileName, new File(MainActivity.pluginFolder + fileName), new ProgressResponseBody.ProgressListener() {
                    @Override
                    public void onPreExecute(long totalByte) {
                        total = totalByte;
                    }
                    @Override
                    public void update(final long current) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("正在下载  "+(current*100/total)+"%");
                            }
                        });
                    }

                    @Override
                    public void onSuccess(File file) {
                        total = 0;
                        Toast.makeText(getApplicationContext(), "下载完成！", Toast.LENGTH_SHORT).show();
                        Log.e("aiitec", "下载完成！");
                        for(int i=0; i<adapter.getCount(); i++){
                            if(adapter.getItem(i).equalsIgnoreCase(file.getName())){
                                adapter.remove(adapter.getItem(i));
                                break;
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        if(!progressDialog.isShowing()){
                            progressDialog.show();
                        }
                    }


                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "下载失败！", Toast.LENGTH_SHORT).show();
                        Log.e("aiitec", "下载失败！");
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
       /* aiiRequest.download(url + fileName, new AIIResponse(this) {
            @Override
            public void onStart(int index) {
                super.onStart(index);
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }

            @Override
            public void onSuccess(String content, int index) {
                super.onSuccess(content, index);
                Toast.makeText(getApplicationContext(), "下载完成！", Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载完成！");
                for(int i=0; i<adapter.getCount(); i++){
                    if(adapter.getItem(i).equalsIgnoreCase(fileName)){
                        adapter.remove(adapter.getItem(i));
                        break;
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String content, int index) {
                super.onFailure(content, index);
                Toast.makeText(getApplicationContext(), "下载失败！"+content, Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载失败！"+content);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }*/
//        , new ProgressResponseBody.ProgressListener() {
//            @Override
//            public void onPreExecute(long totalByte) {
//                total = totalByte;
//            }
//
//            @Override
//            public void update(long current, boolean b) {
//                progressDialog.setMessage("正在下载  "+(current*100/total)+"%");
//
//            }
//        }, 1);
        /*httpUtils.download(url+fileName, MainActivity.pluginFolder+"/"+fileName, true, new RequestCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(), "下载完成！", Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载完成！");
                for(int i=0; i<adapter.getCount(); i++){
                    if(adapter.getItem(i).equalsIgnoreCase(responseInfo.result.getName())){
                        adapter.remove(adapter.getItem(i));
                        break;
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                progressDialog.setMessage("正在下载  "+(current*100/total)+"%");

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getApplicationContext(), "下载失败！"+msg, Toast.LENGTH_SHORT).show();
                Log.e("aiitec", "下载失败！"+msg);
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });*/
    }
}
