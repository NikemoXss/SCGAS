package com.lmq.main.activity.investmanager;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.dialog.DedetCancleDialog;
import com.lmq.main.dialog.InvestPopList;
import com.lmq.main.item.InvestManageItem;
import com.lmq.main.tools.LMQWebViewActivity;
import com.lmq.main.util.Default;

/**
 * 投资管理-企业直投
 *
 * @author sunjianchao
 */
public class InvestManagerDirectlyActivity extends BaseActivity implements OnClickListener {

	private InvestPopList popList;
	private int current_type = -1;
	private ArrayList<InvestManageItem> data;
	private PullToRefreshListView listView;
	private Adapter adapter;
	private int page = 1;
	private int limit = 8;
	private int totalPage = 0;
	private TextView text;
	private String requestURL = Default.tborrow_list;
	private String[] tipsData;
	private int tipsType = 0;
	private ImageView mtriangle;
	private DedetCancleDialog dialog;

	private String[] titleArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_manage_debet_layout);

		titleArray = getResources().getStringArray(R.array.zt_type);
		text = (TextView) findViewById(R.id.title);
		text.setText("回收中");
		text.setOnClickListener(this);

		mtriangle = (ImageView) findViewById(R.id.triangle);
		findViewById(R.id.back).setOnClickListener(this);

		data = new ArrayList<InvestManageItem>();
		listView = (PullToRefreshListView) findViewById(R.id.invest_manager_debet_list);
		adapter = new Adapter();
		listView.setAdapter(adapter);
		tipsData = InvestManagerDirectlyActivity.this.getResources().getStringArray(R.array.zt_tips1);
		listView.setMode(PullToRefreshBase.Mode.BOTH);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.isHeaderShown()) {
					resetPostParams();
					getInvestData(getPostParams(), true);
				}
				if (refreshView.isFooterShown()) {

					if (page < totalPage) {

						page += 1;
						getInvestData(getPostParams(), false);

					} else {

						handler.sendEmptyMessage(1);
					}

				}

			}
		});

		popList = new InvestPopList(InvestManagerDirectlyActivity.this);
		popList.setOnItemClickLinstener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position != popList.getDefauleSelect()) {
					popList.setDefauleSelect(position);

				}
				tipsType = position;
				Animation animation = AnimationUtils.loadAnimation(InvestManagerDirectlyActivity.this, R.anim.triangle_totate2);
				mtriangle.clearAnimation();
				mtriangle.setImageResource(R.drawable.wite_arrow_up);
				mtriangle.startAnimation(animation);
				animation.setFillAfter(true);

				popList.dissmiss();
				resetPostParams();
				text.setText(getResources().getStringArray(R.array.zt_type)[position]);

				switch (position) {

				case 0:

					requestURL = Default.tborrow_list;

					break;
				case 1:
					requestURL = Default.tborrow_list;
					break;

				}

				text.setText(titleArray[position]);
				getInvestData(getPostParams(), true);

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				position = position - 1;
				switch (tipsType) {

				case 0:
					Intent abIntent = new Intent();
					abIntent.putExtra("title", "合同详情");
					abIntent.putExtra("url", Default.ip + "/member/agreement/downliuzhuanfile?id=" + adapter.getItem(position).getId());
					abIntent.setClass(InvestManagerDirectlyActivity.this, LMQWebViewActivity.class);
					startActivity(abIntent);
					break;

				}

			}
		});

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				showCustomToast("无更多数据！");

				listView.onRefreshComplete();
			}
		}

	};

	@Override
	protected void onResume() {
		super.onResume();
		getInvestData(getPostParams(), true);
	}

	private JsonBuilder getPostParams() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("page", page);
		builder.put("limit", limit);
		builder.put("type", getType());

		return builder;
	}

	public int getType() {
		switch (tipsType) {
		case 0:
			return 1;
		case 1:
			return 2;

		default:
			return 1;
		}
	}

	private JsonBuilder resetPostParams() {

		page = 0;
		limit = 8;

		return getPostParams();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish(0);
			break;

		case R.id.title:

			Animation animation = AnimationUtils.loadAnimation(InvestManagerDirectlyActivity.this, R.anim.triangle_totate);
			mtriangle.clearAnimation();
			mtriangle.setImageResource(R.drawable.wite_arrow_down);
			mtriangle.startAnimation(animation);
			animation.setFillAfter(true);

			showPopList(0, v);

		}

	}

	private void showPopList(int type, View view) {

		if (current_type != type) {

			if (popList.isShowing()) {

				popList.dissmiss();
			}

			popList.clearData();
			popList.setDefauleSelect(0);

			if (type == 0) {
				popList.addItems(getResources().getStringArray(R.array.zt_type));

			}

			current_type = type;

		}

		popList.showPOpList(view);

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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (null == convertView) {

				viewHolder = new ViewHolder();
				convertView = View.inflate(InvestManagerDirectlyActivity.this, R.layout.invest_manager_zt_item, null);

				viewHolder.invest_manager_item_tbmc = (TextView) convertView.findViewById(R.id.invest_manager_item_tbmc);
				viewHolder.invest_manager_item_tz_time = (TextView) convertView.findViewById(R.id.invest_manager_item_tz_time);
				viewHolder.invest_manager_item_ztje = (TextView) convertView.findViewById(R.id.invest_manager_item_ztje);
				viewHolder.invest_manager_item_nhll = (TextView) convertView.findViewById(R.id.invest_manager_item_nhll);
				viewHolder.invest_manager_item_tzsj = (TextView) convertView.findViewById(R.id.invest_manager_item_tzsj);
				viewHolder.invest_manager_item_dqsj = (TextView) convertView.findViewById(R.id.invest_manager_item_dqsj);

				/*** 提示 */
				viewHolder.debet_item_tips1 = (TextView) convertView.findViewById(R.id.debet_item_tips1);
				viewHolder.debet_item_tips2 = (TextView) convertView.findViewById(R.id.debet_item_tips2);
				viewHolder.debet_item_tips3 = (TextView) convertView.findViewById(R.id.debet_item_tips3);
				viewHolder.debet_item_tips4 = (TextView) convertView.findViewById(R.id.debet_item_tips4);

				convertView.setTag(viewHolder);

			} else {

				viewHolder = (ViewHolder) convertView.getTag();

			}

			cheangeTips();

			viewHolder.debet_item_tips2.setText(tipsData[1]);

			double investor_capital = Double.parseDouble(adapter.getItem(position).getInvestor_capital());
			viewHolder.invest_manager_item_tbmc.setText(adapter.getItem(position).getBorrow_name());
			viewHolder.invest_manager_item_tz_time.setText(adapter.getItem(position).getAddtime());
			viewHolder.invest_manager_item_ztje.setText(adapter.getItem(position).getInvestor_capital());
			// viewHolder.invest_manager_item_ztje.setText(SystenmApi.getMoneyInfo(investor_capital));
			viewHolder.invest_manager_item_nhll.setText(adapter.getItem(position).getBorrow_interest_rate() + "%");
			viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(position).getCapital_interest());
			viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getDeadline());

			if (tipsType == 1) {
				viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(position).getReceive_money());
				viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getDeadline());
			}

			return convertView;
		}

	}

	private static class ViewHolder {

		/** 提示 */
		TextView debet_item_tips1;
		TextView debet_item_tips2;
		TextView debet_item_tips3;
		TextView debet_item_tips4;

		TextView invest_manager_item_tbmc;
		TextView invest_manager_item_tz_time;
		TextView invest_manager_item_ztje;
		TextView invest_manager_item_nhll;
		TextView invest_manager_item_tzsj;
		TextView invest_manager_item_dqsj;

	}

	private void cheangeTips() {

		tipsData = null;

		switch (tipsType) {

		case 0:
			tipsData = InvestManagerDirectlyActivity.this.getResources().getStringArray(R.array.zt_tips1);
			break;

		case 1:
			tipsData = InvestManagerDirectlyActivity.this.getResources().getStringArray(R.array.zt_tips2);
			break;

		}

	}

	// 提交用户反馈到服务器
	public void getInvestData(JsonBuilder builder, boolean refsh) {

		if (refsh) {
			data.clear();
		}

		BaseHttpClient.post(getBaseContext(), requestURL, builder, new JsonHttpResponseHandler() {

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
