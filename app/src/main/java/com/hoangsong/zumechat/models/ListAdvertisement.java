package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by pvthiendeveloper on 3/17/2017.
 */

public class ListAdvertisement {
    private int total_page;
    private ArrayList<Advertisement> listAds;

    public ListAdvertisement() {
    }

    public ListAdvertisement(int total_page, ArrayList<Advertisement> listAds) {
        this.total_page = total_page;
        this.listAds = listAds;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public ArrayList<Advertisement> getListAds() {
        return listAds;
    }

    public void setListAds(ArrayList<Advertisement> listAds) {
        this.listAds = listAds;
    }
}
