package com.hoangsong.zumechat.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogConfirmOK;
import com.hoangsong.zumechat.gcm.QuickstartPreferences;
import com.hoangsong.zumechat.gcm.RegistrationIntentService;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.MasterData;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;

import java.util.Locale;

public class SplashActivity extends Activity implements JsonCallback, PopupCallback{

    private static int SPLASH_TIME_OUT = 1000;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //language
    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_splash);
        setLocale(Prefs.getCurrentLanguage());
        // new gcm
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
				/*boolean sentToken = Prefs.getIsTokenSentToServer();
				if (sentToken) {
					//mInformationTextView.setText(getString(R.string.gcm_send_message));
					if(Constants.DEBUG_MODE){
						//Log.d(Constants.TAG, getString(R.string.gcm_send_message));
					}
				} else {
					//mInformationTextView.setText(getString(R.string.token_error_message));
					if(Constants.DEBUG_MODE){
						//Log.d(Constants.TAG, getString(R.string.token_error_message));
					}
				}*/
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

            goToNextPage();
        }

    }

    public void goToNextPage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO handler
                new DownloadAsyncTask(SplashActivity.this, Constants.GET_MASTER_DATA, Constants.ID_METHOD_GET_MASTER_DATA, SplashActivity.this, false, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        //checkPlayServices();
        //Utils.initGA(SplashActivity.this);
        //Utils.updateGA(SplashActivity.this, getString(R.string.ga_screen_splash));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory(){
        System.gc();
    }

    // new gcm
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                if(Constants.DEBUG_MODE){
                    Log.i(Constants.TAG, "This device is not supported.");
                }
                new DialogConfirmOK(SplashActivity.this, "This device is not supported.", getString(R.string.app_name), 0, SplashActivity.this).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_GET_MASTER_DATA){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(response.getData() != null){
                        MasterData masterData = (MasterData) response.getData();
                        Prefs.setMasterData(masterData);
                    }
                }
            }

            if(Prefs.getUserInfo() != null){
                startActivity(new Intent(SplashActivity.this, MainActivityPhone.class));
            }else {
                startActivity(new Intent(SplashActivity.this, LogInActivity.class));
            }
            finish();
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
        //Intent refresh = new Intent(this, AndroidLocalize.class);
        //startActivity(refresh);
        //finish();
    }

    @Override
    public void jsonError(String msg, int processID) {
        //Utils.showSimpleDialogAlert(this, msg);
        if(Prefs.getUserInfo() != null){
            startActivity(new Intent(SplashActivity.this, MainActivityPhone.class));
        }else {
            startActivity(new Intent(SplashActivity.this, LogInActivity.class));
        }
        finish();
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CONFIRM_OK){
            finish();
        }
    }
}
