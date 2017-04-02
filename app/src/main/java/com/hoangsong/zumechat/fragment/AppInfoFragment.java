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

public class AppInfoFragment extends Fragment {
    private Context context;
    private TextView tvVersion, tvEmail;

    public AppInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_info, container, false);
        context = getActivity();

        tvVersion = (TextView) view.findViewById(R.id.tvVersion);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        tvVersion.setText("Version "+Utils.getVersionName(context));

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sendMail(context, tvEmail.getText().toString(), getString(R.string.app_name));
            }
        });
        return view;
    }

}
