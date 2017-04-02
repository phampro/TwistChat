package com.hoangsong.zumechat.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.dialog.DialogViewPhoto;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.MyDateTimeISO;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<ChatInfo> mDataset = new ArrayList<ChatInfo>();
    private static Activity context;
    private String userid = "";
    public static MyLongItemClickListener myClickListener;
    private Drawable avatar_bitmap= null;
    private PopupCallback popupCallback;


    public static class DataObjectHolder extends RecyclerView.ViewHolder
    implements View
            .OnLongClickListener {
    	RelativeLayout rlChatLeft, rlTvChatRight, rlTvChatLeft;
    	LinearLayout llChatRIght, llInfo;
        TextView tvName, chatTextLeft, chatTextRight, tvInfo, tvRightTime, tvLeftTime, tvIsRead;
        ImageView ivAvata, ivChatLeft, ivChatRight;

        public DataObjectHolder(View itemView) {
            super(itemView);
            rlChatLeft = (RelativeLayout) itemView.findViewById(R.id.rlChatLeft);
            rlTvChatRight = (RelativeLayout) itemView.findViewById(R.id.rlTvChatRight);
            rlTvChatLeft = (RelativeLayout) itemView.findViewById(R.id.rlTvChatLeft);
            llChatRIght = (LinearLayout) itemView.findViewById(R.id.llChatRIght);
            llInfo = (LinearLayout) itemView.findViewById(R.id.llInfo);
            ivAvata = (ImageView) itemView.findViewById(R.id.ivAvata);
            ivChatLeft = (ImageView) itemView.findViewById(R.id.ivChatLeft);
            ivChatRight = (ImageView) itemView.findViewById(R.id.ivChatRight);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            chatTextLeft = (TextView) itemView.findViewById(R.id.msgrLeft);
            chatTextRight = (TextView) itemView.findViewById(R.id.msgrRight);
            tvInfo = (TextView) itemView.findViewById(R.id.msgrInfo);
            tvLeftTime = (TextView) itemView.findViewById(R.id.tvLeftTime);
            tvIsRead = (TextView) itemView.findViewById(R.id.tvIsRead);
            tvRightTime = (TextView) itemView.findViewById(R.id.tvRightTime);
            
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            ListChatAdapter.myClickListener.onLongItemClick(getPosition(), v);
            return false;
        }

        /*@Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }*/
    }

    public void setOnItemLongClickListener(MyLongItemClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ListChatAdapter(Activity context, ArrayList<ChatInfo> myDataset, PopupCallback popupCallback) {
        this.mDataset = myDataset;
        this.userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";
        this.context = context;
        this.popupCallback = popupCallback;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        final ChatInfo chatObj = mDataset.get(position);
    	if(chatObj.getBackground()!=0){
    		if(chatObj.getBackground() == 1){
    			holder.rlTvChatRight.setBackgroundResource(R.drawable.balloon_outgoing_normal);
    		}else if(chatObj.getBackground() == -1){
    			holder.rlTvChatRight.setBackgroundResource(R.drawable.balloon_outgoing_normal_etx);
    		}else if(chatObj.getBackground() == 2){
    			holder.rlTvChatLeft.setBackgroundResource(R.drawable.balloon_incoming_normal);
    		}else if(chatObj.getBackground() == -2){
    			holder.rlTvChatLeft.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
    		}
    	}

        holder.chatTextLeft.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.chatTextRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        holder.chatTextRight.setTextSize(15);
        holder.chatTextLeft.setTextSize(15);
        holder.ivChatRight.setVisibility(View.GONE);
        holder.ivChatLeft.setVisibility(View.GONE);
    	if(userid.equals(chatObj.getSender_id())){
    		holder.rlChatLeft.setVisibility(View.GONE);
    		holder.llChatRIght.setVisibility(View.VISIBLE);
    		holder.llInfo.setVisibility(View.GONE);
            if(!chatObj.is_delete()){
                switch (chatObj.getChat_type()){
                    case Constants.CHAT_TYPE_PHOTO:
                        holder.chatTextRight.setText("");
                        holder.ivChatRight.setVisibility(View.VISIBLE);
                        final String img = chatObj.getPhoto_url();
                        if(!img.equals("")){
                            Picasso.with(context).load(img).into(holder.ivChatRight);
                            holder.ivChatRight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new DialogViewPhoto(context, img).show();
                                }
                            });
                        }
                        //holder.chatTextRight.setBackgroundDrawable(new BitmapDrawable(context.getResources(),Utils.getImageFileFromSDCard(chatObj.getChat_message())));
                        break;
                    case Constants.CHAT_TYPE_TEXT:
                        holder.chatTextRight.setText(chatObj.getChat_messageDecrypt());
                        holder.ivChatRight.setVisibility(View.GONE);
                        break;
                    case Constants.CHAT_TYPE_HI:
                        holder.chatTextRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hand_yellow, 0, 0, 0);
                        holder.chatTextRight.setTextSize(23);
                        holder.chatTextRight.setText(context.getString(R.string.lbl_hi));
                        holder.ivChatRight.setVisibility(View.GONE);
                        break;
                }
                if(chatObj.is_update()){
                    holder.chatTextRight.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_edit_black_24dp), null, null, null);
                }
                if(!chatObj.getCreated_on().equals("") && chatObj.isShowTime()){
                    holder.tvRightTime.setVisibility(View.VISIBLE);
                    holder.tvRightTime.setText(MyDateTimeISO.getFormatHourRegStr(chatObj.getCreated_on()));
                    //holder.tvRightTime.setText(chatObj.getCreated_on_display());
                }else{
                    holder.tvRightTime.setVisibility(View.GONE);
                }
            }else {
                holder.chatTextRight.setText(context.getString(R.string.lbl_message_removed));
                holder.tvRightTime.setVisibility(View.GONE);
            }
    		
    	}else if(!chatObj.getSender_id().equals("")){
    		holder.rlChatLeft.setVisibility(View.VISIBLE);
    		holder.llChatRIght.setVisibility(View.GONE);
    		holder.llInfo.setVisibility(View.GONE);
            if(!chatObj.is_read()){
                holder.tvIsRead.setVisibility(View.VISIBLE);
                if(popupCallback != null)
                    popupCallback.popUpCallback(null, Constants.ID_POPUP_IS_READ_MESSAGE, null, position, 0);
            }else {
                holder.tvIsRead.setVisibility(View.GONE);
            }
            if(!chatObj.is_delete()){
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvName.setText(chatObj.getSender_name());
                switch (chatObj.getChat_type()){
                    case Constants.CHAT_TYPE_PHOTO:
                        holder.chatTextLeft.setText("");
                        holder.ivChatLeft.setVisibility(View.VISIBLE);
                        final String img = chatObj.getPhoto_url();
                        if(!img.equals("")){
                            Picasso.with(context).load(img).into(holder.ivChatLeft);
                            holder.ivChatLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new DialogViewPhoto(context, img).show();
                                }
                            });
                        }
                        //holder.chatTextLeft.setBackgroundDrawable(new BitmapDrawable(context.getResources(),Utils.getImageFileFromSDCard(chatObj.getChat_message())));
                        break;
                    case Constants.CHAT_TYPE_TEXT:
                        holder.ivChatLeft.setVisibility(View.GONE);
                        holder.chatTextLeft.setText(chatObj.getChat_messageDecrypt());
                        break;
                    case Constants.CHAT_TYPE_HI:
                        holder.chatTextLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hand_yellow, 0, 0, 0);
                        holder.chatTextLeft.setTextSize(23);
                        holder.chatTextLeft.setText(context.getString(R.string.lbl_hi));
                        holder.ivChatLeft.setVisibility(View.GONE);
                        break;
                }
                if(chatObj.is_update()){
                    holder.chatTextLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_edit_black_24dp), null);
                }
                if(!chatObj.getCreated_on().equals("") && chatObj.isShowTime()){
                    holder.tvLeftTime.setVisibility(View.VISIBLE);
                    holder.tvLeftTime.setText(MyDateTimeISO.getFormatHourRegStr(chatObj.getCreated_on()));
                    //holder.tvLeftTime.setText(chatObj.getCreated_on_display());
                }else{
                    holder.tvLeftTime.setVisibility(View.GONE);
                }
            }else {
                holder.chatTextLeft.setText(context.getString(R.string.lbl_message_removed));
                holder.tvLeftTime.setVisibility(View.GONE);
                holder.tvName.setVisibility(View.GONE);
            }

    		holder.ivAvata.setBackgroundResource(0);
    		if(chatObj.getBackground()!= -1 && chatObj.getBackground()!= -2){
    			holder.tvName.setVisibility(View.VISIBLE);

                if(avatar_bitmap != null){
                    holder.ivAvata.setImageDrawable(avatar_bitmap);
                }else {
                    String img = chatObj.getSender_profile_url();
                    if(!img.equals(""))
                        Picasso.with(context).load(img).transform(new CircleTransform()).error(R.drawable.ic_profile_normal).resize(200, 200).centerCrop().placeholder(R.drawable.ic_profile_normal).into(holder.ivAvata, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                avatar_bitmap = holder.ivAvata.getDrawable();
                            }

                            @Override
                            public void onError() {
                            }
                        });
                }
    			/*String urlImage = chatObj.getReceiver_profile_url();
        		if(!urlImage.equalsIgnoreCase("")){
                    Picasso.with(context).load(urlImage).transform(new CircleTransform()).error(R.drawable.ic_profile_normal).resize(200, 200).centerCrop().placeholder(R.drawable.ic_profile_normal).into(holder.ivAvata);
        		}*/
    		}else{
    			holder.ivAvata.setImageResource(R.drawable.ic_message_soild);
    			holder.tvName.setVisibility(View.GONE);
    		}
    		
    	}else{
    		holder.rlChatLeft.setVisibility(View.GONE);
    		holder.llChatRIght.setVisibility(View.GONE);
    		holder.llInfo.setVisibility(View.VISIBLE);
    		holder.tvInfo.setText(chatObj.getChat_message());
    	}
    }

    public void addItem(ChatInfo dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public ChatInfo getItem(int index) {
        return this.mDataset.get(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    public interface MyLongItemClickListener {
        public void onLongItemClick(int position, View v);
    }

    public void add(ChatInfo object) {
        mDataset.add(object);
        notifyDataSetChanged();
    }
}