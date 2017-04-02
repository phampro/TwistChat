package com.hoangsong.zumechat.helpers;

import android.content.Context;
import android.util.Log;


import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.CreditPackageInfo;
import com.hoangsong.zumechat.models.Image;
import com.hoangsong.zumechat.models.ListAdvertisement;
import com.hoangsong.zumechat.models.MasterData;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.BroadcastSignalRInfo;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.ChatMessageList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final String ERROR_CODE = "errorCode";
    private static final String MESSAGE = "message";

    public Object parseData(Context context, String parseString, int processID, int statusCode) {
        if (Constants.DEBUG_MODE) {
            Log.d(TAG, "STRING_PARSER: " + parseString);
        }
        if (processID == Constants.ID_METHOD_LOGIN) {
            return getLoginResponse(parseString);
        }else if (processID == Constants.ID_METHOD_GET_MASTER_DATA) {
            return getMasterData(parseString);
        }else if (processID == Constants.ID_METHOD_REGISTER) {
            return getLoginResponse(parseString);
        }else if (processID == Constants.ID_METHOD_GET_PROFILE) {
            return getLoginResponse(parseString);
        }else if (processID == Constants.ID_METHOD_CHANGE_PASSWORD) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_FORGOT_PASSWORD) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_FEEDBACK) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_SEND_MESSAGE_CHAT) {
            return getSendMessageChat(parseString);
        }else if (processID == Constants.ID_METHOD_GET_CHAT_MESSAGES) {
            return getMessageChat(parseString);
        }else if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            return getSearchFriend(parseString);
        }else if (processID == Constants.ID_METHOD_SEARCH_FRIEND_NEARBY) {
            return getSearchFriend(parseString);
        }else if (processID == Constants.ID_METHOD_UPDATE_MESSAGE_CHAT) {
            return getSendMessageChat(parseString);
        }else if (processID == Constants.ID_METHOD_DELETE_MESSAGE_CHAT) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_GET_FAVOURITES) {
            return getFavourites(parseString);
        }else if (processID == Constants.ID_METHOD_DELETE_CHAT_BY_ACCOUNT) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_BLOCK_ACCOUNT || processID == Constants.ID_METHOD_UNBLOCK_ACCOUNT) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_GET_CONTACT_PROFILE) {
            return getLoginResponse(parseString);
        }else if (processID == Constants.ID_METHOD_REMOVE_FAVOURITE) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_CHECK_ACCOUNT_REPORT) {
            return getCheckBoolean(parseString);
        }else if (processID == Constants.ID_METHOD_CHECK_ACCOUNT_BLOCK) {
            return getCheckBoolean(parseString);
        }else if (processID == Constants.ID_METHOD_REPORT_ACCOUNT) {
            return getResponse(parseString);
        }else if (processID == Constants.ID_METHOD_BUY_CREDIT_PACKAGE) {
            return getLoginResponse(parseString);
        }else if (processID == Constants.ID_METHOD_GET_CREDIT_PACKAGE) {
            return getCreditPackage(parseString);
        }else if (processID == Constants.ID_METHOD_DELETE_ACCOUNT) {
            return getResponse(parseString);
        }

        else if (processID == Constants.ID_METHOD_GET_PROFILE) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_UPDATE_PROFILE || processID == Constants.ID_METHOD_UPDATE_PROFILE_PICTURE) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_UPDATE_JOB_STATUS) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_ADD_FAVOURITE) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_METHOD_CREATE_ADS) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_METHOD_GET_ALL_ADVERTISEMENT_LIST) {
            return getAllAdvertisementList(parseString);
        }

        return null;
    }

    private Response getResponse(String parseString){
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                int errorCode = checkIntValue(object.getString(ERROR_CODE));
                String message = checkStringValue(object.getString(MESSAGE));
                if(!object.isNull("data") && errorCode == Constants.ERROR_CODE_SUCCESS){
                    String data = checkStringValue(object.getString("data"));
                    return new Response(errorCode, message, data);
                }else{
                    return new Response(errorCode, message, "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }



    private Object getResponseNoData(String parseString) {
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                int errorCode = checkIntValue(object.getString(ERROR_CODE));
                String message = checkStringValue(object.getString(MESSAGE));
                response = new Response(errorCode, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private Response getMasterData(String parseString){
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if(errorCode== Constants.ERROR_CODE_SUCCESS && object != null){
                    //app_content
                    JSONObject obj = object.isNull("app_content") ? null : object.getJSONObject("app_content");
                    MasterData.AppContent appContent = null;
                    if(obj != null){
                        String private_policy = obj.has("private_policy") ? checkStringValue(obj.getString("private_policy")) : "";
                        String terms_of_use = obj.has("terms_of_use") ? checkStringValue(obj.getString("terms_of_use")) : "";
                        appContent = new MasterData.AppContent(terms_of_use, private_policy);
                    }
                    return new Response(errorCode, message, new MasterData(appContent));
                }
                return new Response(errorCode, message, null);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else
            return null;
    }

    private Response getLoginResponse(String parseString){
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                AccountInfo accountInfo = null;
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if(errorCode== Constants.ERROR_CODE_SUCCESS && object != null){
                    String id = object.has("id") ? checkStringValue(object.getString("id")) : "";
                    String token = object.has("token") ? checkStringValue(object.getString("token")) : "";
                    String code = object.has("code") ? checkStringValue(object.getString("code")) : "";
                    String username = object.has("username") ? checkStringValue(object.getString("username")) : "";
                    String email = object.has("email") ? checkStringValue(object.getString("email")) : "";
                    String gender = object.has("gender") ? checkStringValue(object.getString("gender")) : "";
                    String reg_date = object.has("reg_date") ? checkStringValue(object.getString("reg_date")) : "";
                    String job_status = object.has("job_status") ? checkStringValue(object.getString("job_status")) : "";
                    String online_status = object.has("online_status") ? checkStringValue(object.getString("online_status")) : "";
                    String profile_url = object.has("profile_url") ? checkStringValue(object.getString("profile_url")) : "";
                    String background_url = object.has("background_url") ? checkStringValue(object.getString("background_url")) : "";
                    String country_name = object.has("country_name") ? checkStringValue(object.getString("country_name")) : "";
                    String country_code = object.has("country_code") ? checkStringValue(object.getString("country_code")) : "";
                    String description = object.has("description") ? checkStringValue(object.getString("description")) : "";
                    int total_favorites = object.has("total_favorites") ? checkIntValue(object.getString("total_favorites")) : 0;
                    int total_credit = object.has("total_credit") ? checkIntValue(object.getString("total_credit")) : 0;
                    String credit_expiry_date = object.has("credit_expiry_date") ? checkStringValue(object.getString("credit_expiry_date")) : "";
                    boolean is_favorites = object.has("is_favorites") ? checkBooleanValue(object.getString("is_favorites")) : false;
                    accountInfo = new AccountInfo(id, token, code, username, email, gender, reg_date, job_status, online_status, profile_url, background_url, country_name, country_code, description, total_favorites, total_credit, credit_expiry_date, is_favorites);
                }
                response = new Response(errorCode, message, accountInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    public static Response getBroadcastSignalR(String parseString){
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                BroadcastSignalRInfo broadcastSignalRInfo = null;
                int type = mainData.has("type") ? checkIntValue(mainData.getString("type")) : 0;
                if(type == 3){
                    ArrayList<MemberInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = mainData.isNull("data") ? null : mainData.getJSONArray("data");
                    if(arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String sender_name = obj.has("sender_name") ? checkStringValue(obj.getString("sender_name")) : "";
                            String profile_url = obj.has("profile_url") ? checkStringValue(obj.getString("profile_url")) : "";
                            boolean online_status = obj.has("online_status") ? checkBooleanValue(obj.getString("online_status")) : false;
                            String job_status = obj.has("job_status") ? checkStringValue(obj.getString("job_status")) : "";
                            String offline_on = obj.has("offline_on") ? checkStringValue(obj.getString("offline_on")) : "";
                            String description = obj.has("description") ? checkStringValue(obj.getString("description")) : "";
                            String country_code = obj.has("country_code") ? checkStringValue(obj.getString("country_code")) : "";
                            boolean is_block = obj.has("ordering") ? checkBooleanValue(obj.getString("is_block")) : false;
                            listMod.add(new MemberInfo(ordering, id, sender_name, profile_url, online_status, job_status, offline_on, description, country_code, is_block, "", null));
                        }
                        broadcastSignalRInfo = new BroadcastSignalRInfo(type, 0, listMod);
                    }
                }else {
                    JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                    if(errorCode== Constants.ERROR_CODE_SUCCESS && object != null){
                        int action = object.has("action") ? checkIntValue(object.getString("action")) : 0;
                        JSONObject chat = object.isNull("chat") ? null : object.getJSONObject("chat");
                        ChatInfo chatInfo = null;
                        if(chat != null){
                            String id = chat.has("id") ? checkStringValue(chat.getString("id")) : "";
                            String chat_message = chat.has("chat_message") ? checkStringValue(chat.getString("chat_message")) : "";
                            String sender_profile_url = chat.has("sender_profile_url") ? checkStringValue(chat.getString("sender_profile_url")) : "";
                            String receiver_profile_url = chat.has("receiver_profile_url") ? checkStringValue(chat.getString("receiver_profile_url")) : "";
                            String photo_url = chat.has("photo_url") ? checkStringValue(chat.getString("photo_url")) : "";
                            String chat_type = chat.has("chat_type") ? checkStringValue(chat.getString("chat_type")) : "";
                            String created_on = chat.has("created_on") ? checkStringValue(chat.getString("created_on")) : "";
                            String created_on_display = chat.has("created_on_display") ? checkStringValue(chat.getString("created_on_display")) : "";
                            String sender_id = chat.has("sender_id") ? checkStringValue(chat.getString("sender_id")) : "";
                            String sender_name = chat.has("sender_name") ? checkStringValue(chat.getString("sender_name")) : "";
                            String receiver_id = chat.has("receiver_id") ? checkStringValue(chat.getString("receiver_id")) : "";
                            String receiver_username = chat.has("receiver_username") ? checkStringValue(chat.getString("receiver_username")) : "";
                            boolean is_delete = chat.has("is_delete") ? checkBooleanValue(chat.getString("is_delete")) : false;
                            boolean is_update = chat.has("is_update") ? checkBooleanValue(chat.getString("is_update")) : false;
                            boolean is_read = chat.has("is_read") ? checkBooleanValue(chat.getString("is_read")) : true;
                            chatInfo = new ChatInfo(id, chat_message, sender_profile_url, receiver_profile_url, photo_url, chat_type, created_on, created_on_display, sender_id, sender_name, receiver_id, receiver_username, is_delete, is_update, is_read);
                        }
                        broadcastSignalRInfo = new BroadcastSignalRInfo(type, action, chatInfo);
                    }
                }
                response = new Response(errorCode, message, broadcastSignalRInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    private Response getMessageChat(String parseString){
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = data.has("total_page") ? checkIntValue(data.getString("total_page")) : 0;
                    //chats_list
                    ArrayList<ChatInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = data.isNull("chats") ? null : data.getJSONArray("chats");
                    if(arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String chat_message = obj.has("chat_message") ? checkStringValue(obj.getString("chat_message")) : "";
                            String sender_profile_url = obj.has("sender_profile_url") ? checkStringValue(obj.getString("sender_profile_url")) : "";
                            String receiver_profile_url = obj.has("receiver_profile_url") ? checkStringValue(obj.getString("receiver_profile_url")) : "";
                            String photo_url = obj.has("photo_url") ? checkStringValue(obj.getString("photo_url")) : "";
                            String chat_type = obj.has("chat_type") ? checkStringValue(obj.getString("chat_type")) : "";
                            String created_on = obj.has("created_on") ? checkStringValue(obj.getString("created_on")) : "";
                            String created_on_display = obj.has("created_on_display") ? checkStringValue(obj.getString("created_on_display")) : "";
                            String sender_id = obj.has("sender_id") ? checkStringValue(obj.getString("sender_id")) : "";
                            String sender_name = obj.has("sender_name") ? checkStringValue(obj.getString("sender_name")) : "";
                            String receiver_id = obj.has("receiver_id") ? checkStringValue(obj.getString("receiver_id")) : "";
                            String receiver_username = obj.has("receiver_username") ? checkStringValue(obj.getString("receiver_username")) : "";
                            boolean is_delete = obj.has("is_delete") ? checkBooleanValue(obj.getString("is_delete")) : false;
                            boolean is_update = obj.has("is_update") ? checkBooleanValue(obj.getString("is_update")) : false;
                            boolean is_read = obj.has("is_read") ? checkBooleanValue(obj.getString("is_read")) : true;
                            listMod.add(new ChatInfo(id, chat_message, sender_profile_url, receiver_profile_url, photo_url, chat_type, created_on, created_on_display, sender_id, sender_name, receiver_id, receiver_username, is_delete, is_update, is_read));
                        }
                    }
                    return new Response(errorCode, message, new ChatMessageList(total_page, listMod));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private Response getSendMessageChat(String parseString){
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                ChatInfo chatInfo = null;
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if(errorCode== Constants.ERROR_CODE_SUCCESS && object != null){
                    String id = object.has("id") ? checkStringValue(object.getString("id")) : "";
                    String chat_message = object.has("chat_message") ? checkStringValue(object.getString("chat_message")) : "";
                    String sender_profile_url = object.has("sender_profile_url") ? checkStringValue(object.getString("sender_profile_url")) : "";
                    String receiver_profile_url = object.has("receiver_profile_url") ? checkStringValue(object.getString("receiver_profile_url")) : "";
                    String photo_url = object.has("photo_url") ? checkStringValue(object.getString("photo_url")) : "";
                    String chat_type = object.has("chat_type") ? checkStringValue(object.getString("chat_type")) : "";
                    String created_on = object.has("created_on") ? checkStringValue(object.getString("created_on")) : "";
                    String created_on_display = object.has("created_on_display") ? checkStringValue(object.getString("created_on_display")) : "";
                    String sender_id = object.has("sender_id") ? checkStringValue(object.getString("sender_id")) : "";
                    String sender_name = object.has("sender_name") ? checkStringValue(object.getString("sender_name")) : "";
                    String receiver_id = object.has("receiver_id") ? checkStringValue(object.getString("receiver_id")) : "";
                    String receiver_username = object.has("receiver_username") ? checkStringValue(object.getString("receiver_username")) : "";
                    boolean is_delete = object.has("is_delete") ? checkBooleanValue(object.getString("is_delete")) : false;
                    boolean is_update = object.has("is_update") ? checkBooleanValue(object.getString("is_update")) : false;
                    boolean is_read = object.has("is_read") ? checkBooleanValue(object.getString("is_read")) : true;
                    chatInfo = new ChatInfo(id, chat_message, sender_profile_url, receiver_profile_url, photo_url, chat_type, created_on, created_on_display, sender_id, sender_name, receiver_id, receiver_username, is_delete, is_update, is_read);
                }
                response = new Response(errorCode, message, chatInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    private Response getSearchFriend(String parseString){
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = data.has("total_page") ? checkIntValue(data.getString("total_page")) : 0;
                    //chats_list
                    ArrayList<MemberInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = data.isNull("friends") ? null : data.getJSONArray("friends");
                    if(arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String username = obj.has("username") ? checkStringValue(obj.getString("username")) : "";
                            String profile_url = obj.has("profile_url") ? checkStringValue(obj.getString("profile_url")) : "";
                            boolean online_status = obj.has("online_status") ? checkBooleanValue(obj.getString("online_status")) : false;
                            String job_status = obj.has("job_status") ? checkStringValue(obj.getString("job_status")) : "";
                            String offline_on = obj.has("offline_on") ? checkStringValue(obj.getString("offline_on")) : "";
                            String description = obj.has("description") ? checkStringValue(obj.getString("description")) : "";
                            String country_code = obj.has("country_code") ? checkStringValue(obj.getString("country_code")) : "";
                            String distance = obj.has("distance") ? checkStringValue(obj.getString("distance")) : "";
                            boolean is_block = obj.has("ordering") ? checkBooleanValue(obj.getString("is_block")) : false;
                            String chat_id = obj.has("chat_id") ? checkStringValue(obj.getString("chat_id")) : "";
                            String chat_message = obj.has("chat_message") ? checkStringValue(obj.getString("chat_message")) : "";
                            String photo_url = obj.has("photo_url") ? checkStringValue(obj.getString("photo_url")) : "";
                            String chat_type = obj.has("chat_type") ? checkStringValue(obj.getString("chat_type")) : "";
                            boolean is_read = obj.has("is_read") ? checkBooleanValue(obj.getString("is_read")) : true;
                            String chat_created_on = obj.has("chat_created_on") ? checkStringValue(obj.getString("chat_created_on")) : "";
                            String chat_created_on_display = obj.has("chat_created_on_display") ? checkStringValue(obj.getString("chat_created_on_display")) : "";
                            listMod.add(new MemberInfo(ordering, id, username, profile_url, online_status, job_status, offline_on, description, country_code, is_block, distance, new ChatInfo(chat_id, chat_message, "", "", photo_url, chat_type, chat_created_on, chat_created_on_display, "", "", "", "", false, false, is_read)));
                        }
                    }
                    return new Response(errorCode, message, new MemberList(total_page, listMod));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private Response getCreditPackage(String parseString){
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONArray arrayMod = mainData.isNull("data") ? null : mainData.getJSONArray("data");
                ArrayList<CreditPackageInfo> listMod = new ArrayList<>();
                if (errorCode == Constants.ERROR_CODE_SUCCESS && arrayMod != null) {
                    for (int i = 0; i < arrayMod.length(); i++) {
                        JSONObject obj = arrayMod.getJSONObject(i);
                        int id = obj.has("id") ? checkIntValue(obj.getString("id")) : 0;
                        String name = obj.has("name") ? checkStringValue(obj.getString("name")) : "";
                        int day = obj.has("day") ? checkIntValue(obj.getString("day")) : 0;
                        float price = obj.has("price") ? checkFloatValue(obj.getString("price")) : 0;
                        listMod.add(new CreditPackageInfo(id, name, day, price));
                    }
                    return new Response(errorCode, message, listMod);
                } else {
                    return new Response(errorCode, message, listMod);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }



    private Response getCheckBoolean(String parseString){
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                int errorCode = checkIntValue(object.getString(ERROR_CODE));
                String message = checkStringValue(object.getString(MESSAGE));
                if(!object.isNull("data") && errorCode == Constants.ERROR_CODE_SUCCESS){
                    boolean data = checkBooleanValue(object.getString("data"));
                    return new Response(errorCode, message, data);
                }else{
                    return new Response(errorCode, message, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    private Response getFavourites(String parseString){
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = data.has("total_page") ? checkIntValue(data.getString("total_page")) : 0;
                    //chats_list
                    ArrayList<MemberInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = data.isNull("favourites") ? null : data.getJSONArray("favourites");
                    if(arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String name = obj.has("name") ? checkStringValue(obj.getString("name")) : "";
                            String profile_url = obj.has("profile_url") ? checkStringValue(obj.getString("profile_url")) : "";
                            boolean online_status = obj.has("online_status") ? checkBooleanValue(obj.getString("online_status")) : false;
                            String job_status = obj.has("job_status") ? checkStringValue(obj.getString("job_status")) : "";
                            String offline_on = obj.has("offline_on") ? checkStringValue(obj.getString("offline_on")) : "";
                            String description = obj.has("description") ? checkStringValue(obj.getString("description")) : "";
                            String country_code = obj.has("country_code") ? checkStringValue(obj.getString("country_code")) : "";
                            boolean is_block = obj.has("is_block") ? checkBooleanValue(obj.getString("is_block")) : false;
                            listMod.add(new MemberInfo(ordering, id, name, profile_url, online_status, job_status, offline_on, description, country_code, is_block, "", null));
                        }
                    }
                    return new Response(errorCode, message, new MemberList(total_page, listMod));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    //thien
    private Response getAllAdvertisementList(String parseString) {
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = mainData.has("total_page") ? checkIntValue(mainData.getString("total_page")) : 0;
                    //advertisements
                    ArrayList<Advertisement> listAds = new ArrayList<>();
                    JSONArray advertisements = data.isNull("advertisements") ? null : data.getJSONArray("advertisements");
                    if (advertisements != null) {
                        int length = advertisements.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = advertisements.getJSONObject(i);
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String name = obj.has("name") ? checkStringValue(obj.getString("name")) : "";
                            String content = obj.has("content") ? checkStringValue(obj.getString("content")) : "";
                            String url = obj.has("url") ? checkStringValue(obj.getString("url")) : "";
                            JSONArray images = data.isNull("images") ? null : data.getJSONArray("images");
                            ArrayList<Image> listImg = new ArrayList<>();
                            if (images != null) {
                                int lengthImg = images.length();
                                for (int j = 0; j < lengthImg; j++) {
                                    JSONObject obj2 = images.getJSONObject(j);
                                    String urlImg = obj2.has("url") ? checkStringValue(obj2.getString("url")) : "";
                                    String type = obj2.has("type") ? checkStringValue(obj2.getString("type")) : "";
                                    listImg.add(new Image(urlImg, type));
                                }
                            }
                            listAds.add(new Advertisement(id, name, content, url, listImg));
                        }
                    }
                    return new Response(errorCode, message, new ListAdvertisement(total_page, listAds));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private Double getDoubleValue(String parseString, String objectKey) {
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                return checkDoubleValue(object.getString(objectKey));
            } catch (JSONException e) {
                e.printStackTrace();
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

    private String getStringValue(String parseString, String objectKey) {
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                return checkStringValue(object.getString(objectKey));
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    private static String checkStringValue(String sSource) {
        String sRetVal = sSource;
        if (sSource.equalsIgnoreCase("null")) {
            sRetVal = "";
        }
        return sRetVal;
    }

    private JSONObject getJsonObject(String jsonString,JSONObject jsonObject) {
        JSONObject jsonObjectResult=null;
        try{
            jsonObjectResult=jsonObject.getJSONObject(jsonString);

        }catch (Exception ex){

        }
        return  jsonObjectResult;
    }

    private JSONArray getJsonArray(String jsonString,JSONObject jsonObject) {
        JSONArray jsonArray=null;
        try{
            jsonArray=jsonObject.getJSONArray(jsonString);

        }catch (Exception ex){

        }
        return  jsonArray;
    }

    private String  getJsonString(String jsonString,JSONObject jsonObject) {
        String result="";
        try{
            result=jsonObject.isNull(jsonString) ? "" : jsonObject.getString(jsonString);
        }catch (Exception ex){
        }
        return  result;
    }

    private double  getJsonDouble(String jsonString,JSONObject jsonObject) {
        double result=0.0;
        try{
            result=jsonObject.getDouble(jsonString);

        }catch (Exception ex){
        }
        return  result;
    }

    private int  getJsonInt(String jsonString,JSONObject jsonObject) {
        int result=0;
        try{
            result=jsonObject.getInt(jsonString);
        }catch (Exception ex){
        }
        return  result;
    }
    private boolean  getJsonBoolean(String jsonString,JSONObject jsonObject) {
        boolean result=false;
        try{
            result=jsonObject.getBoolean(jsonString);
        }catch (Exception ex){
        }
        return  result;
    }

    // xml

    private static Boolean checkBooleanValue(String sSource) {
        Boolean sRetVal = true;
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("false")) {
            sRetVal = false;
        }
        return sRetVal;
    }

    private static int checkIntValue(String sSource) {
        int sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("0.0")) {
            sRetVal = 0;
        } else {
            sRetVal = Integer.parseInt(sSource);
        }
        return sRetVal;
    }

    private long checkLongValue(String sSource) {
        long sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("0.0")) {
            sRetVal = 0;
        } else {
            sRetVal = Long.parseLong(sSource);
        }
        return sRetVal;
    }

    private double checkDoubleValue(String sSource) {
        double sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")) {
            sRetVal = 0;
        } else {
            sRetVal = Double.parseDouble(sSource);
        }
        return sRetVal;
    }

    private float checkFloatValue(String sSource) {
        float sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")) {
            sRetVal = 0;
        } else {
            sRetVal = Float.parseFloat(sSource);
        }
        return sRetVal;
    }

}
