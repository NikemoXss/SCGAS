package com.lmq.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.adapter.CanAndNotUseJxjAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.dialog.InvestPopList;
import com.lmq.main.util.Default;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加息劵
 * 
 */
public class InterestratesecuritiesActivity extends BaseActivity implements OnClickListener {

	private InvestPopList popList;
	private int current_type = -1;
	private PullToRefreshListView listView;
	private CanAndNotUseJxjAdapter adapter;
	private int page = 1;
	private int limit = 8;
	private int totalPage = 0;
	private TextView text;
	private String requestURL = Default.jxjlist;
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
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Map<String, Object> map = null;
	Toast t=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_jxj_scg);

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

		listView = (PullToRefreshListView) findViewById(R.id.jxj_list);
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

		popList = new InvestPopList(InterestratesecuritiesActivity.this);
		popList.setOnItemClickLinstener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position != popList.getDefauleSelect()) {
					popList.setDefauleSelect(position);

				}
				tipsType = position;
				Animation animation = AnimationUtils.loadAnimation(InterestratesecuritiesActivity.this,
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

					requestURL = Default.jxjlist;

					break;
				case 1:
					requestURL = Default.jxjlist;
					break;
				case 2:
					requestURL = Default.jxjlist;
					break;
				case 3:
					requestURL = Default.jxjlist;
					break;
				}

				text.setText(titleArray[position]);
				getInvestData(getPostParams(), true);

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
				t=showCustomToast("无更多数据！",t);

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
		builder.put("status", getType());
		return builder;
	}

	public int getType() {
		switch (tipsType) {
		case 0:
			return 2;
		case 1:
			return 3;
		case 2:
			return 4;
		default:
			return 2;
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
			tipsType = 0;
			getInvestData(getPostParams(), true);
			break;
		case R.id.used:
			showLoadingDialogNoCancle("请稍后努力加载中...");
			tipsType = 1;
			TheAnimation(one);
			getInvestData(getPostParams(), true);
			break;
		case R.id.cannouse:
			showLoadingDialogNoCancle("请稍后努力加载中...");
			tipsType = 2;
			TheAnimation(two);
			getInvestData(getPostParams(), true);
			break;
		case R.id.title:

			Animation animation = AnimationUtils.loadAnimation(InterestratesecuritiesActivity.this,
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

	// 提交用户反馈到服务器
	public void getInvestData(JsonBuilder builder, boolean refsh) {

		if (refsh) {
			list.clear();
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
							getData(json);
						} else if (json.optInt("status") == 0) {
							t=showCustomToast(json.getString("message"),t);
						} else {
							t=showCustomToast(json.getString("message"),t);
						}
					} else {
						t=showCustomToast(R.string.toast1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismissLoadingDialog();
				adapter = new CanAndNotUseJxjAdapter(InterestratesecuritiesActivity.this, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				t=showCustomToast(responseString,t);
				listView.onRefreshComplete();

			}

		});

		// adapter.notifyDataSetChanged();

	}

	// public TextView rate_scg;
	// public TextView infor_scg;
	// public TextView type_scg;
	// public TextView start_time_scg;
	// public TextView duration_scg;

	@SuppressLint("NewApi")
	protected void getData(JSONObject json) {
		// TODO Auto-generated method stub
		try {

			JSONArray jsonArray = json.getJSONArray("msg");
			for (int i = 0; i < jsonArray.length(); i++) {
				map = new HashMap<String, Object>();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				map.put("rate_scg", jsonObject.optString("rate"));
				// map.put("infor_scg", jsonObject.optString("infor"));
				map.put("type_scg", jsonObject.optString("type"));// 使用条件
				map.put("end_time_scg", stampToDate(jsonObject.optString("end_time")));// 结束时间
				// map.put("duration_scg", jsonObject.optString("duration"));
				map.put("status_scg", jsonObject.optString("status"));// 使用状态 2
																		// 3 4
				map.put("borrow_id_scg", jsonObject.optString("borrow_id"));// 表的ID
				map.put("use_time_scg", stampToDate(jsonObject.optString("use_time")));// 使用时间
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String stampToDate(String s) {
		String res;
		if (!"null".equals(s)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long lt = new Long(s + "000");// java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别，所以通过java生成的时间戳需要除以1000。
			Date date = new Date(lt);
			res = simpleDateFormat.format(date);
		} else {
			res = "00";
		}

		return res;
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(t!=null){
			t.cancel();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(t!=null){
			t.cancel();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(t!=null){
			t.cancel();
		}
	}

}
