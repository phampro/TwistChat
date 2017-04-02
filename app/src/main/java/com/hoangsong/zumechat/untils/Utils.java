package com.hoangsong.zumechat.untils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListPopupDataAdapter;
import com.hoangsong.zumechat.dialog.DialogOK;
import com.hoangsong.zumechat.sercurity.EasySSLSocketFactory;


public class Utils {
	public static Dialog pd;
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	public static final SimpleDateFormat FULLDATETIMEFORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
	public static final SimpleDateFormat MOTHYEAR = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
	public static final SimpleDateFormat DATE = new SimpleDateFormat("dd E", Locale.ENGLISH);

	public static int minWidth = 405;
	public static int minHeight = 720;
	public static float ratio_scale = 1.6f;
	public static int ratio_resize = 2;

	public static void setTranslucentStatusBar(Window w){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//				w.setStatusBarColor(w.getContext().getResources().getColor(R.color.transparent));
		}
	}

	public static void setViewPaddingStatusBar(RelativeLayout v, Context context){
		if(v != null){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.0F);
				// Changes the height and width to the specified *pixels*
				params.height = getStatusBarHeight(context);
				params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
				v.setLayoutParams(params);
				v.requestLayout();
			}
		}
	}
	public static void setToolbarPaddingStatusBar(Toolbar toolbar, Context context){
		if(toolbar != null){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
				toolbar.setPadding(0, Utils.getStatusBarHeight(context), 0, 0);
			}
		}
	}

	public static void textChange(final EditText id_editText, final TextView id_label){
		id_editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().equals("")){
					id_label.setVisibility(View.GONE);
				}else{
					id_label.setVisibility(View.VISIBLE);
				}
			}
		});

	}
	public static Calendar getDateFromFormat(String dateString,String pattern){
		Calendar cal=null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date date = format.parse(dateString);
			cal=Calendar.getInstance();
			cal.setTime(date);
		} catch (ParseException e) {
			cal=Calendar.getInstance();
			e.printStackTrace();
		}
		return cal;
	}
	public static String getDateFormat1(Calendar calendar,String format){
		try{
			SimpleDateFormat sdf1=new SimpleDateFormat(format);
			String dateTime= sdf1.format(calendar.getTime());
			return dateTime.toUpperCase();
		}catch(Exception ex){
			String str=ex.toString();
		}
		return "";
	}
	public static String getDateFormat2(Date date,String format){
		try{
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			SimpleDateFormat sdf1=new SimpleDateFormat(format);
			String dateTime= sdf1.format(calendar.getTime());
			Log.e("date ",dateTime.toUpperCase());
			return dateTime.toUpperCase();
		}catch(Exception ex){
			String str=ex.toString();
		}
		return "";
	}
	public static void textViewChange(final TextView id_editText, final TextView id_label){
		id_editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().equals("")){
					id_label.setVisibility(View.GONE);
				}else{
					id_label.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	public static void showListPopupWindow(Context context, final String arrTime[], final TextView tvPushData) {
		try {
			final ListPopupWindow lpw = new ListPopupWindow(context);
			lpw.setAdapter(new ListPopupDataAdapter(context, arrTime));
			lpw.setAnchorView(tvPushData);
			lpw.setModal(true);
			lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					tvPushData.setText(arrTime[position]);
					lpw.dismiss();
				}
			});
			lpw.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showListPopupWindow(Context context, final String arrTime[], final TextView tvPushData, final View viewShow) {
		try {
			final ListPopupWindow lpw = new ListPopupWindow(context);
			lpw.setWidth(400);
			lpw.setAdapter(new ListPopupDataAdapter(context, arrTime));
			lpw.setAnchorView(viewShow);
			lpw.setModal(true);
			lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					tvPushData.setText(arrTime[position]);
					lpw.dismiss();
				}
			});
			lpw.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public  static String decodedJWTBody(String JWTEncoded) {
		try {
			String[] split = JWTEncoded.split("\\.");
			return  split[1];
		} catch (Exception e) {
			//Error
		}
		return "";
	}


	public static boolean checkIsPositionAdd(int numCheck, int maxCheck){
		if(numCheck % 2 == 0 && numCheck <= maxCheck){
			return true;
		}else {
			return false;
		}
	}

	public static void sendMail(Context context, String to, String title_contact) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse("mailto:" + to));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, title_contact);
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	/*public static String getHostLocationId(String decodeString){
		try{
			String str=decodeString;
			JSONObject jsonObject=new JSONObject(str);

			JSONObject jsonObject1=jsonObject.getJSONObject("host");
			String hostID=jsonObject1.getString("host_location_id");
			return  hostID;
		}catch (Exception ex){

		}
		return "";
	}*/

	public static void showDialogShareNormal(Context context) {
		Intent galleryIntent = new Intent(Intent.ACTION_SEND);
		galleryIntent.setType("image/jpeg");
		List<ResolveInfo> listGel = context.getPackageManager().queryIntentActivities(galleryIntent, 0);
		for (ResolveInfo res : listGel) {
			Log.d("package: ", res.activityInfo.packageName);
			Log.d("name: ", res.activityInfo.name);
			Log.d("proname: ", res.loadLabel(context.getPackageManager()).toString());
		}
		context.startActivity(galleryIntent);
	}

	public static void showDialogShareText(Context context, String title, String description) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		// Add data to the intent, the receiving app will decide
		// what to do with it.
		//Title Of The Post
		share.putExtra(Intent.EXTRA_SUBJECT, title);
		share.putExtra(Intent.EXTRA_TEXT, description);
		context.startActivity(Intent.createChooser(share, "Share"));
	}

	@SuppressLint("DefaultLocale")
	public static void showSimpleDialogAlert(Context ctx, String msg) {
		if (!(ctx instanceof Activity) || ((Activity)ctx).isFinishing()) return;
		DialogOK alert = new DialogOK(ctx, msg, ctx.getString(R.string.app_name).toUpperCase());
		alert.show();

		
		/*AlertDialog alert = new AlertDialog.Builder(ctx, android.R.style.Theme_Holo_Light_Dialog_MinWidth)
			.setTitle(ctx.getString(R.string.app_name))
			.setMessage(msg)
			.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
				    }
				})
			.create();
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		alert.show();*/
	}



	public static void showNotificationSetting(final Context mContext){
		Intent i = new Intent();
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			i.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
			i.putExtra("app_package", mContext.getPackageName());
			i.putExtra("app_uid", mContext.getApplicationInfo().uid);
		} else /*if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)*/ {
			i.setAction("android.settings.ACTION_APPLICATION_DETAILS_SETTINGS");
			i.addCategory(Intent.CATEGORY_DEFAULT);
			i.setData(Uri.parse("package:" + mContext.getPackageName()));
		}
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			mContext.startActivity(i);
		}catch (Exception e){e.printStackTrace();}
	}

	public static void showSimpleDialogAlert(Context ctx, int resId) {
		showSimpleDialogAlert(ctx, ctx.getString(resId));
	}
	public static void showLoadingDialog(Context context){
		try{
			pd = new Dialog (context);
			pd.requestWindowFeature (Window.FEATURE_NO_TITLE);
			pd.setContentView(R.layout.dialog_progress);
	     	pd.getWindow().setBackgroundDrawableResource (android.R.color.transparent);
	     	pd.setCancelable(false);
	     	pd.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean isShowingLoadingDialog(Context context){
		if(pd!=null){
			return pd.isShowing();
		}else{
			return false;
		}
	}

	public static void hideLoadingDialog(Context context) throws IllegalStateException{
		if(pd!=null && pd.isShowing()){
			pd.dismiss();
		}
	}


	public static Calendar getTodaySGT() {
		// current SGT day
		TimeZone sgt = TimeZone.getTimeZone("GMT+8");

		return Calendar.getInstance(sgt);
	}


	public static boolean isValidString(String string) {
		if(string == null){
			return false;
		}else{
			string = string.trim();
			if(string == "" || string.isEmpty())
				return false;

			else
				return true;
		}
	}
	public static boolean isValidEmail(String email){
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		boolean matchFound = m.matches();
		return matchFound;
	}
	public static boolean isValidDigit(int digit) {
		String data = String.valueOf(digit);
		for(int i=0;i<data.length();i++)
		{
			 if (!Character.isDigit(data.charAt(i)))
	                return false;

		}
		return true;
	}

	public static boolean isValidDouble(String data) {
		try{
			Double.parseDouble(data);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public static boolean isValidDigit(String data) {
		//String data = String.valueOf(digit);
		for(int i=0;i<data.length();i++)
		{
			 if (!Character.isDigit(data.charAt(i)))
	                return false;

		}
		return true;
	}
	
	
	/*public static final boolean isConnectedToInternet(Context context) {
		NetworkInfo info = getActiveNetwork(context);
		return info != null ? info.isConnected() : false;
	}

	public static NetworkInfo getActiveNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		return connManager != null ? connManager.getActiveNetworkInfo() : null;
	}*/

	public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken()!=null){
        	InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

	public static HttpClient sslClient(HttpClient client) {
		try {
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	public static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		public MySSLSocketFactory(SSLContext context)
				throws KeyManagementException, NoSuchAlgorithmException,
				KeyStoreException, UnrecoverableKeyException {
			super(null);
			sslContext = context;
		}

		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public static boolean isConnectionAvailable(Context context) {
		try {
			final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifi.isAvailable() && wifi.isConnected()) {
				return true;
			} else if (mobile.isAvailable() && mobile.isConnected()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}


	public static DefaultHttpClient getHTTPSClient() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);

		HttpConnectionParams.setConnectionTimeout(params, Constants.DEFAULT_CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(params, Constants.DEFAULT_SOCKET_TIMEOUT);
	    ConnManagerParams.setTimeout(params, Constants.DEFAULT_SOCKET_TIMEOUT);
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
		DefaultHttpClient httpClient= new DefaultHttpClient(cm, params);
		return httpClient;
	}


	public static String fortmatTextURL(String url){
		try {
			return  URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		}
	}

	public static String trimText(String text){
		try {
			while (text.contains(" ")){
				text = text.replace(" ", "");
			}
			return text;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return text;
		}
	}


	/**
     * Trust every server - dont check for any certificate
     */
    @SuppressLint("TrulyRandom")
	public static void trustAllHosts() {
	      // Create a trust manager that does not validate certificate chains
	      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	              public X509Certificate[] getAcceptedIssuers() {
	                      return new X509Certificate[] {};
	              }

	              public void checkClientTrusted(X509Certificate[] chain,
	                              String authType) throws CertificateException {
	              }

	              public void checkServerTrusted(X509Certificate[] chain,
	                              String authType) throws CertificateException {
	              }
	      } };

	      // Install the all-trusting trust manager
	      try {
	              SSLContext sc = SSLContext.getInstance("TLS");
	              sc.init(null, trustAllCerts, new java.security.SecureRandom());
	              HttpsURLConnection
	                              .setDefaultSSLSocketFactory(sc.getSocketFactory());
	      } catch (Exception e) {
	              e.printStackTrace();
	      }
    }

	public static int convertDPToPixel(Context context, int dp){
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);
	}

	public static float pixelsToSp(Context context, Float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}



	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scale = 0;

		if (width > height) {
			scale = (float) newWidth / width;

		} else {
			scale = (float) newHeight / height;

		}
		newHeight = (int) (height * scale);
		newWidth = (int) (width * scale);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = bm;
		if (width > 0 && height > 0) {
			resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, false);
		}
		bm = null;
		return resizedBitmap;
	}
	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
	    int sourceWidth = source.getWidth();
	    int sourceHeight = source.getHeight();

	    // Compute the scaling factors to fit the new height and width, respectively.
	    // To cover the final image, the final scaling will be the bigger
	    // of these two.
	    float xScale = (float) newWidth / sourceWidth;
	    float yScale = (float) newHeight / sourceHeight;
	    float scale = Math.max(xScale, yScale);

	    // Now get the size of the source bitmap when scaled
	    float scaledWidth = scale * sourceWidth;
	    float scaledHeight = scale * sourceHeight;

	    // Let's find out the upper left coordinates if the scaled bitmap
	    // should be centered in the new size give by the parameters
	    float left = (newWidth - scaledWidth) / 2;
	    float top = (newHeight - scaledHeight) / 2;

	    // The target rectangle for the new, scaled version of the source bitmap will now
	    // be
	    RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

	    // Finally, we create a new bitmap of the specified size and draw our new,
	    // scaled bitmap onto it.
	    Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
	    Canvas canvas = new Canvas(dest);
	    canvas.drawBitmap(source, null, targetRect, null);

	    return dest;
    }

    public static Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Utils.getRootFolder() + Constants._CACHED_FOLDER[2] + "/" + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

	public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
          return false;
        }
        final String packageName = context.getPackageName();
        for (RunningAppProcessInfo appProcess : appProcesses) {
          if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
            return true;
          }
        }
        return false;
    }
	@SuppressLint("DefaultLocale")
	public static String getCurrentDateTopBar(){
		/*android.text.format.DateFormat df = new android.text.format.DateFormat();
		if(Constants.DEBUG_MODE){
			Log.d("Utils current UTC", (String) df.format(FULLDATETIMEFORMAT, new java.util.Date()));
		}*/
		return (String) MOTHYEAR.format(new Date()).toUpperCase()+"\n"+ DATE.format(new Date()).toUpperCase();
	}
	@SuppressLint("DefaultLocale")
	public static String getCurrentDate(){
		return (String) FULLDATETIMEFORMAT.format(new Date()).toUpperCase();

	}
	public static void createCachedFolder(){
		for (int i = 0; i < Constants._CACHED_FOLDER.length; i++) {
			File direct = new File(getRootFolder() + Constants._CACHED_FOLDER[i]);
			if(!direct.exists())
			{
			   direct.mkdirs();
			   if(Constants.DEBUG_MODE){
					Log.d("---Utils---", "Created folder " + Constants._CACHED_FOLDER[i]+" successfully");
				}
			}
		}
	}
	public static String getRootFolder(){
		return  Environment.getExternalStorageDirectory().toString() + "/Android/data/com.hoangsong.zumechat/cache/" ;
	}
	public static String generateTempFileName(String fileExtension){
		return String.format("%d." + fileExtension,
				System.currentTimeMillis());
	}
	public static ArrayList<String> readFile(Activity context, int path)
	{
		ArrayList<String> list = new ArrayList<String>();
		InputStream inputStream = context.getResources().openRawResource(path);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                list.add(line.toString());
            }
        }
        catch (IOException e)
        {
            return null;
        }
		return list;
	}
	public static Boolean write(String path, String fcontent){
        try {

            String fpath = path;

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

 }
	@SuppressWarnings("resource")
	public static String readTxt(String fpath, String fname){

        BufferedReader br = null;
        String response = null;

           try {

               StringBuffer output = new StringBuffer();

               br = new BufferedReader(new FileReader(fpath+fname));
               String line = "";
               while ((line = br.readLine()) != null) {
                   output.append(line);
               }
               response = output.toString();

           } catch (IOException e) {
               e.printStackTrace();
               return null;

           }
           return response;

    }

	@SuppressWarnings("resource")
	public static ArrayList<String> readTxtArray(String fpath, String fname){

        BufferedReader br = null;
        //String response = null;
        ArrayList<String> response = new ArrayList<String>();

           try {

               //StringBuffer output = new StringBuffer();

               br = new BufferedReader(new FileReader(fpath+fname));
               String line = "";
               while ((line = br.readLine()) != null) {
                   //output.append(line);
                   response.add(line);
               }
              // response = output.toString();

           } catch (IOException e) {
               e.printStackTrace();
               return null;

           }
           return response;

    }
	public static ArrayList<String> readAllFileName(String path, String folder)
	{
		ArrayList<String> list = new ArrayList<String>();
		File yourDir = new File(path, folder);
		for (File f : yourDir.listFiles()) {
		    if (f.isFile())
		    	list.add(f.getName());
		}
		return list;
	}
	public static void deleteFileTxt(String path)
	{
		File deletePath = new File(path);
		deletePath.delete();
	}

	public static String securityMobile(String mobile){
		if(mobile.equalsIgnoreCase("")){
			return "";
		}else{
			if(mobile.length()>=8){
				String temp = "";
				for(int i =0; i< mobile.length()-4; i++){
					temp+="*";
				}
				return temp+=mobile.substring((mobile.length()-4), mobile.length());
			}else{
				return mobile;
			}
		}
	}

	public static int getVersionCode(Context context){
		int ver = -1;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(
					context.getPackageName(), 0);
			ver = info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return ver;
	}

	public static String getVersionName(Context context){
		String ver = "_ _ _";
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(
					context.getPackageName(), 0);
			ver = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return ver;
	}


	/**********************
	 *      font          *
	 *********************/


	public static Typeface getFontBold(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Bold.otf");
		return tf;
	}

	public static Typeface getFontBlack(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Black.otf");
		return tf;
	}

	public static Typeface getFontLight(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Light.otf");
		return tf;
	}


	public static Typeface getFontRegular(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Regular.otf");
		return tf;
	}

	public static Typeface getFontSemibold(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Semibold.otf");
		return tf;
	}

	public static Typeface getFontTahoma(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/texgyreschola-regular.otf");
		return tf;
	}

	/**
	 * Thay doi ca layout
	 * @param view
	 * @param context
	 * @param iType
	 */
	public static void getChangeFont(View view, Context context, int iType) {
		Typeface typeFace=null;
		if(iType == FontStyle.LIGHT.getVal()){
			typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Light.otf");
		}
		else if(iType == FontStyle.REGULAR.getVal()){
			typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/vnf-optima-regular.ttf");
		}
		else if(iType == FontStyle.BOLD.getVal()){
			typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf");
		}else if(iType == FontStyle.BOOK.getVal()){
			typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Semibold.otf");
		}else if(iType == FontStyle.TOHOMA.getVal()){
			typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/texgyreschola-regular.otf");
		}
		try {
			if (view instanceof LinearLayout) {
				int count = ((LinearLayout) view).getChildCount();
				for (int i = 0; i < count; i++) {
					View childView = ((LinearLayout) view).getChildAt(i);
					if (childView instanceof TextView) {
						String str = ((TextView) childView).getText()
								.toString();
						if (!str.startsWith("..........")) {
							((TextView) childView).setTypeface(typeFace);
						}
					} else if (childView instanceof Button) {
						((Button) childView).setTypeface(typeFace);
					} else if (childView instanceof LinearLayout) {
						getChangeFont(childView, context, iType);
					} else if (childView instanceof RelativeLayout) {
						getChangeFont(childView, context, iType);

					} else if (childView instanceof ScrollView) {
						getChangeFont(childView, context, iType);
					}
				}
			} else if (view instanceof RelativeLayout) {
				int count = ((RelativeLayout) view).getChildCount();
				for (int i = 0; i < count; i++) {
					View childView = ((RelativeLayout) view).getChildAt(i);
					if (childView instanceof TextView) {
						String str = ((TextView) childView).getText()
								.toString();
						if (!str.startsWith("..........")
								&& !str.startsWith("*")) {
							((TextView) childView).setTypeface(typeFace);
						}
					} else if (childView instanceof Button) {
						((Button) childView).setTypeface(typeFace);
					} else if (childView instanceof LinearLayout) {
						getChangeFont(childView, context, iType);
					} else if (childView instanceof RelativeLayout) {
						getChangeFont(childView, context, iType);
					} else if (childView instanceof ScrollView) {
						getChangeFont(childView, context, iType);
					}

				}
			} else if (view instanceof ScrollView) {
				int count = ((ScrollView) view).getChildCount();
				for (int i = 0; i < count; i++) {
					View childView = ((ScrollView) view).getChildAt(i);
					if (childView instanceof TextView) {
						String str = ((TextView) childView).getText()
								.toString();
						if (!str.startsWith("..........")) {
							((TextView) childView).setTypeface(typeFace);
						}
					} else if (childView instanceof Button) {
						((Button) childView).setTypeface(typeFace);
					} else if (childView instanceof LinearLayout) {
						getChangeFont(childView, context, iType);
					} else if (childView instanceof RelativeLayout) {
						getChangeFont(childView, context, iType);
					} else if (childView instanceof ScrollView) {
						getChangeFont(childView, context, iType);
					}

				}
			} else if (view instanceof TextView) {
				String str = ((TextView) view).getText().toString();
				if (!str.startsWith("..........")) {
					((TextView) view).setTypeface(typeFace);
				}

			} else if (view instanceof Button) {
				((Button) view).setTypeface(typeFace);
			}
		} catch (Exception e) {
		}
	}
	public static enum FontStyle {
		LIGHT(0), REGULAR(1), BOLD(2), BOOK(3), TOHOMA(4);

		private int sVal;
		private FontStyle(int value) {
			sVal = value;
		}
		public int getVal() {
			return sVal;
		}
	}


	public static int getScreenWidth(Context context){
		return getDisplayMetrics(context).widthPixels;
	}

	private static DisplayMetrics getDisplayMetrics(Context context){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	public static int getScreenHeight(Context context){
		return getDisplayMetrics(context).heightPixels;
	}
	public static void setFullScreen(Activity activity) {
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}else {
			result = 19;
		}
		Log.d("status_bar_height", "status_bar_height: "+result);
		return result;
	}
	/*public static void changeTabLayoutFont(TabLayout tabLayout, Typeface typeface) {

		ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
		int tabsCount = vg.getChildCount();
		for (int j = 0; j < tabsCount; j++) {
			ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
			int tabChildsCount = vgTab.getChildCount();
			for (int i = 0; i < tabChildsCount; i++) {
				View tabViewChild = vgTab.getChildAt(i);
				if (tabViewChild instanceof TextView) {
					((TextView) tabViewChild).setTypeface(typeface, Typeface.NORMAL);
				}
			}
		}
	}*/

	public static String covertTimeString24To12(String time24) {
		String[] timePath = time24.split(":");
		int hours = Integer.parseInt(timePath[0]);
		int minutes = Integer.parseInt(timePath[1]);
		try {
			if (hours <= 12 && hours > 0) {
				return hours + ":" + minutes + " am";
			} else if (hours > 12) {
				return String.valueOf(hours - 12) + ":" + minutes + " pm";
			} else if (hours == 0) {
				return "12:" + minutes + " am";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time24.trim();
	}

	public static byte[] getBytesFromBitmap(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		return stream.toByteArray();
	}

	public static String encodeFileToBase64Binary(Bitmap bitmap) throws IOException {
		// byte[] bytes = loadFile(fileName);
		byte[] bytes = getBytesFromBitmap(bitmap);
		return Base64.encode(bytes).toString();
	}

	/*public static String getProductBalanceListJSON(ArrayList<ProductBalanceInfo> listproduct){

		String json = "";
		json += "[";
		Log.e("Utill.class json", "json: da co goi ham: "+json);
		for(int i = 0; i < listproduct.size(); i++){
			String bitmap = "";
			try {
				bitmap = encodeFileToBase64Binary(listproduct.get(i).getPhoto_bitmap());
			} catch (IOException e) {
				e.printStackTrace();
			}
			json += "{";
			json += "\"name\":\"" + listproduct.get(i).getName() + "\"";
			json += ",\"status_used\":" + listproduct.get(i).getStatus_used();
			json += ",\"photo\":\"" + bitmap + "\"";
			json += "}";
			if(i < listproduct.size()-1){
				json +=",";
			}
		}
		json += "]";

		Log.e("Utill.class json", "json: "+json);
		return json;
	}*/

	public static void playSounDefuat(Context context){
		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(context, notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//hash
	public static String hash(String s, MessageDigestId mdId) {
		StringBuffer buffer = new StringBuffer();
		MessageDigest md = null;
		byte[] hash;
		// check input string
		if (s == null) {
			return buffer.toString();
		}
		// calculate digest
		try {
			md = MessageDigest.getInstance(mdId.toString());
			buffer = new StringBuffer();
			hash = md.digest(s.getBytes());
			for (Byte b : hash) {
				String hex = Integer.toHexString(0xFF & b);
				if (hex.length() == 1)
					buffer.append(0);
				buffer.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static void writeLogOnSD(String sBody) {
		try {
			String fileName = DATEFORMAT.format(new Date()).toUpperCase();

			File root = new File(getRootFolder(), "API-LOGS");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, fileName+".txt");
			write(root+"", sBody);
			FileWriter writer = new FileWriter(gpxfile, true);
			writer.append(FULLDATETIMEFORMAT.format(new Date()).toUpperCase()
					+ ": " + sBody + "\n\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static String sha1(String s) {
		return hash(s, MessageDigestId.SHA1);
	}


	public static String sha256(String s) {
		return hash(s, MessageDigestId.SHA256);
	}


	public static String md5(String s) {
		return hash(s, MessageDigestId.MD5);
	}



	public static String randomUUID(int length) {
		if (length > 0) {
			String uuid = UUID.randomUUID().toString();
			if (uuid.length() <= length)
				return uuid;
			else
				return uuid.substring(0, length - 1);
		} else
			return null;
	}


	public static enum MessageDigestId {
		SHA256("SHA-256"), MD5("MD5"), SHA1("SHA-1");

		private final String name;

		private MessageDigestId(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	//animation
	public static void startAnimationColor(final TextView view){
		ValueAnimator colerAnimation = ObjectAnimator.ofInt(view, "textColor", Color.WHITE, Color.RED);
		colerAnimation.setDuration(300);
		colerAnimation.setEvaluator(new ArgbEvaluator());
		colerAnimation.setRepeatCount(10);
		colerAnimation.setRepeatMode(ValueAnimator.REVERSE);
		colerAnimation.start();
		colerAnimation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				view.setTextColor(Color.WHITE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				view.setTextColor(Color.WHITE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
	}

	public static String getScanerTicketJSON(String ticket, String ref_hash_code, String token){

		String json = "";
		json += "{";
		json += "\"ticket\":\"" + ticket + "\"";
		json += ",\"ref_hash_code\":\"" + ref_hash_code + "\"";
		json += ",\"token\":\"" + token + "\"";
		json += "}";

		return json;
	}

    public static void storeCameraPhotoInSDCard(Bitmap bitmap, String path, String fileName){
        Utils.createCachedFolder();
        File outputFile = new File(path, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void storePhotoInGallery(Context context, Bitmap bitmap){
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File dir = new File(storageDir.getAbsolutePath() + "/Twist");
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		File outputFile = new File(dir+ "/" + MyDateTimeISO.currentDateFormat()+".jpg");
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap scaleCenterCropHalf(Bitmap source) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		int newWidth = getHalfImageWidth(sourceWidth);
		int newHeight = getHalfImageHeight(sourceHeight);

		// Compute the scaling factors to fit the new height and width, respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	public static int getHalfImageHeight(int sourceHeight){
		int newHeight = minHeight;
		if((float)sourceHeight/minHeight > ratio_scale){
			newHeight = (int)(sourceHeight/ratio_resize);
		}
		return newHeight;
	}

	public static int getHalfImageWidth(int sourceWidth){
		int newWidth = minWidth;
		if((float)sourceWidth/minWidth > ratio_scale){
			newWidth = (int)(sourceWidth/ratio_resize);
		}
		return newWidth;
	}

	public static void deleteFile(String path)
	{
		File deletePath = new File(path);
		deletePath.delete();
	}
}
