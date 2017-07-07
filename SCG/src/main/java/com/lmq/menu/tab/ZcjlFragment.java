package com.lmq.menu.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lmq.adapter.ZcjlItemAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.lmq.view.MyPullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZcjlFragment extends Fragment {

	private MyPullToRefreshListView mListView;
	ZcjlItemAdapter zcjlItemAdapter;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mainView = inflater.inflate(R.layout.activity_zcjlfragment, null);
		mListView = (MyPullToRefreshListView) mainView.findViewById(R.id.zcjl_lv);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.DISABLED);
		// doHttp();
		initData();
		initView();// PullToRefreshListView 冲突
		return mainView;
	}

	private void initData() {
		list.clear();
		Bundle bundle = getArguments();// 从activity传过来的Bundle
		String log_zcjl = bundle.getString("log_zcjl");
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(log_zcjl);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				map = new HashMap<String, Object>();
				map.put("zcjl_tbje", jsonObject2.optString("invest_money"));
				map.put("zcjl_tbsj", stampToDate(jsonObject2.optString("invest_time")));
				map.put("zcjl_tbfs", statusToStr(jsonObject2.optString("status")));
				map.put("zcjl_tbr", jsonObject2.optString("user_name"));
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		// for (int i = 0; i < 5; i++) {
		// map = new HashMap<String, Object>();
		// map.put("zcjl_tbr", "阿三" + i);
		// map.put("zcjl_tbje", "100万");
		// map.put("zcjl_tbsj", "2010-1-1");
		// map.put("zcjl_tbfs", "手动");
		// list.add(map);
		// }

	}

	public static String statusToStr(String status) {
		String str = null;
		switch (Integer.parseInt(status)) {
		case 1:
			str = "手动";
			break;
		case 0:
			str = "自动";
			break;
		default:
			break;
		}

		return str;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long lt = new Long(s + "000");
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	private void initView() {
		// TODO Auto-generated method stub

		zcjlItemAdapter = new ZcjlItemAdapter(getActivity(), list);
		mListView.setAdapter(zcjlItemAdapter);
		// list.clear();
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

			}
		});

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

	// 首次请求
	public void doHttp() {
		JsonBuilder builder = new JsonBuilder();

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.tzList, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());

				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 1) {
							initJsonData(json);
						} else {
							list.clear();
						}
						zcjlItemAdapter.notifyDataSetChanged();
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
			}
		});

	}

	protected void initJsonData(JSONObject json) {
		// list.clear();
		// try {
		// // 散标数据初始化
		// List<Map<String, Object>> list2 = new ArrayList<Map<String,
		// Object>>();
		// maxPage = json.getInt("totalPage");
		// curPage = json.getInt("nowPage");
		// if (!json.isNull("list")) {
		// jsonArray = json.getJSONArray("list");
		// if (jsonArray != null) {
		// for (int i = 0; i < jsonArray.length(); i++) {
		// JSONObject templist = jsonArray.getJSONObject(i);
		// map = new HashMap<String, Object>();
		// map.put("id", templist.getLong("id"));
		// map.put("type", templist.getInt("type"));
		// map.put("progress_tz_sx", templist.getDouble("progress"));
		// map.put("item_tzbt_sx", templist.getString("borrow_name"));
		// map.put("item_jkje_sx", "借款金额:" + templist.getString("borrow_money")
		// + "元");
		// map.put("item_tzqx_sx", "投资期限:" +
		// templist.getString("borrow_duration"));
		// map.put("item_nhl_sx", templist.getString("borrow_interest_rate") +
		// "%");
		// map.put("item_time_sx", templist.getString("addtime"));
		// list2.add(map);
		// }
		// }
		// }
		// list.clear();
		// list.addAll(list2);
		// // handler.sendEmptyMessage(1);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		zcjlItemAdapter.notifyDataSetChanged();
	}
}
