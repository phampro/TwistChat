package com.hoangsong.zumechat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogForgetPassword;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 13/10/2016.
 */

public class LogInActivity extends Activity implements JsonCallback {

    //private RelativeLayout vPaddingActionBar;
    public PopupCallback popupCallback;
    //private ImageButton ibtnBack;
    private Button btnLogin;
    private TextView tvHinLabelEmail, tvHinLabelPassword, tvRegister, tvForgetPass;
    private EditText txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_login);

        //ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvHinLabelEmail = (TextView) findViewById(R.id.tvHinLabelEmail);
        tvHinLabelPassword = (TextView) findViewById(R.id.tvHinLabelPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvForgetPass = (TextView) findViewById(R.id.tvForgetPass);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Utils.textChange(txtEmail, tvHinLabelEmail);
        Utils.textChange(txtPassword, tvHinLabelPassword);
        btnLogin.setTypeface(Utils.getFontLight(this));

        //vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        //Utils.setViewPaddingStatusBar(vPaddingActionBar, this);

        txtEmail.setText(Prefs.getEmail());

        /*ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));

            }
        });

        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO){
                    btnLogin.performClick();
                }
                return false;
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogForgetPassword(LogInActivity.this).show();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                if(!Utils.isValidString(userName)){
                    Utils.showSimpleDialogAlert(LogInActivity.this, getString(R.string.msg_error_enter_username));
                }else if(!Utils.isValidString(pass)){
                    Utils.showSimpleDialogAlert(LogInActivity.this, getString(R.string.msg_error_enter_password));
                }else {
                    Prefs.setEmail(userName);
                    JSONObject objectUser = new JSONObject();
                    try {
                        objectUser.put("email", userName);
                        objectUser.put("password", pass);
                        objectUser.put("push_notification_token", Prefs.getDeviceID());
                        objectUser.put("device_type", Constants.API_DEVICE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String postData = objectUser.toString();
                    if(Constants.DEBUG_MODE){
                        Log.e("postData", "postData"+postData);
                    }
                    new DownloadAsyncTask(LogInActivity.this, Constants.LOGIN, Constants.ID_METHOD_LOGIN, LogInActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);
                }
            }
        });
    }



    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_LOGIN){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    Prefs.setUserInfo((AccountInfo) response.getData());
                    sendBookingEvent(Constants.PUSH_RELOAD_MENU);
                    startActivity(new Intent(this, MainActivityPhone.class));
                    finish();
                }else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }else {
                Utils.showSimpleDialogAlert(this, this.getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(this, msg);
    }

}
