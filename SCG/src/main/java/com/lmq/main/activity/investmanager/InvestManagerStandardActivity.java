package com.lmq.main.activity.investmanager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.dialog.InvestPopList;
import com.lmq.main.item.InvestManageItem;
import com.lmq.main.util.Default;
import com.lmqutil.PixelUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 投资管理-散标
 * 
 */
public class InvestManagerStandardActivity extends BaseActivity implements OnClickListener {

	private InvestPopList popList;
	private int current_type = -1;
	private ArrayList<InvestManageItem> data;
	private PullToRefreshListView listView;
	private Adapter adapter;
	private int page = 1;
	private int limit = 8;
	private int totalPage = 0;
	private TextView text;
	private String requestURL = Default.borrow_list;
	private String[] tipsData;
	private int tipsType = 0;
	private ImageView mtriangle;
	private LinearLayout lineview;
	private int ww;
	int one = 1;
	int two = 2;
	int zero = 0;
	int coo = one;
	private TextView tv1, tv2, tv3;
	private String[] titleArray;
	DecimalFormat df = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_manage_debet_layout);

		titleArray = getResources().getStringArray(R.array.sb_type);
		text = (TextView) findViewById(R.id.title);
		text.setText("竞标中");
		text.setOnClickListener(this);

		tv1 = (TextView) findViewById(R.id.canuse);
		tv2 = (TextView) findViewById(R.id.used);
		tv3 = (TextView) findViewById(R.id.cannouse);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);

		findViewById(R.id.back).setOnClickListener(this);
		mtriangle = (ImageView) findViewById(R.id.triangle);

		data = new ArrayList<InvestManageItem>();
		listView = (PullToRefreshListView) findViewById(R.id.invest_manager_debet_list);
		adapter = new Adapter();
		listView.setAdapter(adapter);
		tipsData = InvestManagerStandardActivity.this.getResources().getStringArray(R.array.sb_tips1);
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

		popList = new InvestPopList(InvestManagerStandardActivity.this);
		popList.setOnItemClickLinstener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position != popList.getDefauleSelect()) {
					popList.setDefauleSelect(position);

				}
				tipsType = position;
				Animation animation = AnimationUtils.loadAnimation(InvestManagerStandardActivity.this,
						R.anim.triangle_totate2);
				mtriangle.clearAnimation();
				mtriangle.setImageResource(R.drawable.wite_arrow_up);
				mtriangle.startAnimation(animation);
				animation.setFillAfter(true);

				popList.dissmiss();
				resetPostParams();
				text.setText(getResources().getStringArray(R.array.sb_type)[position]);

				switch (position) {

				case 0:

					requestURL = Default.borrow_list;

					break;
				case 1:
					requestURL = Default.borrow_list;
					break;
				case 2:
					requestURL = Default.borrow_list;
					break;
				case 3:
					requestURL = Default.borrow_list;
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

				case 1:
					InvestManageItem item = adapter.getItem(position);
					Intent intent = new Intent();
					intent.putExtra("id", item.getId());
					intent.setClass(InvestManagerStandardActivity.this, RepaymentStandardDetailActivity.class);
					startActivity(intent);
					break;

				}

			}
		});

	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		lineview = (LinearLayout) findViewById(R.id.line);
		WindowManager wManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int w = wManager.getDefaultDisplay().getWidth();
		ww = w / 3;
		LayoutParams params = new LayoutParams(ww, 7); // 宽 ， 高
		lineview.setLayoutParams(params);
	}

	public void TheAnimation(int arg0) {
		Animation translateAnimation3 = new TranslateAnimation(ww * coo, ww * arg0, 0, 0);
		coo = arg0;
		// 设置动画时间
		translateAnimation3.setDuration(200);
		translateAnimation3.setFillAfter(true);
		lineview.startAnimation(translateAnimation3);
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
			return 4;
		// case 2:
		// return 0;
		case 2:
			return 5;
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
		case R.id.canuse:
			showLoadingDialogNoCancle("请稍后努力加载中...");
			TheAnimation(zero);// 平移动画
			tipsType=0;
			getInvestData(getPostParams(), true);
			break;
		case R.id.used:
			showLoadingDialogNoCancle("请稍后努力加载中...");
			tipsType=1;
			TheAnimation(one);
			getInvestData(getPostParams(), true);
			break;
		case R.id.cannouse:
			showLoadingDialogNoCancle("请稍后努力加载中...");
			tipsType=2;
			TheAnimation(two);
			getInvestData(getPostParams(), true);
			break;
		case R.id.title:

			Animation animation = AnimationUtils.loadAnimation(InvestManagerStandardActivity.this,
					R.anim.triangle_totate);
			mtriangle.clearAnimation();
			mtriangle.setImageResource(R.drawable.wite_arrow_down);
			mtriangle.startAnimation(animation);
			animation.setFillAfter(true);

			showPopList(0, v);

			break;

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
				popList.addItems(getResources().getStringArray(R.array.sb_type));

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
				convertView = View.inflate(InvestManagerStandardActivity.this, R.layout.invest_manager_sb_item, null);

				viewHolder.invest_manager_item_tbmc = (TextView) convertView
						.findViewById(R.id.invest_manager_item_tbmc);
				viewHolder.invest_manager_item_time = (TextView) convertView
						.findViewById(R.id.invest_manager_item_time);
				viewHolder.invest_manager_item_ztje = (TextView) convertView
						.findViewById(R.id.invest_manager_item_ztje);
				viewHolder.invest_manager_item_nhll = (TextView) convertView
						.findViewById(R.id.invest_manager_item_nhll);
				viewHolder.invest_manager_item_tzsj = (TextView) convertView
						.findViewById(R.id.invest_manager_item_tzsj);
				viewHolder.invest_manager_item_dqsj = (TextView) convertView
						.findViewById(R.id.invest_manager_item_dqsj);

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

			viewHolder.debet_item_tips1.setText(tipsData[0]);
			viewHolder.debet_item_tips2.setText(tipsData[1]);
			viewHolder.debet_item_tips3.setText(tipsData[2]);
			viewHolder.debet_item_tips4.setText(tipsData[3]);

			viewHolder.invest_manager_item_tbmc.setText(adapter.getItem(position).getBorrow_name());
			viewHolder.invest_manager_item_ztje.setText(adapter.getItem(position).getInvestor_capital());
			viewHolder.invest_manager_item_nhll.setText(adapter.getItem(position).getBorrow_interest_rate() + "%");
			viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(position).getBorrow_duration());

			if (tipsType == 0) {
				viewHolder.debet_item_tips1.setText("出借金额");
				viewHolder.debet_item_tips2.setText("年化收益");
				viewHolder.debet_item_tips3.setText("出借期限");
				viewHolder.debet_item_tips4.setText("出借日期");
				viewHolder.invest_manager_item_time.setVisibility(View.GONE);

				viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getInvest_time());

			} else if (tipsType == 1) {
				viewHolder.debet_item_tips1.setText("出借金额");
				viewHolder.debet_item_tips2.setText("年化收益");
				viewHolder.debet_item_tips3.setText("出借期限");
				viewHolder.debet_item_tips4.setText("待收利息");
				viewHolder.invest_manager_item_time.setVisibility(View.VISIBLE);
				viewHolder.invest_manager_item_tbmc.setMaxWidth(PixelUtil.dp2px(220));
				viewHolder.invest_manager_item_time.setText("到期：" + adapter.getItem(position).getDeadline());

				viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).investor_interest);
			} else if (tipsType == 2) {
				viewHolder.invest_manager_item_time.setVisibility(View.GONE);
				viewHolder.invest_manager_item_tbmc.setMaxWidth(PixelUtil.dp2px(220));

				viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getInvest_time());
			}

			// if (tipsType == 1) {
			// viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(position).getBack()
			// + "/" + adapter.getItem(position).getTotal());//
			// +"("+adapter.getItem(position).getRepayment_time()+")"
			// viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getReceive_money());
			// }

			/*
			 * if (tipsType == 2) {
			 * viewHolder.invest_manager_item_ztje.setText(adapter.getItem(
			 * position).getCapital() + "");
			 * viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(
			 * position).getBreakday());
			 * viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(
			 * position).getInterest()); }
			 */

			// if (tipsType == 2) {
			// viewHolder.invest_manager_item_tzsj.setText(adapter.getItem(position).getReceive_capital());
			// viewHolder.invest_manager_item_dqsj.setText(adapter.getItem(position).getReceive_interest());
			// }
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
		TextView invest_manager_item_time;
		TextView invest_manager_item_ztje;
		TextView invest_manager_item_nhll;
		TextView invest_manager_item_tzsj;
		TextView invest_manager_item_dqsj;

	}

	private void cheangeTips() {

		tipsData = null;

		switch (tipsType) {

		case 0:
			tipsData = InvestManagerStandardActivity.this.getResources().getStringArray(R.array.sb_tips1);
			break;

		case 1:
			tipsData = InvestManagerStandardActivity.this.getResources().getStringArray(R.array.sb_tips2);
			break;
		// case 2:
		// tipsData =
		// InvestManagerStandardActivity.this.getResources().getStringArray(R.array.sb_tips3);
		// break;
		case 2:
			tipsData = InvestManagerStandardActivity.this.getResources().getStringArray(R.array.sb_tips4);
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

						} else if (json.optInt("status") == 0) {
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
