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

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Tang on 24/06/2016.
 */
public class DialogInvalidToken extends Dialog {

    private Button btnOK;

    public DialogInvalidToken(Context context, String message, String title) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_message);
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
        TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);


        lblMessage.setText(message);
        if(!title.equals("")){
            lblTitle.setText(title);
            Utils.getChangeFont(lblTitle, context, Utils.FontStyle.BOLD.getVal());
        }

        btnOK=(Button)this.findViewById(R.id.btnOk);
        Utils.getChangeFont(btnOK, context, Utils.FontStyle.BOLD.getVal());
        Utils.getChangeFont(lblMessage, context, Utils.FontStyle.LIGHT.getVal());
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                sendBookingEvent(Constants.PUSH_TOKEN_INVALID);
            }
        });
    }

    public DialogInvalidToken(Context context, String message, String title, final PopupCallback popupCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_message);
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView lblMessage =(TextView)this.findViewById(R.id.lblMessage);
        TextView lblTitle =(TextView)this.findViewById(R.id.lblTitle);


        lblMessage.setText(message);
        if(!title.equals("")){
            lblTitle.setText(title);
            Utils.getChangeFont(lblTitle, context, Utils.FontStyle.BOLD.getVal());
        }

        btnOK=(Button)this.findViewById(R.id.btnOk);
        Utils.getChangeFont(btnOK, context, Utils.FontStyle.BOLD.getVal());
        Utils.getChangeFont(lblMessage, context, Utils.FontStyle.LIGHT.getVal());
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                sendBookingEvent(Constants.PUSH_TOKEN_INVALID);
                popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_OK, null, 0, 0);
            }
        });
    }

    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }
}
