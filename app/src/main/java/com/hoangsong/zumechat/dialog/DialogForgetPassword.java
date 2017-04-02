package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 17/10/2016.
 */

public class DialogForgetPassword  extends Dialog implements JsonCallback {
    private Context context;
    ImageButton ibtnClosePopup;
    private Button btnSubmit;
    private TextView tvHinLabelEmail, tvTitlePopup, tvSubTitlePopup;
    private EditText txtEmail;

    public DialogForgetPassword(Context context_) {
        super(context_);
        this.context = context_;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.dialog_forget_password);
        setCancelable(true);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);

        ibtnClosePopup = (ImageButton)findViewById(R.id.ibtnClosePopup);
        ibtnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvHinLabelEmail = (TextView) findViewById(R.id.tvHinLabelEmail);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvTitlePopup = (TextView) findViewById(R.id.tvTitlePopup);
        tvSubTitlePopup = (TextView) findViewById(R.id.tvSubTitlePopup);

        Utils.textChange(txtEmail, tvHinLabelEmail);

        txtEmail.setTypeface(Utils.getFontRegular(context_));
        tvHinLabelEmail.setTypeface(Utils.getFontBold(context_));
        btnSubmit.setTypeface(Utils.getFontRegular(context_));
        tvTitlePopup.setTypeface(Utils.getFontBold(context_));
        tvSubTitlePopup.setTypeface(Utils.getFontRegular(context_));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                if(!Utils.isValidString(email)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_enter_mail));
                }else if(!Utils.isValidEmail(email)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_mail_invalid));
                }else{
                    JSONObject objectUser = new JSONObject();
                    try {
                        objectUser.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String postData = objectUser.toString();
                    new DownloadAsyncTask(context, Constants.FORGOT_PASSWORD, Constants.ID_METHOD_FORGOT_PASSWORD, DialogForgetPassword.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);
                }
            }
        });

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_FORGOT_PASSWORD){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == 0){
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }else{
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else{
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }
}
