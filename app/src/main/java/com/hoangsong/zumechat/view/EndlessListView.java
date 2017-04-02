package com.hoangsong.zumechat.view;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoangsong.zumechat.R;

public class EndlessListView extends ListView implements OnScrollListener {
	
	private View footer;
	private ProgressBar progressBar;
	private TextView tvfooter;
	private EndlessListener listener;
	public View getFooter() {
		return footer;
	}


	public int previousTotal = 0;
	public boolean loading = true;
	public int visibleThreshold = 0;

	public int current_page = 1;

	public void setFooter(View footer) {
		this.footer = footer;
	}

	public void hildeFooter() {

		if(footer != null){
			//footer.setVisibility(View.GONE);
			showLoading(false);
			//tvfooter.setVisibility(View.VISIBLE);
		}
	}

	//private ListOrderHistoryEndlessAdapter adapter;
	
	public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);	
		this.setOnScrollListener(this);
	}

	public EndlessListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnScrollListener(this);
	}

	public EndlessListView(Context context) {
		super(context);		
		this.setOnScrollListener(this);
	}
	
	public void setListener(EndlessListener listener) {
		this.listener = listener;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
				
		if (getAdapter() == null)
			return ;
		
		if (getAdapter().getCount() == 0)
			return ;

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				/*Log.v("visibleItemCount", visibleItemCount + "++++");
				Log.v("totalItemCount", totalItemCount + "++++");
				Log.v("firstVisibleItem", firstVisibleItem + "++++");
				Log.v("previousTotal", previousTotal + "++++");*/
			}
		}
		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// End has been reached

			// Do something
			current_page++;

			if(listener != null)
				listener.loadData();

			loading = true;
			//footer.setVisibility(View.VISIBLE);
			showLoading(true);

			/*Log.v("visibleItemCount load", visibleItemCount + "---");
			Log.v("totalItemCount load", totalItemCount + "---");
			Log.v("firstVisibleItem load", firstVisibleItem + "---");
			Log.v("previousTotal load", previousTotal + "---");*/
		}
	}

	private void showLoading(boolean isShow){
		if(isShow){
			//progressBar.setVisibility(View.VISIBLE);
			//tvfooter.setVisibility(View.GONE);
			try{
				if(this.getFooterViewsCount() <= 0)
					this.addFooterView(footer);
			}catch (Exception e){}
		}else{
			//progressBar.setVisibility(View.GONE);
			//tvfooter.setVisibility(View.VISIBLE);
			try{
				if(this.getFooterViewsCount() >0)
					this.removeFooterView(footer);
			}catch (Exception e){}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	
	public void setLoadingView(int resId) {
		LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = (View) inflater.inflate(resId, null);
		progressBar = (ProgressBar) footer.findViewById(R.id.dialog_progress_bar);
		tvfooter = (TextView) footer.findViewById(R.id.tvFooter);

		//this.addFooterView(footer);
	}

	
	/*public void setAdapter(ListOrderHistoryEndlessAdapter adapter, int current_page) {
		super.setAdapter(adapter);
		this.adapter = adapter;
		footer.setVisibility(View.GONE);
	}*/
	public EndlessListener setListener() {
		return listener;
	}

	public static interface EndlessListener {
		public void loadData() ;
	}

	public void reset(int previoustotal, boolean loadingtemp) {
		previousTotal = previoustotal;
		loading = loadingtemp;
	}
	
}
