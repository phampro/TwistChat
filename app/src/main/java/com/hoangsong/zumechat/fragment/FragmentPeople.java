package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.ChatDetailActivity;
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
import com.hoangsong.zumechat.untils.GPSTracker;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentPeople extends BaseFragment implements EndlessListView.EndlessListener{
    private Context context;
    private View thisView;
    private int page_size = 20;
    private String token = "";
    private EndlessListView lvMember;
    private TextView tvFromEmpty;
    private SwipeRefreshLayout swMember;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;
    private int page = 1;
    private int pageError = -1;
    boolean firstLoad = true;
    boolean isLoadMore = false;
    private MemberInfo memberInfo;
    private RadioButton rbAll, rbNearby;

    //nearby
    public static LatLng arg0 = new LatLng(0.0, 0.0);
    private SwipeRefreshLayout swMemberNearby;
    private EndlessListView lvMemberNear;
    private ArrayList<MemberInfo> list_member_near = new ArrayList<>();
    private ListMemberAdapter adpNear;
    private int page_near = 1;
    private int pageError_near = -1;
    boolean firstLoadNear = true;

    public FragmentPeople() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        setFragmentActivity(getActivity());
        if (thisView == null) {
            thisView = inflater.inflate(R.layout.fragment_people, null);
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
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        tvFromEmpty = (TextView) thisView.findViewById(R.id.tvFromEmpty);
        tvFromEmpty.setVisibility(View.GONE);
        rbNearby = (RadioButton) v.findViewById(R.id.rbNearby);
        rbAll = (RadioButton) v.findViewById(R.id.rbAll);
        swMember = (SwipeRefreshLayout) v.findViewById(R.id.swMember);
        lvMember = (EndlessListView) v.findViewById(R.id.lvMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        adp = new ListMemberAdapter(context, list_member, context.getString(R.string.tab_people), this);
        lvMember.setAdapter(adp);
        lvMember.reset(0, true);
        lvMember.setListener(this);

        rbAll.setOnClickListener(this);
        rbNearby.setOnClickListener(this);

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Bundle bundle = new Bundle();
                bundle.putString("sender_id", list_member.get(position).getId());
                bundle.putString("user_name", list_member.get(position).getUsername());
                switchFragment(Constants.side_nav_fr_chat_detail, bundle, true);*/
                Intent in = new Intent(context, ChatDetailActivity.class);
                in.putExtra("sender_id", list_member.get(position).getId());
                in.putExtra("user_name", list_member.get(position).getUsername());
                in.putExtra("block", list_member.get(position).is_block());
                context.startActivity(in);
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
                        firstLoad = true;
                        searchFriend(page);
                    }
                }, 0);
            }
        });

        //near
        swMemberNearby = (SwipeRefreshLayout) v.findViewById(R.id.swMemberNearby);
        lvMemberNear = (EndlessListView) v.findViewById(R.id.lvMemberNear);
        lvMemberNear.setLoadingView(R.layout.layout_bottom_list_view_enless);
        adpNear = new ListMemberAdapter(context, list_member_near, context.getString(R.string.tab_people), this);
        adpNear.setLocation(true);
        lvMemberNear.setAdapter(adpNear);
        lvMemberNear.reset(0, true);
        lvMemberNear.setListener(this);

        swMemberNearby.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvMemberNear.reset(0, true);
                        page_near = 1;
                        firstLoadNear = true;
                        searchFriendNear(page_near);
                    }
                }, 0);
            }
        });

        lvMemberNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Bundle bundle = new Bundle();
                bundle.putString("sender_id", list_member.get(position).getId());
                bundle.putString("user_name", list_member.get(position).getUsername());
                switchFragment(Constants.side_nav_fr_chat_detail, bundle, true);*/
                Intent in = new Intent(context, ChatDetailActivity.class);
                in.putExtra("sender_id", list_member_near.get(position).getId());
                in.putExtra("user_name", list_member_near.get(position).getUsername());
                in.putExtra("block", list_member_near.get(position).is_block());
                context.startActivity(in);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchFriend(page);
            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.rbAll:
                swMember.setVisibility(View.VISIBLE);
                swMemberNearby.setVisibility(View.GONE);
                rbAll.setTextColor(getResources().getColor(R.color.colorPrimary));
                rbNearby.setTextColor(Color.BLACK);
                checkList(list_member);
                break;
            case R.id.rbNearby:
                if(list_member_near.size()<=0)
                    searchFriendNear(page_near);
                swMember.setVisibility(View.GONE);
                swMemberNearby.setVisibility(View.VISIBLE);
                rbNearby.setTextColor(getResources().getColor(R.color.colorPrimary));
                rbAll.setTextColor(Color.BLACK);
                checkList(list_member_near);
                break;
        }
    }

    private void searchFriend(int page_index){
        JSONObject obj = new JSONObject();
        try {
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
        new DownloadAsyncTask(context, Constants.SEARCH_FRIEND, Constants.ID_METHOD_SEARCH_FRIEND,
                FragmentPeople.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void searchFriendNear(int page_index){
        loadPicHome();
        JSONObject obj = new JSONObject();
        try {
            obj.put("country_code", "");
            obj.put("type", "location");
            obj.put("latitude", arg0.latitude);
            obj.put("longtitude", arg0.longitude);
            obj.put("page_index", page_index);
            obj.put("page_size", page_size);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data searchFriendNear: "+obj.toString());
        new DownloadAsyncTask(context, Constants.SEARCH_FRIEND, Constants.ID_METHOD_SEARCH_FRIEND_NEARBY,
                FragmentPeople.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
            if(adp != null)
                adp.notifyDataSetChanged();
            for (int j = 0; j < member_list.size(); j++){
                for (int i = 0; i < list_member_near.size(); i++){
                    if(list_member_near.get(i).getId().equals(member_list.get(j).getId())){
                        MemberInfo item = member_list.get(j);
                        list_member_near.get(i).setOnline_status(item.isOnline_status());
                        list_member_near.get(i).setJob_status(item.getJob_status());
                        //list_member.get(i).setIs_block(item.is_block());
                        list_member_near.get(i).setOffline_on(item.getOffline_on());
                        break;
                    }
                }
            }
            if(adpNear != null)
                adpNear.notifyDataSetChanged();
        }
    }


    private void checkList(ArrayList<MemberInfo> list){
        if(list.size() > 0){
            tvFromEmpty.setVisibility(View.GONE);
        }else{
            tvFromEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void loadPicHome() {
        //LatLng arg0 = null;
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            arg0 = new LatLng(gps.getLatitude(), gps.getLongitude());
            //	arg0 = new LatLng(1.301718, 103.837854);
            if (Constants.DEBUG_MODE) {
                Log.v("FragmentPeople", "FragmentPeople: latitude: " + gps.getLatitude() + " logtitude: " + gps.getLongitude());
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            if (Constants.DEBUG_MODE) {
                Log.v("FragmentPeople", "conection time out");
            }
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
        new DownloadAsyncTask(context, Constants.SEND_MESSAGE_CHAT, Constants.ID_METHOD_SEND_MESSAGE_CHAT,
                FragmentPeople.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
                        memberInfo.setChatInfo(chatInfo);
                        updateToFragment(memberInfo, Constants.ID_POPUP_TAB_CHAT, 0, 0);
                    }
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
        }else if (processID == Constants.ID_METHOD_SEARCH_FRIEND_NEARBY) {
            swMemberNearby.setRefreshing(false);
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
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
                    if (firstLoadNear) {
                        list_member_near.clear();
                        list_member_near.addAll(dataInfo.getFriends());
                        adpNear.notifyDataSetChanged();
                        firstLoadNear = false;
                        /*if(dataInfo.getTotal_page()>1){
                            isLoadMore = true;
                        }*/
                    } else {
                        list_member_near.addAll(dataInfo.getFriends());
                        adpNear.notifyDataSetChanged();
                        lvMemberNear.hildeFooter();
                        pageError_near = -1;
                    }
                    Log.v("page num ", page_near + "----");
                    Log.v("size list ", list_member_near.size() + "----");
                    //Log.v("size adapter ", adp.getItemCount() + "----");
                }else{
                    if (pageError_near < 0) {
                        Utils.showSimpleDialogAlert(context, response.getMessage());
                    }
                    lvMemberNear.reset(0, true);
                    pageError_near = page_near;
                }
            }else{
                if (pageError_near < 0) {
                    Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
                }
                lvMemberNear.reset(0, true);
                pageError_near = page_near;
            }
            checkList(list_member_near);
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if(processID == Constants.ID_METHOD_SEARCH_FRIEND){
            swMember.setRefreshing(false);
            if (pageError < 0) {
                Utils.showSimpleDialogAlert(context, msg);
            }
            lvMember.reset(0, true);
            pageError = page;
            checkList(list_member);
        }else if(processID == Constants.ID_METHOD_SEARCH_FRIEND_NEARBY){
            swMemberNearby.setRefreshing(false);
            if (pageError_near < 0) {
                Utils.showSimpleDialogAlert(context, msg);
            }
            lvMemberNear.reset(0, true);
            pageError_near = page_near;
            checkList(list_member_near);
        }
    }

    @Override
    public void loadData() {
        if(swMember.getVisibility() == View.VISIBLE){
            if (pageError > 0) {
                searchFriend(pageError);

            } else {
                page = page + 1;
                searchFriend(page);
            }
        }else {
            if (pageError_near > 0) {
                searchFriendNear(pageError_near);

            } else {
                page_near = page_near + 1;
                searchFriendNear(page_near);
            }
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        super.popUpCallback(data, processID, obj, num, index);
        if(processID == Constants.ID_POPUP_CALLBACK_ADAPTER){
            if(data != null){
                memberInfo = (MemberInfo) data;
                if(swMember.getVisibility() == View.VISIBLE){
                    list_member.get(num).setIs_chat(true);
                    adp.notifyDataSetChanged();
                }else {
                    list_member_near.get(num).setIs_chat(true);
                    adpNear.notifyDataSetChanged();
                }
                sendMessage(memberInfo);
            }
        }
    }
}
