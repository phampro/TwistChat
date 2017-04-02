package com.hoangsong.zumechat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 13/10/2016.
 */

public class RegisterActivity extends AppCompatActivity implements JsonCallback {

    private RelativeLayout vPaddingActionBar;
    //public PopupCallback popupCallback;
    private ImageButton ibtnBack;
    private Button btnRegister;
    private TextView tvTitle, tvHinLabelEmail, tvHinLabelUsername, tvHinLabelCountry, tvHinLabelPassword, tvHinLabelRetypePassword;
    private EditText txtEmail, txtUsername, txtCountry, txtPassword, txtRetypePassword;
    private String countryCode ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_register);

        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvHinLabelUsername = (TextView) findViewById(R.id.tvHinLabelUsername);
        tvHinLabelCountry = (TextView) findViewById(R.id.tvHinLabelCountry);
        tvHinLabelEmail = (TextView) findViewById(R.id.tvHinLabelEmail);
        tvHinLabelPassword = (TextView) findViewById(R.id.tvHinLabelPassword);
        tvHinLabelRetypePassword = (TextView) findViewById(R.id.tvHinLabelRetypePassword);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtCountry = (EditText) findViewById(R.id.txtCountry);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRetypePassword = (EditText) findViewById(R.id.txtRetypePassword);

        Utils.textChange(txtUsername, tvHinLabelUsername);
        Utils.textChange(txtEmail, tvHinLabelEmail);
        Utils.textChange(txtCountry, tvHinLabelCountry);
        Utils.textChange(txtPassword, tvHinLabelPassword);
        Utils.textChange(txtRetypePassword, tvHinLabelRetypePassword);

        btnRegister.setTypeface(Utils.getFontLight(this));

        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, this);
        tvTitle.setText(getString(R.string.title_register));

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtRetypePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO){
                    btnRegister.performClick();
                }
                return false;
            }
        });

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCountryCode();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();
                String rePass = txtRetypePassword.getText().toString();
                if(!Utils.isValidString(userName)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_enter_username));
                }else if(!Utils.isValidString(email)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_enter_mail));
                }else if(!Utils.isValidEmail(email)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_mail_invalid));
                }else if(!Utils.isValidString(countryCode)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_choose_country));
                }else if(!Utils.isValidString(pass)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_enter_password));
                }else if(!rePass.equals(pass)){
                    Utils.showSimpleDialogAlert(RegisterActivity.this, getString(R.string.msg_error_enter_password_not_match));
                }else {
                    Prefs.setEmail(userName);
                    JSONObject objectUser = new JSONObject();
                    try {
                        objectUser.put("username", userName);
                        objectUser.put("email", email);
                        objectUser.put("password", pass);
                        objectUser.put("country_code", countryCode);
                        objectUser.put("push_notification_token", Prefs.getDeviceID());
                        objectUser.put("device_type", Constants.API_DEVICE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String postData = objectUser.toString();
                    if(Constants.DEBUG_MODE){
                        Log.e("postData", "postData "+postData);
                    }
                    new DownloadAsyncTask(RegisterActivity.this, Constants.REGISTER, Constants.ID_METHOD_REGISTER, RegisterActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);
                }
            }
        });


    }

    private void selectCountryCode() {
        final CountryPicker picker = CountryPicker.newInstance(getString(R.string.title_select_country));
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                countryCode = code;
//                ivFlag.setBackground(getResources().getDrawable(flagDrawableResID));
//                countryCode = Integer.parseInt(dialCode.substring(1,dialCode.length()));
                txtCountry.setText(name);
                picker.dismiss();
            }
        });
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_REGISTER){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    Prefs.setUserInfo((AccountInfo) response.getData());
                    sendBookingEvent(Constants.PUSH_RELOAD_MENU);
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

    /*@Override
    public void dismiss() {
        popupCallback.popUpCallback(null, Constants.ID_POPUP_CONFIRM_OK, null, 0, 0);
        super.dismiss();
    }*/

    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }
}