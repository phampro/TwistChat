package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.MyProfileActivity;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.SideMenuItem;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by WeiGuang on 14/12/2015.
 */
public class ListMemberAdapter extends BaseAdapter {
    private final Context context;
    private View rowView;
    private final List<MemberInfo> arrData;
    private String type = "";
    private boolean is_delete = false;
    private PopupCallback popupCallback;
    private boolean isLocation;

    public ListMemberAdapter(Context context, List<MemberInfo> arrData, String type) {
        this.context = context;
        this.arrData = arrData;
        this.type = type;
    }

    public ListMemberAdapter(Context context, List<MemberInfo> arrData, String type, PopupCallback popupCallback) {
        this.context = context;
        this.arrData = arrData;
        this.type = type;
        this.popupCallback = popupCallback;
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public MemberInfo getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = convertView;
        rowView = inflater.inflate(R.layout.item_list_base, null);
        final ViewHolder holder = new ViewHolder(rowView);
        final MemberInfo item = arrData.get(position);
        holder.llHi.setVisibility(View.VISIBLE);
        holder.tvName.setText(item.getUsername());
        if(!item.getDescription().equals("")){
            holder.tvStatus.setText(item.getDescription());
        }
        if(isLocation){
            holder.tvDistance.setVisibility(View.VISIBLE);
            holder.tvDistance.setText(item.getDistance());
        }else {
            holder.tvDistance.setVisibility(View.GONE);
        }
        holder.tvTimeAfterOffline.setText(item.getOffline_on());
        String img_url = item.getProfile_url();
        if(!img_url.equals(""))
            Picasso.with(context).load(img_url).placeholder(R.drawable.ic_profile_normal).error(R.drawable.ic_profile_normal).transform(new CircleTransform()).into(holder.ivAvatar);
        if(!item.isOnline_status()){
            holder.ivStatus.setImageResource(R.drawable.bg_dot_gay);
        }else {
            switch (item.getJob_status()){
                case Constants.TYPE_STATUS_ONLINE:
                    holder.ivStatus.setImageResource(R.drawable.bg_dot_green);
                    break;
                case Constants.TYPE_STATUS_OFFLINE:
                    holder.ivStatus.setImageResource(R.drawable.bg_dot_gay);
                    break;
                case Constants.TYPE_STATUS_BUSY:
                    holder.ivStatus.setImageResource(R.drawable.bg_dot_red);
                    break;
            }
        }
        if(type.equals(context.getString(R.string.tab_people))){
            if(item.isHasAds() && Constants.IS_SHOW_ADS){
                holder.mAdView.setVisibility(View.VISIBLE);
                holder.tvLine.setVisibility(View.VISIBLE);
                AdRequest adRequest = null;
                if(Constants.IS_ADMOB_PRO){
                    adRequest = new AdRequest.Builder().build();
                }else {
                    adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            // Check the LogCat to get your test device ID
                            .addTestDevice("A02FA5C2D61CF2C23B8B722531DD68E5")
                            .build();
                }
                holder.mAdView.loadAd(adRequest);
            }else {
                holder.mAdView.setVisibility(View.GONE);
                holder.tvLine.setVisibility(View.GONE);
            }
        }else if(type.equals(context.getString(R.string.tab_chat))){
            holder.llHi.setVisibility(View.GONE);
            if(item.is_new())
                holder.llMainItem.setBackgroundResource(R.color.light_grey_03);
            else {
                holder.llMainItem.setBackgroundResource(R.color.white);
            }
            if(item.getChatInfo() != null){
                holder.tvTimeAfterOffline.setText(item.getChatInfo().getCreated_on_display());
                switch (item.getChatInfo().getChat_type()){
                    case Constants.CHAT_TYPE_PHOTO:
                        holder.tvStatus.setText(context.getString(R.string.lbl_send_a_photo));
                        break;
                    case Constants.CHAT_TYPE_TEXT:
                        holder.tvStatus.setText(item.getChatInfo().getChat_messageDecrypt());
                        break;
                    case Constants.CHAT_TYPE_HI:
                        holder.tvStatus.setText(context.getString(R.string.lbl_hi));
                        break;
                }
            }
        }else if(type.equals(context.getString(R.string.tab_followed))){
            holder.llHi.setVisibility(View.GONE);
        }
        if(item.is_chat()){
            holder.ivHi.setImageResource(R.drawable.ic_hand_yellow);
        }else {
            holder.ivHi.setImageResource(R.drawable.ic_hand);
        }
        holder.llHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupCallback != null && !item.is_chat()){
                    popupCallback.popUpCallback(item, Constants.ID_POPUP_CALLBACK_ADAPTER, null, position, 0);
                }
            }
        });
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, MyProfileActivity.class);
                in.putExtra("receiver_id", item.getId());
                context.startActivity(in);
            }
        });
        if(is_delete){
            holder.cbDelete.setVisibility(View.VISIBLE);
            holder.cbDelete.setChecked(item.is_check());
            holder.cbDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupCallback.popUpCallback(null, Constants.ID_POPUP_CALLBACK_ADAPTER, null, position, 0);
                }
            });
        }else {
            holder.cbDelete.setVisibility(View.GONE);
        }
        return rowView;
    }

    public class ViewHolder {
        TextView tvName, tvStatus, tvTimeAfterOffline, tvLine, tvDistance;
        private ImageView ivAvatar, ivStatus, ivHi;
        private LinearLayout llHi, llMainItem;
        private CheckBox cbDelete;
        private AdView mAdView;

        public ViewHolder(View v) {
            this.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
            this.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
            this.ivHi = (ImageView) v.findViewById(R.id.ivHi);
            this.tvName = (TextView) v.findViewById(R.id.tvName);
            this.tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            this.tvTimeAfterOffline = (TextView) v.findViewById(R.id.tvTimeAfterOffline);
            this.tvDistance = (TextView) v.findViewById(R.id.tvDistance);
            this.tvLine = (TextView) v.findViewById(R.id.tvLine);
            this.llHi = (LinearLayout) v.findViewById(R.id.llHi);
            this.llMainItem = (LinearLayout) v.findViewById(R.id.llMainItem);
            this.cbDelete = (CheckBox) v.findViewById(R.id.cbDelete);
            mAdView = (AdView) v.findViewById(R.id.adView);
        }
    }

    public boolean is_delete() {
        return is_delete;
    }

    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setLocation(boolean location) {
        isLocation = location;
    }
}
