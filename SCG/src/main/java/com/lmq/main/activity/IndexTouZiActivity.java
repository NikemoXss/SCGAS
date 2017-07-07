package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.dialog.ZqzrDialog;
import com.lmq.main.item.tzItem;
import com.lmq.main.util.Default;
import com.lmq.view.MyListView;
import com.lmq.view.MyListView.LoadMoreInfo;
import com.lmq.view.RoundProgressBar;

public class IndexTouZiActivity extends BaseActivity implements OnClickListener {
	private View parentView;
	private MyListView mListView;
	private tzAdapter mTzAdapter;

	public List ListInfo = new ArrayList<tzItem>();
	public List mListpt, mListlz, dList;

	private JSONArray list = null;
	private JSONArray tlist = null;

	// 债券转让
	private JSONArray llist = null;
	private JSONArray dlist = null;
	private List lListInfo = null;

	private int mSelectIndex;
	LayoutInflater mInflater;
	private int maxChose = 3;

	// 用户查看标种类 Flag 0:散标 1：定投宝 2：企业直投 3：债权转让
	private int swith_flag = 0;

	private Button button1, button2, button3, button4;

	private boolean mboolean[] = new boolean[maxChose];
	private Button mChose;
	private PopupWindow popWindow;

	private int maxPage, curPage = 1, pageCount = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_tz);
		initView();
	}

	protected void initView() {
		// mChose = (Button) mainView.findViewById(R.id.chose);
		mInflater = LayoutInflater.from(IndexTouZiActivity.this);
		mListView = (MyListView) findViewById(R.id.listview);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setDividerHeight(0);
		mListView.showFootView(true);
		TextView title = (TextView) findViewById(R.id.title);

		Intent intent = getIntent();

		switch (intent.getIntExtra("swith_flag", 0)) {
		case 0:
			swith_flag = 0;
			title.setText(getResources().getString(R.string.index_str14));
			break;
		case 1:
			swith_flag = 1;
			title.setText(getResources().getString(R.string.index_str16));
			break;
		case 2:
			swith_flag = 2;
			title.setText(getResources().getString(R.string.index_str18));
			break;
		case 3:
			swith_flag = 3;
			title.setText(getResources().getString(R.string.index_str20));
			break;

		default:
			break;
		}

		// if (mListpt == null || mListpt.size() == 0) {
		//
		// doHttp();
		// } else {
		// ListInfo.clear();
		// ListInfo.addAll(mListpt);
		// mTzAdapter.notifyDataSetChanged();
		// mListView.onLoadMoreComplete();
		//
		// }

		mListpt = new ArrayList<tzItem>();
		mListlz = new ArrayList<tzItem>();
		lListInfo = new ArrayList<tzItem>();
		dList = new ArrayList<tzItem>();

		findViewById(R.id.back).setOnClickListener(this);

		initInfo();
	}

	public void initInfo() {

		mTzAdapter = new tzAdapter(IndexTouZiActivity.this);
		mListView.setAdapter(mTzAdapter);

		// mboolean[0] = false;
		// updateChose(0);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSelectIndex = position - 1;
				tzItem item = (tzItem) ListInfo.get(mSelectIndex);
				if (swith_flag == 3) {
					// 显示操作
					if (Default.userId == 0) {
						startActivity(new Intent(IndexTouZiActivity.this, loginActivity.class));

					} else {
						if (item.getUid() == Default.userId) {
							showCustomToast("不能购买自己发布的债权标");
						} else {
							if (item.getValid() == 0) {
								showCustomToast("交易已经完成，请浏览其他标");
							} else {
								doHttpBuyZQZR(item);
							}
						}

					}

				} else {
					showItem(item);
					// showItem(item);
				}

			}
		});

		// 增加请求服务器提交字段
		mListView.setOnLoadMoreInfo(new LoadMoreInfo() {
			@Override
			public void onRefresh() {
				curPage = 1;
				doHttp();
			}

			// TODO 增加请求服务器提交字段
			@Override
			public void onLoadMore() {
				JsonBuilder builder = new JsonBuilder();
				builder.put("type", getType());

				if (curPage + 1 <= maxPage) {
					curPage++;

					builder.put("page", curPage);
					builder.put("limit", pageCount);
					doHttp(builder);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		});
	}

	public long[] getListId(boolean max) {

		long[] info = new long[1];
		long maxid1 = 1;
		long minid1 = 1;

		if (ListInfo != null) {
			if (ListInfo.size() > 1) {
				maxid1 = minid1 = ((tzItem) ListInfo.get(0)).getId();
			}
		}
		for (int i = 1; i < ListInfo.size(); i++) {
			tzItem item = (tzItem) ListInfo.get(i);
			long id = item.getId();
			if (max) {
				if (maxid1 < id) {
					maxid1 = id;
				}
			} else {
				if (minid1 > id) {
					minid1 = id;
				}
			}
		}

		if (max) {
			info[0] = maxid1;
		} else {
			info[0] = minid1;
		}

		MyLog.e("123", "最大小 =" + info[0]);
		return info;
	}

	class tzAdapter extends BaseAdapter {

		public tzAdapter(Context context) {}

		@Override
		public int getCount() {

			if (ListInfo == null) {
				return 0;
			} else {
				return ListInfo.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 获取当前positon 中包含的信息
			tzItem item = (tzItem) ListInfo.get(position);

			/**
			 * 对item 中的type进行判断 101 :债券转让
			 */

			if (swith_flag == 3) {

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.zq_item2_new, null);
				} else {
					String tagFlag = (String) convertView.getTag();
					if (tagFlag.equals("1")) {
						convertView = mInflater.inflate(R.layout.zq_item2_new, null);
					}

				}

				{

					TextView zq_title = (TextView) convertView.findViewById(R.id.zq_title);
					TextView zq_money = (TextView) convertView.findViewById(R.id.zq_num1);
					TextView zq_bx = (TextView) convertView.findViewById(R.id.zq_num2);
					TextView zq_lv = (TextView) convertView.findViewById(R.id.zq_num3);
					TextView zq_qx = (TextView) convertView.findViewById(R.id.zq_num4);
					View zq_has_done = convertView.findViewById(R.id.zq_has_done);

					zq_title.setText(item.getName());

					zq_money.setText(SystenmApi.getMoneyInfo(item.getMoney()) + "元");
					zq_bx.setText(item.getDq_money() + "元");
					zq_lv.setText(item.getNhll() + "%");
					zq_qx.setText(item.getPeriod());
					if (item.getValid() == 1) {

						zq_has_done.setBackgroundDrawable(null);
					}
				}
				convertView.setTag("2");
				return convertView;

			} else {

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.adapter_item_tz, null);
				} else {

					String tagFlag = (String) convertView.getTag();
					if (tagFlag.equals("2")) {
						convertView = mInflater.inflate(R.layout.adapter_item_tz, null);
					}
				}

				{
					if (swith_flag == 0) {

						ImageView normal_kind_flag = (ImageView) convertView.findViewById(R.id.normal_kind_flag);
						normal_kind_flag.setVisibility(View.VISIBLE);

						switch (item.getType()) {
						case 3:
							normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_3);
							break;
						case 4:
							normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_4);
							break;
						case 5:
							normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_5);
							break;
						case 6:
							normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_6);
							break;
						case 7:
							normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_7);
							break;

						default:
							break;
						}

					}
					TextView tz_title = (TextView) convertView.findViewById(R.id.tz_info);
					TextView tz_money = (TextView) convertView.findViewById(R.id.tz_num1);
					TextView tz_lv = (TextView) convertView.findViewById(R.id.tz_num2);
					TextView tz_time = (TextView) convertView.findViewById(R.id.tz_num4);

					RoundProgressBar tz_pro = (RoundProgressBar) convertView.findViewById(R.id.progress);
					TextView post_time = (TextView) convertView.findViewById(R.id.tz_date);

					tz_title.setText("" + item.getName());
					post_time.setText("" + item.getTime());
					tz_money.setText(SystenmApi.getMoneyInfo(item.getMoney()) + "元");
					tz_lv.setText(item.getNhll() + "%");
					tz_time.setText(item.getJkqx());

					tz_pro.setProgress((int) item.getProgress());

				}
				convertView.setTag("1");
				return convertView;
			}

		}
	}

	public void showItem(tzItem item) {

		long id = item.getId();
		final int type = item.getType();
		MyLog.e("123", "获取的Type值=====Type==" + type);
		Intent intent = new Intent(IndexTouZiActivity.this, type == 201 || type == 301 ? itemInfo3_1Activity.class : itemInfo2_1Activity.class);
		intent.putExtra("id", id);
		intent.putExtra("type", type);

		startActivity(intent);

		overridePendingTransition(R.anim.right_to_left, R.anim.to_left);
	}

	public void doHttp(JsonBuilder builder) {

		BaseHttpClient.post(getBaseContext(), Default.tzList, builder, new JsonHttpResponseHandler() {
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
							updateAddInfo(json);
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
					dismissLoadingDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				mTzAdapter.notifyDataSetChanged();

				mListView.onRefreshComplete();
				mTzAdapter.notifyDataSetChanged();
				mListView.onLoadMoreComplete();
				dismissLoadingDialog();
				showCustomToast(responseString);
			}
		});
	}

	public void updateAddInfo(JSONObject json) {
		try {

			// 初始化 散标数据
			maxPage = json.getInt("totalPage");

			list = null;
			ArrayList<tzItem> temp_list = new ArrayList<tzItem>();
			if (!json.isNull("list")) {
				list = json.getJSONArray("list");

				if (list != null) {
					for (int i = 0; i < list.length(); i++) {
						JSONObject templist = list.getJSONObject(i);
						tzItem item = new tzItem();
						item.init(templist);
						temp_list.add(item);

					}
				}
				switch (swith_flag) {
				case 0:

					mListpt.addAll(temp_list);
					ListInfo.clear();
					ListInfo.addAll(mListpt);
					break;
				case 1:

					dList.addAll(temp_list);
					ListInfo.clear();
					ListInfo.addAll(dList);
					break;

				case 2:

					mListlz.addAll(temp_list);
					ListInfo.clear();
					ListInfo.addAll(mListlz);
					break;

				case 3:

					lListInfo.addAll(temp_list);
					ListInfo.clear();
					ListInfo.addAll(lListInfo);
					break;

				default:
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTzAdapter.notifyDataSetChanged();

		mListView.onRefreshComplete();
		mListView.onLoadMoreComplete();
	}

	public void doHttp() {
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("type", getType());
		builder.put("page", curPage);
		builder.put("limit", pageCount);
		BaseHttpClient.post(getBaseContext(), Default.tzList, builder, new JsonHttpResponseHandler() {

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
							initData(json);
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
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				mTzAdapter.notifyDataSetChanged();

				mListView.onRefreshComplete();
				mTzAdapter.notifyDataSetChanged();
				mListView.onLoadMoreComplete();

				showCustomToast(responseString);
			}

		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.arg1 == 2) {
				showCustomToast(msg.getData().getString("info"));
			}
			if (msg.what == 1) {
				showCustomToast("无更多数据！");

				mListView.onRefreshComplete();
				mListView.onLoadMoreComplete();
			}
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		doHttp();
	}

	public void loadImage() {
		int start = mListView.getFirstVisiblePosition();
		int end = mListView.getLastVisiblePosition();
		if (end >= ListInfo.size()) {
			end = ListInfo.size() - 1;
		}
		// MainActivity.mMainActivity.getImageLodader().setLoadLimit(start,
		// end);
		// MainActivity.mMainActivity.getImageLodader().unlock();
	}

	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				// MainActivity.mMainActivity.getImageLodader().lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				loadImage();
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

	// 处理ListView加载性能处理

	private int beginIndex, endIndex;

	public void moveChose(int index) {
		endIndex = beginIndex;
		beginIndex = index;

		float cw = mChose.getMeasuredWidth();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int off = width / maxChose;
		float hh = (off - cw) / 2;
		TranslateAnimation animation = new TranslateAnimation(endIndex * off + hh, beginIndex * off + hh, 0, 0);
		animation.setDuration(100);
		animation.setFillAfter(true);
		animation.setFillBefore(true);
		mChose.startAnimation(animation);

	}

	public void initData(JSONObject json) {
		ListInfo.clear();

		try {
			// 散标数据初始化

			maxPage = json.getInt("totalPage");
			list = null;
			ArrayList<tzItem> temp_list = new ArrayList<tzItem>();
			if (!json.isNull("list")) {
				list = json.getJSONArray("list");

				if (list != null) {
					for (int i = 0; i < list.length(); i++) {
						JSONObject templist = list.getJSONObject(i);
						tzItem item = new tzItem();
						item.init(templist);
						temp_list.add(item);
					}
				}
				switch (swith_flag) {
				case 0:

					mListpt.clear();
					mListpt.addAll(temp_list);
					ListInfo.addAll(mListpt);
					break;
				case 1:

					dList.clear();
					dList.addAll(temp_list);
					ListInfo.addAll(dList);
					break;

				case 2:

					mListlz.clear();
					mListlz.addAll(temp_list);
					ListInfo.addAll(mListlz);
					break;

				case 3:

					lListInfo.clear();
					lListInfo.addAll(temp_list);
					ListInfo.addAll(lListInfo);
					break;

				default:
					break;
				}
				// 将数据全部填充到 ListInfo中
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTzAdapter.notifyDataSetChanged();

		mboolean[0] = false;

		mListView.onRefreshComplete();
		mListView.onLoadMoreComplete();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.back) {

			finish();
		}

	}

	public int getType() {
		switch (swith_flag) {
		case 0: // 普通标
			return 11;
		case 1: // 定投宝
			return 201;
		case 2:// 企业直投
			return 301;
		case 3:// 定投宝
			return 101;
		default:
			return 11;
		}
	}

	public void doHttpBuyZQZR(final tzItem item) {
		long id = item.getId();
		JsonBuilder builder = new JsonBuilder();
		builder.put("uid", Default.userId + "");
		builder.put("id", id + "");

		BaseHttpClient.post(getBaseContext(), Default.zq_request, builder, new JsonHttpResponseHandler() {

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
						dismissLoadingDialog();
						if (!json.getBoolean("has_pin")) {
							showCustomToast("请先设置支付密码！");
						} else {
							showZQDialog(json);
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

	private ZqzrDialog dialog;

	public void showZQDialog(JSONObject json) {
		try {
			final int id = json.getInt("id");
			final double price = json.getDouble("price");
			final double dq_money = json.getDouble("dq_money");
			final double account_money = json.getDouble("account_money");
			final String password = null;

			dialog = new ZqzrDialog(IndexTouZiActivity.this, price, dq_money, account_money, password);
			dialog.setListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v.getId() == R.id.gm) {

						{
							String pass = dialog.getPassword();
							doHttpZQZR(id, pass);
						}
					}
				}
			});
			dialog.show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doHttpZQZR(long id, String pass) {

		JsonBuilder builder = new JsonBuilder();
		builder.put("uid", Default.userId + "");
		builder.put("id", id + "");
		builder.put("pin", pass);

		BaseHttpClient.post(getBaseContext(), Default.zq_buy_request, builder, new JsonHttpResponseHandler() {

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

							showCustomToast(json.getString("message"));

						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}

					dialog.dismiss();
					doHttp();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int code = data.getIntExtra("code", -1);
		String message = data.getStringExtra("message");
		int code1 = data.getIntExtra("code1", -1);
		String message1 = data.getStringExtra("message1");
		int code2 = data.getIntExtra("code2", -1);
		String message2 = data.getStringExtra("message2");
		if (code == 88) {
			showCustomToast("恭喜您，投标成功！");
		} else if (code == -1) {
			if (code1 == 88 && code2 == 88) {
				dialog.dismiss();
				showCustomToast("恭喜您，投标成功！");
				swith_flag = 3;
			} else {
				if (data.getStringExtra("message") != null) {
					showCustomToast(data.getStringExtra("message"));
				}
			}
		} else {
			showCustomToast(data.getStringExtra("message"));
		}
	}
}
