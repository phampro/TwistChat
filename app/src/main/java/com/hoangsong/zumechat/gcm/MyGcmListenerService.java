/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hoangsong.zumechat.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.google.android.gms.gcm.GcmListenerService;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.MainActivityPhone;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.CustomNotification;
import com.hoangsong.zumechat.untils.Constants;

import java.util.concurrent.CancellationException;

public class MyGcmListenerService extends GcmListenerService {

    public static boolean isTab = false;
    private static final String TAG = Constants.TAG; // + " MyGcmListenerService";
//    private AppPreferences appPrefs;
//    private DatabaseHandler db;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        try{
            String message = data.getString("message");
            String sound = data.getString("sound");
            String vibrate = data.getString("vibrate");
            if(Constants.DEBUG_MODE) {
                Log.d(TAG, "data: " + data.toString());
                Log.d(TAG, "From: " + from);
                Log.d(TAG, "Message: " + message);
                Log.d(TAG, "sound: " + sound);
                Log.d(TAG, "vibrate: " + vibrate);
            }
            CustomNotification notification = new CustomNotification();
            notification.setMessage(message);
            notification.setSound(Prefs.getSoundNotification());
            notification.setVibrate(Prefs.getVibrationNotification());
            sendNotification(notification);

        } catch(CancellationException e){
            if(Constants.DEBUG_MODE){
                Log.e(Constants.TAG, MyGcmListenerService.class.getName() + " CancellationException: " + e.toString());
            }
        } /*catch (InterruptedException e) {
            if(Constants.DEBUG_MODE){
                Log.e(Constants.TAG, MyGcmListenerService.class.getName() + " InterruptedException: " + e.toString());
            }
        } catch (ExecutionException e) {
            if(Constants.DEBUG_MODE){
                Log.e(Constants.TAG, MyGcmListenerService.class.getName() + " ExecutionException: " + e.toString());
            }
        } */catch (Exception e) {
            if(Constants.DEBUG_MODE){
                Log.e(Constants.TAG, MyGcmListenerService.class.getName() + " Exception: " + e.toString());
            }
        }




        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     *
     */
    public void sendNotification(CustomNotification customNotification) {


        String title = getString(R.string.app_name);
        long when = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder = null;
        if(!customNotification.getSound() && !customNotification.getVibrate()){
            mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(customNotification.getMessage())// + " " + notificationsList.get(notificationsList.size()-1).getTime())
                    //.setNumber(count)
                    .setWhen(when)
                    .setAutoCancel(true)
            //.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationsList.get(notificationsList.size()-1).getMSG() + " " + notificationsList.get(notificationsList.size()-1).getTime()))
            //.setStyle(bigTextStyle)
            //.setTicker(message)
            ;
        }else if(customNotification.getSound()){
            mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(customNotification.getMessage())// + " " + notificationsList.get(notificationsList.size()-1).getTime())
                    //.setNumber(count)
                    .setWhen(when)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
            //.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationsList.get(notificationsList.size()-1).getMSG() + " " + notificationsList.get(notificationsList.size()-1).getTime()))
            //.setStyle(bigTextStyle)
            //.setTicker(message)
            ;
        }else if(customNotification.getVibrate()){
            mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(customNotification.getMessage())// + " " + notificationsList.get(notificationsList.size()-1).getTime())
                    //.setNumber(count)
                    .setWhen(when)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
            //.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationsList.get(notificationsList.size()-1).getMSG() + " " + notificationsList.get(notificationsList.size()-1).getTime()))
            //.setStyle(bigTextStyle)
            //.setTicker(message)
            ;
        }else{
            Log.e(Constants.TAG, MyGcmListenerService.class.getName() + " Exception: test all" );
            mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(customNotification.getMessage())// + " " + notificationsList.get(notificationsList.size()-1).getTime())
                    //.setNumber(count)
                    .setWhen(when)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
            //.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationsList.get(notificationsList.size()-1).getMSG() + " " + notificationsList.get(notificationsList.size()-1).getTime()))
            //.setStyle(bigTextStyle)
            //.setTicker(message)
            ;
        }

        // Creates an explicit intent for an Activity in your app
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivityPhone.class);
        if(customNotification!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("customNotification", customNotification);
            notificationIntent.putExtras(bundle);
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivityPhone.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        customNotification.getId(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(customNotification.getId(), mBuilder.build()); //Constants.NOTIFICATION_TYPE_ANNOUNCEMENT, mBuilder.build());
    }
}
