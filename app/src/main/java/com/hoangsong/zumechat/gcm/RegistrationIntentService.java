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

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationIntentService extends IntentService implements JsonCallback {

    private static final String TAG = Constants.TAG; // + " RegIntentService";
    private static final String[] TOPICS = {"global"};

    private JsonCallback jsonCallback;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        jsonCallback = this;

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(Constants.getGCMSenderID(), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            if(Constants.DEBUG_MODE) {
                Log.i(TAG, "GCM Registration Token: " + token);
            }
            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            //appPrefs.setIsTokenSentToServer(true);

            // TODO: Implement this method to send any registration to your app's servers.
            if(!Prefs.getDeviceID().equals(token) && !Prefs.getDeviceID().equals("")) {
                if(Prefs.getUserInfo() != null)
                    updateRegistrationToServer(token);
            }else if(Prefs.getDeviceID().equals("")){
                updateRegistrationToServer(token);
            }
            Prefs.setDeviceId(token);

            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
            //Prefs.setIsTokenSentToServer(false);

        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void updateRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        JSONObject object = new JSONObject();
        try {
            object.put("device_id", token);
            object.put("api_token", Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postData = object.toString();
        new DownloadAsyncTask(getApplicationContext(), Constants.API_UPDATE_DEVICE_ID, Constants.ID_METHOD_API_UPDATE_DEVICE_ID, jsonCallback, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_API_UPDATE_DEVICE_ID){
            if(data!=null) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    //Prefs.setIsTokenSentToServer(true);
                }else{
                    //Prefs.setIsTokenSentToServer(false);
                    if(Constants.DEBUG_MODE){
                        Log.e(TAG, Constants.API_UPDATE_DEVICE_ID + " error: " + response.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if(Constants.DEBUG_MODE){
            Log.i(TAG, "jsonError: " + msg);
        }

    }
    // [END subscribe_topics]

}
