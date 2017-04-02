package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListAdsAdapter;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.ListAdvertisement;
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

public class FragmentTravelTo extends BaseFragment implements EndlessListView.EndlessListener{
    private Context context;
    private View thisView;
    private int page_size = 10;
    private String token = "";
    private TextView tvFromEmpty;
    private SwipeRefreshLayout swMember;
    private EndlessListView lvMember;
    private ArrayList<Advertisement> list_member = new ArrayList<>();
    private ListAdsAdapter adp;
    private ListAdvertisement listAdvertisement;
    private int page = 1;
    private int pageError = -1;
    boolean firstLoad = true;

    public FragmentTravelTo() {
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
        lvMember = (EndlessListView) v.findViewById(R.id.lvMember);
        tvFromEmpty = (TextView) thisView.findViewById(R.id.tvFromEmpty);
        tvFromEmpty.setVisibility(View.GONE);
        swMember = (SwipeRefreshLayout) v.findViewById(R.id.swMember);
        lvMember.setLoadingView(R.layout.layout_bottom_list_view_enless);
        lvMember.reset(0, true);
        adp = new ListAdsAdapter(context, list_member);
        lvMember.setAdapter(adp);

        swMember.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvMember.reset(0, true);
                        page = 1;
                        firstLoad = true;
                        getAdvertisementList(page, false);
                    }
                }, 0);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(list_member.size() <=0){
            getAdvertisementList(page, false);
        }
        lvMember.setListener(this);
    }

    private void checkList(ArrayList<Advertisement> list){
        if(list.size() > 0){
            tvFromEmpty.setVisibility(View.GONE);
        }else{
            tvFromEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getAdvertisementList(int page, boolean showDialog) {
        try {
            new DownloadAsyncTask(context, Constants.GET_ALL_ADVERTISEMENT_LIST + "?page_index=" + page + "&page_size=" + page_size + "&token=" + token, Constants.ID_METHOD_GET_ALL_ADVERTISEMENT_LIST, FragmentTravelTo.this, showDialog, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) e.printStackTrace();
        }
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_GET_ALL_ADVERTISEMENT_LIST) {
            swMember.setRefreshing(false);
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    listAdvertisement = (ListAdvertisement) response.getData();
                    if (firstLoad) {
                        list_member.clear();
                        list_member.addAll(listAdvertisement.getListAds());
                        adp = new ListAdsAdapter(context, list_member);
                        lvMember.setAdapter(adp);
                        firstLoad = false;
                    } else {
                        list_member.addAll(listAdvertisement.getListAds());
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
            getAdvertisementList(pageError, false);

        } else {
            page = page + 1;
            getAdvertisementList(page, false);
        }
    }
}
