package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.Image;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pvthiendeveloper on 3/16/2017.
 */

public class ListAdsAdapter extends BaseAdapter implements JsonCallback {
    private Context context;
    private ArrayList<Advertisement> list;

    public ListAdsAdapter(Context context, ArrayList<Advertisement> list) {
        this.context = context;
        this.list = list;
    }

    private String getThumb(ArrayList<Image> images) {
        String url = "";
        int size = images.size();
        for (int i = 0; i < size; i++) {
            Image image = images.get(i);
            if (image.getType().equalsIgnoreCase("thumb")) {
                url = image.getUrl();
            }
        }
        return url;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        final OneItem oneItem;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_list_ads, viewGroup, false);
            oneItem = new OneItem(v);
            v.setTag(oneItem);
        } else {
            oneItem = (OneItem) v.getTag();
        }
        final Advertisement ads = list.get(i);
        oneItem.tvName.setText(ads.getName());
        oneItem.tvContent.setText(ads.getContent());
        oneItem.tvUrl.setText(ads.getUrl());
        String urlThumb = getThumb(ads.getImages());
        if (!urlThumb.equals("")) {
            Picasso.with(context).load(urlThumb).centerCrop().fit().into(oneItem.ivBanner);
        }
        oneItem.ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneItem.showListPopupWindow(ads.getId());
                list.remove(i);
            }
        });
        return v;
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_DELETE_ADVERTISEMENT) {
            if (data != null) {
                Response response = (Response) data;
                String msg = response.getMessage();
                int error = response.getError_code();
                if (error == Constants.ERROR_CODE_SUCCESS) {
                    notifyDataSetChanged();
                } else {
                    Utils.showSimpleDialogAlert(context, msg);
                }
            } else {
                Utils.showSimpleDialogAlert(context, context.getResources().getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }

    private class OneItem {
        ImageView ivBanner;
        TextView tvName, tvContent, tvUrl;
        ImageButton ibtnMenu;

        public OneItem(View v) {
            ivBanner = (ImageView) v.findViewById(R.id.ivBanner);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvContent = (TextView) v.findViewById(R.id.tvContent);
            tvUrl = (TextView) v.findViewById(R.id.tvUrl);
            ibtnMenu = (ImageButton) v.findViewById(R.id.ibtnMenu);
        }

        private void showListPopupWindow(final String idAdver) {
            try {
                String[] listItem = new String[]{context.getResources().getString(R.string.btn_edit), context.getResources().getString(R.string.menu_delete)};
                final ListPopupWindow lpw = new ListPopupWindow(context);
                lpw.setWidth(400);
                lpw.setAdapter(new ListPopupDataAdapter(context, listItem));
                lpw.setAnchorView(ibtnMenu);
                lpw.setModal(true);
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                        } else if (position == 1) {
                            deleteAdvertisement(idAdver);
                        }
                        lpw.dismiss();
                    }
                });
                lpw.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void deleteAdvertisement(String id) {
            try {
                JSONObject postData = new JSONObject();
                postData.put("id", id);
                postData.put("token", Prefs.getUserInfo().getToken());
                new DownloadAsyncTask(context, Constants.DELETE_ADVERTISEMENT, Constants.ID_METHOD_DELETE_ADVERTISEMENT, ListAdsAdapter.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
            } catch (Exception e) {
                if (Constants.DEBUG_MODE) e.printStackTrace();
            }
        }

    }
}
