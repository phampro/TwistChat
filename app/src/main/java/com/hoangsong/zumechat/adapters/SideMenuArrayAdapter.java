package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.models.SideMenuItem;

import java.util.List;

/**
 * Created by WeiGuang on 14/12/2015.
 */
public class SideMenuArrayAdapter extends ArrayAdapter<SideMenuItem> {
    private final Context context;
    private final List<SideMenuItem> sideMenuItemList;

    public SideMenuArrayAdapter(Context context, List<SideMenuItem> sideMenuItemList) {
        super(context, R.layout.side_menu_row, sideMenuItemList);
        this.context = context;
        this.sideMenuItemList = sideMenuItemList;
    }

    @Override
    public int getCount() {
        return this.sideMenuItemList.size();
    }

    @Override
    public SideMenuItem getItem(int index) {
        return this.sideMenuItemList.get(index);
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        final SideMenuItem sideMenuItem = getItem(index);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.side_menu_row, null);

        ImageView iconImageView = (ImageView) rowView.findViewById(R.id.menu_item_icon);
        iconImageView.setImageResource(sideMenuItem.getIconResource());

        TextView titleTextView = (TextView) rowView.findViewById(R.id.menu_item_text);

        /**if (sideMenuItem.getLabelText().equals(R.string.menu_profile)) {
            if (SharedPreferencesHelper.getInstance(context).getUserId() == null) {
                titleTextView.setText(R.string.menu_login);
            } else {
                titleTextView.setText(R.string.menu_profile);
            }
        } else {
            titleTextView.setText(sideMenuItem.getLabelText());
        }**/
        titleTextView.setText(sideMenuItem.getLabelText());

        return rowView;
    }
}
