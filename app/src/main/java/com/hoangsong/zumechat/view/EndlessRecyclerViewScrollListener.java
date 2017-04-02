package com.hoangsong.zumechat.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
	public int previousTotal = 0;
	public boolean loading = true;
	public int visibleThreshold = 0;
	int firstVisibleItem, visibleItemCount, totalItemCount;

	public int current_page = 0;

	private LinearLayoutManager mLinearLayoutManager;

	public EndlessRecyclerViewScrollListener(LinearLayoutManager linearLayoutManager) {
		this.mLinearLayoutManager = linearLayoutManager;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		
		
		visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mLinearLayoutManager.getItemCount();
		firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
			
		}
		if (!loading){
           if (firstVisibleItem == 0){
                // top of list reached
                // if you start loading
                loading = true;
                current_page++;

    			onLoadMore(current_page);
            }
        }
	}

	public abstract void onLoadMore(int current_page);

	public void reset(int previoustotal, boolean loadingtemp) {
		previousTotal = previoustotal;
		loading = loadingtemp;
	}
	
}
