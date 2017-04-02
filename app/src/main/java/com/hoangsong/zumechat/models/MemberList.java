package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by HOAI AN on 27/02/2017.
 */

public class MemberList {
    private int total_page;
    private ArrayList<MemberInfo> friends;

    public MemberList(int total_page, ArrayList<MemberInfo> friends) {
        this.total_page = total_page;
        this.friends = friends;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public ArrayList<MemberInfo> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<MemberInfo> friends) {
        this.friends = friends;
    }
}
