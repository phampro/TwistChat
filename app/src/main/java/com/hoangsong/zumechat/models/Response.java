package com.hoangsong.zumechat.models;

/**
 * Created by nhat2 on 05/17/2016.
 * Project name BottleflyMerchant
 * Email: nhat292@gmail.com
 * Phone number: +84 169 643 6023
 */
public class Response {
    private int error_code;
    private String message;
    private String token;
    private Object data;
    private long coin;

    public Response(int error_code, String message) {
        this.error_code = error_code;
        this.message = message;
    }

    public Response(int error_code, String message, Object data) {
        this.data = data;
        this.message = message;
        this.error_code = error_code;
    }

    public Response(int error_code, String message, long coin) {
        this.coin = coin;
        this.message = message;
        this.error_code = error_code;
    }

    public Response(int error_code, String message, String token, Object data) {
        this.data = data;
        this.message = message;
        this.error_code = error_code;
        this.token = token;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }
}
