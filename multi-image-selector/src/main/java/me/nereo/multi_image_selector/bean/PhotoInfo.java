package me.nereo.multi_image_selector.bean;

/**
 * Created by Administrator on 2016/3/14.
 */
public class PhotoInfo {
    private String filePath;
    private String urlPath;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
}
