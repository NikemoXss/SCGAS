package com.lmq.main.activity;

import java.text.DecimalFormat;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.item.tzItem;
import com.lmq.main.tools.LMQWebViewActivity;
import com.lmq.main.util.Data;
import com.lmq.main.util.Default;

@SuppressLint("NewApi")
public class itemInfo2_1Activity extends BaseActivity implements OnClickListener {

	private TextView qtje;

	private tzItem item;
	/** 项目名称 */
	private TextView titTextView;
	/** 借款用途 */
	private TextView borrowing_purposes;
	/** 发布日期 */
	private TextView dateTextView;
	private TextView lvTextView;
	/** 借款期限 */
	private TextView borrowTimeTextView2;
	/** 还款方式 */
	private TextView boorowTypeTextView;
	/** 起投金额 */
	private TextView lowerMoneyTextView;
	/** 融资金额 */
	private TextView allMoneyTextView;
	/** 投标奖励 */
	private TextView rewardTextView;
	/** 剩余金额 **/
	private TextView hasLendyMoneyTextView;
	private ProgressBar progress;
	private ImageView mImageViewType;
	private TextView progressText;

	/** 购买按钮 */
	private Button enterBtn;

	/** 投标记录的显示 */
	private TextView info1;
	/** 更多详情的显示 */
	private TextView info2;

	private long itemId;
	private int itemType;
	private boolean mShowDx;

