package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.models.CreditPackageInfo;

import java.util.ArrayList;

public class ListCreditPackageAdapter extends BaseAdapter {
    private ArrayList<CreditPackageInfo> listItems;
    private Context context;

    public ListCreditPackageAdapter(Context context, ArrayList<CreditPackageInfo> listItems) {
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
        return listItems.size();
    }

    @Override
    public CreditPackageInfo getItem(int i) {
        return listItems.get(i);
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

        CreditPackageInfo item = listItems.get(position);
        textview.setText(Html.fromHtml(item.getName()+ "<font color='#FF504E'>" + " ($"+String.format("%.2f",item.getPrice())+"/"+item.getDay()+" "+context.getString(R.string.lbl_days)+")" + "</font>"));
        return row;
    }
}
