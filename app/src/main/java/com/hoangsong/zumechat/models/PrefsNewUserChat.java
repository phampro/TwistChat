package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by Tang on 3/12/2017.
 */

public class PrefsNewUserChat {
    private ArrayList<String> arr_id_user;

    public PrefsNewUserChat() {
        this.arr_id_user = new ArrayList<>();
    }

    public PrefsNewUserChat(ArrayList<String> arr_id_user) {
        this.arr_id_user = arr_id_user;
    }

    public ArrayList<String> getArr_id_user() {
        return arr_id_user;
    }

    public void setArr_id_user(ArrayList<String> arr_id_user) {
        this.arr_id_user = arr_id_user;
    }

    public void add_id_user(String id_user) {
        boolean is_add = false;
        for (int i = 0; i < arr_id_user.size(); i++) {
            if(arr_id_user.get(i).equals(id_user))
                return;
        }
        this.arr_id_user.add(id_user);
    }

    public void remove_id_user(String id_user) {
        for (int i = 0; i < arr_id_user.size(); i++) {
            if(arr_id_user.get(i).equals(id_user)){
                arr_id_user.remove(i);
                return;
            }
        }
    }
}
