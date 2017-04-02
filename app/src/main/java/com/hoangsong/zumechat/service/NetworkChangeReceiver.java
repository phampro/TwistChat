package com.hoangsong.zumechat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hoangsong.zumechat.untils.Constants;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Tang on 3/19/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        /*String status = NetworkUtil.getConnectivityStatusString(context);

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();*/
        if(NetworkUtil.getConnectivityStatus(context) == NetworkUtil.TYPE_NOT_CONNECTED){
            sendBookingEvent(Constants.PUSH_NO_INTERNET);
        }else {
            sendBookingEvent(Constants.PUSH_CONNECT_INTERNET);
        }
    }

    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }
}
