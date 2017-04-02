package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 17/10/2016.
 */

public class DialogChangePassword  extends Dialog implements JsonCallback {

    private RelativeLayout vPaddingActionBar;
    private Context context;
    private ImageButton ibtnBack;
    private Button btnChangePass;
    private TextView tvHinLabelRetypePassword, tvHinLabelPassword, tvTitle;
    private EditText txtRetypePassword, txtPassword;

    public DialogChangePassword(final Context context/*, PopupCallback popupCallback, String message*/) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.dialog_change_pasword);
        setCancelable(true);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        btnChangePass = (Button) findViewById(R.id.btnChangePass);
        tvHinLabelRetypePassword = (TextView) findViewById(R.id.tvHinLabelRetypePassword);
        tvHinLabelPassword = (TextView) findViewById(R.id.tvHinLabelPassword);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        txtRetypePassword = (EditText) findViewById(R.id.txtRetypePassword);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        tvTitle.setText("Thay đổi mật khẩu!");

        Utils.textChange(txtRetypePassword, tvHinLabelRetypePassword);
        Utils.textChange(txtPassword, tvHinLabelPassword);

        tvHinLabelRetypePassword.setTypeface(Utils.getFontTahoma(context));
        tvHinLabelPassword.setTypeface(Utils.getFontTahoma(context));
        txtRetypePassword.setTypeface(Utils.getFontTahoma(context));
        txtPassword.setTypeface(Utils.getFontTahoma(context));
        btnChangePass.setTypeface(Utils.getFontTahoma(context));

        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, context);

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txtRetypePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO){
                    btnChangePass.performClick();
                }
                return false;
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rePass = txtRetypePassword.getText().toString();
                String pass = txtPassword.getText().toString();
                if(!Utils.isValidString(pass)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_enter_password));
                }else if(!rePass.equals(pass)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_enter_password_not_match));
                }else {
                    JSONObject objectUser = new JSONObject();
                    try {
                        objectUser.put("api_token", Prefs.getUserInfo().getToken());
                        objectUser.put("password", pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String postData = objectUser.toString();
                    new DownloadAsyncTask(context, Constants.CHANGE_PASSWORD, Constants.ID_METHOD_CHANGE_PASSWORD, DialogChangePassword.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);
                }
            }
        });

    }

    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_CHANGE_PASSWORD){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    Prefs.setUserInfo(null);
                    sendBookingEvent(Constants.PUSH_RELOAD_MENU_AND_CHANGE_PASS);
                    dismiss();
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else {
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }
}
