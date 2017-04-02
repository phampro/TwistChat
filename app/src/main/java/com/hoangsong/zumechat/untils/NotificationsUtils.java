package com.hoangsong.zumechat.untils;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.ChatDetailActivity;
import com.hoangsong.zumechat.activities.MainActivityPhone;
import com.hoangsong.zumechat.gcm.MyGcmListenerService;
import com.hoangsong.zumechat.models.CustomNotification;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Tang on 29/06/2016.
 */
public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

            ApplicationInfo appInfo = context.getApplicationInfo();

            String pkg = context.getApplicationContext().getPackageName();

            int uid = appInfo.uid;

            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

            try {

                appOpsClass = Class.forName(AppOpsManager.class.getName());

                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int)opPostNotificationValue.get(Integer.class);

                return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }else{
            return true;
        }
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public static void showSettingsAlert(final Context mContext, final String package_name, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Push notification setting");

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // On pressing Settings button
        alertDialog.setPositiveButton("setting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent();
                        i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + package_name));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try{
                            mContext.startActivity(i);
                        }catch (Exception e){e.printStackTrace();}
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public static void sendNotification(Context context, CustomNotification customNotification) {


        String title = customNotification.getFrom();
        long when = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder = null;
        if(!customNotification.getSound()&& !customNotification.getVibrate()){
            mBuilder = new NotificationCompat.Builder(context)
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
            mBuilder = new NotificationCompat.Builder(context)
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
            mBuilder = new NotificationCompat.Builder(context)
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
            mBuilder = new NotificationCompat.Builder(context)
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
        Intent notificationIntent = new Intent(context, MainActivityPhone.class);
        if(customNotification!=null){
            /*Bundle bundle = new Bundle();
            bundle.putSerializable("customNotification", customNotification);
            notificationIntent.putExtras(bundle);*/
            notificationIntent.putExtra("sender_id", customNotification.getBookingId());
            notificationIntent.putExtra("user_name", customNotification.getFrom());
            notificationIntent.putExtra("block", false);
        }

        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
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

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(customNotification.getId(), mBuilder.build()); //Constants.NOTIFICATION_TYPE_ANNOUNCEMENT, mBuilder.build());
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        if(notifyId != -1){
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
            nMgr.cancel(notifyId);
        }
    }
}