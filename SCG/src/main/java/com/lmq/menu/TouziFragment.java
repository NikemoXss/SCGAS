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
import com.lmq.adapter.TouZiItemAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//投资页
public class TouziFragment extends BaseFragment implements OnClickListener {

	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private int maxPage, curPage = 1, pageCount = 5;
	private JSONArray jsonArray = null;
	String type = "";

	ListView videoList;
	RefreshLayout refreshLayout;
	TextView atTime,atRate,atDate;
	Boolean isJx[]=new Boolean[]{true,true,true};
	TouZiItemAdapter  touziitemAdaoter;

	String orders[]=new String[]{"1","2","3"};
	int i=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.layout_touzi_scw_fragment, null);
		initView(mainView);
		initView();
		return mainView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		atTime.setBackgroundResource(R.color.main_color_new);
	}

	private void initView(View mainView) {
		atTime=(TextView) mainView.findViewById(R.id.atTime);
		atRate=(TextView) mainView.findViewById(R.id.atRate);
		atDate=(TextView) mainView.findViewById(R.id.atDate);
		atTime.setOnClickListener(this);
		atRate.setOnClickListener(this);
		atDate.setOnClickListener(this);

		videoList=(ListView) mainView.findViewById(R.id.videoList);
		refreshLayout=(RefreshLayout) mainView.findViewById(R.id.refreshLayout);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.atTime:
				atTime.setBackgroundResource(R.color.main_color_new);
				atRate.setBackgroundResource(R.color.gray_sx);
				atDate.setBackgroundResource(R.color.gray_sx);
				i=0;
				if(isJx[i]){
					atTime.setText("按时间↑");
					isJx[i]=false;
				}else {
					atTime.setText("按时间↓");
					isJx[i]=true;
				}
				break;
			case R.id.atRate:
				atRate.setBackgroundResource(R.color.main_color_new);
				atTime.setBackgroundResource(R.color.gray_sx);
				atDate.setBackgroundResource(R.color.gray_sx);
				i=1;
				if(isJx[i]){
					atRate.setText("按利率↑");
					isJx[i]=false;
				}else {
					atRate.setText("按利率↓");
					isJx[i]=true;
				}
				break;
			case R.id.atDate:
				atDate.setBackgroundResource(R.color.main_color_new);
				atRate.setBackgroundResource(R.color.gray_sx);
				atTime.setBackgroundResource(R.color.gray_sx);
				i=2;
				if(isJx[i]){
					atDate.setText("按期限↑");
					isJx[i]=false;
				}else {
					atDate.setText("按期限↓");
					isJx[i]=true;
				}
				break;
		}
		doHttp(orders[i]);
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doHttp(orders[i]);
	}

	public int firstVisible = 0, visibleCount = 0, totalCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void initView() {
		touziitemAdaoter = new TouZiItemAdapter(getActivity(), list);
		videoList.setAdapter(touziitemAdaoter);
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
				doHttp(orders[i]);
			}
		});
		refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				JsonBuilder builder = new JsonBuilder();
				if (curPage + 1 <= maxPage) {
					curPage += 1;
					builder.put("order",orders[i]);
					builder.put("page", curPage);
					builder.put("limit", pageCount);
					doHttp(builder);
					// handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(2);
				}
				refreshlayout.finishLoadmore();
			}
		});

		//设置 Header 为 Material风格
		refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
		//设置 Footer 为 球脉冲
		refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

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
						if (response.getInt("status") == 200) {
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

				touziitemAdaoter.notifyDataSetChanged();
				dismissLoadingDialog();
			}
		});
	}

	protected void updateAddInfo(JSONObject json) {
		// TODO Auto-generated method stub

		try {
			// 散标数据初始化

			maxPage = json.getInt("totalpages");
			curPage = json.getInt("nowpage");
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			if (!json.isNull("list")) {
				jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						map = new HashMap<String, Object>();
						map.put("id", templist.getLong("id"));
						map.put("item_tzqx_sx",  templist.optString("borrow_duration"));
						map.put("repayment_type", templist.optString("repayment_type"));
						map.put("item_nhl_sx", templist.optString("borrow_interest_rate"));
						map.put("borrow_name", templist.optString("borrow_name"));
						map.put("has_borrow", templist.optDouble("has_borrow"));
						map.put("item_jkje_sx",   templist.optDouble("borrow_money") );
						map.put("extra_rate", templist.optString("extra_rate"));
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
		touziitemAdaoter.notifyDataSetChanged();

	}

	public void doHttp(String order) {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("page", curPage);
		builder.put("limit", pageCount);
		builder.put("order", order);

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
				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 200) {
							initJsonData(json);
						} else {
							showCustomToast(json.getString("message"));
							list.clear();
						}
						dismissLoadingDialog();
						touziitemAdaoter.notifyDataSetChanged();
//						mListView.onRefreshComplete();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					touziitemAdaoter.notifyDataSetChanged();
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
			maxPage = json.getInt("totalpages");
			curPage = json.getInt("nowpage");
			if (!json.isNull("list")) {
				jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						map = new HashMap<String, Object>();
						map.put("id", templist.getLong("id"));
						map.put("item_tzqx_sx",  templist.optString("borrow_duration"));
						map.put("repayment_type", templist.optString("repayment_type"));
						map.put("item_nhl_sx", templist.optString("borrow_interest_rate"));
						map.put("borrow_name", templist.optString("borrow_name"));
						map.put("has_borrow", templist.optDouble("has_borrow"));
						map.put("item_jkje_sx", templist.optDouble("borrow_money") );
						map.put("extra_rate", templist.optString("extra_rate"));
						list2.add(map);
					}
				}
			}
			list.clear();
			list.addAll(list2);
			touziitemAdaoter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		touziitemAdaoter.notifyDataSetChanged();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				touziitemAdaoter.notifyDataSetChanged();
			}
			if (msg.what == 2) {
				showCustomToast("没有更多数据了");
			}
		}

	};
}
