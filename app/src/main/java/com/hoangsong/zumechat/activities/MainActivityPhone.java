package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.adapters.TabMenuAdapter;
import com.hoangsong.zumechat.inappbilling.BuyCreditActivity;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.fragment.AppInfoFragment;
import com.hoangsong.zumechat.fragment.FeedBackFragment;
import com.hoangsong.zumechat.fragment.FragmentChat;
import com.hoangsong.zumechat.fragment.FragmentFollowed;
import com.hoangsong.zumechat.fragment.FragmentPeople;
import com.hoangsong.zumechat.fragment.FragmentTravelTo;
import com.hoangsong.zumechat.fragment.ListAppFragment;
import com.hoangsong.zumechat.helpers.ParserHelper;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.BroadcastSignalRInfo;
import com.hoangsong.zumechat.models.CustomNotification;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.socket.AndroidPlatformComponent;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.NotificationsUtils;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class MainActivityPhone extends AppCompatActivity implements PopupCallback, View.OnClickListener{
    private static final String TAG = MainActivityPhone.class.getSimpleName();
    private String TAB_PEOPLE = "";
    private String TAB_CHAT = "";
    private String TAB_FOLLOWED = "";
    private String TAB_TRAVEL_TO = "";
    //menu
    private  String MENU_SETTING = "";
    private  String MENU_CONTACT_US = "";
    private String MENU_SHARE_APP = "";
    private  String MENU_LOG_OUT = "";
    private  String MENU_DELETE = "";
    private  String MENU_BLOCK = "";
    private  String MENU_MANAGE_ADS = "";
    private  String MENU_ACTIVE_VIP = "";
    public String[] arrMenuPeople;
    public String[] arrMenuChat;
    public String[] arrMenuFollowed;
    public String[] arrMenuTravelTo;

    private int exitNumber = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPeople fragmentPeople;
    private FragmentChat fragmentChat;
    private FragmentFollowed fragmentFollowed;
    private FragmentTravelTo fragmentTravelTo;
    private RelativeLayout vPaddingActionBar;
    private TextView tvStatusConnect;
    private static Context context;
    private static FragmentManager fm;
    private static TextView tvTitle, tvActionMenu, tvSearch;
    private static ImageButton ibtnMenu;
    private ImageView ivAvatar;
    //private Button btnAddXu;
    private boolean isShowAds = false;
    private String current_name_menu = "";

    //Signal
    private HubConnection connection;
    private HubProxy hub;

    //notification
    public static ArrayList<CustomNotification> notificationArrayList = new ArrayList<>();

    //pop back
    private static String sender_id_current = "";
    private static PopupCallback popupCallbackSub;
    private static PopupCallback popupCallbackChat;
    private static PopupCallback popupCallbackFollowed;
    private static  int numPopupSub = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_main);
        context = MainActivityPhone.this;
        EventBus.getDefault().register(this);
        pageInit();
        connectSignalr(Constants.HOST_SIGNAL_R);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            Intent in = new Intent(context, ChatDetailActivity.class);
            in.putExtra("sender_id", extra.getString("sender_id"));
            in.putExtra("user_name", extra.getString("user_name"));
            context.startActivity(in);
        }
    }

    public void pageInit(){
        loadStringData();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvStatusConnect = (TextView) findViewById(R.id.tvStatusConnect);
        tvActionMenu = (TextView) findViewById(R.id.tvActionMenu);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvTitle.setTypeface(Utils.getFontTahoma(context));
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ibtnMenu = (ImageButton)findViewById(R.id.ibtnMenu);

        ibtnMenu.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        loadImageProfile();

        fm = getSupportFragmentManager();

        tvTitle.setText(getString(R.string.app_name));
        loadTab();

        tvActionMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(MENU_SETTING)){
                    startActivity(new Intent(MainActivityPhone.this, SettingActivity.class));
                }else if(s.toString().equals(MENU_CONTACT_US)){
                    Utils.sendMail(MainActivityPhone.this, Constants.EMAIL_CONTACT, getString(R.string.app_name));
                }else if(s.toString().equals(MENU_SHARE_APP)){
                    Utils.showDialogShareText(MainActivityPhone.this, getString(R.string.app_name), Constants.URL_SHARE_APP);
                }else if(s.toString().equals(MENU_LOG_OUT)){
                    new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_log_out_app), 0, MainActivityPhone.this).show();
                }else if(s.toString().equals(MENU_DELETE)){
                    if(current_name_menu.equals(TAB_PEOPLE)){

                    }else if(current_name_menu.equals(TAB_CHAT)){
                        popupCallbackChat.popUpCallback(null, Constants.ID_POPUP_MENU_DELETE, null, 0, 0);
                    }else if(current_name_menu.equals(TAB_FOLLOWED)){
                        showActionMenu(arrMenuFollowed);
                    }else if(current_name_menu.equals(TAB_TRAVEL_TO)){
                        showActionMenu(arrMenuTravelTo);
                    }
                }else if(s.toString().equals(MENU_BLOCK)){

                }else if(s.toString().equals(MENU_MANAGE_ADS)){

                }else if(s.toString().equals(MENU_ACTIVE_VIP)){
                    startActivity(new Intent(context, BuyCreditActivity.class));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnMenu:
                if(current_name_menu.equals(TAB_PEOPLE)){
                    showActionMenu(arrMenuPeople);
                }else if(current_name_menu.equals(TAB_CHAT)){
                    showActionMenu(arrMenuChat);
                }else if(current_name_menu.equals(TAB_FOLLOWED)){
                    showActionMenu(arrMenuFollowed);
                }else if(current_name_menu.equals(TAB_TRAVEL_TO)){
                    showActionMenu(arrMenuTravelTo);
                }
                break;
            case R.id.ivAvatar:
                startActivity(new Intent(MainActivityPhone.this, MyProfileActivity.class));
                break;
            case R.id.tvSearch:
                startActivity(new Intent(MainActivityPhone.this, SearchActivity.class));
                break;
        }
    }

    private void loadImageProfile(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Prefs.getUserInfo()!= null){
                    String img_url = Prefs.getUserInfo().getProfile_url();
                    if(!img_url.equals(""))
                        Picasso.with(MainActivityPhone.this).load(img_url+"&timestamp="+new Random().nextInt(123456)).placeholder(R.drawable.ic_profile_normal).error(R.drawable.ic_profile_normal).transform(new CircleTransform()).into(ivAvatar);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        sender_id_current = "";
        /*try{
            if(connection != null)
                connection.disconnect();
        }catch(Exception e){e.printStackTrace();}*/
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(String type) {
        if(type.equalsIgnoreCase(Constants.PUSH_CONNECT_INTERNET)){
            tvStatusConnect.setVisibility(View.GONE);
            checkConnectSignalR();
        }else if(type.equals(Constants.PUSH_NO_INTERNET)){
            tvStatusConnect.setVisibility(View.VISIBLE);
        }else if(type.equals(Constants.PUSH_RELOAD_MENU)){
            loadImageProfile();
        }
    }

    private void loadStringData(){
        TAB_CHAT = getString(R.string.tab_chat);
        TAB_PEOPLE = getString(R.string.tab_people);
        TAB_TRAVEL_TO = getString(R.string.tab_travel_to);
        TAB_FOLLOWED = getString(R.string.tab_followed);
        MENU_SETTING = getString(R.string.menu_setting);
        MENU_CONTACT_US = getString(R.string.menu_contact_us);
        MENU_SHARE_APP = getString(R.string.menu_share_app);
        MENU_LOG_OUT = getString(R.string.menu_log_out);
        MENU_DELETE = getString(R.string.menu_delete);
        MENU_BLOCK = getString(R.string.menu_block);
        MENU_ACTIVE_VIP = getString(R.string.menu_active_vip);
        MENU_MANAGE_ADS = getString(R.string.menu_manage_ads);
        arrMenuPeople = new String[]{MENU_SETTING, MENU_CONTACT_US, MENU_SHARE_APP, MENU_LOG_OUT};
        arrMenuChat = new String[]{MENU_DELETE, MENU_SHARE_APP};
        arrMenuFollowed = new String[]{MENU_SHARE_APP};
        arrMenuTravelTo = new String[]{MENU_SHARE_APP, Prefs.getUserInfo().getTotal_credit()>0 ? MENU_MANAGE_ADS : MENU_ACTIVE_VIP};
        /*if(Prefs.getUserInfo().getTotal_credit()>0){
            arrMenuTravelTo = new String[]{MENU_SHARE_APP, MENU_MANAGE_ADS};
        }else {
            arrMenuTravelTo = new String[]{MENU_SHARE_APP};
        }*/
    }

    public static void switchFragment(String fragmentName, Bundle bundle, boolean isAddToBackStack) {

        if (fm != null) {
            // Perform the FragmentTransaction to load in the list tab content.
            // Using FragmentTransaction#replace will destroy any Fragments
            // currently inside R.id.fragment_content and add the new Fragment
            // in its place.

            FragmentTransaction ft = fm.beginTransaction();

            Fragment fragment = null;

            if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_app_info))){
                fragment = new AppInfoFragment();
            }

            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_list_app))){
                fragment = new ListAppFragment();
            }

            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_feedback))){
                fragment = new FeedBackFragment();
            }

            /*else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(Constants.side_nav_fr_chat_detail)){
                fragment = new ChatDetailActivity();
            }*/

            if(fragment !=null && ft !=null){


                fragment.setRetainInstance(false);
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame, fragment, fragmentName);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if(isAddToBackStack){
                    ft.addToBackStack(null);
                }
                //ft.commit();

                ft.commitAllowingStateLoss();

                //tvTitle.setText(fragmentName);
					/*
					if(fragmentName.equalsIgnoreCase(context.getString(R.string.side_nav_home))) {
						updateActionBarTitle(true, fragmentName);
					}else{
						updateActionBarTitle(false, fragmentName);
					}
					*/

            }
            //tvTitle.setText(fragmentName);
        }
    }

    private void loadTab() {
        current_name_menu = TAB_PEOPLE;
        fragmentPeople = new FragmentPeople();
        fragmentChat = new FragmentChat();
        fragmentFollowed = new FragmentFollowed();
        fragmentTravelTo = new FragmentTravelTo();
        final TabMenuAdapter tabMenuAdapter = new TabMenuAdapter(getSupportFragmentManager());
        tabMenuAdapter.addFrag(fragmentPeople, TAB_PEOPLE);
        tabMenuAdapter.addFrag(fragmentChat, TAB_CHAT);
        tabMenuAdapter.addFrag(fragmentFollowed, TAB_FOLLOWED);
        tabMenuAdapter.addFrag(fragmentTravelTo, TAB_TRAVEL_TO);
        viewPager.setAdapter(tabMenuAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                current_name_menu = tabMenuAdapter.getPageTitle(tab.getPosition()).toString();
                if(current_name_menu.equals(TAB_PEOPLE)){
                    tvSearch.setVisibility(View.VISIBLE);
                }else {
                    tvSearch.setVisibility(View.GONE  );
                }
                //tabLayout.getTabAt(tab.getPosition()).getCustomView().setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(TAB_PEOPLE);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_browser, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(TAB_CHAT);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_message, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(TAB_FOLLOWED);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_user, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(TAB_TRAVEL_TO);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_travel, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    public static  void showActionMenu(String[] menu){
        Utils.showListPopupWindow(context, menu, tvActionMenu, ibtnMenu);
    }

    //pop back
    public static void popBackToFragment(Object data, int process, int num, int index){
        if(popupCallbackSub != null) {
            popupCallbackSub.popUpCallback(data, process, null, num, index);
        }
    }

    public static  void setSubPopUpCallback(PopupCallback popupCallback, int numPopup){
        popupCallbackSub = popupCallback;
        numPopupSub = numPopup;
    }

    public static  void setPopUpCallback(PopupCallback popupCallback, int index){
        if(index == 1)
            popupCallbackChat = popupCallback;
        else if(index == 2)
            popupCallbackFollowed = popupCallback;
    }

    public static  void setSenderIdCurrent(String sender_id){
        sender_id_current = sender_id;
    }

    public static void updateToFragment(Object data, int process, int num, int index){
        if(process == Constants.ID_POPUP_TAB_CHAT) {
            popupCallbackChat.popUpCallback(data, process, null, num, index);
        }
    }

    @Override
    public void onBackPressed() {
        if(fm!=null && fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        }else{
            if(Prefs.getUserInfo() != null){
                if(exitNumber > 0){
                    moveTaskToBack(true);
                    exitNumber = 0;
                }else{
                    Toast.makeText(MainActivityPhone.this, getString(R.string.msg_exit_app), Toast.LENGTH_SHORT).show();
                    exitNumber++;
                }
            }else{
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CONFIRM_YES){
            if(num == 1){
            }else if(num == 0){
                Prefs.setUserInfo(null);
                Intent in = new Intent(this, LogInActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        }else if(processID == Constants.ID_POPUP_LOG_IN){
            //loadMenu();
        }else if(processID == Constants.ID_POPUP_CONFIRM_OK){
            finish();
        }
    }

    //SignalR

    private void checkConnectSignalR(){
        if(connection != null){
            if(!connection.getState().toString().equalsIgnoreCase("Connected")){
                try {
                    connection.disconnect();
                }catch (Exception e){e.printStackTrace();}
                connectSignalr(Constants.HOST_SIGNAL_R);
                //connectSignalr("http://192.168.10.50/MyApi/signalr/Hubs");
            }
        }
    }

    private void connectSignalr(String host){
        Platform.loadPlatformComponent( new AndroidPlatformComponent() );
        //connection = new HubConnection(host);
        String text = "connType=ZuChat_API&customer_token="+(Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "");
        connection = new HubConnection(host, text, false, new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                //Log.d(getClass().getSimpleName(), "text: "+s);
            }
        });
        hub = connection.createHubProxy("MobileHub");
        //hub = connection.createHubProxy("chatHub");

        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
            if(Constants.DEBUG_MODE){
                Log.e(TAG+": SignalR connected", "SignalR getConnectionId: "+connection.getConnectionId());
            }
            //updateSignalrConnectionID(connection.getConnectionId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        hub.subscribe( this );
        hub.on("receiverMessage", new SubscriptionHandler1<String>()
                {
                    @Override
                    public void run(String returnObject) {
                        final String jsonContent = returnObject;
                        if(Constants.DEBUG_MODE){
                            Log.d(TAG+": jsonContent", jsonContent );
                            Log.e(TAG+": SignalR braodcast", "braodcast message ");
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Response response = ParserHelper.getBroadcastSignalR(jsonContent);
                                if(response != null){
                                    if(response.getError_code() == Constants.ERROR_CODE_SUCCESS && response.getData() != null){
                                        BroadcastSignalRInfo broadcastSignalRInfo = (BroadcastSignalRInfo) response.getData();
                                        //Toast.makeText(MainActivityPhone.this, jsonContent, Toast.LENGTH_LONG).show();
                                        processReceiverMessage(broadcastSignalRInfo);
                                    }else {

                                    }
                                }
                            }
                        });
                    }
                },
                String.class);
    }

    private void processReceiverMessage(BroadcastSignalRInfo broadcastSignalRInfo){
        if(broadcastSignalRInfo != null){
            switch (broadcastSignalRInfo.getType()){
                case 1://message
                    switch (broadcastSignalRInfo.getAction()){
                        case Constants.ACTION_RECEIVED_MESSAGE://received Message
                            if(broadcastSignalRInfo.getChat() != null && broadcastSignalRInfo.getChat().getSender_id().equals(sender_id_current)){
                                popBackToFragment(broadcastSignalRInfo.getChat(), Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE, Constants.ACTION_RECEIVED_MESSAGE, 0);
                            }else {
                                fragmentChat.addMessage(broadcastSignalRInfo.getChat());
                                if(!current_name_menu.equals(TAB_CHAT)){
                                    CustomNotification notification = new CustomNotification();
                                    notification.setBookingId(broadcastSignalRInfo.getChat().getSender_id());
                                    notification.setFrom(broadcastSignalRInfo.getChat().getSender_name());
                                    notification.setMessage(broadcastSignalRInfo.getChat().getChat_messageNotification(this));
                                    notification.setSound(Prefs.getSoundNotification());
                                    notification.setVibrate(Prefs.getVibrationNotification());

                                    int id_notifi = checkNotification(notification.getBookingId());
                                    if(id_notifi != -1){
                                        notification.setId(id_notifi);
                                    }else {
                                        notification.setId(notificationArrayList.size());
                                        notificationArrayList.add(notification);
                                    }
                                    NotificationsUtils.sendNotification(this, notification);
                                }
                            }
                            break;
                        case Constants.ACTION_DELETE_MESSAGE:
                            if(broadcastSignalRInfo.getChat() != null && broadcastSignalRInfo.getChat().getSender_id().equals(sender_id_current)){
                                popBackToFragment(broadcastSignalRInfo.getChat(), Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE, Constants.ACTION_DELETE_MESSAGE, 0);
                            }
                            break;
                        case Constants.ACTION_UPDATE_MESSAGE:
                            if(broadcastSignalRInfo.getChat() != null && broadcastSignalRInfo.getChat().getSender_id().equals(sender_id_current)){
                                popBackToFragment(broadcastSignalRInfo.getChat(), Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE, Constants.ACTION_UPDATE_MESSAGE, 0);
                            }
                            break;
                    }
                    break;
                case 3://member connect
                    if(broadcastSignalRInfo.getMember_list() != null){
                        fragmentPeople.updateData(broadcastSignalRInfo.getMember_list());
                        fragmentChat.updateData(broadcastSignalRInfo.getMember_list());
                        fragmentFollowed.updateData(broadcastSignalRInfo.getMember_list());
                    }
                    break;
            }
        }

    }

    @Override
    protected void onPause() {
        ZuMeChat.setIs_background(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        ZuMeChat.setIs_background(true);
        super.onResume();
    }

    //notification
    public static int checkNotification(String sender_id){
        for (int i = 0; i < notificationArrayList.size(); i++){
            if(notificationArrayList.get(i).getBookingId().equals(sender_id)){
                return notificationArrayList.get(i).getId();
            }
        }
        return -1;
    }
}