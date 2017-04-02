package com.hoangsong.zumechat.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.adapters.ListPopupDataAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.dialog.DialogConfirmOK;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.MasterData;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tang on 10/10/2016.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, EndlessListView.EndlessListener, JsonCallback, PopupCallback {
    private RelativeLayout vPaddingActionBar;
    private ScrollView swSetting;
    private TextView tvTitle, tvNotification, tvBlockedUser, tvLanguage, tvOther, tvLabelLanguage, tvTermOfUse, tvPrivacyPolicy, tvContactUs, tvDeleteAccount;
    private LinearLayout llNotification, llOther, llDeleteAccount;
    private SwitchCompat swSound, swVibration;
    private ImageButton ibtnBack;
    public String[] arrMenu;
    //language
    Locale myLocale;
    private boolean isChangeLanguage;

    //block user
    private int page_size = 20;
    private String token = "";
    private EndlessListView lvMember;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;
    private int page = 1;
    private int pageError = -1;
    boolean firstLoad = true;
    private int current_position = 0;
    private int POPUP_UNBLOCK = 999;
    private int POPUP_DELETE_ACCOUNT = 998;

    //Terms and Privacy
    private WebView wvTermsPrivacy;
    private MasterData.AppContent appContent = null;

    //delete account
    private Button btnDelete;
    private EditText txtPassword;
    private TextView tvHinLabelPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_setting);
        pageInit();
    }

    private void pageInit(){
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        appContent = Prefs.getMasterData() != null ? Prefs.getMasterData().getApp_content() : null;
        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, this);
        arrMenu = new String[]{getString(R.string.lbl_english), getString(R.string.lbl_vietnamese)};
        swSetting = (ScrollView) findViewById(R.id.swSetting);
        tvNotification = (TextView) this.findViewById(R.id.tvNotification);
        tvBlockedUser = (TextView) this.findViewById(R.id.tvBlockedUser);
        tvLanguage = (TextView) this.findViewById(R.id.tvLanguage);
        tvOther = (TextView) this.findViewById(R.id.tvOther);
        tvLabelLanguage = (TextView) this.findViewById(R.id.tvLabelLanguage);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvTermOfUse = (TextView) this.findViewById(R.id.tvTermOfUse);
        tvPrivacyPolicy = (TextView) this.findViewById(R.id.tvPrivacyPolicy);
        tvContactUs = (TextView) this.findViewById(R.id.tvContactUs);
        tvDeleteAccount = (TextView) this.findViewById(R.id.tvDeleteAccount);
        ibtnBack = (ImageButton) this.findViewById(R.id.ibtnBack);
        llNotification = (LinearLayout) this.findViewById(R.id.llNotification);
        llOther = (LinearLayout) this.findViewById(R.id.llOther);
        llDeleteAccount = (LinearLayout) this.findViewById(R.id.llDeleteAccount);
        swSound = (SwitchCompat) findViewById(R.id.swSound);
        swVibration = (SwitchCompat) findViewById(R.id.swVibration);

        swSound.setTypeface(Utils.getFontLight(this));
        swVibration.setTypeface(Utils.getFontLight(this));
        tvTitle.setText(getString(R.string.menu_setting));

        tvLanguage.setOnClickListener(this);
        tvNotification.setOnClickListener(this);
        tvBlockedUser.setOnClickListener(this);
        tvOther.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);
        tvTermOfUse.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);
        tvContactUs.setOnClickListener(this);
        tvDeleteAccount.setOnClickListener(this);

        swSound.setChecked(Prefs.getSoundNotification());
        swVibration.setChecked(Prefs.getVibrationNotification());

        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Prefs.setSoundNotification(b);
            }
        });
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Prefs.setVibrationNotification(b);
            }
        });

        if(Prefs.getCurrentLanguage().equals(Constants.LANGUAGE_EN)){
            tvLanguage.setText(getString(R.string.lbl_english));
        }else {
            tvLanguage.setText(getString(R.string.lbl_vietnamese));
        }

        //blocked user
        lvMember = (EndlessListView) this.findViewById(R.id.lvMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        adp = new ListMemberAdapter(this, list_member, getString(R.string.tab_followed), SettingActivity.this);
        lvMember.setAdapter(adp);
        lvMember.reset(0, true);
        lvMember.setListener(this);

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_position = position;
                new DialogConfirm(SettingActivity.this, getString(R.string.app_name), getString(R.string.msg_unblock_account), POPUP_UNBLOCK, SettingActivity.this).show();
            }
        });

        //Terms and Privacy
        wvTermsPrivacy = (WebView) findViewById(R.id.wvTermsPrivacy);

        //delete account
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setTypeface(Utils.getFontLight(this));
        tvHinLabelPassword = (TextView) findViewById(R.id.tvHinLabelPassword);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        Utils.textChange(txtPassword, tvHinLabelPassword);

        btnDelete.setOnClickListener(this);

    }

    private void refresh(){
        arrMenu = new String[]{getString(R.string.lbl_english), getString(R.string.lbl_vietnamese)};
        tvTitle.setText(getString(R.string.menu_setting));
        tvOther.setText(getString(R.string.lbl_other));
        tvBlockedUser.setText(getString(R.string.lbl_blocked_user));
        tvNotification.setText(getString(R.string.lbl_notification_setting));
        tvLabelLanguage.setText(getString(R.string.lbl_language));
        if(Prefs.getCurrentLanguage().equals(Constants.LANGUAGE_EN)){
            tvLanguage.setText(getString(R.string.lbl_english));
        }else {
            tvLanguage.setText(getString(R.string.lbl_vietnamese));
        }
        tvTermOfUse.setText(getString(R.string.title_terms_of_use));
        tvPrivacyPolicy.setText(getString(R.string.title_privacy_policy));
        tvContactUs.setText(getString(R.string.menu_contact_us));
        tvDeleteAccount.setText(getString(R.string.lbl_delete_account));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvNotification:
                if(llNotification.getVisibility() == View.VISIBLE){
                    llNotification.setVisibility(View.GONE);
                    tvNotification.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right_small, 0);
                }else {
                    llNotification.setVisibility(View.VISIBLE);
                    tvNotification.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                }
                break;
            case R.id.tvBlockedUser:
                swSetting.setVisibility(View.GONE);
                llDeleteAccount.setVisibility(View.GONE);
                wvTermsPrivacy.setVisibility(View.GONE);
                lvMember.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.lbl_blocked_user));
                searchFriend(page);
                break;
            case R.id.tvLanguage:
                showListPopupWindow(tvLanguage);
                break;
            case R.id.tvOther:
                if(llOther.getVisibility() == View.VISIBLE){
                    llOther.setVisibility(View.GONE);
                    tvOther.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_right_small, 0);
                }else {
                    llOther.setVisibility(View.VISIBLE);
                    tvOther.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                }
                break;
            case R.id.ibtnBack:
                onBackPressed();
                break;
            case R.id.tvTermOfUse:
                swSetting.setVisibility(View.GONE);
                llDeleteAccount.setVisibility(View.GONE);
                lvMember.setVisibility(View.GONE);
                wvTermsPrivacy.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.title_terms_of_use));
                wvTermsPrivacy.setWebViewClient(new SettingActivity.WebViewController());
                wvTermsPrivacy.loadUrl(appContent != null ? appContent.getTerms_of_use() : "");
                wvTermsPrivacy.getSettings().setJavaScriptEnabled(false);
                wvTermsPrivacy.requestFocus();
                break;
            case R.id.tvPrivacyPolicy:
                swSetting.setVisibility(View.GONE);
                llDeleteAccount.setVisibility(View.GONE);
                lvMember.setVisibility(View.GONE);
                wvTermsPrivacy.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.title_privacy_policy));
                wvTermsPrivacy.setWebViewClient(new SettingActivity.WebViewController());
                wvTermsPrivacy.loadUrl(appContent != null ? appContent.getPrivate_policy() : "");
                wvTermsPrivacy.getSettings().setJavaScriptEnabled(false);
                wvTermsPrivacy.requestFocus();
                break;
            case R.id.tvContactUs:
                Utils.sendMail(SettingActivity.this, Constants.EMAIL_CONTACT, getString(R.string.app_name));
                break;
            case R.id.tvDeleteAccount:
                swSetting.setVisibility(View.GONE);
                wvTermsPrivacy.setVisibility(View.GONE);
                lvMember.setVisibility(View.GONE);
                llDeleteAccount.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.lbl_delete_account));
                break;
            case R.id.btnDelete:
                String pass = txtPassword.getText().toString();
                if(!Utils.isValidString(pass)){
                    Utils.showSimpleDialogAlert(SettingActivity.this, getString(R.string.msg_error_enter_password));
                }else
                    new DialogConfirm(SettingActivity.this, getString(R.string.app_name), getString(R.string.msg_delete_account), POPUP_DELETE_ACCOUNT, SettingActivity.this).show();
                break;
        }
    }
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        ZuMeChat.language = lang;
        Prefs.setCurrentLanguage(lang);
        /*Intent refresh = new Intent(this, SettingActivity.class);
        startActivity(refresh);
        finish();*/
        refresh();
    }

    public void showListPopupWindow(final TextView tvPushData) {
        try {
            final ListPopupWindow lpw = new ListPopupWindow(this);
            lpw.setAdapter(new ListPopupDataAdapter(this, arrMenu));
            lpw.setAnchorView(tvPushData);
            lpw.setModal(true);
            lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvPushData.setText(arrMenu[position]);
                    if(arrMenu[position].equals(getString(R.string.lbl_english))){
                        setLocale(Constants.LANGUAGE_EN);
                    }else {
                        setLocale(Constants.LANGUAGE_VI);
                    }
                    isChangeLanguage = true;
                    lpw.dismiss();

                }
            });
            lpw.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchFriend(int page_index){
        JSONObject obj = new JSONObject();
        try {
            obj.put("country_code", "");
            obj.put("type", "block");
            obj.put("latitude", 0.0);
            obj.put("longtitude", 0.0);
            obj.put("page_index", page_index);
            obj.put("page_size", page_size);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data searchFriend: "+obj.toString());
        new DownloadAsyncTask(this, Constants.SEARCH_FRIEND, Constants.ID_METHOD_SEARCH_FRIEND,
                SettingActivity.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }
    private void unBlock(String member_id){
        JSONObject obj = new JSONObject();
        try {
            obj.put("member_id", member_id);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(this, Constants.UNBLOCK_ACCOUNT, Constants.ID_METHOD_UNBLOCK_ACCOUNT,
                SettingActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }
    private void deleteAccount(String password){
        JSONObject obj = new JSONObject();
        try {
            obj.put("password", password);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(this, Constants.DELETE_ACCOUNT, Constants.ID_METHOD_DELETE_ACCOUNT,
                SettingActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    @Override
    public void loadData() {
        if (pageError > 0) {
            searchFriend(pageError);

        } else {
            page = page + 1;
            searchFriend(page);
        }
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(this, "", this.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(this, response.getMessage(), this.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    MemberList dataInfo = (MemberList) response.getData();
                    if (firstLoad) {
                        list_member.clear();
                        list_member.addAll(dataInfo.getFriends());
                        adp.notifyDataSetChanged();
                        firstLoad = false;
                        /*if(dataInfo.getTotal_page()>1){
                            isLoadMore = true;
                        }*/
                    } else {
                        list_member.addAll(dataInfo.getFriends());
                        adp.notifyDataSetChanged();
                        lvMember.hildeFooter();
                        pageError = -1;
                    }
                    Log.v("page num ", page + "----");
                    Log.v("size list ", list_member.size() + "----");
                    //Log.v("size adapter ", adp.getItemCount() + "----");
                }else{
                    if (pageError < 0) {
                        Utils.showSimpleDialogAlert(this, response.getMessage());
                    }
                    lvMember.reset(0, true);
                    pageError = page;
                }
            }else{
                if (pageError < 0) {
                    Utils.showSimpleDialogAlert(this, getString(R.string.alert_unexpected_error));
                }
                lvMember.reset(0, true);
                pageError = page;
            }
        }else if(processID == Constants.ID_METHOD_UNBLOCK_ACCOUNT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(this, "", getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(this, response.getMessage(), this.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    list_member.remove(current_position);
                    adp.notifyDataSetChanged();
                }else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(this, this.getString(R.string.alert_unexpected_error));
        }else if(processID == Constants.ID_METHOD_DELETE_ACCOUNT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(this, "", getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(this, response.getMessage(), this.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    Prefs.setUserInfo(null);
                    Intent in = new Intent(this, LogInActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(this, this.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if(processID == Constants.ID_METHOD_SEARCH_FRIEND){
            if (pageError < 0) {
                Utils.showSimpleDialogAlert(this, msg);
            }
            lvMember.reset(0, true);
            pageError = page;
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CONFIRM_YES){
            if(num == POPUP_UNBLOCK)
                unBlock(list_member.get(current_position).getId());
            else {
                deleteAccount(txtPassword.getText().toString());
            }
        }else if(processID == Constants.ID_POPUP_CONFIRM_OK){
            Intent refresh = new Intent(this, MainActivityPhone.class);
            startActivity(refresh);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(swSetting.getVisibility() == View.VISIBLE){
            if(isChangeLanguage){
                new DialogConfirmOK(this, getString(R.string.msg_update_change_language), getString(R.string.app_name), 0, this).show();
            }else
                super.onBackPressed();
        }else if(lvMember.getVisibility() == View.VISIBLE){
            lvMember.setVisibility(View.GONE);
            swSetting.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.menu_setting));
        }else if(wvTermsPrivacy.getVisibility() == View.VISIBLE){
            wvTermsPrivacy.setVisibility(View.GONE);
            swSetting.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.menu_setting));
        }else if(llDeleteAccount.getVisibility() == View.VISIBLE){
            llDeleteAccount.setVisibility(View.GONE);
            swSetting.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.menu_setting));
        }
    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Utils.showLoadingDialog(SettingActivity.this);
            if(Constants.DEBUG_MODE)
                Log.d("url", "url: "+url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Utils.hideLoadingDialog(SettingActivity.this);
            super.onPageFinished(view, url);
        }
    }


    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }

}