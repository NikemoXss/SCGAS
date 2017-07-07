package com.lmq.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.adapter.RedPkg_tzAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseRedPacketList extends BaseActivity implements OnClickListener {
	private PullToRefreshListView mListView;
	private RedPkg_tzAdapter redAdapter;
	Map<String, Object> map = null;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	ImageView imageview;
	TextView title;
	String borrow_duration, repayment_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_jxjlist_tz);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("我的红包");
		mListView = (PullToRefreshListView) findViewById(R.id.jxjlist_tz);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.BOTH);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView tv_id = (TextView) view.findViewById(R.id.id_jxj);
				TextView tv_money = (TextView) view.findViewById(R.id.money_tz);
				String cbid = tv_id.getText().toString();
				String cbmoney = tv_money.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("return_cbid", cbid);
				intent.putExtra("return_cbmoney", cbmoney);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		Intent intent = getIntent();
		borrow_duration = intent.getStringExtra("borrow_duration");
		repayment_type = intent.getStringExtra("repayment_type");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		doHttp();
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
			Intent intent = new Intent();
			intent.putExtra("return_cbid", "00");
			intent.putExtra("return_cbmoney", "00");
			setResult(RESULT_OK, intent);
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
		builder.put("borrow_duration", borrow_duration);
		builder.put("repayment_type", repayment_type);

		// 填充参数
		BaseHttpClient.post(ChooseRedPacketList.this, Default.chooseredpkgList, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				MyLog.e("statusCode=" + statusCode + "--headers=" + headers.toString() + "--json=" + json.toString());
//				showCustomToast(json.toString());
				if (statusCode == 200) {
					try {
						if (json.getInt("status") == 2) {
							initJsonData(json);
						} else {
							showCustomToast(json.getString("message"));
							list.clear();
						}
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
		// TODO Auto-generated method stub
		try {
			JSONArray jsonArray = json.getJSONArray("msg");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				map = new HashMap<String, Object>();
				map.put("id", jsonObject.getString("id"));
				map.put("restrict_duration", jsonObject.getString("restrict_duration"));
				map.put("money", jsonObject.getString("money"));
				map.put("end_time", stampToDate(jsonObject.getString("end_time")));
				map.put("invest_money", jsonObject.getString("invest_money"));
				list.add(map);
			}
			redAdapter = new RedPkg_tzAdapter(ChooseRedPacketList.this, list);
			mListView.setAdapter(redAdapter);
			redAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  public static String stampToDate(String s){
	        String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        long lt = new Long(s+"000");//java中生成的时间戳精确到毫秒级别，而unix中精确到秒级别，所以通过java生成的时间戳需要除以1000。
	        Date date = new Date(lt);
	        res = simpleDateFormat.format(date);
	        return res;
	    }

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				
			}
			if (msg.what == 2) {
				showCustomToast("没有更多数据了");
			}
		}

	};

}
