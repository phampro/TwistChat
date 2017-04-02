package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoangsong.zumechat.R;

import java.util.ArrayList;

public class ListPopupDataAdapter extends BaseAdapter {
    private String[] listItems;
    private Context context;

    public ListPopupDataAdapter(Context context,  String[] listItems) {
        //super(context, resource, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return listItems.length;
    }

    @Override
    public Object getItem(int i) {
        return listItems[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView textview = (TextView) row.findViewById(R.id.text_spinner_item);

        textview.setText(listItems[position]);
        return row;
    }
}
