package com.hoangsong.zumechat.models;

/**
 * Created by Tang on 13/10/2016.
 */

public class AccountInfo {

    private String id;
    private String token;
    private String username;
    private String code;
    private String email;
    private String gender;
    private String reg_date;
    private String job_status;
    private String online_status;
    private String profile_url;
    private String background_url;
    private String country;
    private String country_code;
    private String description;
    private int total_favorites;
    private int total_credit;
    private String credit_expiry_date;
    private boolean is_favorites;

    public AccountInfo(String id, String token, String code, String username, String email, String gender, String reg_date, String job_status, String online_status, String profile_url, String background_url, String country, String country_code, String description, int total_favorites, int total_credit, String credit_expiry_date, boolean is_favorites) {
        this.id = id;
        this.token = token;
        this.code = code;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.reg_date = reg_date;
        this.job_status = job_status;
        this.online_status = online_status;
        this.profile_url = profile_url;
        this.background_url = background_url;
        this.country = country;
        this.country_code = country_code;
        this.description = description;
        this.total_favorites = total_favorites;
        this.total_credit = total_credit;
        this.credit_expiry_date = credit_expiry_date;
        this.is_favorites = is_favorites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getBackground_url() {
        return background_url;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal_favorites() {
        return total_favorites;
    }

    public void setTotal_favorites(int total_favorites) {
        this.total_favorites = total_favorites;
    }

    public int getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(int total_credit) {
        this.total_credit = total_credit;
    }

    public String getCredit_expiry_date() {
        return credit_expiry_date;
    }

    public void setCredit_expiry_date(String credit_expiry_date) {
        this.credit_expiry_date = credit_expiry_date;
    }

    public boolean is_favorites() {
        return is_favorites;
    }

    public void setIs_favorites(boolean is_favorites) {
        this.is_favorites = is_favorites;
    }
}
