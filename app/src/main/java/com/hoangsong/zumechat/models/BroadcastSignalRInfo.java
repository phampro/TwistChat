package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by HOAI AN on 23/02/2017.
 */

public class BroadcastSignalRInfo {
    private int type;
    private int action;
    private ChatInfo chat;
    private ArrayList<MemberInfo> member_list;

    public BroadcastSignalRInfo(int type, int action, ChatInfo chat) {
        this.type = type;
        this.action = action;
        this.chat = chat;
    }

    public BroadcastSignalRInfo(int type, int action, ArrayList<MemberInfo> member_list) {
        this.type = type;
        this.action = action;
        this.member_list = member_list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public ChatInfo getChat() {
        return chat;
    }

    public void setChat(ChatInfo chat) {
        this.chat = chat;
    }

    public ArrayList<MemberInfo> getMember_list() {
        return member_list;
    }

    public void setMember_list(ArrayList<MemberInfo> member_list) {
        this.member_list = member_list;
    }
}
