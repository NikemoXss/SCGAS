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
import com.lmq.adapter.ZhongChouAdapter;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crowd_Sx extends BaseFragment implements OnClickListener {
	TextView _all, _pre, _toubiaoing, _chushouing, _toupiaoing, _finish;
	private PullToRefreshListView mListView;
	private ZhongChouAdapter Zcadapter;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private int maxPage, curPage = 1, pageCount = 5;
	private JSONArray jsonArray = null;
	String type = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.layout_crowd_sx, null);
		_all = (TextView) mainView.findViewById(R.id._all);
		_pre = (TextView) mainView.findViewById(R.id._pre);
		_toubiaoing = (TextView) mainView.findViewById(R.id._toubiaoing);
		_chushouing = (TextView) mainView.findViewById(R.id._chushouing);
		_toupiaoing = (TextView) mainView.findViewById(R.id._toupiaoing);
		_finish = (TextView) mainView.findViewById(R.id._finish);
		_all.setOnClickListener(this);
		_pre.setOnClickListener(this);
		_toubiaoing.setOnClickListener(this);
		_chushouing.setOnClickListener(this);
		_toupiaoing.setOnClickListener(this);
		_finish.setOnClickListener(this);
		mListView = (PullToRefreshListView) mainView.findViewById(R.id.zhongchong_lv);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.BOTH);
		// initData();
		initView();
		return mainView;
	}

	private void initData() {
		for (int i = 0; i < 5; i++) {
			map = new HashMap<String, Object>();
			map.put("zc_title", "title" + i);
			map.put("zc_fbsj", "2011-1-1");
			map.put("Zc_zcje", "1000");
			map.put("Zc_ycje", "200");
			map.put("Zc_czrs", "10");
			map.put("Zc_progress", "40");
			map.put("Zc_jindu", "40%");
			map.put("Zc_starttime", "2011-1-2");
			map.put("Zc_pic", "http://imga.mumayi.com/android/wallpaper/2011/10/04/sl_600_2011100404165496487334.jpg");
			list.add(map);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (refreshView.isHeaderShown()) {

					doHttp();
				} else {
					JsonBuilder builder = new JsonBuilder();
					if (curPage + 1 <= maxPage) {
						curPage += 1;
						if (!"".equals(type)) {
							builder.put("status", type);
						}
						// builder.put("type", 11);
						builder.put("page", curPage);
						Log.e("builder", curPage + "");
						builder.put("limit", pageCount);
						doHttp(builder);
					} else {
						handler.sendEmptyMessage(2);
					}
				}

			}
		});

	}

	// 首次请求
	public void doHttp() {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		// builder.put("type", 11);
		builder.put("page", curPage);
		builder.put("limit", pageCount);

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.zcList, builder, new JsonHttpResponseHandler() {

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
						Zcadapter = new ZhongChouAdapter(getActivity(), list);
						mListView.setAdapter(Zcadapter);
						Zcadapter.notifyDataSetChanged();
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

	// 首次请求
	public void doHttp1(String status) {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("status", status);
		builder.put("page", curPage);
		builder.put("limit", pageCount);

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.zcList, builder, new JsonHttpResponseHandler() {

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
						Zcadapter = new ZhongChouAdapter(getActivity(), list);
						mListView.setAdapter(Zcadapter);
						Zcadapter.notifyDataSetChanged();
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

	// 上啦加载
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
				Zcadapter.notifyDataSetChanged();
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
						map.put("id", templist.getString("id"));
						map.put("zc_title", templist.getString("raise_name"));
						map.put("Zc_zcje", templist.getString("raise_money"));
						map.put("Zc_ycje", templist.getString("have_money"));
						map.put("raise_status", templist.getString("raise_status"));
						map.put("Zc_pic", templist.getString("hand_img"));
						map.put("Zc_progress",
								getProgress(templist.getString("raise_money"), templist.getString("have_money")));
						map.put("Zc_jindu",
								getProgress(templist.getString("raise_money"), templist.getString("have_money")) + "%");
						map.put("Zc_starttime", stampToDate(templist.getString("start_time")));
						map.put("Zc_czrs", templist.getString("people"));
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
		Zcadapter.notifyDataSetChanged();

		mListView.onRefreshComplete();
		Zcadapter.notifyDataSetChanged();

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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doHttp();
	}

	public void stop() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		_all.setBackgroundResource(R.drawable.btn_clear_bk);
		_pre.setBackgroundResource(R.drawable.btn_clear_bk);
		_toubiaoing.setBackgroundResource(R.drawable.btn_clear_bk);
		_chushouing.setBackgroundResource(R.drawable.btn_clear_bk);
		_toupiaoing.setBackgroundResource(R.drawable.btn_clear_bk);
		_finish.setBackgroundResource(R.drawable.btn_clear_bk);
		// 1:预热中 3:投标中 5：出售中 6：投票中 10：完成
		switch (v.getId()) {
		case R.id._all:
			_all.setBackgroundResource(R.drawable.btn_bk);
			doHttp();
			type = "";
			break;
		case R.id._pre:
			_pre.setBackgroundResource(R.drawable.btn_bk);
			doHttp1("1");
			type = "1";
			break;
		case R.id._toubiaoing:
			_toubiaoing.setBackgroundResource(R.drawable.btn_bk);
			doHttp1("3");
			type = "3";
			break;
		case R.id._chushouing:
			_chushouing.setBackgroundResource(R.drawable.btn_bk);
			doHttp1("5");
			type = "5";
			break;
		case R.id._toupiaoing:
			_toupiaoing.setBackgroundResource(R.drawable.btn_bk);
			doHttp1("6");
			type = "6";
			break;
		case R.id._finish:
			_finish.setBackgroundResource(R.drawable.btn_bk);
			doHttp1("10");
			type = "10";
			break;
		default:
			break;
		}
	}

	protected void initJsonData(JSONObject json) {
		list.clear();
		// public TextView zc_title;
		//// public TextView zc_fbsj;
		// public TextView Zc_zcje;
		// public TextView Zc_ycje;
		// public TextView Zc_czrs;
		// ProgressBar Zc_progress;
		// public TextView Zc_jindu;
		// public TextView Zc_starttime;
		// TextView Zc_item_ljrg;
		// ImageView Zc_pic;
		try {
			// 散标数据初始化
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			maxPage = json.getInt("totalpage");
			curPage = json.getInt("nowpage");
			if (!json.isNull("list")) {
				jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						map = new HashMap<String, Object>();
						map.put("id", templist.getString("id"));
						map.put("zc_title", templist.getString("raise_name"));
						map.put("Zc_zcje", templist.getString("raise_money")+"(元)");
						map.put("Zc_ycje", templist.getString("have_money")+"(元)");
						map.put("raise_status", templist.getString("raise_status"));
						map.put("Zc_pic", templist.getString("hand_img"));
						map.put("Zc_progress",
								getProgress(templist.getString("raise_money"), templist.getString("have_money")));
						map.put("Zc_jindu",
								getProgress(templist.getString("raise_money"), templist.getString("have_money")) + "%");
						map.put("Zc_starttime", stampToDate(templist.getString("start_time")));
						map.put("Zc_czrs", templist.getString("people")+"(人)");
						
						list2.add(map);
					}
				}
			}
			list.clear();
			list.addAll(list2);
			list.addAll(list2);
			// handler.sendEmptyMessage(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Zcadapter.notifyDataSetChanged();
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

	public static String getProgress(String ze, String yc) {
		// Double.parseDouble(ze);
		// new BigDecimal(ze);
		if (yc.equals("0.00")) {
			return "0";
		}
		if(ze.equals(yc)){
			return "100";
		}
		BigDecimal b1 = new BigDecimal(ze);
		BigDecimal b2 = new BigDecimal(yc);
		return b1.divide(b2, 2, RoundingMode.HALF_UP).doubleValue() + "";
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				Zcadapter.notifyDataSetChanged();
				mListView.onRefreshComplete();
			}
			if (msg.what == 2) {
				showCustomToast("没有更多数据了");
				mListView.onRefreshComplete();
				// Toast.makeText(TouziActivity_Sx.this, "没有更多数据了",
				// Toast.LENGTH_SHORT).show();
			}
		}

	};
}
