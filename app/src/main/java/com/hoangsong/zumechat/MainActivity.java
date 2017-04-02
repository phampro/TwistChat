package com.hoangsong.zumechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hoangsong.zumechat.activities.SplashActivity;
import com.hoangsong.zumechat.gcm.MyGcmListenerService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getResources().getConfiguration().smallestScreenWidthDp>=600) { // honeycomb
        if (isTabletDevice()) {
            //startActivity(new Intent(this, SplashScreenTab.class));
            //startActivity(new Intent(this, SplashActivityTab.class));
            Log.v("Kiem tra thiet bi", "tab let");
            MyGcmListenerService.isTab=true;
        } else {
            Log.v("Kiem tra thiet bi", "phone");
            startActivity(new Intent(this, SplashActivity.class));
            //startActivity(new Intent(this, SplashScreen.class));
            MyGcmListenerService.isTab=false;
        }
	/*	String s= "devicd "+android.os.Build.DEVICE.toString()+"-- incremental--"+android.os.Build.VERSION.INCREMENTAL.toString()+"  model  "+android.os.Build.MODEL + " product"+ android.os.Build.PRODUCT;

		for(int i=0; i<20; i++){
			Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
		}*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
    private boolean isTabletDevice() {
	     /*if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
	         // test screen size, use reflection because isLayoutSizeAtLeast is only available since 11
	         Configuration con = getResources().getConfiguration();
	         try {
	              Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast");
	              Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
	              return r;
	         } catch (Exception x) {
	              return false;
	         }
	      }
	    return false;*/
        return getResources().getBoolean(R.bool.isTablet);


    }
}
