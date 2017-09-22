package com.lmq.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.adapter.TouZiCouponAdapter;
import com.lmq.main.api.BaseActivity;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//投资详情-->我要投资
public class Coupon_tzy extends BaseActivity implements OnClickListener {
	ImageView imageview;
	TextView title;
	ListView videoList;
	RefreshLayout refreshLayout;
	TouZiCouponAdapter touZiCouponAdapter;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupon_tzy);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);

		title = (TextView) findViewById(R.id.title);
		title.setText("选择加息劵");

		videoList=(ListView)findViewById(R.id.videoList);
		refreshLayout=(RefreshLayout)findViewById(R.id.refreshLayout);

		initView();
	}
	public int firstVisible = 0, visibleCount = 0, totalCount = 0;

	private void initView() {
		touZiCouponAdapter = new TouZiCouponAdapter(this, list);
		videoList.setAdapter(touZiCouponAdapter);
		videoList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
						Log.e("videoTest", "SCROLL_STATE_FLING");
						break;
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						Log.e("videoTest", "SCROLL_STATE_IDLE");
//                        autoPlayVideo(view);
						break;
					case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
						Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");

						break;
					default:
						break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// firstVisibleItem   当前第一个可见的item
				// visibleItemCount   当前可见的item个数
				if (firstVisible == firstVisibleItem) {
					return;
				}
				firstVisible = firstVisibleItem;
				visibleCount = visibleItemCount;
				totalCount = totalItemCount;
			}
		});

		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				refreshlayout.finishRefresh();

			}
		});
		refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {

				refreshlayout.finishLoadmore();
			}
		});

		//设置 Header 为 Material风格
		refreshLayout.setRefreshHeader(new MaterialHeader(Coupon_tzy.this).setShowBezierWave(true));
		//设置 Footer 为 球脉冲
		refreshLayout.setRefreshFooter(new BallPulseFooter(Coupon_tzy.this).setSpinnerStyle(SpinnerStyle.Scale));

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.title_right:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		getPageInfoHttp();
	}

}
