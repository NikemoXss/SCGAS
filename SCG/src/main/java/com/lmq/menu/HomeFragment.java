package com.lmq.menu;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.itemInfo2_1Activity;
import com.lmq.main.activity.itemInfo3_1Activity;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.dialog.ZqzrDialog;
import com.lmq.main.item.tzItem;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
//投资
public class HomeFragment extends BaseFragment implements OnClickListener {
	private View parentView;
	private PullToRefreshListView mListView;
	private tzAdapter mTzAdapter;

	public ArrayList<tzItem> ListInfo = new ArrayList<tzItem>();
	// 散标
	private JSONArray list = null;

	private int mSelectIndex;
	private LayoutInflater mInflater;

	// 用户查看标种类 Flag 0:散标 1：定投宝 2：企业直投 3：债权转让
	public int swith_flag = 0;
	private RadioGroup top_switch;
	// private View button1, button2, button3, button4;
	// private TextView title1, title2, title3, title4;
	// private ImageView line1, line2, line3, line4;
	private View button1, button2;
	private TextView title1, title2;
	private ImageView line1, line2;
	private PopupWindow popWindow;

	private int maxPage, curPage = 1, pageCount = 5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.layout_home, container, false);
		initView(parentView);
		changeFlag(swith_flag);
		return parentView;
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	protected void initView(View mainView) {
		// mChose = (Button) mainView.findViewById(R.id.chose);
		mInflater = LayoutInflater.from(getActivity());
		mListView = (PullToRefreshListView) mainView.findViewById(R.id.listview);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setMode(Mode.BOTH);
		// swith_flag = 0;

		// 绑定选项卡点击事件
		button1 = mainView.findViewById(R.id.normal_btn);
		button2 = mainView.findViewById(R.id.normal2_btn);
		// button3 = (View) mainView.findViewById(R.id.normal3_btn);
		// button4 = (View) mainView.findViewById(R.id.normal4_btn);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		// button3.setOnClickListener(this);
		// button4.setOnClickListener(this);

		title1 = (TextView) mainView.findViewById(R.id.top_title1);
		title2 = (TextView) mainView.findViewById(R.id.top_title2);
		// 表列表只保留sa
		// title3 = (TextView) mainView.findViewById(R.id.top_title3);
		// title4 = (TextView) mainView.findViewById(R.id.top_title4);

		line1 = (ImageView) mainView.findViewById(R.id.top_title1_img);
		line2 = (ImageView) mainView.findViewById(R.id.top_title2_img);
		// line3 = (ImageView) mainView.findViewById(R.id.top_title3_img);
		// line4 = (ImageView) mainView.findViewById(R.id.top_title4_img);

		title1.setTextColor(getResources().getColor(R.color.red));
		line1.setVisibility(View.VISIBLE);

		initInfo(mainView);
	}

	public void initInfo(View mainView) {

		mTzAdapter = new tzAdapter(getActivity());
		mListView.setAdapter(mTzAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSelectIndex = position - 1;
				tzItem item = ListInfo.get(mSelectIndex);
				if (swith_flag == 3) {
					// 显示操作
					if (Default.userId == 0) {
						getActivity().startActivity(new Intent(getActivity(), loginActivity.class));

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
						builder.put("type", getType());
						builder.put("page", curPage);
						builder.put("limit", pageCount);
						doHttp(builder);
					} else {
						handler.sendEmptyMessage(1);
					}
				}

			}

		});
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
			tzItem item = ListInfo.get(position);

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
				ImageView normal_kind_flag = (ImageView) convertView.findViewById(R.id.normal_kind_flag);
				normal_kind_flag.setVisibility(View.VISIBLE);
				normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_101);
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

						zq_has_done.setVisibility(View.GONE);
					} else {
						zq_has_done.setVisibility(View.VISIBLE);
					}

				}
				convertView.setTag("2");
				return convertView;

			} else {

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.adapter_item_tz2, null);
				} else {

					String tagFlag = (String) convertView.getTag();
					if (tagFlag.equals("2")) {
						convertView = mInflater.inflate(R.layout.adapter_item_tz2, null);
					}
				}

				{

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
					case 201:
						normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_201);
						break;
					case 301:
						normal_kind_flag.setBackgroundResource(R.drawable.b_tz_type_301);
						break;

					default:
						break;
					}

					TextView tz_title = (TextView) convertView.findViewById(R.id.tz_info);
					TextView tz_money = (TextView) convertView.findViewById(R.id.tz_num1);
					TextView tz_lv = (TextView) convertView.findViewById(R.id.tz_num2);
					TextView tz_time = (TextView) convertView.findViewById(R.id.tz_num4);
					//by  adapter_item_tz2
					TextView tz_jd = (TextView) convertView.findViewById(R.id.tz_num3);
					

//					RoundProgressBar tz_pro = (RoundProgressBar) convertView.findViewById(R.id.progress);
					
					//by  adapter_item_tz2
					ProgressBar tz_pro = (ProgressBar) convertView.findViewById(R.id.progress);
					
					
					TextView post_time = (TextView) convertView.findViewById(R.id.tz_date);

					tz_title.setText("" + item.getName());
					post_time.setText("" + item.getTime());
					tz_money.setText(SystenmApi.getMoneyInfo(item.getMoney()) + "");
					tz_lv.setText(item.getNhll() + "");
					tz_time.setText(item.getJkqx());
					tz_pro.setProgress((int) item.getProgress());
					//by  adapter_item_tz2
					tz_jd.setText((int) item.getProgress()+"%");
					
					

				}
				convertView.setTag("1");
				return convertView;
			}

		}
	}
