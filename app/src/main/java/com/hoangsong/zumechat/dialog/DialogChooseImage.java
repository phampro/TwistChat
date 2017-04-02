package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;


/**
 * Created by pvthien on 30-Aug-16.
 */
public class DialogChooseImage extends Dialog implements PopupCallback {
    public PopupCallback popupCallback;
    private Button btnPhotoLibrary, btnTakePhoto, btnCancel;

    public DialogChooseImage(Context context, final PopupCallback popupCallback) {
        super(context);
        this.popupCallback = popupCallback;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_image);
        setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        btnPhotoLibrary = (Button) findViewById(R.id.btnPhotoLibrary);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnPhotoLibrary.setTypeface(Utils.getFontRegular(context));
        btnTakePhoto.setTypeface(Utils.getFontRegular(context));
        btnCancel.setTypeface(Utils.getFontSemibold(context));

        btnPhotoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popupCallback.popUpCallback(null, Constants.ID_POPUP_CHOOSE_IMAGE, null, Constants.ID_POPUP_CHOSE_PHOTO_LIBRARY, 0);
            }
        });
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popupCallback.popUpCallback(null, Constants.ID_POPUP_CHOOSE_IMAGE, null, Constants.ID_POPUP_TAKE_PHOTO, 0);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {

    }
}
