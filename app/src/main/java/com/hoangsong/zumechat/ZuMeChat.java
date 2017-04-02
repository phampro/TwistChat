package com.hoangsong.zumechat;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hoangsong.zumechat.untils.Constants;

/**
 * Created by Tang on 03/10/2016.
 */

public class ZuMeChat extends Application {

    private static ZuMeChat _instance;
    public static String language = Constants.LANGUAGE_VI;
    private static boolean is_background = false;

    public ZuMeChat(){
        _instance = this;
    }

    public static ZuMeChat getInstance(){
        if(_instance == null){
            _instance = new ZuMeChat();
        }
        return _instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean is_background() {
        return is_background;
    }

    public static void setIs_background(boolean is_background) {
        is_background = is_background;
    }
}
