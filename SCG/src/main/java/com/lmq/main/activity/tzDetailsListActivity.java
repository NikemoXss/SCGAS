package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.item.tzDetailItem;
import com.lmq.main.util.Default;

public class tzDetailsListActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView mListView;
	private List mList;
	private tzDetailAdapter adapter;

	private long itemId;
	private int itemType;

	private int maxPage, curPage = 1, pageCount = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzdetail);
		initView();
	}

	public void initView() {
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.tzdetail);

		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setMode(PullToRefreshBase.Mode.BOTH);

		findViewById(R.id.back).setOnClickListener(this);

		Intent intent = getIntent();
		if (intent != null) {
			itemId = intent.getLongExtra("id", 0);
			itemType = intent.getIntExtra("type", 0);
		}

		adapter = new tzDetailAdapter();
		mListView.setAdapter(adapter);

		// mListView.setOnLoadMoreInfo(new LoadMoreInfo() {
		// public void onRefresh() {
		// curPage = 1;
		// doHttp(false);
		// }
		//
		// public void onLoadMore() {
		// if (curPage + 1 <= maxPage) {
		// curPage++;
		// JsonBuilder builder = new JsonBuilder();
		// builder.put("limit", pageCount);
		// builder.put("page", curPage);
		// doHttp(true);
		// } else {
		// showCustomToast(("无更多数据！"));
		// handler.sendEmptyMessage(1);
		// }
		// }
		// });
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.isHeaderShown()) {
					curPage = 1;
					doHttp(false);
				} else {

					if (curPage + 1 <= maxPage) {
						curPage++;
						JsonBuilder builder = new JsonBuilder();
						builder.put("limit", pageCount);
						builder.put("page", curPage);
						doHttp(true);
					} else {
						showCustomToast("无更多数据！");
						handler.sendEmptyMessage(1);
					}
				}

			}
		});
		curPage = 1;
		doHttp(false);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				mListView.onRefreshComplete();
			}
		}

	};

	public void updateData(JSONObject json) {
		mList = new ArrayList<tzDetailItem>();
		JSONArray list;
		try {
			maxPage = json.getInt("totalPage");
			list = json.getJSONArray("list");

			for (int i = 0; i < list.length(); i++) {
				JSONObject temp = list.getJSONObject(i);

				tzDetailItem item = new tzDetailItem(temp);

				mList.add(item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
		handler.sendEmptyMessage(1);
	}

	public void addData(JSONObject json) {
		JSONArray list;
		try {
			maxPage = json.getInt("totalPage");
			list = json.getJSONArray("list");

			for (int i = 0; i < list.length(); i++) {
				JSONObject temp = list.getJSONObject(i);

				tzDetailItem item = new tzDetailItem(temp);

				mList.add(item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
		handler.sendEmptyMessage(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

	class tzDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mList == null) {
				return 0;
			} else {
				return mList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(tzDetailsListActivity.this).inflate(R.layout.adapter_item_tz_detail, null);
			}
			TextView money1 = (TextView) convertView.findViewById(R.id.money1);
			// TextView money2 = (TextView) convertView.findViewById(R.id.money2);
			TextView people = (TextView) convertView.findViewById(R.id.tzpeo);
			TextView time = (TextView) convertView.findViewById(R.id.tztime);

			tzDetailItem item = (tzDetailItem) mList.get(position);

			money1.setText(item.getInvestor_capital() + "元");
			// money2.setText(item.getInvestor_capital() + "元");
			people.setText(item.getUser_name().length() > 3 ? item.getUser_name().substring(0, 3) + "***" : item.getUser_name());
			time.setText(item.getAdd_time() + "");

			return convertView;
		}

	}

	private void doHttp(final boolean add) {

		JsonBuilder builder = new JsonBuilder();

		builder.put("limit", pageCount);
		builder.put("page", curPage);
		builder.put("id", itemId);
		builder.put("type", itemType);

		BaseHttpClient.post(getBaseContext(), Default.showPtbjl, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				try {

					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							if (add) {
								addData(json);
							} else {
								updateData(json);
							}
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				showCustomToast(responseString);
			}

		});

	}

}
