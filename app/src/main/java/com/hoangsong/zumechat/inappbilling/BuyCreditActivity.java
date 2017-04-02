package com.hoangsong.zumechat.inappbilling;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListCreditPackageAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.CreditPackageInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.ExpandableHeightListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BuyCreditActivity extends Activity implements JsonCallback{
	private ExpandableHeightListView lvReport;
	private ListCreditPackageAdapter adp;
	private Button btnSubmit;
	private String token = "";
	private LinearLayout llMainContent;

	//InAppBilling
	private static final String TAG = "com.hoangsong.zumechat.inappbilling";
	//IabHelper mHelper;
	//static final String ITEM_SKU = "com.vgroupsolutions.tppbook.zzz";
	static final String ITEM_SKU = "android.test.purchased";
	private String isPay = "false";
	boolean blnBind;
	// log tag
	String tag = "in_app_billing_ex";

	private IInAppBillingService mService;
	ServiceConnection mServiceConn;

	private ArrayList<CreditPackageInfo> list_credit_package = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.setTranslucentStatusBar(getWindow());
		setContentView(R.layout.dialog_buy_credit);

		token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
		llMainContent = (LinearLayout) findViewById(R.id.llMainContent);
		lvReport =(ExpandableHeightListView) this.findViewById(R.id.lvReport);
		lvReport.setExpanded(true);
		btnSubmit =(Button) this.findViewById(R.id.btnSubmit);

		btnSubmit.setTypeface(Utils.getFontLight(this));

		llMainContent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		adp = new ListCreditPackageAdapter(this, list_credit_package);
		lvReport.setAdapter(adp);
		lvReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*if(popupCallback != null){
					//popupCallback.popUpCallback(list_credit_package.get(position), Constants.ID_POPUP_CALLBACK_ADAPTER, null, 0, 0);
				}*/
				//finish();
				buy();
			}
		});
		getCreditPackage();

		mServiceConn = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				mService = null;
			}

			@Override
			public void onServiceConnected(ComponentName name,
										   IBinder service) {
				mService = IInAppBillingService.Stub.asInterface(service);
			}
		};

		try{
			Intent serviceIntent = new Intent(
					"com.android.vending.billing.InAppBillingService.BIND");
			serviceIntent.setPackage("com.android.vending");
			blnBind = bindService(serviceIntent, mServiceConn,
					Context.BIND_AUTO_CREATE);
		}catch(Exception e){Toast.makeText(this, "loi o day", Toast.LENGTH_LONG).show();}
	}

	private void getCreditPackage() {
		new DownloadAsyncTask(this, Constants.GET_CREDIT_PACKAGE, Constants.ID_METHOD_GET_CREDIT_PACKAGE, this, true,
				DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
	}

	private void buyCreditPackage(int package_id) {
		try {
			JSONObject postData = new JSONObject();
			postData.put("credit_package", package_id);
			postData.put("token", token);
			if(Constants.DEBUG_MODE)
				Log.d("postData", "postData: "+postData.toString());
			new DownloadAsyncTask(this, Constants.BUY_CREDIT_PACKAGE, Constants.ID_METHOD_BUY_CREDIT_PACKAGE, this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void jsonCallback(Object data, int processID, int index) {
		if(processID == Constants.ID_METHOD_GET_CREDIT_PACKAGE){
			if(data != null){
				Response response = (Response) data;
				if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
					if(response.getData() != null){
						ArrayList<CreditPackageInfo> list = (ArrayList<CreditPackageInfo>) response.getData();
						list_credit_package.clear();
						list_credit_package.addAll(list);
						adp.notifyDataSetChanged();
					}
				}else {
					Utils.showSimpleDialogAlert(this, response.getMessage());
				}
			}else {
				Utils.showSimpleDialogAlert(this, getString(R.string.alert_unexpected_error));
			}
		}else if(processID == Constants.ID_METHOD_BUY_CREDIT_PACKAGE){
			if(data != null){
				Response response = (Response) data;
				if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
					if(response.getData() != null){
						AccountInfo accountInfo = (AccountInfo) response.getData();
						Prefs.setUserInfo(accountInfo);
						Utils.showSimpleDialogAlert(this, response.getMessage());
						finish();
					}
				}else {
					Utils.showSimpleDialogAlert(this, response.getMessage());
				}
			}else {
				Utils.showSimpleDialogAlert(this, getString(R.string.alert_unexpected_error));
			}
		}
	}

	@Override
	public void jsonError(String msg, int processID) {

	}

	@Override
	protected void onDestroy() {
		if (mService != null) {
			unbindService(mServiceConn);
		}
		super.onDestroy();
	}

	//InAppBilling
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1001) {
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

			if (resultCode == RESULT_OK) {
				try {
					JSONObject jo = new JSONObject(purchaseData);
					//String sku = jo.getString("productId");
					String token = jo.getString("purchaseToken");
					//Prefs.setKeyBuy(token.toString());
					isPay = "true";
					buyCreditPackage(list_credit_package.get(0).getId());
					//onBackPressed();

				} catch (JSONException e) {
					System.out.println("Failed to parse purchase data.");
					e.printStackTrace();
				}
			}
		}
	}

	public void buy() {
		ArrayList<String> skuList = new ArrayList<String>();
		skuList.add(ITEM_SKU);
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
		Bundle skuDetails;
		try {
			skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);

			int response = skuDetails.getInt("RESPONSE_CODE");
			if (response == 0) {

				ArrayList<String> responseList = skuDetails
						.getStringArrayList("DETAILS_LIST");

				for (String thisResponse : responseList) {
					JSONObject object = new JSONObject(thisResponse);
					String sku = object.getString("productId");
					String price = object.getString("price");
					//String type = object.getString("type");
					if (sku.equals(ITEM_SKU)) {
						System.out.println("price " + price);
						//System.out.println("sku " + sku);
						//System.out.println("type " + type);
						Bundle buyIntentBundle = mService
								.getBuyIntent(3, getPackageName(), sku,
										"inapp",
										"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
						PendingIntent pendingIntent = buyIntentBundle
								.getParcelable("BUY_INTENT");
						if (buyIntentBundle.getInt("RESPONSE_CODE") == 0) {
							try{
								startIntentSenderForResult(
										pendingIntent.getIntentSender(), 1001,
										new Intent(), Integer.valueOf(0),
										Integer.valueOf(0), Integer.valueOf(0));
							}catch(IntentSender.SendIntentException e){e.printStackTrace();}
						}
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
