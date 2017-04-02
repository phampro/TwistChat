package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private int mResource;
    private List<? extends Map<String, ?>> mSelfData;
    private String[] from;
    private int[] to;

    public ListViewAdapter(Context context, List<? extends Map<String, ?>> data, int resouce, String[] from, int[] to) {
        this.mSelfData = data;
        this.mResource = resouce;
        this.mSelfData = data;
        this.from = from;
        this.to = to;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mSelfData.size();
    }

    public Object getItem(int position) {
        return mSelfData.get(position);
    }

    public String get(int position, Object key) {
        Map<String, ?> map = (Map<String, ?>) getItem(position);
        return map.get(key).toString();
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Format ListView's Item
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mResource, null);
        }

        Map<String, ?> item = mSelfData.get(position);

        int count = to.length;

        for (int i = 0; i < count; i++) {
            View v = convertView.findViewById(to[i]);
            bindView(v, item, from[i]);
        }

        convertView.setTag(position);

        return convertView;
    }

    private void bindView(View view, Map<String, ?> item, String from) {
        Object data = item.get(from);

        if (view instanceof TextView) {
            ((TextView) view).setText(data == null ? "" : data.toString());
        }
    }

}