//进入详情页
	public void showItem(tzItem item) {

		long id = item.getId();
		final int type = item.getType();
		// Log.e("123", "获取的Type值=====Type==" + type);
		Intent intent = new Intent(getActivity(), type == 201 || type == 301 ? itemInfo3_1Activity.class : itemInfo2_1Activity.class);
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		// intent.pute

		// startActivityForResult(intent, 123);
		startActivity(intent);

		getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.to_left);
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

				mTzAdapter.notifyDataSetChanged();

				mListView.onRefreshComplete();
				mTzAdapter.notifyDataSetChanged();
				dismissLoadingDialog();
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

				ListInfo.addAll(temp_list);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTzAdapter.notifyDataSetChanged();

		mListView.onRefreshComplete();
		mTzAdapter.notifyDataSetChanged();
	}

	public void doHttp() {
		// RequestParams params = new RequestParams();
		JsonBuilder builder = new JsonBuilder();
		curPage = 1;
		builder.put("type", getType());
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
							initData(json);
						} else {
							showCustomToast(json.getString("message"));
							ListInfo.clear();
						}

						dismissLoadingDialog();
						mTzAdapter.notifyDataSetChanged();
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

				mTzAdapter.notifyDataSetChanged();
				mListView.onRefreshComplete();
				dismissLoadingDialog();
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
			}
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		doHttp();
	}

	// public void loadImage() {
	// int start = mListView.getFirstVisiblePosition();
	// int end = mListView.getLastVisiblePosition();
	// if (end >= ListInfo.size()) {
	// end = ListInfo.size() - 1;
	// }
	// // MainActivity.mMainActivity.getImageLodader().setLoadLimit(start,
	// // end);
	// // MainActivity.mMainActivity.getImageLodader().unlock();
	// }

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

	// 处理ListView加载性能处理

	private int beginIndex, endIndex;

	// public void moveChose(int index) {
	// endIndex = beginIndex;
	// beginIndex = index;
	//
	// float cw = mChose.getMeasuredWidth();
	// int width = getActivity().getWindowManager().getDefaultDisplay()
	// .getWidth();
	// int off = width / maxChose;
	// float hh = (off - cw) / 2;
	// TranslateAnimation animation = new TranslateAnimation(endIndex * off
	// + hh, beginIndex * off + hh, 0, 0);
	// animation.setDuration(100);
	// animation.setFillAfter(true);
	// animation.setFillBefore(true);
	// mChose.startAnimation(animation);
	//
	// }

	private void changeBackground() {

		title1.setTextColor(getActivity().getResources().getColor(R.color.black));
		title2.setTextColor(getActivity().getResources().getColor(R.color.black));
		// title3.setTextColor(getActivity().getResources()
		// .getColor(R.color.black));
		// title4.setTextColor(getActivity().getResources()
		// .getColor(R.color.black));

		line1.setVisibility(View.GONE);
		line2.setVisibility(View.GONE);
		// line3.setVisibility(View.GONE);
		// line4.setVisibility(View.GONE);

	}

	public void initData(JSONObject json) {
		ListInfo.clear();

		try {
			// 散标数据初始化

			maxPage = json.getInt("totalPage");
			curPage = json.getInt("nowPage");
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
				ListInfo.clear();
				ListInfo.addAll(temp_list);
				// 将数据全部填充到 ListInfo中
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTzAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal_btn:
			changeFlag(0);
			break;
		case R.id.normal2_btn:
			changeFlag(1);
			break;
		case R.id.normal3_btn:
			changeFlag(2);
			break;
		case R.id.normal4_btn:
			changeFlag(3);
			break;

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
		case 3:// 债权
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
		BaseHttpClient.post(getActivity(), Default.zq_request, builder, new JsonHttpResponseHandler() {

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
						dismissLoadingDialog();
						if (!response.getBoolean("has_pin")) {
							showCustomToast("请先设置支付密码！");
						} else {
							showZQDialog(response);
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

				mListView.onRefreshComplete();
				mTzAdapter.notifyDataSetChanged();
				dismissLoadingDialog();
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

			dialog = new ZqzrDialog(getActivity(), price, dq_money, account_money, password);
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

		BaseHttpClient.post(getActivity(), Default.zq_buy_request, builder, new JsonHttpResponseHandler() {

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
							{
								showCustomToast(response.getString("message"));
							}
						} else {
							showCustomToast(response.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
					doHttp();
					dialog.dismiss();
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

				mTzAdapter.notifyDataSetChanged();

				mListView.onRefreshComplete();
				mTzAdapter.notifyDataSetChanged();
				dismissLoadingDialog();
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
				showCustomToast("转账成功");
				swith_flag = 3;
				doHttp();
			} else {
				if (data.getStringExtra("message") != null) {
					showCustomToast(data.getStringExtra("message"));
				}
			}
		} else {
			showCustomToast(data.getStringExtra("message"));
		}
	}

	public void changeFlag(int type) {
		swith_flag = type;
		changeBackground();
		switch (type) {
		case 0:
			title1.setTextColor(getActivity().getResources().getColor(R.color.red));
			line1.setVisibility(View.VISIBLE);
			break;
		case 1:
			title2.setTextColor(getActivity().getResources().getColor(R.color.red));
			line2.setVisibility(View.VISIBLE);
			break;
		case 2:
			// title3.setTextColor(getActivity().getResources().getColor(
			// R.color.red));
			// line3.setVisibility(View.VISIBLE);
			// break;
		case 3:
			// title4.setTextColor(getActivity().getResources().getColor(
			// R.color.red));
			// line4.setVisibility(View.VISIBLE);
			// break;
		}
		doHttp();

		mListView.requestFocusFromTouch();
		// mListView.setSelection(0);
	}

}
