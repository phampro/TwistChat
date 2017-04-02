package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentFollowed extends BaseFragment implements EndlessListView.EndlessListener{
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
    boolean firstload = true;
    private int current_position = 0;

    public FragmentFollowed() {
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

    @Override
    public void onResume() {
        super.onResume();
        if(list_member.size() <=0){
            getFavourites(page);
        }
        checkList(list_member);
        lvMember.setListener(this);
    }

    private void initView(View v){
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        tvFromEmpty = (TextView) thisView.findViewById(R.id.tvFromEmpty);
        tvFromEmpty.setVisibility(View.GONE);
        swMember = (SwipeRefreshLayout) v.findViewById(R.id.swMember);
        lvMember = (EndlessListView) v.findViewById(R.id.lvMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        lvMember.reset(0, true);
        adp = new ListMemberAdapter(context, list_member, context.getString(R.string.tab_followed));
        lvMember.setAdapter(adp);

        swMember.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvMember.reset(0, true);
                        page = 1;
                        firstload = true;
                        getFavourites(page);
                    }
                }, 0);
            }
        });

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(context, ChatDetailActivity.class);
                in.putExtra("sender_id", list_member.get(position).getId());
                in.putExtra("user_name", list_member.get(position).getUsername());
                in.putExtra("block", list_member.get(position).is_block());
                context.startActivity(in);
            }
        });

        lvMember.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_position = i;
                new DialogActionMember(context, list_member.get(i), -999, FragmentFollowed.this).show();
                return true;
            }
        });

    }

    private void getFavourites(int page_index){
        new DownloadAsyncTask(context, Constants.GET_FAVOURITES+"?page_index="+page_index+"&page_size="+page_size+"&token="+token, Constants.ID_METHOD_GET_FAVOURITES,
                FragmentFollowed.this, false, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
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
                FragmentFollowed.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
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
                FragmentFollowed.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }

    private void removeFavourite(String id_member) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", id_member);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.REMOVE_FAVOURITE, Constants.ID_METHOD_REMOVE_FAVOURITE, this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkList(ArrayList<MemberInfo> list){
        if(list.size() > 0){
            tvFromEmpty.setVisibility(View.GONE);
        }else{
            tvFromEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void updateData(ArrayList<MemberInfo> member_list){
        if(adp != null && member_list != null){
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
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_GET_FAVOURITES) {
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
                        list_member.addAll(dataInfo.getFriends());
                        adp = new ListMemberAdapter(context, list_member, context.getString(R.string.tab_followed));
                        lvMember.setAdapter(adp);
                        firstload = false;
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
        }else if(processID == Constants.ID_METHOD_REMOVE_FAVOURITE){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    list_member.remove(current_position);
                    adp.notifyDataSetChanged();
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if(processID == Constants.ID_METHOD_GET_FAVOURITES){
            swMember.setRefreshing(false);
            if (pageError < 0) {
                Utils.showSimpleDialogAlert(context, msg);
            }
            lvMember.reset(0, true);
            pageError = page;
            checkList(list_member);
        }else
            Utils.showSimpleDialogAlert(context, msg);
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        super.popUpCallback(data, processID, obj, num, index);
        if(processID == Constants.ID_POPUP_ACTION_MEMBER_BLOCK){
            if(data != null){
                MemberInfo memberInfo = (MemberInfo) data;
                if(!memberInfo.is_block()){
                    new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_block_account), 0, FragmentFollowed.this).show();
                }
                else {
                    //unblock account
                    unBlock(list_member.get(current_position).getId());
                }
            }

        }else if(processID == Constants.ID_POPUP_ACTION_MEMBER_DELETE){
            new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_remove_favourite_account), 1, FragmentFollowed.this).show();
        }else if(processID == Constants.ID_POPUP_CONFIRM_YES){
            if(num == 1){
                removeFavourite(list_member.get(current_position).getId());
            }else {
                block(list_member.get(current_position).getId());
            }
        }
    }

    @Override
    public void loadData() {
        if (pageError > 0) {
            getFavourites(pageError);

        } else {
            page = page + 1;
            getFavourites(page);
        }
    }
}
