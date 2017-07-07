package com.lmq.menu;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.BidItem_Scg;
import com.lmq.main.activity.BorrowMoney_Sx;
import com.lmq.main.activity.LoginActivity_Scg;
import com.lmq.main.activity.Stretch_sx;
import com.lmq.main.activity.TouziActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.czscg.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//首页
@SuppressLint("NewApi")
public class IndexFragment_Sx extends BaseFragment implements OnClickListener {

	Long id;
	int type;
	ImageView stretch_sx;
	TextView sy_title_sx, bir;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.layout_index_sx, null);
		mainView.findViewById(R.id.borrow_money).setOnClickListener(this);
		mainView.findViewById(R.id.invest_sx).setOnClickListener(this);
		mainView.findViewById(R.id.to_tzxqitem_sx).setOnClickListener(this);
		stretch_sx = (ImageView) mainView.findViewById(R.id.stretch_sx);
		stretch_sx.setOnClickListener(this);
		sy_title_sx = (TextView) mainView.findViewById(R.id.sy_title_sx);
		bir = (TextView) mainView.findViewById(R.id.bir);
		initView(mainView);
		//
		// mHandler.postDelayed(runable, 5000);
		return mainView;
	}

	private void initView(View mainView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		indexnewdoHttp();

		// indexdoHttp();

		// WindowManager wm = getActivity().getWindowManager();
		// int width = wm.getDefaultDisplay().getWidth();
		// Log.e("onResume", stretch_sx.getHeight()+"");

		getPicUrlHttp();

		// Picasso.with(getActivity()).load("http://120.26.55.64//UF/Uploads/Article/1439458072.jpg")
		// .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.activityloading)
		// .error(R.drawable.activityloading).fit().into(stretch_sx);
		// Picasso.with(context)
		// .load(url)
		// .placeholder(R.drawable.user_placeholder)
		// .error(R.drawable.user_placeholder_error)
		// .into(imageView);
	}

	public void stop() {

	}

	Transformation transformation = new Transformation() {

		@Override
		public Bitmap transform(Bitmap source) {
			// WindowManager wm = getActivity().getWindowManager();
			// int width = wm.getDefaultDisplay().getWidth();
			int targetWidth = stretch_sx.getWidth();
			int targeth = stretch_sx.getHeight();
			int targeth2 = source.getHeight();
			// LogCat.i("source.getHeight()="+source.getHeight()+",source.getWidth()="+source.getWidth()+",targetWidth="+targetWidth);

			if (source.getWidth() == 0) {
				return source;
			}

			// 如果图片小于设置的宽度，则返回原图
			if (source.getWidth() < targetWidth) {
				// WindowManager wm = getActivity().getWindowManager();
				// int width = wm.getDefaultDisplay().getWidth();
				// source.setWidth(width);
				// source.setHeight(targetWidth);
				Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targeth, false);
				return result;
			} else {
				// 如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
				double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
				int targetHeight = (int) (targetWidth * aspectRatio);
				if (targetHeight != 0 && targetWidth != 0) {
					Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
					if (result != source) {
						// Same bitmap is returned if sizes are the same
						source.recycle();
					}
					return result;
				} else {
					// source.setHeight(targetWidth);
					return source;
				}
			}

		}

		@Override
		public String key() {
			return "transformation" + " desiredWidth";
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.borrow_money:
			if (Default.userId == 0) {
				Intent intent33 = new Intent(getActivity(), LoginActivity_Scg.class);
				startActivity(intent33);
			} else {
				Intent intent = new Intent(getActivity(), BorrowMoney_Sx.class);
				startActivity(intent);
			}
			break;
		case R.id.invest_sx:
			Intent intent1 = new Intent(getActivity(), TouziActivity.class);
			startActivity(intent1);
			break;
		case R.id.to_tzxqitem_sx:
			if (Default.userId == 0) {
				Intent intent2 = new Intent(getActivity(), LoginActivity_Scg.class);
				startActivity(intent2);
			} else {
				Intent intent2 = new Intent(getActivity(), BidItem_Scg.class);
				intent2.putExtra("id", id);
				intent2.putExtra("type", type);
				startActivity(intent2);
			}
			break;
		case R.id.stretch_sx:
			Intent intent3 = new Intent(getActivity(), Stretch_sx.class);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}

	/**
	 * 首页地址图片请求
	 */
	public void getPicUrlHttp() {

		JsonBuilder builder = new JsonBuilder();

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.indexurlpic, builder, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);

				try {
					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							showPic(json.getString("message"));
						} else {
							// showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
			}
		});

	}

	protected void showPic(String string) {
		// TODO Auto-generated method stub
		Picasso.with(getActivity()).load(Default.ip + string).memoryPolicy(MemoryPolicy.NO_CACHE)
				.placeholder(R.drawable.activityloading).error(R.drawable.activityloading).fit().into(stretch_sx);
	}

	/**
	 * 首页推荐标
	 */
	public void indexdoHttp() {

		JsonBuilder builder = new JsonBuilder();

		// builder.put("type",11);
		builder.put("is_tuijian", 1);
		builder.put("type", 11);
		builder.put("page", 1);
		builder.put("limit", 1);

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.tzList, builder, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);

				try {
					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							jxtjbjson(json);
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
			}
		});

	}

	protected void jxtjbjson(JSONObject json) {
		// TODO Auto-generated method stub

		try {
			// 散标数据初始化
			if (!json.isNull("list")) {
				// showCustomToast(json.toString());
				JSONArray jsonArray = json.getJSONArray("list");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject templist = jsonArray.getJSONObject(i);
						id = templist.getLong("id");
						type = templist.getInt("type");
						sy_title_sx.setText(templist.getString("borrow_name"));
						// map.put("id", templist.getLong("id"));
						// map.put("type", templist.getInt("type"));
						// map.put("progress_tz_sx",
						// templist.getDouble("progress"));
						// map.put("item_tzbt_sx",
						// templist.getString("borrow_name"));
						// map.put("item_jkje_sx", "借款金额:" +
						// templist.getString("borrow_money") + "元");
						// map.put("item_tzqx_sx", "投资期限:" +
						// templist.getString("borrow_duration") + "月");
						// map.put("item_nhl_sx",
						// templist.getString("borrow_interest_rate") + "%");
						// map.put("item_time_sx",
						// templist.getString("addtime"));
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 首页推荐新手标
	 */
	public void indexnewdoHttp() {

		JsonBuilder builder = new JsonBuilder();

		// 填充参数
		BaseHttpClient.post(getActivity(), Default.tznewList, null, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);

				try {
					if (statusCode == 200) {
						if (json.getInt("status") == 0) {
							jxtjbnewjson(json);
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				dismissLoadingDialog();
			}
		});

	}

	protected void jxtjbnewjson(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			String borrow_name = json.getString("borrow_name");
			String borrow_interest_rate = json.getString("borrow_interest_rate");
			sy_title_sx.setText(borrow_name);
			bir.setText(borrow_interest_rate + "%");
			id = json.getLong("id");
			type = json.getInt("borrow_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
