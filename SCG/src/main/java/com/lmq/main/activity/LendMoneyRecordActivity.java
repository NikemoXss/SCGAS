package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.item.LendMoneyItem;
import com.lmq.main.util.Default;

/**
 * 借款记录
 */
public class LendMoneyRecordActivity extends BaseActivity implements OnClickListener {

	private LendMoneyOfUserAdapter adapter;
	// 借款列表
	public List lendMoneyList = new ArrayList<LendMoneyItem>();

	private int pageCount = 5;
	private int curPage = 1;
	private int maxPage;
	private PullToRefreshListView mListView;

	private int borrowImage[] = { R.drawable.borrow_type_0, R.drawable.borrow_type_1, R.drawable.borrow_type_2, R.drawable.borrow_type_3, R.drawable.borrow_type_4, R.drawable.borrow_type_5, R.drawable.borrow_type_6, R.drawable.borrow_type_7, R.drawable.borrow_type_8, R.drawable.borrow_type_9, R.drawable.borrow_type_10 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_of_lendmoney);

		findViewById(R.id.back).setOnClickListener(this);

		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		lendMoneyList.clear();
		curPage = 1;
		doHttp();
	}

	public void initView() {

		findViewById(R.id.back).setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.record_lendmoney);

		mListView = (PullToRefreshListView) findViewById(R.id.record_list);
		mListView.setMode(PullToRefreshBase.Mode.BOTH);

		adapter = new LendMoneyOfUserAdapter(this);
		mListView.setAdapter(adapter);

		// lendMoneyOfUser.setDividerHeight(0);
		// lendMoneyOfUser.showFootView(true);

		// lendMoneyOfUser.setOnLoadMoreInfo(new LoadMoreInfo() {
		// public void onRefresh() {
		// lendMoneyList.clear();
		// curPage = 1;
		// doHttp();
		//
		// }
		//
		// @Override
		// public void onLoadMore() {
		// if (curPage + 1 <= maxPage) {
		// curPage++;
		// doHttp();
		// } else {
		// handler.sendEmptyMessage(1);
		// }
		// }
		// });

		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.isHeaderShown()) {
					lendMoneyList.clear();
					curPage = 1;
					doHttp();
				} else {

					if (curPage + 1 <= maxPage) {
						curPage++;
						doHttp();
					} else {
						showCustomToast("无更多数据！");
						handler.sendEmptyMessage(1);
					}
				}

			}
		});

	}

	public void doHttp() {

		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("limit", pageCount);
		jsonBuilder.put("page", curPage);

		BaseHttpClient.post(getBaseContext(), Default.LENDMONEY, jsonBuilder, new JsonHttpResponseHandler() {

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
						MyLog.e("用户借款信息列表：", json.toString());
						if (json.getInt("status") == 1) {
							initData(json);
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

	// 处理从服务器获得数据
	public void initData(JSONObject json) {
		JSONArray list = null;
		try {
			if (json.has("list") && !json.isNull("list")) {
				list = json.getJSONArray("list");
			}

			if (list != null) {
				for (int i = 0; i < list.length(); i++) {
					JSONObject temp = list.getJSONObject(i);
					LendMoneyItem item = new LendMoneyItem(temp);
					lendMoneyList.add(item);
				}
			}

			maxPage = json.getInt("totalPage");
			curPage = json.getInt("nowPage");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
		mListView.onRefreshComplete();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void finish() {

		super.finish();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {

				mListView.onRefreshComplete();
				showCustomToast("无更多数据！");
			}
		}

	};

	public class LendMoneyOfUserAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */

		public LendMoneyOfUserAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (lendMoneyList == null) {
				return 0;
			}
			return lendMoneyList.size();
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = mInflater.inflate(R.layout.tz_myborrow_item2, null);
			}

			{

				TextView L_title = (TextView) arg1.findViewById(R.id.lendmoney_title);
				TextView L_id = (TextView) arg1.findViewById(R.id.lendmoney_id);
				TextView L_amount = (TextView) arg1.findViewById(R.id.lendmoney_count);
				TextView L_inerest_rate = (TextView) arg1.findViewById(R.id.interest_rate);
				TextView L_repay_amount = (TextView) arg1.findViewById(R.id.repay_amount);
				TextView L_repay_date = (TextView) arg1.findViewById(R.id.repay_due_dat);

				TextView L_repay_type = (TextView) arg1.findViewById(R.id.repay_type);

				ImageView L_kind = (ImageView) arg1.findViewById(R.id.repay_kind);

				ImageView L_borrow_type = (ImageView) arg1.findViewById(R.id.borrow_type);

				LendMoneyItem lendMoneyItem = (LendMoneyItem) lendMoneyList.get(arg0);
				// 填充数据
				L_title.setText(lendMoneyItem.getTitle());
				L_id.setText(lendMoneyItem.getId() + "");
				L_amount.setText(SystenmApi.getMoneyInfo((long) lendMoneyItem.getAmount()) + "");
				L_inerest_rate.setText(lendMoneyItem.getInterest_rate() + "");
				L_repay_amount.setText(SystenmApi.getMoneyInfo((long) lendMoneyItem.getRepay_amount()) + "");
				L_repay_date.setText(lendMoneyItem.getRepay_due_datae().toString());
				L_repay_type.setText(lendMoneyItem.getRepay_due_uint().equals("1") ? "天" : "个月");
				L_borrow_type.setBackgroundResource(borrowImage[lendMoneyItem.getStatue()]);

				// 判断分期
				switch (Integer.parseInt(lendMoneyItem.getRepay_kind().toString())) {
				case 1:
					L_kind.setImageResource(R.drawable.pay_kind1);
					break;
				case 2:
					L_kind.setImageResource(R.drawable.pay_kind2);
					break;
				case 3:
					L_kind.setImageResource(R.drawable.pay_kind3);
					break;
				case 4:
					L_kind.setImageResource(R.drawable.pay_kind4);
					break;
				case 5:
					L_kind.setImageResource(R.drawable.pay_kind5);
					break;
				case 6:
					L_kind.setImageResource(R.drawable.pay_kind6);
					break;

				default:
					break;
				}

			}
			return arg1;
		}

	}

}
