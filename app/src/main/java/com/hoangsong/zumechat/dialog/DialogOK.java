package com.hoangsong.zumechat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;

public class DialogOK extends Dialog {
	
	private Button btnOK;
	
	public DialogOK(Context context,String message,String title) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		setCancelable(false);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
		TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);

		lblMessage.setTypeface(Utils.getFontTahoma(context));
		lblTitle.setTypeface(Utils.getFontTahoma(context));

		lblMessage.setText(Html.fromHtml(message));
		//tvDetailDecription.setText(Html.fromHtml(Html.fromHtml((String) foodDetail.getProduct_description().toString()).toString()));
		if(!title.equals("")){
			lblTitle.setText(title);
			Utils.getChangeFont(lblTitle, context, Utils.FontStyle.BOLD.getVal());
		}
		
		btnOK=(Button)this.findViewById(R.id.btnOk);
		btnOK.setTypeface(Utils.getFontTahoma(context));
		btnOK.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public DialogOK(Context context,String message,String title, int gravity) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		setCancelable(false);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
		TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);

		lblMessage.setTypeface(Utils.getFontTahoma(context));
		lblTitle.setTypeface(Utils.getFontTahoma(context));

		lblMessage.setGravity(Gravity.LEFT);
		lblMessage.setText(Html.fromHtml(message));
		//tvDetailDecription.setText(Html.fromHtml(Html.fromHtml((String) foodDetail.getProduct_description().toString()).toString()));
		if(!title.equals("")){
			lblTitle.setText(title);
			Utils.getChangeFont(lblTitle, context, Utils.FontStyle.BOLD.getVal());
		}

		btnOK=(Button)this.findViewById(R.id.btnOk);
		btnOK.setTypeface(Utils.getFontTahoma(context));
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public DialogOK(Context context, String message, String title, final PopupCallback popupCallback, final int processID) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		setCancelable(false);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
		TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);

		lblMessage.setTypeface(Utils.getFontTahoma(context));
		lblTitle.setTypeface(Utils.getFontTahoma(context));

		lblMessage.setText(Html.fromHtml(message), TextView.BufferType.SPANNABLE);
		if(!title.equals("")){
			lblTitle.setText(title);
		}

		btnOK=(Button)this.findViewById(R.id.btnOk);
		btnOK.setTypeface(Utils.getFontTahoma(context));
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupCallback.popUpCallback("OK",processID,null,0,0);
				dismiss();
			}
		});
	}
	
	
}
