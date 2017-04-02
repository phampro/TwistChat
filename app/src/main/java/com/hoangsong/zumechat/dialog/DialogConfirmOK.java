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


public class DialogConfirmOK extends Dialog {
	
	public PopupCallback popupCallback;
	private Button btnOK;
	
	public DialogConfirmOK(Context context,String message,String title, final int num, final PopupCallback popupCallback) {
		super(context);
		this.popupCallback = popupCallback;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		setCancelable(false);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
		TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);

		lblMessage.setTypeface(Utils.getFontTahoma(context));
		lblTitle.setTypeface(Utils.getFontTahoma(context));

		lblMessage.setText(message);
		if(!title.equals("")){
			lblTitle.setText(title.toUpperCase());
		}
		
		btnOK=(Button)this.findViewById(R.id.btnOk);
		btnOK.setTypeface(Utils.getFontTahoma(context));
		btnOK.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();
				popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_OK, null, num, 0);
			}
		});
	}
	
	
}
