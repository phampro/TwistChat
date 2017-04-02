package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tang on 10/22/2016.
 */

public class FeedBackFragment extends Fragment implements JsonCallback{
    private Context context;

    private LinearLayout llMainContent;
    private TextView tvHinLabelTitle, tvHinLabelContent, tvHinLabelPhone, tvHinLabelEmail;
    private EditText txtTitle, txtContent, txtPhone, txtEmail;
    private Button btnSenFeedBack;

    public FeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        context = getActivity();
        llMainContent = (LinearLayout) view.findViewById(R.id.llMainContent);
        tvHinLabelTitle = (TextView) view.findViewById(R.id.tvHinLabelTitle);
        tvHinLabelContent = (TextView) view.findViewById(R.id.tvHinLabelContent);
        tvHinLabelPhone = (TextView) view.findViewById(R.id.tvHinLabelPhone);
        tvHinLabelEmail = (TextView) view.findViewById(R.id.tvHinLabelEmail);

        txtTitle = (EditText) view.findViewById(R.id.txtTitle);
        txtContent = (EditText) view.findViewById(R.id.txtContent);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);

        Utils.textChange(txtTitle, tvHinLabelTitle);
        Utils.textChange(txtContent, tvHinLabelContent);
        Utils.textChange(txtPhone, tvHinLabelPhone);
        Utils.textChange(txtEmail, tvHinLabelEmail);

        btnSenFeedBack = (Button) view.findViewById(R.id.btnSenFeedBack);

        btnSenFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txtTitle.getText().toString();
                String content = txtContent.getText().toString();
                String phone = txtPhone.getText().toString();
                String email = txtEmail.getText().toString();
                if(!Utils.isValidString(content)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_enter_content));
                }else if(!Utils.isValidString(email)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_enter_mail));
                }else if(!Utils.isValidEmail(email)){
                    Utils.showSimpleDialogAlert(context, context.getString(R.string.msg_error_mail_invalid));
                }else {
                    senFeedBack(title, content, phone, email);
                }
            }
        });

        return view;
    }

    private void senFeedBack(String title, String content, String phone, String email){
        JSONObject objectUser = new JSONObject();
        try {
            objectUser.put("title", title);
            objectUser.put("content", content);
            objectUser.put("phone", phone);
            objectUser.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postData = objectUser.toString();
        new DownloadAsyncTask(context, Constants.FEEDBACK, Constants.ID_METHOD_FEEDBACK, FeedBackFragment.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData);
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_FEEDBACK){
            if(data != null){
                Response response = (Response) data;
                Utils.showSimpleDialogAlert(context, response.getMessage());
            }else {
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }
}
