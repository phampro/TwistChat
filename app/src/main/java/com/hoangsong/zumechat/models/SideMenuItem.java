package com.hoangsong.zumechat.models;

/**
 * Created by WeiGuang on 14/12/2015.
 */
public class SideMenuItem {
    private int id;
    private int iconResource;
    private String label;

    public SideMenuItem(int id, String label, int iconResource) {
        this.id = id;
        this.iconResource = iconResource;
        this.label = label;
    }

    public String getLabelText() {
        return this.label;
    }

    public int getIconResource() {
        return this.iconResource;
    }
}
