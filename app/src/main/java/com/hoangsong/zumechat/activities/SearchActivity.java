package com.hoangsong.zumechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Encryption;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class SearchActivity extends AppCompatActivity implements EndlessListView.EndlessListener, PopupCallback, JsonCallback, View.OnClickListener{
    private RelativeLayout vPaddingActionBar;
    private ImageButton ibtnBack;
    private int page_size = 20;
    private String token = "";
    private EndlessListView lvMember;
    private TextView tvFromEmpty;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;
    private int page = 1;
    private int pageError = -1;
    boolean firstLoad = true;
    private MemberInfo memberInfo;
    private EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_search_member);
        pageInit();
    }
    private void pageInit(){
        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, this);
        MainActivityPhone.setSubPopUpCallback(this, Constants.ID_POPUP_TAB_CHAT);
        ibtnBack = (ImageButton) this.findViewById(R.id.ibtnBack);
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        txtSearch = (EditText) this.findViewById(R.id.txtSearch);
        tvFromEmpty = (TextView) this.findViewById(R.id.tvFromEmpty);
        tvFromEmpty.setVisibility(View.GONE);
        lvMember = (EndlessListView) this.findViewById(R.id.lvMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        adp = new ListMemberAdapter(this, list_member, getString(R.string.tab_people), this);
        lvMember.setAdapter(adp);
        lvMember.reset(0, true);
        lvMember.setListener(this);
        ibtnBack.setOnClickListener(this);

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(SearchActivity.this, ChatDetailActivity.class);
                in.putExtra("sender_id", list_member.get(position).getId());
                in.putExtra("user_name", list_member.get(position).getUsername());
                in.putExtra("block", list_member.get(position).is_block());
                startActivity(in);
            }
        });

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    page = 1;
                    firstLoad = true;
                    searchFriend(txtSearch.getText().toString(), page, true);
                }
                return false;
            }
        });
        txtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (txtSearch.getCompoundDrawables()[2] != null) {
                        boolean touchable = motionEvent.getX() > (txtSearch.getWidth() - txtSearch.getTotalPaddingRight())
                                && (motionEvent.getX() < ((txtSearch.getWidth() - txtSearch.getPaddingRight())));
                        if (touchable) {
                            page = 1;
                            firstLoad = true;
                            searchFriend(txtSearch.getText().toString(), page, true);
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtnBack:
                onBackPressed();
                break;
        }
    }

    private void searchFriend(String text_search, int page_index, boolean isLoading){
        tvFromEmpty.setVisibility(View.GONE);
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", text_search);
            obj.put("country_code", "");
            obj.put("type", "all");
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
                SearchActivity.this, isLoading, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void checkList(ArrayList<MemberInfo> list){
        if(list.size() > 0){
            tvFromEmpty.setVisibility(View.GONE);
        }else{
            tvFromEmpty.setVisibility(View.VISIBLE);
            tvFromEmpty.setText(getString(R.string.lbl_search_not_found_with)+" \""+txtSearch.getText().toString()+"\"");
        }
    }

    private void sendMessage(MemberInfo memberInfo){
        JSONObject obj = new JSONObject();
        try {
            obj.put("receiver_id", memberInfo.getId());
            obj.put("chat_type", Constants.CHAT_TYPE_HI);
            obj.put("chat_message", Encryption.encrypt(Constants.CHAT_TYPE_HI));
            obj.put("photo", "");
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(this, Constants.SEND_MESSAGE_CHAT, Constants.ID_METHOD_SEND_MESSAGE_CHAT,
                SearchActivity.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(this, "", getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(this, response.getMessage(), getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    MemberList dataInfo = (MemberList) response.getData();
                    int count = 1;
                    for(int i = 0; i < dataInfo.getFriends().size(); i++){
                        if(count == 5){
                            count = 0;
                            dataInfo.getFriends().get(i).setHasAds(true);
                        }else {
                            dataInfo.getFriends().get(i).setHasAds(false);
                        }
                        count++;
                    }
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
            checkList(list_member);
        }else if(processID == Constants.ID_METHOD_SEND_MESSAGE_CHAT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(this, "", getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(this, response.getMessage(), getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(response.getData() != null){
                        ChatInfo chatInfo = (ChatInfo) response.getData();
                        memberInfo.setChatInfo(chatInfo);
                        MainActivityPhone.updateToFragment(memberInfo, Constants.ID_POPUP_TAB_CHAT, 0, 0);
                    }
                }else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(this, getString(R.string.alert_unexpected_error));
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
            checkList(list_member);
        }
    }

    @Override
    public void loadData() {
        if (pageError > 0) {
            searchFriend(txtSearch.getText().toString(), pageError, false);

        } else {
            page = page + 1;
            searchFriend(txtSearch.getText().toString(), page, false);
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CALLBACK_ADAPTER){
            if(data != null){
                memberInfo = (MemberInfo) data;
                list_member.get(num).setIs_chat(true);
                adp.notifyDataSetChanged();
                sendMessage(memberInfo);
            }
        }
    }
}
