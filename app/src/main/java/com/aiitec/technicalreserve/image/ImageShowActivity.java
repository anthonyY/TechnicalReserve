package com.aiitec.technicalreserve.image;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aiitec.openapi.view.ViewUtils;
import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.openapi.view.annatation.Resource;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片列表展示类，
 */
public class ImageShowActivity extends BaseActivity {

    @Resource(R.id.recycler_view_image_show)
    private RecyclerView recycler_view_image_show;

    private ImageShowAdapter adapter;
    @Resource(stringArrayId = R.array.image_show_datas)
    private String[] contents ;

    /**
     * 默认图片路径，百度找的地址
     */
    public static String[] imagePaths = {
            "http://img1.3lian.com/img013/v4/96/d/50.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3088045350,1557105434&fm=21&gp=0.jpg",
            "http://img1.3lian.com/img013/v4/96/d/51.jpg",
            "http://pic41.nipic.com/20140511/14571813_084320532135_2.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1405/10/c15/34073188_1399701342998_mthumb.jpg",
            "http://img1.3lian.com/img013/v4/96/d/53.jpg",
            "http://img5.duitang.com/uploads/item/201405/16/20140516205900_zdGk2.thumb.700_0.jpeg",
            "http://pic75.nipic.com/file/20150814/13154091_170951424000_2.jpg",
            "http://attach.66rpg.com/bbs/attachment/forum/201509/17/171611yvph81hzfpgpplun.jpg"
    };

    @ContentView(R.layout.activity_image_show)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("图片展示");
        List<String> imageArrays = new ArrayList<>();

        //如果是从图片选择过来的，就使用图片选择的图片，否则显示默认的网上的图片
        if(getIntent().hasExtra(MultiImageActivity.KEY_SELECTED_IMAGE)){
            imageArrays = getIntent().getStringArrayListExtra(MultiImageActivity.KEY_SELECTED_IMAGE);
        } else {
            imageArrays = Arrays.asList(imagePaths);
        }
        List<Message> datas = new ArrayList<>();
        int index = 0;
        for (String content: contents){
            Message message = new Message();
            message.setContent(content);
            List<Image> images = new ArrayList<>();
            for (int i=0; i< imageArrays.size(); i++){
                Image image = new Image();
                image.setPath(imageArrays.get(i));
                images.add(image);
                if(i >= index){
                    break;
                }
            }
            index++;
            message.setImages(images);
            datas.add(message);

        }

        adapter = new ImageShowAdapter(this, datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_image_show.setLayoutManager(layoutManager);
        recycler_view_image_show.setAdapter(adapter);
    }

}
