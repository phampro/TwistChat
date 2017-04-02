package com.hoangsong.zumechat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;


public class DialogConfirm extends Dialog implements PopupCallback {
	public  PopupCallback popupCallback;
	private Button btnOK;
	private Button btnCancel;
	//private int processID;
	
	public DialogConfirm(Context context, 
			String messageTitle,
			String messageConfirm,
			/*String positiveButtonText, 
			String negativeButtonText,*/
			final int num,
			final PopupCallback popupCallback) {
		super(context);
		this.popupCallback = popupCallback;
		//this.processID = processID;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_confirm);
		setCancelable(false);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
		TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);

		lblMessage.setTypeface(Utils.getFontTahoma(context));
		lblTitle.setTypeface(Utils.getFontTahoma(context));

		if(messageTitle.equalsIgnoreCase("")){
			lblTitle.setVisibility(View.GONE);
		}
		else{
			lblTitle.setText(messageTitle.toUpperCase());
		}
		
		btnCancel =(Button)this.findViewById(R.id.btnCancel);
		btnOK =(Button)this.findViewById(R.id.btnOk);
		
		lblMessage.setText(messageConfirm);
		
		btnCancel.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();
				//popupCallback.popUpCallback(Constants.ID_POPUP_CONFIRM_NO, processID, null, 0, 0);
				popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_NO, null, num, 0);
			}
		});
		btnOK.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();
				//popupCallback.popUpCallback(Constants.ID_POPUP_CONFIRM_YES, processID, null, 0, 0);
				popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_YES, null, num, 0);
			}
		});

		btnCancel.setTypeface(Utils.getFontTahoma(context));
		btnOK.setTypeface(Utils.getFontTahoma(context));
		
	}


	@Override
	public void popUpCallback(Object data, int processID, Object obj, int num,
			int index) {
		
	}
	
	
}
