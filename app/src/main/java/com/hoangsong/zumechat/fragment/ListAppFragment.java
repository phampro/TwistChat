package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.untils.Utils;

/**
 * Created by Tang on 03/10/2016.
 */

public class ListAppFragment extends Fragment {
    private Context context;
    private TextView tvMenuAppInfo;

    public ListAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_app, container, false);
        context = getActivity();
        tvMenuAppInfo = (TextView) view.findViewById(R.id.tvMenuAppInfo);
        tvMenuAppInfo.setTypeface(Utils.getFontTahoma(context));

        return view;
    }

}
