package com.hoangsong.zumechat.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListPopupDataAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Encryption;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.MyDateTimeISO;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DialogReportUser extends Dialog {
	public  PopupCallback popupCallback;
	private Context context;
	private ExpandableHeightListView lvReport;
	private Button btnSubmit;

	private String[] arrReport;

	public DialogReportUser(Context context,
			final PopupCallback popupCallback) {
		super(context);
		this.context = context;
		this.popupCallback = popupCallback;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_report_user);
		setCancelable(true);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lvReport =(ExpandableHeightListView) this.findViewById(R.id.lvReport);
		lvReport.setExpanded(true);
		btnSubmit =(Button) this.findViewById(R.id.btnSubmit);

		btnSubmit.setTypeface(Utils.getFontLight(context));

		arrReport = context.getResources().getStringArray(R.array.reportArray);


		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});

		lvReport.setAdapter(new ListPopupDataAdapter(context, arrReport));
		lvReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(popupCallback != null){
					popupCallback.popUpCallback(arrReport[position], Constants.ID_POPUP_CALLBACK_ADAPTER, null, 0, 0);
				}
				dismiss();
			}
		});

	}
}
