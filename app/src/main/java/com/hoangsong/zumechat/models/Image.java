package com.hoangsong.zumechat.models;

/**
 * Created by pvthiendeveloper on 3/17/2017.
 */

public class Image {
    private String url;
    private String type;

    public Image() {
    }

    public Image(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
