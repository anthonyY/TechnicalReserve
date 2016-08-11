package com.aiitec.technicalreserve.image;

import com.aiitec.openapi.model.Entity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class Message extends Entity {

    private String content;
    private List<Image> images;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
