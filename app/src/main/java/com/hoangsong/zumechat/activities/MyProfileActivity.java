package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.plus.PlusShare;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.dialog.DialogViewPhoto;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.UtilCountry;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.mukesh.countrypicker.models.Country;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class MyProfileActivity extends AppCompatActivity implements JsonCallback, View.OnClickListener, PopupCallback {
    private ImageView ivAvatar, ivBackground, ivStatus, ivCountry;
    private TextView tvEdit, tvNickName, tvStatus, tvGender, tvCountry, tvTotalFavorite, tvStatusOnline, tvShareGoogle, tvShareFacebook, tvAddContact, tvHi, tvTitle;
    private Context context;
    private ImageButton ibtnBack;
    private final int REFRESH_CODE = 1;
    private LinearLayout llGuest, llNormal;
    private AccountInfo accountInfo;
    public String[] arrMenu;
    private boolean is_check = true;
    private String receiver_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initUI();
    }

    private void initUI() {
        context = MyProfileActivity.this;
        arrMenu = new String[]{Constants.TYPE_STATUS_ONLINE, Constants.TYPE_STATUS_BUSY, Constants.TYPE_STATUS_OFFLINE};
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);
        ivCountry = (ImageView) findViewById(R.id.ivCountry);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvTotalFavorite = (TextView) findViewById(R.id.tvTotalFavorite);
        tvStatusOnline = (TextView) findViewById(R.id.tvStatusOnline);
        tvShareGoogle = (TextView) findViewById(R.id.tvShareGoogle);
        tvShareFacebook = (TextView) findViewById(R.id.tvShareFacebook);
        tvAddContact = (TextView) findViewById(R.id.tvAddContact);
        tvHi = (TextView) findViewById(R.id.tvHi);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);

        llGuest = (LinearLayout) findViewById(R.id.llGuest);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);

        tvShareGoogle.setOnClickListener(this);
        tvShareFacebook.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvStatusOnline.setOnClickListener(this);
        tvAddContact.setOnClickListener(this);
        tvHi.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        ivBackground.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            receiver_id = bundle.getString("receiver_id");
            getContactProfile(receiver_id);
            llNormal.setVisibility(View.GONE);
            llGuest.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.GONE);
            tvStatusOnline.setEnabled(false);
            tvStatusOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            getProfile();
            llNormal.setVisibility(View.VISIBLE);
            llGuest.setVisibility(View.GONE);
        }

        tvStatusOnline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(is_check){
                    switch (editable.toString().toLowerCase()) {
                        case Constants.TYPE_STATUS_ONLINE:
                            updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                            break;
                        case Constants.TYPE_STATUS_OFFLINE:
                            updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                            break;
                        case Constants.TYPE_STATUS_BUSY:
                            updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                            break;
                        default:
                            break;
                    }
                }
            }
        });

    }

    private void getProfile() {
        new DownloadAsyncTask(context, Constants.GET_PROFILE + "?token=" + Prefs.getUserInfo().getToken(), Constants.ID_METHOD_GET_PROFILE, MyProfileActivity.this, true,
                DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    private void getContactProfile(String receiver_id) {
        new DownloadAsyncTask(context, Constants.GET_CONTACT_PROFILE + "?token=" + Prefs.getUserInfo().getToken()+"&receiver_id="+receiver_id, Constants.ID_METHOD_GET_CONTACT_PROFILE, MyProfileActivity.this, true,
                DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    private void setDataForm() {
        if (accountInfo != null) {
            String urlAvatar = accountInfo.getProfile_url();
            String urlBackground = accountInfo.getBackground_url();
            if (!urlAvatar.equals("")) {
                Picasso.with(context).load(urlAvatar+"&timestamp="+new Random().nextInt(123456)).placeholder(R.drawable.ic_profile_normal).fit().centerCrop().transform(new CircleTransform()).into(ivAvatar);
            }
            if (!urlBackground.equals("")) {
                Picasso.with(context).load(urlBackground+"&timestamp="+new Random().nextInt(123456)).fit().centerCrop().into(ivBackground);
            }
            tvNickName.setText(accountInfo.getUsername());
            tvStatus.setText(accountInfo.getDescription().equals("") ? getString(R.string.lbl_hi) : accountInfo.getDescription());
            tvGender.setText(accountInfo.getGender());
            tvCountry.setText(accountInfo.getCountry());
            Country country = UtilCountry.getCountry(accountInfo.getCountry_code());
            if (country != null) {
                ivCountry.setBackgroundResource(UtilCountry.getFlagResId(this, accountInfo.getCountry_code()));
            }
            tvTotalFavorite.setText(accountInfo.getTotal_favorites() + "");
            is_check = false;
            tvStatusOnline.setText(accountInfo.getJob_status().substring(0, 1).toUpperCase() + accountInfo.getJob_status().substring(1, accountInfo.getJob_status().length()));
            is_check = true;
            tvTitle.setText(getString(R.string.app_name));


            if(accountInfo.is_favorites()){
                tvAddContact.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_yellow, 0, 0);
            }

            updateStatus();
        }
    }

    private void updateStatus() {
        String status = accountInfo.getJob_status();
        switch (status){
            case Constants.TYPE_STATUS_ONLINE:
                ivStatus.setImageResource(R.drawable.bg_dot_green);
                break;
            case Constants.TYPE_STATUS_BUSY:
                ivStatus.setImageResource(R.drawable.bg_dot_red);
                break;
            case Constants.TYPE_STATUS_OFFLINE:
                ivStatus.setImageResource(R.drawable.bg_dot_gay);
                break;
        }
    }

    private void shereWithFacebook() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, Constants.URL_SHARE_APP);
        intent.setType("text/plain");

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().contains("facebook")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    private void shereWithGooglePlus() {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(getString(R.string.app_name))
                .setContentUrl(Uri.parse(Constants.URL_SHARE_APP))
                .getIntent();
        startActivity(shareIntent);
    }

    private void updateJobStatus(String status) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("job_status", status);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.UPDATE_JOB_STATUS, Constants.ID_METHOD_UPDATE_JOB_STATUS, MyProfileActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFavourite() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", accountInfo.getId());
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.ADD_FAVOURITE, Constants.ID_METHOD_ADD_FAVOURITE, MyProfileActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFavourite() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", accountInfo.getId());
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.REMOVE_FAVOURITE, Constants.ID_METHOD_REMOVE_FAVOURITE, MyProfileActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (data != null) {
            if (processID == Constants.ID_METHOD_GET_PROFILE || processID == Constants.ID_METHOD_GET_CONTACT_PROFILE) {
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    accountInfo = (AccountInfo) response.getData();
                    setDataForm();
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }

            } else if (processID == Constants.ID_METHOD_UPDATE_JOB_STATUS) {
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    accountInfo = (AccountInfo) response.getData();
                    Prefs.setUserInfo(accountInfo);
                    updateStatus();
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            } else if (processID == Constants.ID_METHOD_ADD_FAVOURITE) {
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    //getProfile();
                    accountInfo.setTotal_favorites(accountInfo.getTotal_favorites()+1);
                    tvAddContact.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_yellow, 0, 0);
                    accountInfo.setIs_favorites(true);
                    tvTotalFavorite.setText(accountInfo.getTotal_favorites() + "");
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            } else if (processID == Constants.ID_METHOD_REMOVE_FAVOURITE) {
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    //getProfile();
                    accountInfo.setTotal_favorites(accountInfo.getTotal_favorites()-1);
                    tvAddContact.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star, 0, 0);
                    accountInfo.setIs_favorites(false);
                    tvTotalFavorite.setText(accountInfo.getTotal_favorites() + "");
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }
        } else {
            Utils.showSimpleDialogAlert(this, this.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(this, msg);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvShareGoogle:
                shereWithGooglePlus();
                break;
            case R.id.tvShareFacebook:
                shereWithFacebook();
                break;
            case R.id.ibtnBack:
                finish();
                break;
            case R.id.tvStatusOnline:
                Utils.showListPopupWindow(MyProfileActivity.this, arrMenu, tvStatusOnline);
                break;
            case R.id.tvEdit:
                startActivityForResult(new Intent(MyProfileActivity.this, MyProfileEditActivity.class), REFRESH_CODE);
                break;
            case R.id.tvAddContact:
                if(accountInfo.is_favorites()){
                    new DialogConfirm(MyProfileActivity.this, getString(R.string.app_name), getString(R.string.msg_remove_favourite_account), 0, MyProfileActivity.this).show();
                }else
                    addFavourite();
                break;
            case R.id.tvHi:
                if(accountInfo != null){
                    Intent in = new Intent(context, ChatDetailActivity.class);
                    in.putExtra("sender_id", receiver_id);
                    in.putExtra("user_name", accountInfo.getUsername());
                    in.putExtra("block", false);
                    context.startActivity(in);
                    finish();
                }
                break;
            case R.id.ivAvatar:
                if(accountInfo != null && !accountInfo.getProfile_url().equals(""))
                    new DialogViewPhoto(context, accountInfo.getProfile_url()).show();
                break;
            case R.id.ivBackground:
                if(accountInfo != null && !accountInfo.getBackground_url().equals(""))
                    new DialogViewPhoto(context, accountInfo.getBackground_url()).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REFRESH_CODE) {
            getProfile();
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CONFIRM_YES){
            removeFavourite();
        }
    }
}
