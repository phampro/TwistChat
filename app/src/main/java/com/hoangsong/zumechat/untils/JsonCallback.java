package com.hoangsong.zumechat.untils;

public interface JsonCallback {
	public void jsonCallback(Object data, int processID, int index);
	public void jsonError(String msg, int processID);
}
