package com.lmq.main.activity.investmanager;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
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
import com.lmq.main.item.InvestManageItem;
import com.lmq.main.tools.LMQWebViewActivity;
import com.lmq.main.util.Default;

/**
 * 投资管理-散标
 *
 * @author sunjianchao
 */
public class RepaymentStandardDetailActivity extends BaseActivity implements OnClickListener {

	private ArrayList<InvestManageItem> data;
	private PullToRefreshListView listView;
	private Adapter adapter;
	private int page = 1;
	private int limit = 5;
	private int totalPage = 0;
	private TextView text;
	private String hktId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_manage_hk_layout);

		text = (TextView) findViewById(R.id.title);
		text.setText("还款详情");

		findViewById(R.id.back).setOnClickListener(this);
		Intent intent = getIntent();
		if (null != intent) {

			hktId = intent.getStringExtra("id");
		}
		data = new ArrayList<InvestManageItem>();
		listView = (PullToRefreshListView) findViewById(R.id.invest_manager_hk_list);
		adapter = new Adapter();
		listView.setAdapter(adapter);
		listView.setMode(PullToRefreshBase.Mode.BOTH);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.isHeaderShown()) {
					resetPostParams();
					getInvestData(getPostParams());
				}
				if (refreshView.isFooterShown()) {

					if (page < totalPage) {

						page += 1;

					}
					getInvestData(getPostParams());

				}

			}
		});

		// listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//
		// position = position -1;
		// Intent abIntent = new Intent();
		// abIntent.putExtra("title","合同详情");
		// abIntent.putExtra("url",Default.ip+"/member/agreement/downfile?id="+adapter.getItem(position).getInvest_id());
		// abIntent.setClass(RepaymentSBDetailActivity.this, LMQWebViewActivity.class);
		// startActivity(abIntent);
		//
		// }
		// });

	}

	@Override
	protected void onResume() {
		super.onResume();
		getInvestData(getPostParams());
	}

	private JsonBuilder getPostParams() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("page", page);
		builder.put("limit", limit);
		builder.put("id", hktId);

		return builder;
	}

	private JsonBuilder resetPostParams() {

		page = 0;
		limit = 5;

		return getPostParams();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish(0);
			break;

		}

	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public InvestManageItem getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (null == convertView) {

				viewHolder = new ViewHolder();
				convertView = View.inflate(RepaymentStandardDetailActivity.this, R.layout.invest_mange_hk_detail_item, null);

				viewHolder.tv_deadline = (TextView) convertView.findViewById(R.id.tv_deadline);
				viewHolder.tv_capital = (TextView) convertView.findViewById(R.id.tv_capital);
				viewHolder.tv_interest_fee = (TextView) convertView.findViewById(R.id.tv_interest_fee);
				viewHolder.tv_interest_fee2 = (TextView) convertView.findViewById(R.id.tv_interest_fee2);
				viewHolder.tv_receive_money = (TextView) convertView.findViewById(R.id.tv_receive_money);
				viewHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
				viewHolder.tv_sort_order = (TextView) convertView.findViewById(R.id.tv_sort_order);
				viewHolder.tv_ht = (TextView) convertView.findViewById(R.id.tv_ht);

				convertView.setTag(viewHolder);
			} else {

				viewHolder = (ViewHolder) convertView.getTag();

			}

			viewHolder.tv_deadline.setText(adapter.getItem(position).getDeadline());
			viewHolder.tv_capital.setText(adapter.getItem(position).getCapital() + "");
			viewHolder.tv_interest_fee.setText(adapter.getItem(position).getInterest());
			viewHolder.tv_interest_fee2.setText(adapter.getItem(position).getInterest_fee() + "");
			viewHolder.tv_receive_money.setText(adapter.getItem(position).getReceive_money());
			viewHolder.tv_status.setText(adapter.getItem(position).getStatus() + "");
			viewHolder.tv_sort_order.setText(adapter.getItem(position).getSort_order() + "");

			viewHolder.tv_ht.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent abIntent = new Intent();
					abIntent.putExtra("title", "合同详情");
					abIntent.putExtra("url", Default.ip + "/member/agreement/downfile?id=" + adapter.getItem(position).getInvest_id());
					abIntent.setClass(RepaymentStandardDetailActivity.this, LMQWebViewActivity.class);
					startActivity(abIntent);

				}
			});
			return convertView;
		}

	}

	private static class ViewHolder {

		TextView tv_deadline;
		TextView tv_capital;
		TextView tv_interest_fee;
		TextView tv_interest_fee2;
		TextView tv_receive_money;
		TextView tv_status;
		TextView tv_sort_order;
		TextView tv_ht;

	}

	// 提交用户反馈到服务器
	public void getInvestData(JsonBuilder builder) {

		if (data.size() > 0) {
			data.clear();
		}

		BaseHttpClient.post(getBaseContext(), Default.borrow_detail, builder, new JsonHttpResponseHandler() {

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
						// 没有新版本
						if (json.getInt("status") == 1) {

							/*
							 * nowPage
							 */
							if (json.has("totalPage")) {

								totalPage = json.optInt("totalPage", 0);
							}

							if (json.has("nowPage")) {

								page = json.optInt("nowPage", 0);
							}

							if (json.has("list")) {

								JSONArray array = json.getJSONArray("list");

								if (null != array && array.length() > 0) {

									for (int i = 0; i < array.length(); i++) {
										InvestManageItem item = new InvestManageItem(array.getJSONObject(i));
										data.add(item);
									}

								}

							} else {
								showCustomToast(json.getString("message"));

							}

						} else if (json.getInt("status") == 0) {
							showCustomToast(json.getString("message"));

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
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				showCustomToast(responseString);
				listView.onRefreshComplete();

			}

		});

		adapter.notifyDataSetChanged();

	}

	@Override
	public void finish() {
		super.finish();
	}

}
