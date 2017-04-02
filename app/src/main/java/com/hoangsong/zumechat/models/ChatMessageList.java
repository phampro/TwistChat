package com.hoangsong.zumechat.models;

import java.util.ArrayList;

public class ChatMessageList {
	private int maxPage;
	private ArrayList<ChatInfo> listChat;
	
	public ChatMessageList(int maxPage,
			ArrayList<ChatInfo> listChat) {
		super();
		this.maxPage = maxPage;
		this.listChat = listChat;
	}
	
	
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	public ArrayList<ChatInfo> getListChat() {
		return listChat;
	}
	public void setListChat(ArrayList<ChatInfo> listChat) {
		this.listChat = listChat;
	}

}
