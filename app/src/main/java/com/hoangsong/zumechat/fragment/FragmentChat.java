package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.ChatDetailActivity;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogActionMember;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.PrefsNewUserChat;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentChat extends BaseFragment implements EndlessListView.EndlessListener{
    private Context context;
    private View thisView;
    private int page_size = 20;
    private String token = "";
    private String my_sender_id = "";
    private TextView tvFromEmpty;
    private LinearLayout llTop;
    private Button btnCancel, btnDelete;
    private EndlessListView lvMember;
    private SwipeRefreshLayout swMember;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;
    private int page = 1;
    private int pageError = -1;
    boolean firstload = true;

    private int current_position = 0;

    public FragmentChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        setFragmentActivity(getActivity());
        if (thisView == null) {
            thisView = inflater.inflate(R.layout.fragment_base, null);
            initView(thisView);

        } else {
            ViewGroup parent = (ViewGroup) thisView.getParent();
            if (parent != null) {
                parent.removeView(thisView);
            }
        }
        return thisView;
    }

    private void initView(View v){
        setPopUpCallback(this, 1);
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        my_sender_id = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";
        llTop = (LinearLayout) thisView.findViewById(R.id.llTop);
        btnCancel = (Button) thisView.findViewById(R.id.btnCancel);
        btnDelete = (Button) thisView.findViewById(R.id.btnDelete);
        tvFromEmpty = (TextView) thisView.findViewById(R.id.tvFromEmpty);
        tvFromEmpty.setVisibility(View.GONE);
        swMember = (SwipeRefreshLayout) v.findViewById(R.id.swMember);
        lvMember = (EndlessListView) v.findViewById(R.id.lvMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        lvMember.reset(0, true);
        adp = new ListMemberAdapter(context, list_member, context.getString(R.string.tab_chat), FragmentChat.this);
        lvMember.setAdapter(adp);

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(llTop.getVisibility() == View.GONE){
                    Intent in = new Intent(context, ChatDetailActivity.class);
                    in.putExtra("sender_id", list_member.get(position).getId());
                    in.putExtra("user_name", list_member.get(position).getUsername());
                    in.putExtra("block", list_member.get(position).is_block());
                    context.startActivity(in);
                    if(list_member.get(position).is_new()){
                        PrefsNewUserChat prefsNewUserChat = Prefs.getPrefsNewUserChat();
                        prefsNewUserChat.remove_id_user(list_member.get(position).getId());
                        list_member.get(position).setIs_new(false);
                        if(list_member.get(position).getChatInfo() != null)
                            list_member.get(position).getChatInfo().setIs_read(true);
                        Prefs.setPrefsNewUserChat(prefsNewUserChat);
                        adp.notifyDataSetChanged();
                    }
                }else {
                    list_member.get(position).setIs_check(!list_member.get(position).is_check());
                    adp.notifyDataSetChanged();
                }
            }
        });

        lvMember.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_position = i;
                new DialogActionMember(context, list_member.get(i), i, FragmentChat.this).show();
                return true;
            }
        });

        swMember.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvMember.reset(0, true);
                        page = 1;
                        firstload = true;
                        searchFriend(page);
                    }
                }, 0);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adp.setIs_delete(false);
                llTop.setVisibility(View.GONE);
                for (int i = 0; i < list_member.size(); i++) {
                    list_member.get(i).setIs_check(false);
                }
                adp.notifyDataSetChanged();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(list_member.size() <=0){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    searchFriend(page);
                }
            });
        }
        lvMember.setListener(this);
    }

    private void searchFriend(int page_index){
        JSONObject obj = new JSONObject();
        try {
            obj.put("country_code", "");
            obj.put("type", "message");
            obj.put("latitude", 0.0);
            obj.put("longtitude", 0.0);
            obj.put("page_index", page_index);
            obj.put("page_size", page_size);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(context, Constants.SEARCH_FRIEND, Constants.ID_METHOD_SEARCH_FRIEND,
                FragmentChat.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void delete(){
        JSONObject obj = new JSONObject();
        try {
            JSONArray receivers = new JSONArray();
            for (MemberInfo item : list_member) {
                if(item.is_check()){
                    JSONObject id = new JSONObject();
                    id.put("receiver_id", item.getId());
                    receivers.put(id);
                }
            }
            obj.put("receivers", receivers);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(context, Constants.DELETE_CHAT_BY_ACCOUNT, Constants.ID_METHOD_DELETE_CHAT_BY_ACCOUNT,
                FragmentChat.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
                FragmentChat.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
                FragmentChat.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void checkList(ArrayList<MemberInfo> list){
        if(list.size() > 0){
            tvFromEmpty.setVisibility(View.GONE);
        }else{
            tvFromEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void addMember(MemberInfo memberInfo){
        if(memberInfo != null){
            int position = checkEmptyMember(memberInfo.getId());
            if(!memberInfo.getId().equals(my_sender_id)){
                memberInfo.setIs_new(true);
                PrefsNewUserChat prefsNewUserChat = Prefs.getPrefsNewUserChat();
                prefsNewUserChat.add_id_user(memberInfo.getId());
                Prefs.setPrefsNewUserChat(prefsNewUserChat);
            }
            if(position >=0 ){
                list_member.remove(position);
            }
            list_member.add(0, memberInfo);
            if(adp != null)
                adp.notifyDataSetChanged();
        }
    }

    public void updateData(ArrayList<MemberInfo> member_list){
        if(member_list != null){
            for (int j = 0; j < member_list.size(); j++){
                for (int i = 0; i < list_member.size(); i++){
                    if(list_member.get(i).getId().equals(member_list.get(j).getId())){
                        MemberInfo item = member_list.get(j);
                        list_member.get(i).setOnline_status(item.isOnline_status());
                        list_member.get(i).setJob_status(item.getJob_status());
                        //list_member.get(i).setIs_block(item.is_block());
                        list_member.get(i).setOffline_on(item.getOffline_on());
                        break;
                    }
                }
            }
            adp.notifyDataSetChanged();
        }
    }

    public void addMessage(ChatInfo chatInfo){
        if(chatInfo != null){
            MemberInfo memberInfo;
            int position = checkEmptyMember(chatInfo.getSender_id());
            if(position < 0 ){
                memberInfo = new MemberInfo(0, chatInfo.getSender_id(), chatInfo.getSender_name(), chatInfo.getReceiver_profile_url(), false, "", chatInfo.getCreated_on(), "", "", false, "", chatInfo);
            }else {
                memberInfo = list_member.get(position);
                memberInfo.setChatInfo(chatInfo);
                list_member.remove(position);
            }
            if(!chatInfo.getSender_id().equals(my_sender_id)){
                memberInfo.setIs_new(true);
                PrefsNewUserChat prefsNewUserChat = Prefs.getPrefsNewUserChat();
                prefsNewUserChat.add_id_user(chatInfo.getSender_id());
                Prefs.setPrefsNewUserChat(prefsNewUserChat);
            }
            list_member.add(0, memberInfo);
            adp.notifyDataSetChanged();
        }
    }

    private int checkEmptyMember(String member_id){
        for(int i =0; i < list_member.size(); i++){
            if(list_member.get(i).getId().equals(member_id))
                return i;
        }
        return -1;
    }

    private void checkNewMessageList(ArrayList<MemberInfo> list_member){
        if(list_member != null){
            ArrayList<String> list_new = Prefs.getPrefsNewUserChat().getArr_id_user();
            for(int i =0; i < list_new.size(); i++){
                for (int j = 0; j < list_member.size(); j++) {
                    if(list_member.get(j).getId().equals(list_new.get(i)))
                        list_member.get(j).setIs_new(true);
                }
            }
        }
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            swMember.setRefreshing(false);
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    MemberList dataInfo = (MemberList) response.getData();
                    if (firstload) {
                        list_member.clear();
                        checkNewMessageList(dataInfo.getFriends());
                        list_member.addAll(dataInfo.getFriends());
                        adp = new ListMemberAdapter(context, list_member, context.getString(R.string.tab_chat), FragmentChat.this);
                        lvMember.setAdapter(adp);
                        firstload = false;
                    } else {
                        checkNewMessageList(dataInfo.getFriends());
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
                        Utils.showSimpleDialogAlert(context, response.getMessage());
                    }
                    lvMember.reset(0, true);
                    pageError = page;
                }
            }else{
                if (pageError < 0) {
                    Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
                }
                lvMember.reset(0, true);
                pageError = page;
            }
            checkList(list_member);
        }else if(processID == Constants.ID_METHOD_DELETE_MESSAGE_CHAT || processID == Constants.ID_METHOD_DELETE_CHAT_BY_ACCOUNT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    for (int i = 0; i < list_member.size(); i++) {
                        if(list_member.get(i).is_check()){
                            list_member.remove(i);
                            i--;
                        }
                    }
                    adp.setIs_delete(false);
                    llTop.setVisibility(View.GONE);
                    adp.notifyDataSetChanged();
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
        }else if(processID == Constants.ID_METHOD_BLOCK_ACCOUNT || processID == Constants.ID_METHOD_UNBLOCK_ACCOUNT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(processID == Constants.ID_METHOD_BLOCK_ACCOUNT){
                        list_member.get(current_position).setIs_block(true);
                    }else
                        list_member.get(current_position).setIs_block(false);
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        swMember.setRefreshing(false);
        if (pageError < 0) {
            Utils.showSimpleDialogAlert(context, msg);
        }
        lvMember.reset(0, true);
        pageError = page;
        checkList(list_member);
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
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        super.popUpCallback(data, processID, obj, num, index);
        if(processID == Constants.ID_POPUP_TAB_CHAT){
            if(data != null){
                addMember((MemberInfo) data);
            }
        }else if(processID == Constants.ID_POPUP_CALLBACK_ADAPTER){
            list_member.get(num).setIs_check(!list_member.get(num).is_check());
            adp.notifyDataSetChanged();
        }else if(processID == Constants.ID_POPUP_MENU_DELETE){
            adp.setIs_delete(true);
            llTop.setVisibility(View.VISIBLE);
            adp.notifyDataSetChanged();
        }else if(processID == Constants.ID_POPUP_ACTION_MEMBER_BLOCK){
            if(data != null){
                MemberInfo memberInfo = (MemberInfo) data;
                if(!memberInfo.is_block()){
                    new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_block_account), 0, FragmentChat.this).show();
                }
                else {
                    //unblock account
                    unBlock(list_member.get(current_position).getId());
                }
            }

        }else if(processID == Constants.ID_POPUP_ACTION_MEMBER_DELETE){
            new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_delete_chat), 1, FragmentChat.this).show();
        }else if(processID == Constants.ID_POPUP_CONFIRM_YES){
            if(num == 1){
                list_member.get(current_position).setIs_check(true);
                delete();
            }else {
                block(list_member.get(current_position).getId());
            }
        }
    }
}
