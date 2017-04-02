package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.adapters.ListChatAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogActionMessage;
import com.hoangsong.zumechat.dialog.DialogChooseImage;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.dialog.DialogReportUser;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.ChatMessageList;
import com.hoangsong.zumechat.models.PrefsNewUserChat;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Encryption;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.MyDateTimeISO;
import com.hoangsong.zumechat.untils.NotificationsUtils;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessRecyclerViewScrollListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Tang on 10/22/2016.
 */

public class ChatDetailActivity extends AppCompatActivity implements View.OnClickListener, JsonCallback, PopupCallback, ListChatAdapter.MyLongItemClickListener{

    private  String MENU_USER_INFO= "";
    private  String MENU_BLOCK = "";
    private  String MENU_UNBLOCK = "";
    private  String MENU_FLAG_AND_REPORT = "";
    public String[] arrMenu;

    private Context context;
    private int page_size = 20;
    private String token = "";
    private RelativeLayout vPaddingActionBar;
    private TextView tvStatusConnect, tvTitle, tvEmpty, tvActionMenu;
    private RecyclerView listView;
    private ListChatAdapter mAdapter;
    private ProgressBar progressBarLoadChat;
    private LinearLayoutManager llm;
    private EndlessRecyclerViewScrollListener endlessScroll;
    private EditText txtChatMessage;
    private ImageButton ibtnSendMessage, ibtnBack, ibtnAddImage, ibtnMenu;
    private ArrayList<ChatInfo> listChat = new ArrayList<ChatInfo>();
    private ArrayList<ChatInfo> listChat2 = new ArrayList<ChatInfo>();
    private boolean is_block;


    boolean firstload = true;
    boolean isLoadMore = false;
    private int page = 1;
    private int pageError = -1;
    private String userid = "";
    private String sender_id_get = "";
    private String username_get = "";
    Calendar cal = Calendar.getInstance();
    int dayCurrent = cal.get(Calendar.DAY_OF_YEAR);
    int woy = -1;

    //image
    public static final int ID_REQUEST_TAKE_PHOTO = 0;
    public static final int ID_REQUEST_CHOOSE_PHOTO = 1;
    private String pictureImagePath = "";
    private String pictureImageName = "";
    final private int REQUEST_CODE_ASK_PERMISSIONS_WRITE = 123;
    final private int REQUEST_CODE_ASK_PERMISSIONS_READ = 1234;
    private boolean TAKE_PHOTO = false;

