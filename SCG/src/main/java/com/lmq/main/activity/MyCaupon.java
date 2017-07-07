package com.lmq.main.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.adapter.MyBmAdapter;
import com.lmq.adapter.MyBmUsedAdapter;
import com.lmq.adapter.MyCanNoUseBmAdapter;
import com.czscg.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.NewsListActivity.newsAdapter;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.dialog.ProgressDialog;
import com.lmq.main.util.Default;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyCaupon extends BaseActivity implements OnClickListener {
	private TextView tv1, tv2, tv3;
	private PullToRefreshListView mListView;
	private MyBmAdapter myBmAdapter;
	private MyBmUsedAdapter myBmUsedAdapter;
	private MyCanNoUseBmAdapter myCanNoUseBmAdapter;
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> list1;
	private List<Map<String, Object>> list2;
	Map<String, Object> map;
	Map<String, Object> map1;
	Map<String, Object> map2;
	private LinearLayout lineview;
	ProgressDialog mLoadingDialog;
	private int ww;
	int one = 1;
	int two = 2;
	int zero = 0;
	int coo = one;

	public int REQUEST_CODE_PICK_IMAGE = 1;

	public int REQUEST_CODE_CAPTURE_CAMEIA = 2;

	public String url;

	ImageView title_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mycaupon);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		title_right = (ImageView) findViewById(R.id.title_right);
		title_right.setVisibility(View.VISIBLE);
		title_right.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我的加息券");
		lineview = (LinearLayout) findViewById(R.id.line);
		tv1 = (TextView) findViewById(R.id.canuse);
		tv2 = (TextView) findViewById(R.id.used);
		tv3 = (TextView) findViewById(R.id.cannouse);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		list = new ArrayList<Map<String, Object>>();
		list1 = new ArrayList<Map<String, Object>>();
		list2 = new ArrayList<Map<String, Object>>();
		mListView = (PullToRefreshListView) findViewById(R.id.mybmlv);
		// mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(PullToRefreshBase.Mode.DISABLED);
		// doHttpMyCanUseBm();

	}

	public void getPic() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
	}

	public String Bitmap2StrByBase64(Bitmap bit) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bit.compress(CompressFormat.JPEG, 40, bos);// 参数100表示不压缩
		byte[] bytes = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_IMAGE) {
			Uri uri = data.getData();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
				url = Bitmap2StrByBase64(bitmap);
				Toast.makeText(MyCaupon.this, url, Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
			Uri uri = data.getData();
			if (uri == null) {
				// use bundle to get data
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					Bitmap photo = (Bitmap) bundle.get("data"); // get bitmap
					// spath :生成图片取个名字和路径包含类型
					// saveImage(Bitmap photo, String spath);
				} else {
					Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				// to do find the path of pic by uri
			}
		}

	}

	private void doHttpMyCanUseBm() {
		JsonBuilder builder = new JsonBuilder();
		list1.clear();
		list.clear();
		list2.clear();
		// 填充参数
		BaseHttpClient.post(MyCaupon.this, Default.mycanbmList, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				getcanuseBmData(json);
				myBmAdapter = new MyBmAdapter(MyCaupon.this, list1);
				mListView.setAdapter(myBmAdapter);
				myBmAdapter.notifyDataSetChanged();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
			}
		});

	}

	public void getcanuseBmData(JSONObject json) {
		try {
			String status = json.getString("status");
			Log.e("+++++++++++status", json.toString());
			String message = json.getString("message");
			JSONArray jsonArray = new JSONArray(message);

			for (int i = 0; i < jsonArray.length(); i++) {
				map1 = new HashMap<String, Object>();
				JSONObject oj = jsonArray.getJSONObject(i);
				String reg_time = oj.getString("reg_time");
				String is_yh = oj.getString("reg_time");
				String lasttime = oj.getString("lasttime");
				String yhj = oj.getString("yhj");
				String limit = oj.getString("limit");
				map1.put("reg_time", reg_time);
				map1.put("is_yh", is_yh);
				map1.put("lasttime", lasttime);
				map1.put("yhj", yhj);
				map1.put("limit", limit);
				list1.add(map1);
			}

			Log.e("+++++++++++status", list1.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showLoadingDialogNoCancle(String text) {
		mLoadingDialog = new ProgressDialog(this, text);
		if (mLoadingDialog == null) {
			return;
		}
		if (text != null) {
			mLoadingDialog.setText(text);
			// mLoadingDialog.setCancelable(false);
		}

		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog != null) {
			if (mLoadingDialog.isShowing()) {
				mLoadingDialog.dismiss();
			}
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Toast.makeText(MyBriberyMoney.this, tv1.getWidth()+"", 1).show();
		WindowManager wManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int w = wManager.getDefaultDisplay().getWidth();
		ww = w / 3;
		LayoutParams params = new LayoutParams(ww, 7); // 宽 ， 高
		lineview.setLayoutParams(params);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.canuse:
//			showLoadingDialogNoCancle("请稍后努力加载中...");
			TheAnimation(zero);// 平移动画
			doHttpMyCanUseBm();

			// getPic();
			break;
		case R.id.used:
//			showLoadingDialogNoCancle("请稍后努力加载中...");
			TheAnimation(one);
			doHttpMyUsedBm();
			// test();
			break;
		case R.id.cannouse:
//			showLoadingDialogNoCancle("请稍后努力加载中...");
			TheAnimation(two);
			doHttpMyCanNoUseUseBm();
			break;
		case R.id.title_right:
			finish();
			break;
		default:
			break;
		}
	}

	private void test() {
		// TODO Auto-generated method stub
		JsonBuilder builder = new JsonBuilder();
		builder.put("real_name", "33");
		builder.put("personal_id", "44");
		builder.put("photo1", url);
		builder.put("photo2", url);
		Log.e("+++JsonBuilder+++", url);
		// 填充参数
		BaseHttpClient.post(MyCaupon.this, Default.uppic, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				Log.e("+++onSuccess+++", json.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
				Log.e("+++onSuccess+++", errorResponse.toString());
			}
		});
	}

	private void doHttpMyCanNoUseUseBm() {
		list1.clear();
		list.clear();
		list2.clear();
		// TODO Auto-generated method stub
		JsonBuilder builder = new JsonBuilder();

		// 填充参数
		BaseHttpClient.post(MyCaupon.this, Default.mycannousebmList, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				getcannouseBmData(json);
				myCanNoUseBmAdapter = new MyCanNoUseBmAdapter(MyCaupon.this, list2);
				mListView.setAdapter(myCanNoUseBmAdapter);
				myCanNoUseBmAdapter.notifyDataSetChanged();
//				Log.e("+++++++++++++++mycannousebmList", json.toString());

			}

			private void getcannouseBmData(JSONObject json) {
				// TODO Auto-generated method stub

				try {
					String status = json.getString("status");
					Log.e("+++++++++++status", json.toString());
					String message = json.getString("list");
					JSONArray jsonArray = new JSONArray(message);
					for (int i = 0; i < jsonArray.length(); i++) {
						map2 = new HashMap<String, Object>();
						JSONObject oj = jsonArray.getJSONObject(i);
						String money = oj.getString("money");
						String lasttime = oj.getString("last_time");
						String limit = oj.getString("limit");
						map2.put("money", money);
						map2.put("lasttime", lasttime);
						map2.put("limit", limit);
						list2.add(map);
					}

					// Log.e("+++++++++++status", list2.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	private void doHttpMyUsedBm() {
		list1.clear();
		list.clear();
		list2.clear();
		// TODO Auto-generated method stub
		JsonBuilder builder = new JsonBuilder();

		// 填充参数
		BaseHttpClient.post(MyCaupon.this, Default.myusedbmList, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				getcannouseBmData(json);
				myBmUsedAdapter = new MyBmUsedAdapter(MyCaupon.this, list);
				mListView.setAdapter(myBmUsedAdapter);
				myBmUsedAdapter.notifyDataSetChanged();
				// mListView.onRefreshComplete();
//				Log.e("+++++++++++++++mycannousebmList", json.toString());

			}

			private void getcannouseBmData(JSONObject json) {
				// TODO Auto-generated method stub

				try {
					String status = json.getString("status");
					Log.e("+++++++++++status", json.toString());
					String message = json.getString("list");
					JSONArray jsonArray = new JSONArray(message);
					for (int i = 0; i < jsonArray.length(); i++) {
						map = new HashMap<String, Object>();
						JSONObject oj = jsonArray.getJSONObject(i);
						String money = oj.getString("money");
						String lasttime = oj.getString("last_time");
						String limit = oj.getString("limit");
						map.put("money", money);
						map.put("lasttime", lasttime);
						map.put("limit", limit);
						list.add(map);
					}

					Log.e("+++++++++++status", list.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	public void TheAnimation(int arg0) {
		Animation translateAnimation3 = new TranslateAnimation(ww * coo, ww * arg0, 0, 0);
		coo = arg0;
		// 设置动画时间
		translateAnimation3.setDuration(200);
		translateAnimation3.setFillAfter(true);
		lineview.startAnimation(translateAnimation3);
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

}
