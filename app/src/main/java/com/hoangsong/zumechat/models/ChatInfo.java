package com.hoangsong.zumechat.models;

import android.content.Context;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Encryption;

/**
 * Created by HOAI AN on 23/02/2017.
 */

public class ChatInfo {
    private String id;
    private String chat_message;
    private String sender_profile_url;
    private String receiver_profile_url;
    private String photo_url;
    private String chat_type;
    private String created_on;
    private String created_on_display;
    private String sender_id;
    private String sender_name;
    private String receiver_id;
    private String receiver_username;
    private boolean is_delete;
    private boolean is_update;
    private boolean is_read;
    private int background = 0;
    private boolean showTime = true;

    public ChatInfo(String id, String chat_message, String sender_profile_url, String receiver_profile_url, String photo_url, String chat_type, String created_on, String created_on_display, String sender_id, String sender_name, String receiver_id,  String receiver_username, boolean is_delete, boolean is_update, boolean is_read) {
        this.id = id;
        this.chat_message = chat_message;
        this.sender_profile_url = sender_profile_url;
        this.receiver_profile_url = receiver_profile_url;
        this.photo_url = photo_url;
        this.chat_type = chat_type;
        this.created_on = created_on;
        this.created_on_display = created_on_display;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.receiver_id = receiver_id;
        this.receiver_username = receiver_username;
        this.is_delete = is_delete;
        this.is_update = is_update;
        this.is_read = is_read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_messageDecrypt() {
        try {
            return Encryption.decrypt(chat_message);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getChat_messageNotification(Context context) {
        switch (chat_type){
            case Constants.CHAT_TYPE_HI:
                return context.getString(R.string.lbl_send_a_message);
            case Constants.CHAT_TYPE_PHOTO:
                return context.getString(R.string.lbl_send_a_photo);
            case Constants.CHAT_TYPE_TEXT:
                return context.getString(R.string.lbl_send_a_message);
            default:
                return context.getString(R.string.lbl_send_a_message);
        }
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_username() {
        return receiver_username;
    }

    public void setReceiver_username(String receiver_username) {
        this.receiver_username = receiver_username;
    }

    public String getSender_profile_url() {
        return sender_profile_url;
    }

    public void setSender_profile_url(String sender_profile_url) {
        this.sender_profile_url = sender_profile_url;
    }

    public String getReceiver_profile_url() {
        return receiver_profile_url;
    }

    public void setReceiver_profile_url(String receiver_profile_url) {
        this.receiver_profile_url = receiver_profile_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getCreated_on_display() {
        return created_on_display;
    }

    public void setCreated_on_display(String created_on_display) {
        this.created_on_display = created_on_display;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public boolean is_delete() {
        return is_delete;
    }

    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }

    public boolean is_update() {
        return is_update;
    }

    public void setIs_update(boolean is_update) {
        this.is_update = is_update;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
