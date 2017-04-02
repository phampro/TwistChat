package com.hoangsong.zumechat.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.helpers.ParserHelper;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;


public class DownloadAsyncTask extends AsyncTask<String, String, Object> {


    private final String LANGUAGE_EN = "en-GB";
    private final String LANGUAGE_VI = "vi-VN";
    //private static String sBasicAuthentication = "webuser:webserver109";
    private static String sBasicAuthentication = Constants.API_USERNAME+":"+Constants.API_PASSWORD;
    String postData;
    private Context context;
    private ProgressDialog progressDialog;
    private int processID;
    private JsonCallback callback;
    private String url;
    private boolean showDialog;
    private int HTTP_VERB;
    private String errorString = "";

    /**
     *
     * @param context
     * @param methodName
     * @param processID
     * @param callback
     * @param showDialog
     * @param HTTP_VERB
     * @param postData
     */
    public DownloadAsyncTask(Context context, String methodName, int processID, JsonCallback callback, boolean showDialog, int HTTP_VERB, String postData) {
        this.context = context;
        this.processID = processID;
        this.callback = callback;
        this.url = methodName;
        if (Constants.DEBUG_MODE) {
            Log.i("Request URL:", url);
        }
        this.showDialog = showDialog;
        this.HTTP_VERB = HTTP_VERB;
        this.postData = postData;
        if (showDialog) {
            progressDialog = new ProgressDialog(this.context);
            progressDialog.setCancelable(true);
        } else {
            progressDialog = null;
        }

        if (Utils.isConnectionAvailable(context)) {
            execute();
        } else {
            if (progressDialog != null && showDialog) {
                progressDialog.dismiss();
            }
            if (callback != null) {
                callback.jsonError(context.getResources().getString(R.string.alert_no_connection), processID);
            }
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("DownloadAsyncTask", "onCancelled");
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            Utils.showLoadingDialog(context);
        }
        if (!isOnline()) {
            callback.jsonError(context.getResources().getString(R.string.alert_no_connection), processID);
            if (showDialog) {
                Utils.hideLoadingDialog(context);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Object obj) {
        if (errorString.equals("")) {
            if (callback != null) {
                callback.jsonCallback(obj, processID, 0);
            }
        } else {
            callback.jsonError(errorString, processID);
        }
        if (showDialog) {
            try{
                Utils.hideLoadingDialog(context);
            } catch (Exception e){}
        }
    }

    @Override
    protected Object doInBackground(String... obj) {
        BufferedReader in = null;
        String parseString = "";

        Hashtable h=new Hashtable();
        Enumeration enumKey= h.keys();
        List<String> aList = Collections.list(enumKey);

        Log.e("URL",formatParameter(url));

        try {
            if (HTTP_VERB == 0) {        //GET
                HttpGet request = new HttpGet(formatParameter(url));
                HttpClient client = Utils.getHTTPSClient();
                request.setHeader("Authorization", "Basic " + Base64.encodeToString(sBasicAuthentication.getBytes(), Base64.NO_WRAP));
                request.setHeader("Content-type", "application/json");
                request.setHeader("Accept", "application/json");
                request.setHeader("Accept-Language", getLanguage(ZuMeChat.language));
                HttpResponse response = client.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                parseString = sb.toString();
                Object object = new ParserHelper().parseData(context, parseString, processID, response.getStatusLine().getStatusCode());
                return object;
            } else if (HTTP_VERB == 1) {    //POST
                HttpPost request = new HttpPost(formatParameter(url));
                HttpClient client = Utils.getHTTPSClient();
                request.setHeader("Authorization", "Basic "+ Base64.encodeToString(sBasicAuthentication.getBytes(), Base64.NO_WRAP));
                request.setHeader("Content-type", "application/json; charset=UTF-8");
                request.setHeader("Accept", "application/json");
                request.setHeader("Accept-Language", getLanguage(ZuMeChat.language));

                request.setEntity(new StringEntity(postData, "UTF-8"));

                HttpResponse response = client.execute(request);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                if (Constants.DEBUG_MODE) {
                    Log.i(this.getClass().getName() + " POST", url + " Response status: " + response.getStatusLine().getReasonPhrase() + "");
                    //Log.i(this.getClass().getName(), "postData: " + postData);

                }

                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                postData = null;

                parseString = sb.toString();
                ParserHelper parser = new ParserHelper();

                Object object = parser.parseData(context, parseString, processID, response.getStatusLine().getStatusCode());
                return object;
            } else if (HTTP_VERB == 2) {    //DELETE
                HttpDeleteWithBody request = new HttpDeleteWithBody(formatParameter(url));
                request.setHeader("Authorization", "Basic "+ Base64.encodeToString(sBasicAuthentication.getBytes(), Base64.NO_WRAP));
                request.setHeader("Content-type", "application/json");
                request.setHeader("Accept", "application/json");
                request.setHeader("Accept-Language", getLanguage(ZuMeChat.language));

                if (postData != null) {
                    request.setEntity(new StringEntity(postData));
                }

                HttpClient client = Utils.getHTTPSClient();
                HttpResponse response = client.execute(request);
                if (Constants.DEBUG_MODE) {
                    Log.i("DownloadAsync DELETE", response.getStatusLine().getReasonPhrase() + "-" + response.getStatusLine().getStatusCode());
                }
                postData = null;
                return response.getStatusLine().getReasonPhrase();
            }
            Log.e("result",parseString);

        } catch (IOException ex) {
            ex.printStackTrace();
            errorString = context.getResources().getString(R.string.alert_connection_timeout);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorString = context.getResources().getString(R.string.alert_unexpected_error);
        } finally {
            if (in != null) {
                try {
                    System.gc();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... item) {
        /*
		 * ((ArrayAdapter<String>)getListAdapter()).add(item[0]);
		 */
    }

    private String getLanguage(String language){
        if(language.equals(Constants.LANGUAGE_EN)){
            return LANGUAGE_EN;
        }else {
            return LANGUAGE_VI;
        }

    }

    public static enum HTTP_VERB {
        GET(0), POST(1), DEL(2), PATCH(3);
        private int sVal;

        private HTTP_VERB(int value) {
            sVal = value;
        }

        public int getVal() {
            return sVal;
        }
    }

    public static Object loadListGoogleDetail(Context context, String methodName, int processID){
        BufferedReader in = null;
        String parseString = ""; //GET
        try {
            HttpGet request = new HttpGet(methodName);
            HttpClient client = Utils.getHTTPSClient();
            request.setHeader("Authorization", "Basic " + Base64.encodeToString(sBasicAuthentication.getBytes(), Base64.NO_WRAP));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            parseString = sb.toString();
            ParserHelper parser = new ParserHelper();
            Object object = parser.parseData(context, parseString, processID, response.getStatusLine().getStatusCode());
            return object;

        } catch (IOException ex) {
            ex.printStackTrace();
            //Object object = new PlaceInfo("", context.getResources().getString(R.string.alert_connection_timeout), "", "", false);
        } catch (Exception e) {
            e.printStackTrace();
            //Object object = new PlaceInfo("", context.getResources().getString(R.string.alert_unexpected_error), "", "", false);
        } finally {
            if (in != null) {
                try {
                    System.gc();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String formatParameter(String url){
        while (url.contains(" ")){
            url = url.replace(" ", "+");
        }
        return url;
    }

    class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }

        public String getMethod() {
            return METHOD_NAME;
        }
    }

    public class HttpPatch extends HttpPost {
        public static final String METHOD_PATCH = "PATCH";

        public HttpPatch(final String url) {
            super(url);
        }

        @Override
        public String getMethod() {
            return METHOD_PATCH;
        }
    }

}

