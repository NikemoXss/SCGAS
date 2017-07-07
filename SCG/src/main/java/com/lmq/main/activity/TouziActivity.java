package com.lmq.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.adapter.TouZiAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TouziActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView mListView;
	private TouZiAdapter touZiAdapter;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	ImageView imageview;
	TextView title;
	private JSONArray jsonArray = null;
	private int maxPage, curPage = 1, pageCount = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_touzi_sx);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("投资列表");
		mListView = (PullToRefreshListView) findViewById(R.id.touzi_lv_sx);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.BOTH);
		initView();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		doHttp();
	}

	private void initView() {
		touZiAdapter = new TouZiAdapter(TouziActivity.this, list);
		mListView.setAdapter(touZiAdapter);
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.isHeaderShown()) {

					doHttp();
				} else {
					JsonBuilder builder = new JsonBuilder();
					if (curPage + 1 <= maxPage) {
						curPage += 1;
						builder.put("type", 11);
						builder.put("page", curPage);
						Log.e("builder", curPage + "");
						builder.put("limit", pageCount);
						doHttp(builder);
						// handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				}

			}
		});
	}

	public void doHttp(JsonBuilder builder) {

		BaseHttpClient.post(TouziActivity.this, Default.tzAllList, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {

					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							updateAddInfo(response);
						} else {
							showCustomToast(response.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
					dismissLoadingDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);

				mListView.onRefreshComplete();
				touZiAdapter.notifyDataSetChanged();
				dismissLoadingDialog();
			}
		});
	}

	protected void updateAddInfo(JSONObject json) {
		// TODO Auto-generated method stub

		try {
			maxPage = json.getInt("totalPage");
			curPage = json.getInt("nowPage");
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			if (!json.isNull("list")) {
				jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						map = new HashMap<String, Object>();
						map.put("id", templist.getLong("id"));
						map.put("type", templist.getInt("type"));
						
						Double all = templist.getDouble("borrow_money");
						Double has = templist.getDouble("has_borrow");
						
						map.put("has_borrow_sx", getSubtract(all,has));
						
						map.put("progress_tz_sx", templist.getDouble("progress"));
						map.put("item_tzbt_sx", templist.getString("borrow_name"));
						map.put("item_jkje_sx", templist.getString("borrow_money"));
						map.put("item_tzqx_sx", templist.getString("borrow_duration"));
						map.put("item_nhl_sx", templist.getString("borrow_interest_rate"));
						map.put("item_time_sx", templist.getString("addtime"));
						map.put("repayment_type", templist.getString("repayment_type"));
						list1.add(map);
					}

				}
			}
			list.addAll(list1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		touZiAdapter.notifyDataSetChanged();

		mListView.onRefreshComplete();
		touZiAdapter.notifyDataSetChanged();
	}

	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				break;

			default:
				break;
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}
	};

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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void doHttp() {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("type", 11);
		builder.put("page", curPage);
		builder.put("limit", pageCount);

		// 填充参数
		BaseHttpClient.post(TouziActivity.this, Default.tzAllList, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				showLoadingDialogNoCancle("请稍后努力加载中...");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 1) {
							initJsonData(json);
						} else {
							showCustomToast(json.getString("message"));
							list.clear();
						}
						dismissLoadingDialog();
						touZiAdapter.notifyDataSetChanged();
						mListView.onRefreshComplete();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
			}
		});

	}

	protected void initJsonData(JSONObject json) {
		list.clear();
		try {
			// 散标数据初始化
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			maxPage = json.getInt("totalPage");
			curPage = json.getInt("nowPage");
			if (!json.isNull("list")) {
				jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						map = new HashMap<String, Object>();
						map.put("id", templist.getLong("id"));
						map.put("type", templist.getInt("type"));
						
						Double all = templist.getDouble("borrow_money");
						Double has = templist.getDouble("has_borrow");
						
						map.put("has_borrow_sx", getSubtract(all,has));
						
						map.put("progress_tz_sx", templist.getDouble("progress"));
						map.put("item_tzbt_sx", templist.getString("borrow_name"));
						map.put("item_jkje_sx", templist.getString("borrow_money"));
						map.put("item_tzqx_sx", templist.getString("borrow_duration"));
						map.put("item_nhl_sx", templist.getString("borrow_interest_rate"));
						map.put("item_time_sx", templist.getString("addtime"));
						map.put("repayment_type", templist.getString("repayment_type"));
						list2.add(map);
					}
				}
			}
			list.clear();
			list.addAll(list2);
			// handler.sendEmptyMessage(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		touZiAdapter.notifyDataSetChanged();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				touZiAdapter.notifyDataSetChanged();
				mListView.onRefreshComplete();
			}
			if (msg.what == 2) {
				showCustomToast("没有更多数据了");
			}
		}

	};
	
	private String getSubtract(Double all, Double has) {
		BigDecimal b1 = new BigDecimal(all.toString());
		BigDecimal b2 = new BigDecimal(has.toString());
		Double isuse = b1.subtract(b2).doubleValue();
		return isuse + "";
	}

}
