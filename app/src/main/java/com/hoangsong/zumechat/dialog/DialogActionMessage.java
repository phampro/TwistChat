package com.hoangsong.zumechat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Encryption;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.MyDateTimeISO;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DialogActionMessage extends Dialog implements PopupCallback, View.OnClickListener, JsonCallback {
	public  PopupCallback popupCallback;
	private Context context;
	private String token = "";
	private String userid = "";
	private String received_id = "";
	private ImageButton ibtnBack, ibtnSave;
	private LinearLayout llEdit, llMenu;
	private EditText txtChatMessage;
	private ChatInfo chatInfo;
	//private int processID;
	private TextView tvCoppy, tvEdit, tvDelete, tvTitle;

	public DialogActionMessage(Context context, ChatInfo chatInfo,
							   String received_id,
							   /*String messageConfirm,
			String positiveButtonText,
			String negativeButtonText,*/
			final PopupCallback popupCallback) {
		super(context);
		this.context = context;
		this.popupCallback = popupCallback;
		this.chatInfo = chatInfo;
		this.received_id = received_id;
		//this.processID = processID;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_action_message);
		setCancelable(true);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tvCoppy =(TextView)this.findViewById(R.id.tvCoppy);
		tvEdit =(TextView)this.findViewById(R.id.tvEdit);
		tvDelete =(TextView)this.findViewById(R.id.tvDelete);
		tvTitle =(TextView)this.findViewById(R.id.tvTitle);
		txtChatMessage =(EditText) this.findViewById(R.id.txtChatMessage);
		llEdit = (LinearLayout) findViewById(R.id.llEdit);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		llEdit.setVisibility(View.GONE);

		ibtnBack =(ImageButton) this.findViewById(R.id.ibtnBack);
		ibtnSave =(ImageButton)this.findViewById(R.id.ibtnSave);

		ibtnBack.setOnClickListener(this);
		ibtnSave.setOnClickListener(this);
		tvCoppy.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
		tvDelete.setOnClickListener(this);
		token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
		this.userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";

		if(chatInfo != null){
			switch (chatInfo.getChat_type()){
				case Constants.CHAT_TYPE_PHOTO:
					tvCoppy.setText(context.getString(R.string.btn_save_image));
					tvEdit.setVisibility(View.GONE);
					if(!userid.equals(chatInfo.getSender_id())){
						tvDelete.setVisibility(View.GONE);
					}
					break;
				case Constants.CHAT_TYPE_HI:
					tvCoppy.setVisibility(View.GONE);
					tvEdit.setVisibility(View.GONE);
					break;
				default:
					if(!userid.equals(chatInfo.getSender_id())){
						tvDelete.setVisibility(View.GONE);
						tvEdit.setVisibility(View.GONE);
					}
					break;
			}
			if(!checkDateTime(chatInfo.getCreated_on()))
				tvEdit.setVisibility(View.GONE);

		}else {
			dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ibtnBack:
				dismiss();
				//popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_YES, null, 0, 0);
				break;
			case R.id.ibtnSave:
				if(chatInfo != null){
					if(txtChatMessage.getText().length()>0) {
						String msg = txtChatMessage.getText().toString().trim();
						for (int i = 0; i < Constants._MSG_BLACKLIST.length; i++) {
							msg = msg.replaceAll("(?i)" + Constants._MSG_BLACKLIST[i], "****");
						}
						msg = msg.replaceAll("\\w*\\*{4}", "****");
						JSONObject obj = new JSONObject();
						try {
							obj.put("id", chatInfo.getId());
							obj.put("receiver_id", received_id);
							obj.put("chat_type", Constants.CHAT_TYPE_TEXT);
							obj.put("chat_message", Encryption.encrypt(msg));
							obj.put("photo", "");
							obj.put("token", token);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(Constants.DEBUG_MODE)
							Log.d("post data", "post data: "+obj.toString());
						new DownloadAsyncTask(context, Constants.UPDATE_MESSAGE_CHAT, Constants.ID_METHOD_UPDATE_MESSAGE_CHAT,
								DialogActionMessage.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
					}
				}
				break;
			case R.id.tvCoppy:
				if(chatInfo!= null){
                    if(chatInfo.getChat_type().equals(Constants.CHAT_TYPE_PHOTO)){
						popupCallback.popUpCallback(chatInfo, Constants.ID_POPUP_SAVE_IMAGE, null, 0, 0);
						dismiss();
                    }else {
                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(chatInfo.getChat_message());
                            Toast.makeText(context, context.getString(R.string.lbl_coppy_clipboard), Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", chatInfo.getChat_message());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(context, context.getString(R.string.lbl_coppy_clipboard), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
				}else
					dismiss();
				break;
			case R.id.tvEdit:
				llMenu.setVisibility(View.GONE);
				llEdit.setVisibility(View.VISIBLE);
				if(chatInfo!= null)
					txtChatMessage.setText(chatInfo.getChat_messageDecrypt());
				break;
			case R.id.tvDelete:
				if(chatInfo!= null)
					new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_delete_message), 0, DialogActionMessage.this).show();
				break;
		}
	}

	private boolean checkDateTime(String strDateTime){
		if(strDateTime.equals("")){
			return false;
		}
		long minute = MyDateTimeISO.between2StringDate( strDateTime, MyDateTimeISO.getFormatDateISOsystem(), "m");
		//Toast.makeText(context, "minute: "+minute, Toast.LENGTH_LONG).show();
		if(MyDateTimeISO.daysBetween2Dates( strDateTime, MyDateTimeISO.getFormatDateISOsystem()) < 0){
			return false;
		}else if(minute < 0 || minute > 30){
			return false;
		}
		return true;
	}

	private void delete(){
		JSONObject obj = new JSONObject();
		try {
			JSONArray chat_ids = new JSONArray();
			JSONObject id = new JSONObject();
			id.put("id", chatInfo.getId());
			chat_ids.put(id);
			obj.put("chat_ids", chat_ids);
			obj.put("token", token);
			obj.put("receiver_id", received_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(Constants.DEBUG_MODE)
			Log.d("post data", "post data: "+obj.toString());
		new DownloadAsyncTask(context, Constants.DELETE_MESSAGE_CHAT, Constants.ID_METHOD_DELETE_MESSAGE_CHAT,
				DialogActionMessage.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
	}

	@Override
	public void popUpCallback(Object data, int processID, Object obj, int num,
			int index) {
		if(processID == Constants.ID_POPUP_CONFIRM_YES){
			delete();
		}else  if(processID == Constants.ID_POPUP_CONFIRM_NO){
			dismiss();
		}
	}

	@Override
	public void jsonCallback(Object data, int processID, int index) {
		if(processID == Constants.ID_METHOD_UPDATE_MESSAGE_CHAT){
			if(data != null){
				Response response = (Response) data;
				if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
					new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
				}else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
					new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
				}else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
					if(response.getData() != null){
						chatInfo = (ChatInfo) response.getData();
						//popupCallback.popUpCallback(Constants.ID_POPUP_CONFIRM_NO, processID, null, 0, 0);
						popupCallback.popUpCallback(chatInfo, Constants.ID_POPUP_CHAT_UPDATE_MESSAGE, null, Constants.ACTION_UPDATE_MESSAGE, 0);
						dismiss();
					}
				}else {
					Utils.showSimpleDialogAlert(context, response.getMessage());
				}
			}else
				Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
		}else if(processID == Constants.ID_METHOD_DELETE_MESSAGE_CHAT){
			if(data != null){
				Response response = (Response) data;
				if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
					new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
				}else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
					new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
				}else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
					popupCallback.popUpCallback(chatInfo, Constants.ID_POPUP_CHAT_UPDATE_MESSAGE, null, Constants.ACTION_DELETE_MESSAGE, 0);
					dismiss();
				}else {
					Utils.showSimpleDialogAlert(context, response.getMessage());
				}
			}else
				Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
		}
	}

	@Override
	public void jsonError(String msg, int processID) {
		Utils.showSimpleDialogAlert(context, msg);
	}
}
