package com.hoangsong.zumechat.models;

/**
 * Created by HOAI AN on 15/02/2017.
 */

public class MemberInfo {
    private int ordering;
    private String id;
    private String username;
    private String profile_url;
    private boolean online_status;
    private String job_status;
    private String offline_on;
    private String description;
    private String country_code;
    private String distance;
    private boolean is_block;
    private boolean is_new;
    private ChatInfo chatInfo;
    private boolean is_check;//sub
    private boolean is_chat;//sub
    private boolean isHasAds = false;// sub

    public MemberInfo(String id, String username, String status, String description, String offline_on, String url_avatar, String distance) {
        this.id = id;
        this.username = username;
        this.job_status = status;
        this.description = description;
        this.offline_on = offline_on;
        this.profile_url = url_avatar;
        this.distance = distance;
    }

    public MemberInfo(int ordering, String id, String username, String profile_url, boolean online_status, String job_status, String offline_on, String description, String country_code, boolean is_block, String distance, ChatInfo chatInfo) {
        this.ordering = ordering;
        this.id = id;
        this.username = username;
        this.profile_url = profile_url;
        this.online_status = online_status;
        this.job_status = job_status;
        this.offline_on = offline_on;
        this.description = description;
        this.country_code = country_code;
        this.is_block = is_block;
        this.distance = distance;
        this.chatInfo = chatInfo;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public boolean isOnline_status() {
        return online_status;
    }

    public void setOnline_status(boolean online_status) {
        this.online_status = online_status;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getOffline_on() {
        return offline_on;
    }

    public void setOffline_on(String offline_on) {
        this.offline_on = offline_on;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean is_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }

    public boolean is_new() {
        if(chatInfo != null){
            if(is_new || !chatInfo.is_read())
                return true;
            else
                return false;
        }else
            return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }

    public boolean is_check() {
        return is_check;
    }

    public void setIs_check(boolean is_check) {
        this.is_check = is_check;
    }

    public boolean is_chat() {
        return is_chat;
    }

    public void setIs_chat(boolean is_chat) {
        this.is_chat = is_chat;
    }

    public boolean isHasAds() {
        return isHasAds;
    }

    public void setHasAds(boolean hasAds) {
        isHasAds = hasAds;
    }
}
