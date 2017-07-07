package com.lmq.menu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TouziFragment extends BaseFragment implements OnClickListener {
	TextView _all, _pre, _toubiaoing, _chushouing, _toupiaoing, _finish;
	private PullToRefreshListView mListView;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private int maxPage, curPage = 1, pageCount = 5;
	private JSONArray jsonArray = null;
	String type = "";

	private TouZiAdapter touZiAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.layout_touzi_sx_fragment, null);
		mListView = (PullToRefreshListView) mainView.findViewById(R.id.touzi_lv_sx_fragment);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.BOTH);
		initView();

		return mainView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		doHttp();
	}

	private void initView() {
		touZiAdapter = new TouZiAdapter(getActivity(), list);
		mListView.setAdapter(touZiAdapter);
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

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

		BaseHttpClient.post(getActivity(), Default.tzList, builder, new JsonHttpResponseHandler() {

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

				// touZiAdapter.notifyDataSetChanged();

				mListView.onRefreshComplete();
				touZiAdapter.notifyDataSetChanged();
				dismissLoadingDialog();
			}
		});
	}

	protected void updateAddInfo(JSONObject json) {
		// TODO Auto-generated method stub

		try {
			// 散标数据初始化

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
						map.put("progress_tz_sx", templist.getDouble("progress"));
						map.put("item_tzbt_sx", templist.getString("borrow_name"));
						map.put("item_jkje_sx", "借款金额:" + templist.getString("borrow_money") + "元");
						map.put("item_tzqx_sx", "投资期限:" + templist.getString("borrow_duration") + "月");
						map.put("item_nhl_sx", templist.getString("borrow_interest_rate") + "%");
						map.put("item_time_sx", templist.getString("addtime"));
						list1.add(map);
					}

				}
			}
			list.addAll(list1);
			// handler.sendEmptyMessage(1);
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
				// MainActivity.mMainActivity.getImageLodader().lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				// loadImage();
				// loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				// MainActivity.mMainActivity.getImageLodader().lock();
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
	}

	public void doHttp() {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("type", 11);
		builder.put("page", curPage);
		builder.put("limit", pageCount);
		// params.put("", builder);

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.tzList, builder, new JsonHttpResponseHandler() {

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
						// touZiAdapter = new
						// TouZiAdapter(TouziActivity_Sx.this, list);
						// mListView.setAdapter(touZiAdapter);
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
						map.put("progress_tz_sx", templist.getDouble("progress"));
						map.put("item_tzbt_sx", templist.getString("borrow_name"));
						map.put("item_jkje_sx", "借款金额:" + templist.getString("borrow_money") + "元");
						map.put("item_tzqx_sx", "投资期限:" + templist.getString("borrow_duration"));
						map.put("item_nhl_sx", templist.getString("borrow_interest_rate") + "%");
						map.put("item_time_sx", templist.getString("addtime"));
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
				// Toast.makeText(TouziActivity_Sx.this, "没有更多数据了",
				// Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