    //ads
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utils.setTranslucentStatusBar(getWindow());
        EventBus.getDefault().register(this);
        context = ChatDetailActivity.this;
        setContentView(R.layout.fragment_chat_detail);
        initView();
    }

    private void initView(){
        MainActivityPhone.setSubPopUpCallback(this, Constants.ID_POPUP_CHAT_DETAIL);
        //vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        //Utils.setViewPaddingStatusBar(vPaddingActionBar, context);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            sender_id_get = extra.getString("sender_id");
            username_get = extra.getString("user_name");
            is_block = extra.getBoolean("block");
            MainActivityPhone.setSenderIdCurrent(sender_id_get);
            PrefsNewUserChat prefsNewUserChat = Prefs.getPrefsNewUserChat();
            prefsNewUserChat.remove_id_user(sender_id_get);
            Prefs.setPrefsNewUserChat(prefsNewUserChat);
            NotificationsUtils.cancelNotification(context, MainActivityPhone.checkNotification(sender_id_get));
        }

        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";

        progressBarLoadChat = (ProgressBar) findViewById(R.id.progressBarLoadChat);
        tvStatusConnect = (TextView) findViewById(R.id.tvStatusConnect);
        tvActionMenu = (TextView) findViewById(R.id.tvActionMenu);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        listView = (RecyclerView) findViewById(R.id.msgview);
        listView.setHasFixedSize(true);
        llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        txtChatMessage = (EditText) findViewById(R.id.txtChatMessage);
        ibtnSendMessage = (ImageButton) findViewById(R.id.ibtnSendMessage);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        ibtnMenu = (ImageButton) findViewById(R.id.ibtnMenu);
        ibtnAddImage = (ImageButton) findViewById(R.id.ibtnAddImage);
        ibtnMenu.setVisibility(View.VISIBLE);

        tvTitle.setText(username_get);

        ibtnSendMessage.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);
        ibtnAddImage.setOnClickListener(this);
        ibtnMenu.setOnClickListener(this);

        endlessScroll = new EndlessRecyclerViewScrollListener(llm) {

            @Override
            public void onLoadMore(int current_page) {
                // TODO Auto-generated method stub
                customLoadMoreDataFromApi();

            }
        };

        endlessScroll.reset(0, true);
        mAdapter = new ListChatAdapter(this, listChat, this);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(llm);
        listView.setHasFixedSize(true);
        listView.setAdapter(mAdapter);

        listView.addOnScrollListener(endlessScroll);
        loadChatList(page);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAdapter.setOnItemLongClickListener(this);

        loadStringData();
        tvActionMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(MENU_USER_INFO)){
                    Intent in = new Intent(context, MyProfileActivity.class);
                    in.putExtra("receiver_id", sender_id_get);
                    context.startActivity(in);
                }else if(s.toString().equals(MENU_BLOCK)){
                    new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_block_account), 0, ChatDetailActivity.this).show();
                }else if(s.toString().equals(MENU_UNBLOCK)){
                    unBlock(sender_id_get);
                }else if(s.toString().equals(MENU_FLAG_AND_REPORT)){
                    checkReportAccount();
                }
            }
        });

        //ads
        mAdView = (AdView) findViewById(R.id.adView);
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(final boolean isOpen) {
                        // some code depending on keyboard visiblity status
                        //System.gc();
                        if(Constants.IS_SHOW_ADS){
                            if(isOpen){
                                mAdView.setVisibility(View.GONE);
                            }else{
                                mAdView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
        if(Constants.IS_SHOW_ADS){
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = null;
            if(Constants.IS_ADMOB_PRO){
                adRequest = new AdRequest.Builder().build();
            }else {
                adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        // Check the LogCat to get your test device ID
                        .addTestDevice("A02FA5C2D61CF2C23B8B722531DD68E5")
                        .build();
            }
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }

        checkBlockAccount();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtnSendMessage:
                if(txtChatMessage.getText().length()>0) {
                    String msg = txtChatMessage.getText().toString().trim();
                    for (int i = 0; i < Constants._MSG_BLACKLIST.length; i++) {
                        msg = msg.replaceAll("(?i)" + Constants._MSG_BLACKLIST[i], "****");
                    }
                    msg = msg.replaceAll("\\w*\\*{4}", "****");
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("receiver_id", sender_id_get);
                        obj.put("chat_type", Constants.CHAT_TYPE_TEXT);
                        obj.put("chat_message", Encryption.encrypt(msg));
                        obj.put("photo", "");
                        obj.put("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(Constants.DEBUG_MODE)
                        Log.d("post data", "post data: "+obj.toString());
                    new DownloadAsyncTask(context, Constants.SEND_MESSAGE_CHAT, Constants.ID_METHOD_SEND_MESSAGE_CHAT,
                            ChatDetailActivity.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
                    txtChatMessage.setText("");
                }
                break;
            case R.id.ibtnBack:
                onBackPressed();
                break;
            case R.id.ibtnAddImage:
                new DialogChooseImage(context, ChatDetailActivity.this).show();
                break;
            case R.id.ibtnMenu:
                Utils.showListPopupWindow(context, arrMenu, tvActionMenu, ibtnMenu);
                break;

        }
    }

    private void loadStringData() {
        MENU_BLOCK = getString(R.string.menu_block);
        MENU_UNBLOCK = getString(R.string.btn_unblock);
        MENU_USER_INFO = getString(R.string.menu_profile);
        MENU_FLAG_AND_REPORT = getString(R.string.menu_flag_report);
        arrMenu = new String[]{MENU_USER_INFO, is_block ? MENU_UNBLOCK : MENU_BLOCK, MENU_FLAG_AND_REPORT};
    }

    @Subscribe
    public void onEvent(String type) {
        if(type.equalsIgnoreCase(Constants.PUSH_CONNECT_INTERNET)){
            tvStatusConnect.setVisibility(View.GONE);
        }else if(type.equals(Constants.PUSH_NO_INTERNET)){
            tvStatusConnect.setVisibility(View.VISIBLE);
        }
    }

    private void loadChatList(int page_index){
        new DownloadAsyncTask(context,
                Constants.GET_CHAT_MESSAGES+"?receiver_id=" + sender_id_get+"&page_index="+page_index+"&page_size="+page_size+"&token="+token,
                Constants.ID_METHOD_GET_CHAT_MESSAGES,
                this, false, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    private void block(String member_id){
        JSONObject obj = new JSONObject();
        try {
            obj.put("member_id", member_id);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(context, Constants.BLOCK_ACCOUNT, Constants.ID_METHOD_BLOCK_ACCOUNT,
                this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
        new DownloadAsyncTask(context, Constants.UNBLOCK_ACCOUNT, Constants.ID_METHOD_UNBLOCK_ACCOUNT,
                this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void senImage(String base64Image){
        JSONObject obj = new JSONObject();
        try {
            obj.put("receiver_id", sender_id_get);
            obj.put("chat_type", Constants.CHAT_TYPE_PHOTO);
            obj.put("chat_message", Encryption.encrypt(pictureImageName));
            obj.put("photo", base64Image);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(context, Constants.SEND_MESSAGE_CHAT, Constants.ID_METHOD_SEND_MESSAGE_CHAT,
                ChatDetailActivity.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
        pictureImageName = "";
    }

    private void checkBlockAccount() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", sender_id_get);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.CHECK_ACCOUNT_BLOCK, Constants.ID_METHOD_CHECK_ACCOUNT_BLOCK, ChatDetailActivity.this, false,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkReportAccount() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", sender_id_get);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.CHECK_ACCOUNT_REPORT, Constants.ID_METHOD_CHECK_ACCOUNT_REPORT, ChatDetailActivity.this, false,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reportAccount(String content) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", sender_id_get);
            postData.put("content", content);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.REPORT_ACCOUNT, Constants.ID_METHOD_REPORT_ACCOUNT, ChatDetailActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        MainActivityPhone.setSenderIdCurrent("");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void customLoadMoreDataFromApi() {
        if(isLoadMore){
            if (pageError > 0) {
                //Log.e("page error", pageError + "-----");
                progressBarLoadChat.setVisibility(View.VISIBLE);
                loadChatList(pageError);

            } else {
                //Log.e("page error", pageError + "-----");
                page = page + 1;
                progressBarLoadChat.setVisibility(View.VISIBLE);
                loadChatList(page);

            }
        }
    }

    private void sortList(){
        if(listChat2.size()>0){
            tvEmpty.setVisibility(View.GONE);
            listChat.clear();
            listChat.addAll(listChat2);
            for(int i = 0; i< listChat.size(); i++){
                ChatInfo chatObj = listChat.get(i);
                shortBackground(i);
                if(!chatObj.getCreated_on().equals("")){
                    if (woy != MyDateTimeISO.getDateOfYear(MyDateTimeISO.getFormatDateReg(chatObj.getCreated_on()))) {
                        woy = MyDateTimeISO.getDateOfYear(MyDateTimeISO.getFormatDateReg(chatObj.getCreated_on()));
                        if(woy == dayCurrent){
                            listChat.add(i, new ChatInfo("", getString(R.string.lbl_today), "", "", "", "", "", "", "", "", "", "", false, false, false));
                        }else{
                            listChat.add(i, new ChatInfo("", MyDateTimeISO.getFormatDateRegStr(chatObj.getCreated_on()), "", "", "", "", "", "", "", "", "", "", false, false, false));
                        }
                    }
                }
            }
            if(!listChat.get(0).getCreated_on().equals("")){
                if(woy == dayCurrent){
                    listChat.add(0, new ChatInfo("", getString(R.string.lbl_today), "", "", "", "", "", "", "", "", "", "", false, false, false));
                }else{
                    listChat.add(0, new ChatInfo("", MyDateTimeISO.getFormatDateRegStr(listChat.get(0).getCreated_on()), "", "", "", "", "", "", "", "", "", "", false, false, false));
                }
            }
        }else{
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void shortBackground(int index){
        if(index == 0){
            if(listChat.get(0).getSender_id().equals(userid)){
                listChat.get(0).setBackground(1);
            }else{
                listChat.get(0).setBackground(2);
            }
        }else{
            String old = listChat.get(index-1).getSender_id();
            String mnew = listChat.get(index).getSender_id();
            if(mnew.equals(old)){
                //listChat.get(index-1).setShowTime(false);
                if(mnew.equals(userid)){
                    listChat.get(index).setBackground(-1);
                }else{
                    listChat.get(index).setBackground(-2);
                }
            }else if(!mnew.equals(old)){
                if(mnew.equals(userid)){
                    listChat.get(index).setBackground(1);
                }else{
                    listChat.get(index).setBackground(2);
                }
            }
        }

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        // TODO Auto-generated method stub
        if (processID == Constants.ID_METHOD_GET_CHAT_MESSAGES) {
            if (data != null) {
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    ChatMessageList dataInfo = (ChatMessageList) response.getData();
                    if (firstload) {
                        for(int i=dataInfo.getListChat().size()-1; i>=0 ; i--){
                            listChat2.add(dataInfo.getListChat().get(i));
                        }
                        sortList();
                        mAdapter.notifyDataSetChanged();
                        //listView.scrollToPosition(listChat.size());
                        listView.invalidate();
                        listView.scrollToPosition(llm.getChildCount() + dataInfo.getListChat().size());
                        firstload = false;
                        if(dataInfo.getMaxPage()>1){
                            isLoadMore = true;
                        }
                    } else {
                        for (int i = 0; i < dataInfo.getListChat().size(); i++) {
                            listChat2.add(0, dataInfo.getListChat().get(i));
                        }
                        sortList();
                        mAdapter.notifyDataSetChanged();
                        listView.invalidate();
                        listView.scrollToPosition(llm.getChildCount() + (dataInfo.getListChat().size()-1));
                        //Log.e("chitcount: ", "chitcount: "+llm.getChildCount());
                        progressBarLoadChat.setVisibility(View.GONE);
                        pageError = -1;
                    }
                    //}
                    //progressBarLoadChat.setVisibility(View.GONE);
                    //Log.v("page num ", page + "----");
                    //Log.v("size list ", listChat.size() + "----");
                    //Log.v("size adapter ", mAdapter.getItemCount() + "----");
                } else {
                    if (pageError < 0) {
                        try{
                            Utils.showSimpleDialogAlert(context, response.getMessage());
                        }catch(Exception e){}
                    }
                    endlessScroll.reset(0, true);
                    pageError = page;
                }
            } else {
                if (pageError < 0) {
                    try{
                        Utils.showSimpleDialogAlert(context, getString(R.string.alert_no_connection));
                    }catch(Exception e){}
                }
                endlessScroll.reset(0, true);
                pageError = page;
            }
        }else if(processID == Constants.ID_METHOD_SEND_MESSAGE_CHAT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(response.getData() != null){
                        ChatInfo chatInfo = (ChatInfo) response.getData();
                        listChat.add(chatInfo);
                        listChat2.add(chatInfo);
                        sortList();
                        tvEmpty.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        listView.scrollToPosition(mAdapter.getItemCount() - 1);
                        listView.invalidate();
                    }
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
        }else if(processID == Constants.ID_METHOD_BLOCK_ACCOUNT || processID == Constants.ID_METHOD_UNBLOCK_ACCOUNT || processID == Constants.ID_METHOD_REPORT_ACCOUNT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(processID == Constants.ID_METHOD_BLOCK_ACCOUNT){
                        arrMenu[1] = MENU_UNBLOCK;
                    }else if(processID == Constants.ID_METHOD_UNBLOCK_ACCOUNT)
                        arrMenu[1] = MENU_BLOCK;
                    else {
                        Utils.showSimpleDialogAlert(context, response.getMessage());
                    }
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
        }else if(processID == Constants.ID_METHOD_CHECK_ACCOUNT_BLOCK || processID == Constants.ID_METHOD_CHECK_ACCOUNT_REPORT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    boolean isDone = (boolean) response.getData();
                    if(processID == Constants.ID_METHOD_CHECK_ACCOUNT_BLOCK){
                        is_block = isDone;
                        if(isDone){
                            arrMenu[1] = MENU_UNBLOCK;
                        }else {
                            arrMenu[1] = MENU_BLOCK;
                        }
                    }else{
                        if(isDone){
                            Utils.showSimpleDialogAlert(context, getString(R.string.msg_already_reported));
                        }else {
                            new DialogReportUser(this, ChatDetailActivity.this).show();
                        }
                    }

                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        // TODO Auto-generated method stub
        if (pageError < 0) {
            try{
                Utils.showSimpleDialogAlert(context, msg);
            }catch(Exception e){}
        }
        endlessScroll.reset(0, true);
        pageError = page;
    }

    private void updateMessage(ChatInfo chatInfo){
        if(chatInfo != null){
            for (int i = 0; i < listChat.size(); i++){
                if(listChat.get(i).getId().equals(chatInfo.getId())){
                    listChat.get(i).setChat_message(chatInfo.getChat_message());
                    break;
                }
            }
            for (int i = 0; i < listChat2.size(); i++){
                if(listChat2.get(i).getId().equals(chatInfo.getId())){
                    listChat2.get(i).setChat_message(chatInfo.getChat_message());
                    break;
                }
            }
        }
    }
    private void deleteMessage(ChatInfo chatInfo){
        if(chatInfo != null){
            for (int i = 0; i < listChat.size(); i++){
                if(listChat.get(i).getId().equals(chatInfo.getId())){
                    listChat.get(i).setIs_delete(true);
                    break;
                }
            }
            for (int i = 0; i < listChat2.size(); i++){
                if(listChat2.get(i).getId().equals(chatInfo.getId())){
                    listChat2.get(i).setIs_delete(true);
                    break;
                }
            }
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE){
            switch (num){
                case Constants.ACTION_RECEIVED_MESSAGE:
                    if(data != null){
                        ChatInfo chatInfo = (ChatInfo) data;
                        listChat.add(chatInfo);
                        listChat2.add(chatInfo);
                        sortList();
                        tvEmpty.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        listView.scrollToPosition(mAdapter.getItemCount() - 1);
                        listView.invalidate();
                    }
                    break;
                case Constants.ACTION_UPDATE_MESSAGE:
                    if(data != null){
                        ChatInfo chatInfo = (ChatInfo) data;
                        updateMessage(chatInfo);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case Constants.ACTION_DELETE_MESSAGE:
                    ChatInfo chatInfo = (ChatInfo) data;
                    deleteMessage(chatInfo);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }else if(processID == Constants.ID_POPUP_CHAT_UPDATE_MESSAGE){
            if(data != null){
                ChatInfo chatInfo = (ChatInfo) data;
                switch (num){
                    case Constants.ACTION_UPDATE_MESSAGE:
                        updateMessage(chatInfo);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case Constants.ACTION_DELETE_MESSAGE:
                        deleteMessage(chatInfo);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        } else if (processID == Constants.ID_POPUP_CHOOSE_IMAGE) {
            if(num == Constants.ID_POPUP_TAKE_PHOTO){
                openBackCamera(ID_REQUEST_TAKE_PHOTO);
            }else {
                choseImageDevice(ID_REQUEST_CHOOSE_PHOTO);
            }
        }else if(processID == Constants.ID_POPUP_SAVE_IMAGE){
            if(data != null){
                ChatInfo chatInfo = (ChatInfo) data;
                new ImageDownloader().execute(chatInfo.getPhoto_url(), chatInfo.getChat_message());
            }
        }else if(processID == Constants.ID_POPUP_CONFIRM_YES){
            block(sender_id_get);
        }else if(processID == Constants.ID_POPUP_CALLBACK_ADAPTER){
            if(data != null){
                reportAccount((String) data);
            }
        }else if(processID == Constants.ID_POPUP_IS_READ_MESSAGE){
            listChat.get(num).setIs_read(true);
            String id = listChat.get(num).getId();
            for (int i = 0; i < listChat2.size(); i++) {
                if(listChat2.get(i).equals(id)){
                    listChat2.get(i).setIs_read(true);
                }
            }
        }
    }

    @Override
    public void onLongItemClick(int position, View v) {
        ChatInfo chatInfo = listChat.get(position);
        if(!chatInfo.is_delete()){
            if(!(chatInfo.getSender_id().equals(sender_id_get) && chatInfo.getChat_type().equals(Constants.CHAT_TYPE_HI)))
                new DialogActionMessage(context, chatInfo, sender_id_get, ChatDetailActivity.this).show();
        }
    }

    //image
    private void choseImageDevice(int idRequest) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, context.getString(R.string.title_select_picture)), idRequest);
    }

    private void openBackCamera(int requestId) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = getOutputMediaFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, requestId);
            }
        }
    }

    private File getOutputMediaFile() {
        Utils.createCachedFolder();
        String fileName = Utils.generateTempFileName("jpg");
        pictureImageName = fileName;

        return new File(Utils.getRootFolder() + Constants._CACHED_FOLDER[0] + "/" + fileName);
    }

    /*private void checkPermission() {
        int write = ContextCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (write != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS_WRITE);
        }else{
            if (TAKE_PHOTO) {
                openBackCamera(ID_REQUEST_TAKE_PHOTO);
            } else {
                choseImageDevice(ID_REQUEST_CHOOSE_PHOTO);
            }
        }
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS_WRITE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int read = ContextCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (read != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChatDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_ASK_PERMISSIONS_READ);
                    } else {
                        if (TAKE_PHOTO) {
                            openBackCamera(ID_REQUEST_TAKE_PHOTO);
                        } else {
                            choseImageDevice(ID_REQUEST_CHOOSE_PHOTO);
                        }
                    }
                }
                break;
            case REQUEST_CODE_ASK_PERMISSIONS_READ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (TAKE_PHOTO) {
                        openBackCamera(ID_REQUEST_TAKE_PHOTO);
                    } else {
                        choseImageDevice(ID_REQUEST_CHOOSE_PHOTO);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        try{
            if (resultCode == RESULT_OK ) {

                new AsyncTask<String, Void, String>() {
                    Bitmap resizeBitmap;

                    @Override
                    protected String doInBackground(String... params) {
                        Bitmap bitmap = null;
                        String filePath ="";
                        if (requestCode == ID_REQUEST_TAKE_PHOTO) {
                            filePath = Utils.getRootFolder() + Constants._CACHED_FOLDER[0] + "/" + pictureImageName;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            bitmap = BitmapFactory.decodeFile(filePath, options);
                        }else{
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                pictureImageName = MyDateTimeISO.currentDateFormat()+".jpg";
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            filePath = getPath(uri);
                        }
                        try {
                            ExifInterface exif = new ExifInterface(filePath);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            if (Constants.DEBUG_MODE) {
                                Log.d("EXIF", "Exif: " + orientation);
                            }
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            } else if (orientation == 3) {
                                matrix.postRotate(180);
                            } else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                    bitmap.getWidth(), bitmap.getHeight(),
                                    matrix, true); // rotating bitmap
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resizeBitmap = Utils.scaleCenterCropHalf(bitmap);
                        if(Constants.DEBUG_MODE){
                            Log.e("width_height_old", "width: "+bitmap.getWidth()+" height: "+bitmap.getHeight());
                            Log.e("width_height_new", "width: "+resizeBitmap.getWidth()+" height: "+resizeBitmap.getHeight());
                        }
                        if (requestCode == ID_REQUEST_TAKE_PHOTO)
                            Utils.deleteFile(filePath);
                        // resizeBitmap.compress(CompressFormat.JPEG, 100, new
                        // FileOutputStream(filePath + file_name_image_temp));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        String imageData = "";
                        try {
                            imageData = Utils.encodeFileToBase64Binary(resizeBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        senImage(imageData);
                        /*if (requestCode == ivPhoto1.getId()) {
                            ivPhoto1.setImageBitmap(resizeBitmap);
                            ibtnRemove1.setVisibility(View.VISIBLE);
                            try {
                                base_photo1 = Utils.encodeFileToBase64Binary(resizeBitmap);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }*/
                    }
                }.execute("vxv");
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (resultCode == RESULT_OK) {
            if (requestCode == ID_REQUEST_TAKE_PHOTO ) {
                File imgFile = new File(pictureImagePath);
                if (imgFile.exists()) {
                    Uri uri = Uri.fromFile(imgFile);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(pictureImagePath));
                        Utils.storeCameraPhotoInSDCard(bitmap, Utils.getRootFolder() + Constants._CACHED_FOLDER[2] + "/", pictureImageName);
                        String imageData = Utils.encodeFileToBase64Binary(bitmap);
                        senImage(imageData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == ID_REQUEST_CHOOSE_PHOTO  && data != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    pictureImageName = MyDateTimeISO.currentDateFormat()+".jpg";
                    //Utils.storeCameraPhotoInSDCard(bitmap, Utils.getRootFolder() + Constants._CACHED_FOLDER[2] + "/", pictureImageName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Utils.getRootFolder() + Constants._CACHED_FOLDER[2] + "/"+pictureImageName));
                    String imageData = Utils.encodeFileToBase64Binary(bitmap);
                    senImage(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
    public String getPath(Uri uri) {
        try{
            String[] projection = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    //download image
    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            return downloadBitmap(param[0], param[1]);
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
            Utils.storePhotoInGallery(ChatDetailActivity.this, bitmap);
            Toast.makeText(ChatDetailActivity.this, getString(R.string.lbl_save_image_success), Toast.LENGTH_SHORT).show();
        }

        private Bitmap downloadBitmap(String url, String file_name) {
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }
    }
}
