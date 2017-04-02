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
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DialogActionMember extends Dialog implements PopupCallback, View.OnClickListener {
	public  PopupCallback popupCallback;
	private Context context;
	private String token = "";
	private String userid = "";
	private LinearLayout llEdit, llMenu;
	private MemberInfo memberInfo;
	private int num;
	private TextView tvCoppy, tvEdit, tvDelete, tvTitle;

	public DialogActionMember(Context context, MemberInfo memberInfo, int num,
							   /*String messageConfirm,
			String positiveButtonText,
			String negativeButtonText,*/
			final PopupCallback popupCallback) {
		super(context);
		this.context = context;
		this.popupCallback = popupCallback;
		this.memberInfo = memberInfo;
		this.num = num;
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
		llEdit = (LinearLayout) findViewById(R.id.llEdit);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		llEdit.setVisibility(View.GONE);
		tvEdit.setVisibility(View.GONE);

		tvCoppy.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
		tvDelete.setOnClickListener(this);
		token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
		this.userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";

		if(memberInfo != null){
			if(memberInfo.is_block()){
				tvCoppy.setText(context.getString(R.string.btn_unblock));
			}else {
				tvCoppy.setText(context.getString(R.string.btn_block));
			}
			if(num == -999){
				tvDelete.setText(context.getString(R.string.btn_remove_favourite));
			}
		}else {
			dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tvCoppy:
				if(memberInfo!= null && popupCallback != null){
					popupCallback.popUpCallback(memberInfo, Constants.ID_POPUP_ACTION_MEMBER_BLOCK, null, num, 0);
					dismiss();
				}else
					dismiss();
				break;
			case R.id.tvDelete:
				if(memberInfo!= null && popupCallback != null){
					popupCallback.popUpCallback(memberInfo, Constants.ID_POPUP_ACTION_MEMBER_DELETE, null, num, 0);
					dismiss();
				}else
					dismiss();
				break;
		}
	}

	@Override
	public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
	}
}
