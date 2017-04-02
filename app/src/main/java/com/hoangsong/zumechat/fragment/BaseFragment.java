package com.hoangsong.zumechat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hoangsong.zumechat.activities.MainActivityPhone;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;

/**
 * Created by HOAI AN on 11/1/2016.
 */

public class BaseFragment extends Fragment implements PopupCallback,JsonCallback, View.OnClickListener {


    private Activity activity;

    public void setFragmentActivity(Activity activity) {
        this.activity = activity;
    }

    public void  switchFragment(String fragmentName, Bundle bundle, boolean isAddToBackStack){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).switchFragment(fragmentName, bundle, isAddToBackStack);
            }
        }

    }

    public void onBackPressed(){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).onBackPressed();
            }
        }
    }

    public void getGetBookmarked(){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                //((MainActivityPhone)activity).getGetBookmarked();
            }
        }
    }

    public void updateGA(String title, boolean showMenu){
        if(activity !=null ){

            if(activity instanceof MainActivityPhone){
                //((MainActivityPhone)activity).updateGA(title, showMenu);
            }
        }
    }

    public void updateMenuRightGA(boolean showActionText, String textAction, int textColor, boolean showNotifi){
        if(activity !=null ){

            if(activity instanceof MainActivityPhone){
                //((MainActivityPhone)activity).updateMenuRightGA(showActionText, textAction, textColor, showNotifi);
            }
        }
    }

    public void popBackToFragment(Object data, int process, int num, int index){
        if(activity !=null ){

            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).popBackToFragment(data, process, num, index);
            }
        }
    }

    public void updateToFragment(Object data, int process, int num, int index){
        if(activity !=null ){

            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).updateToFragment(data, process, num, index);
            }
        }
    }

    public  void setSubPopUpCallback(PopupCallback popupCallback, int num){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).setSubPopUpCallback(popupCallback, num);
            }
        }
    }

    public  void setPopUpCallback(PopupCallback popupCallback, int index){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).setPopUpCallback(popupCallback, index);
            }
        }
    }

    public  void setSenderIdCurrent(String sender_id){
        if(activity !=null ){
            if(activity instanceof MainActivityPhone){
                ((MainActivityPhone)activity).setSenderIdCurrent(sender_id);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {

    }

    @Override
    public void jsonError(String msg, int processID) {

    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {

    }
}
