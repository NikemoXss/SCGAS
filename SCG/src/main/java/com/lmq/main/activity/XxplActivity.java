package com.lmq.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.adapter.MyGridViewAdapter;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;
import com.lmq.photowallfalls.MyScrollView;
import com.lmq.view.MyGridView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//信息披露
public class XxplActivity extends BaseActivity implements OnClickListener {
	ImageView imageview;
	TextView title, xxpl_tv;
	private long itemId;
	private MyGridView gview;
	private SimpleAdapter sim_adapter;
	private List<Map<String, Object>> data_list;
	MyGridViewAdapter mgva;

	MyScrollView mys;
	String[] image = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxpl_activity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);
		xxpl_tv = (TextView) findViewById(R.id.xxpl_tv);
		title = (TextView) findViewById(R.id.title);
		title.setText("信息披露");
		gview = (MyGridView) findViewById(R.id.xxpl_gv);
		data_list = new ArrayList<Map<String, Object>>();

		mys = (MyScrollView) findViewById(R.id.my_scroll_view);
		// mys.setImage(Images.imageUrls);

		Intent intent = getIntent();
		itemId = intent.getExtras().getLong("id");
	}

	public List<Map<String, Object>> getData() {
		// cion和iconName的长度是相同的，这里任选其一都可以
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", R.drawable.scg_logo_small);
			data_list.add(map);
		}

		return data_list;
	}

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
		getPageInfoHttp();
	}

	public void getPageInfoHttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("id", itemId);

		BaseHttpClient.post(getBaseContext(), Default.xxpl, builder, new JsonHttpResponseHandler() {

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
						if (json.getInt("status") == 1) {
							plJson(json);
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

	protected void plJson(JSONObject json) {
		// TODO Auto-generated method stub
		data_list.clear();
		try {
			xxpl_tv.setText("\u3000\u3000" + json.getString("borrow_info"));
			JSONArray jsonArray = new JSONArray(json.getString("updata"));
			image = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				String img = jsonArray.getString(i);

				// JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				// String img = jsonObject.getString("img");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", Default.ip + "/" + img);
				image[i] = Default.ip + "/" + img;
				data_list.add(map);
			}
			Message message = new Message();
			message.obj = image;
			message.what = 100;
			handler.sendMessage(message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		// mgva = new MyGridViewAdapter(XxplActivity.this, data_list);
		// gview.setAdapter(mgva);
		// mgva.notifyDataSetChanged();
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				String[] ss = (String[]) msg.obj;
				// mys = (MyScrollView) findViewById(R.id.my_scroll_view);
				mys.setImage(ss);
				mys.setOnTouchListener();
				break;

			default:
				break;
			}

		};
	};
}
