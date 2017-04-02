package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.TouchImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by Tang on 05/05/2016.
 */
public class DialogViewPhoto extends Dialog {
    private ImageButton ibtnBack;
    private TouchImageView ivImage;
    private ProgressBar dialog_progress_bar;

    public DialogViewPhoto(final Context context, String image_url) {
        super(context);
        //getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_view_photo);
        setCancelable(true);
        //this.getWindow().setBackgroundDrawableResource(R.color.transparent_black);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        ivImage = (TouchImageView) findViewById(R.id.ivImage);
        ivImage.setMaxZoom(4f);
        dialog_progress_bar = (ProgressBar) findViewById(R.id.dialog_progress_bar);
        dialog_progress_bar.setVisibility(View.GONE);

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if(!image_url.equals("")){
            dialog_progress_bar.setVisibility(View.VISIBLE);
            Picasso.with(context).load(image_url).into(ivImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    dialog_progress_bar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    dialog_progress_bar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
