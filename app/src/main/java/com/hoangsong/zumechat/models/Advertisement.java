package com.hoangsong.zumechat.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pvthiendeveloper on 3/17/2017.
 */

public class Advertisement {
    private String id;
    private String name;
    private String content;
    private String url;
    private ArrayList<Image> images;

    public Advertisement() {
    }

    public Advertisement(String id, String name, String content, String url, ArrayList<Image> images) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.url = url;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
}