	private String messageInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tz_item2_1_new);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("投标详情");
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.sq).setOnClickListener(this);
		// findViewById(R.id.enter).setOnClickListener(this);
		// findViewById(R.id.js).setOnClickListener(this);

		initViews();

		Intent intent = getIntent();
		itemId = intent.getExtras().getLong("id");
		itemType = intent.getExtras().getInt("type");

	}

	public void initViews() {

		titTextView = (TextView) findViewById(R.id.title_detail);
		dateTextView = (TextView) findViewById(R.id.post_date);
		lvTextView = (TextView) findViewById(R.id.nhlv);
		boorowTypeTextView = (TextView) findViewById(R.id.hkfs);
		lowerMoneyTextView = (TextView) findViewById(R.id.zxtbje3);
		allMoneyTextView = (TextView) findViewById(R.id.jkje);
		hasLendyMoneyTextView = (TextView) findViewById(R.id.syje);
		borrowTimeTextView2 = (TextView) findViewById(R.id.jkqx);
		// borrowing_purposes = (TextView) findViewById(R.id.borrowing_purposes);

		qtje = (TextView) findViewById(R.id.qtje);

		progress = (ProgressBar) findViewById(R.id.progressbar);
		// mImageViewType = (ImageView) findViewById(R.id.type);
		progressText = (TextView) findViewById(R.id.progresstext);
		// rewardTextView = (TextView) findViewById(R.id.reward);

		// 详情其他信息
		info1 = (TextView) findViewById(R.id.tv_numberpeople);
		info2 = (TextView) findViewById(R.id.xq_ms_str);

		// 购买标
		enterBtn = (Button) findViewById(R.id.enter);
		enterBtn.setOnClickListener(this);

		findViewById(R.id.shareBtn).setOnClickListener(this);

		findViewById(R.id.tender_people).setOnClickListener(this);
		// tender_miaoshu
		findViewById(R.id.tender_miaoshu).setOnClickListener(this);
		MyLog.e("123", "标类型" + itemType);

	}

	public void getPageInfoHttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("id", itemId);
		builder.put("type", itemType);

		BaseHttpClient.post(getBaseContext(), Default.tzListItem, builder, new JsonHttpResponseHandler() {

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
							MyLog.e("123", "" + json.toString());
							Data.tzListItemJson = json;

							Message msg = new Message();
							msg.arg1 = 3;
							handler.sendMessage(msg);
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

	@Override
	protected void onResume() {
		super.onResume();
		getPageInfoHttp();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 2) {
				showCustomToast(msg.getData().getString("info"));
			} else if (msg.arg1 == 3) {
				updateInfo(Data.tzListItemJson);
			}
		}

	};

	public void updateInfo(JSONObject json) {
		item = new tzItem();
		item.init(json);

		DecimalFormat d = new DecimalFormat("##0.00");

		titTextView.setText(item.getName());
		dateTextView.setText(item.getTime());
		lvTextView.setText(d.format(item.getNhll()) + "");
		borrowTimeTextView2.setText(item.getJkqx() + "");
		allMoneyTextView.setText(SystenmApi.getMoneyInfo(item.getMoney()) + "元");
		boorowTypeTextView.setText(item.getJkfs() + "");
		lowerMoneyTextView.setText(d.format(item.getZxtbje()));
		// borrowing_purposes.setText(item.getBorrow_use());
		hasLendyMoneyTextView.setText(SystenmApi.getMoneyInfo(item.getHxje()));
		// hasLendyMoneyTextView
		// .setText(SystenmApi.getMoneyInfo((item.getMoney() - item
		// .getHxje())) + "");//已投金额
		progress.setProgress((int) item.getProgress());
		progressText.setText((int) item.getProgress() + "");

		qtje.setText(item.getZxtbje() + "元");

		info2.setText(item.getBorrowInfo());
		info1.setText(item.getTzcs() + "人");

		// 设置奖励
		// if (item.getJl().equals("0")) {
		// rewardTextView.setText("无");
		// } else {
		// rewardTextView.setText(item.getJl());
		// }

		// switch (item.getType()) {
		// case 3:
		// mImageViewType.setBackgroundResource(R.drawable.b_tz_type_3);
		// break;
		// case 4:
		// mImageViewType.setBackgroundResource(R.drawable.b_tz_type_4);
		// break;
		// case 5:
		// mImageViewType.setBackgroundResource(R.drawable.b_tz_type_5);
		// break;
		// case 6:
		// mImageViewType.setBackgroundResource(R.drawable.b_tz_type_6);
		// break;
		// case 7:
		// mImageViewType.setBackgroundResource(R.drawable.b_tz_type_7);
		// break;
		//
		// default:
		// break;
		// }
		// MyLog.e("borrow", "状态" + item.getBorrow_status());
		if (item.getBorrow_status() == 0) {
			enterBtn.setText("刚发布的标");

		} else if (item.getBorrow_status() == 1) {
			enterBtn.setText("初审未通过");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		} else if (item.getBorrow_status() == 2) {
			enterBtn.setText("立即投标");
		} else if (item.getBorrow_status() == 3) {
			enterBtn.setText("已流标");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		} else if (item.getBorrow_status() == 4) {
			enterBtn.setText("复审中");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		} else if (item.getBorrow_status() == 5) {
			enterBtn.setText("复审未通过");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		} else if (item.getBorrow_status() == 6) {
			enterBtn.setText("还款中");
			enterBtn.setBackgroundResource(R.drawable.lan);
		} else if (item.getBorrow_status() == 7) {
			enterBtn.setText("已完成");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		} else if (item.getBorrow_status() == 8) {
			enterBtn.setText("即将上线");
			enterBtn.setBackgroundResource(R.drawable.hui);
			enterBtn.setClickable(false);
		}

	}

	@Override
	public void finish() {

		super.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.tender_miaoshu:
			// if (item != null) {
			// Intent intent1 = new Intent();
			// intent1.setClass(itemInfo2_1Activity.this,
			// CustomDetailView.class);
			// intent1.putExtra("html_str", info2.getText().toString());
			// intent1.putStringArrayListExtra("imageArray",
			// item.getImageArray());
			// startActivity(intent1);
			// } else {
			// showCustomToast(R.string.toast1);
			//
			// }
			Intent abIntent = new Intent();
			abIntent.putExtra("title", "项目详情");
			abIntent.putExtra("url", Default.ip + "/des/des?id=" + itemId);
			abIntent.setClass(itemInfo2_1Activity.this, LMQWebViewActivity.class);
			startActivity(abIntent);

			break;
		case R.id.back:
			finish();
			break;
		case R.id.sq:

			if (item != null) {
				Intent info = new Intent(itemInfo2_1Activity.this, calculateActivity.class);
				info.putExtra("lilv", item.getNhll());
				info.putExtra("qixian", item.getJkqx());
				info.putExtra("fangshi", item.getJkfs());
				info.putExtra("jiangli", item.getJl());
				info.putExtra("guanli", "0");
				info.putExtra("zonge", item.getMoney());
				startActivity(info);
			} else {
				showCustomToast(R.string.toast1);
			}

			break;
		case R.id.enter:
			if (Default.userId == 0) {
				startActivity(new Intent(itemInfo2_1Activity.this, loginActivity.class));
			} else {

				if (item.getBorrow_uid() == Default.userId) {
					showCustomToast("不能投自己发布的标！");
				} else if (item.getProgress() == 100) {

					showCustomToast("交易已经结束,请选择其他标");
				} else if (item.getUid() == Default.userId) {
					showCustomToast("不能去投自己的标");
				} else {

					Intent buyInten = new Intent();
					buyInten.putExtra("id", itemId);
					buyInten.putExtra("type", itemType);
					buyInten.setClass(itemInfo2_1Activity.this, itemInfo2_2Activity.class);
					startActivity(buyInten);
				}
			}

			break;

		case R.id.tender_people:
			if (item != null) {
				Intent intent = new Intent(itemInfo2_1Activity.this, tzDetailsListActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("type", item.getType());
				startActivity(intent);
			} else {
				showCustomToast(R.string.toast1);
			}

			break;
		case R.id.shareBtn:
			v.setEnabled(false);
			SystenmApi.showShareView(this, "分享给朋友", "亲，给您推荐德成贷的一个投资项目," + item.getName() + "  收益高，风险低，快来了解一下吧！会理财，财汇来！！，" + Default.ip + "invest/" + itemId + ".html", Default.ip + "invest/" + itemId + ".html");
			v.setEnabled(true);
			break;

		default:
			break;
		}

	}

}
