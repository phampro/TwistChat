package com.hoangsong.zumechat.helpers;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.MasterData;
import com.hoangsong.zumechat.models.PrefsNewUserChat;
import com.hoangsong.zumechat.untils.Constants;

public class Prefs {
    private static final String PREFERENCE_LANGUAGE = "pref.language";
    private static final String PREFERENCE_IMAGE_NAME = "pref.currentimagename";
    private static final String PREFERENCES_TEMPFOLDER = "pref.tempfolder";
    private static final String PREFERENCES_HELPER = "prefhelper";
    private static final String PREFERENCE_DEVICE_ID = "pref.deviceid";
    private static final String PREFERENCE_REGISTER_DEVICE_OK = "pref.device_registed";
    private static final String PREFERENCE_LOGIN_DATA = "pref.login_data";
    private static final String PREFERENCE_MASTER_DATA = "pref.master_data";
    private static final String PREFERENCE_SOUND_NOTIFICATION = "pref.soundnotification";
    private static final String PREFERENCE_VIBRATION_NOTIFICATION = "pref.vibrationnotification";
    private static final String PREFERENCE_EMAIL = "pref.email";
    private static final String PREFERENCE_NEW_USER_CHAT = "pref.newuserchat";

    private static SharedPreferences getPrefs() {
        return ZuMeChat.getInstance().getSharedPreferences(PREFERENCES_HELPER, Context.MODE_PRIVATE);
    }

    //GCM
    public static void setDeviceId(String deviceID){
        getPrefs().edit().putString(PREFERENCE_DEVICE_ID, deviceID).commit();
    }
    public static String getDeviceID(){
        return getPrefs().getString(PREFERENCE_DEVICE_ID, "");
    }
    public static void setDeviceRegister(boolean registed){
        getPrefs().edit().putBoolean(PREFERENCE_REGISTER_DEVICE_OK, registed).commit();
    }

    public static String getCurrentImageName() {
        return getPrefs().getString(PREFERENCE_IMAGE_NAME, "");
    }

    public static void setCurrentImageName(String value) {
        getPrefs().edit().putString(PREFERENCE_IMAGE_NAME, value).commit();
    }

    public static String getCurrentLanguage() {
        return getPrefs().getString(PREFERENCE_LANGUAGE, Constants.LANGUAGE_EN);
    }

    public static void setCurrentLanguage(String value) {
        getPrefs().edit().putString(PREFERENCE_LANGUAGE, value).commit();
    }

    public static boolean getTempFolder() {
        return getPrefs().getBoolean(PREFERENCES_TEMPFOLDER, false);
    }

    public static void setTempFolder(boolean value) {
        getPrefs().edit().putBoolean(PREFERENCES_TEMPFOLDER, value).commit();
    }

    public static void setVibrationNotification(boolean status){
        getPrefs().edit().putBoolean(PREFERENCE_VIBRATION_NOTIFICATION, status).commit();
    }
    public static boolean getVibrationNotification(){
        return getPrefs().getBoolean(PREFERENCE_VIBRATION_NOTIFICATION, true);
    }

    public static void setSoundNotification(boolean status){
        getPrefs().edit().putBoolean(PREFERENCE_SOUND_NOTIFICATION, status).commit();
    }
    public static boolean getSoundNotification(){
        return getPrefs().getBoolean(PREFERENCE_SOUND_NOTIFICATION, true);
    }

    public static void setUserInfo(AccountInfo loginData) {
        String data = "";
        if (loginData != null) {
            Gson gson = new Gson();
            data = gson.toJson(loginData);
        }
        getPrefs().edit().putString(PREFERENCE_LOGIN_DATA, data).commit();
    }

    public static AccountInfo getUserInfo() {
        Gson gson = new Gson();
        String data = getPrefs().getString(PREFERENCE_LOGIN_DATA, "");
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, AccountInfo.class) : null;
    }

    public static void setMasterData(MasterData masterData) {
        String data = "";
        if (masterData != null) {
            Gson gson = new Gson();
            data = gson.toJson(masterData);
        }
        getPrefs().edit().putString(PREFERENCE_MASTER_DATA, data).commit();
    }

    public static MasterData getMasterData() {
        Gson gson = new Gson();
        String data = getPrefs().getString(PREFERENCE_MASTER_DATA, "");
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, MasterData.class) : null;
    }


    public static void setEmail(String email){
        getPrefs().edit().putString(PREFERENCE_EMAIL, email).commit();
    }
    public static String getEmail(){
        return getPrefs().getString(PREFERENCE_EMAIL, "");
    }

    public static void setPrefsNewUserChat(PrefsNewUserChat response) {
        String data = "";
        if (response != null) {
            Gson gson = new Gson();
            data = gson.toJson(response);
        }
        getPrefs().edit().putString(PREFERENCE_NEW_USER_CHAT, data).commit();
    }

    public static PrefsNewUserChat getPrefsNewUserChat() {
        Gson gson = new Gson();
        String data = getPrefs().getString(PREFERENCE_NEW_USER_CHAT, "");
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, PrefsNewUserChat.class) : new PrefsNewUserChat();
    }

}
