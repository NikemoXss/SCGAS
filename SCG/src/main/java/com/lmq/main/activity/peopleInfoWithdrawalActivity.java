/**
 *
 */
package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

/**
 * @author zzx 个人中心-提现
 */
public class peopleInfoWithdrawalActivity extends BaseActivity implements android.view.View.OnClickListener {

	Button mEnter_money;
	private String mName;// 银行名
	private String mNum;// 卡号

	private String mZsxm;// 真实姓名

	private String mSjhm;// 手机号
	private String mTkqx;// 到账日期

	private String mKtje;// 账户余额

	// private TextView info[];
	private TextView tv_allmoney;
	private TextView bank_name;
	private TextView bank_num;
	private TextView tv_name;
	private TextView tv_time;

	private EditText mEdit_money;
	private EditText mEdit_pass;
	private String mMoney, mPassword;

	private String messageInfo;

	private peopleInfoHandler handler;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_withdrawal);

		handler = new peopleInfoHandler();

		findViewById(R.id.back).setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.gerenzhongxin_tixian);

		mEnter_money = (Button) findViewById(R.id.btn_enter_money);
		mEnter_money.setOnClickListener(this);

		// updateInfo(Data.peopleWithdrawalJson);

		// info = new TextView[6];
		// info[0] = (TextView) findViewById(R.id.bank_name);
		// info[1] = (TextView) findViewById(R.id.bank_num);
		// info[2] = (TextView) findViewById(R.id.tv_name);
		// info[4] = (TextView) findViewById(R.id.tv_time);
		// info[5] = (TextView) findViewById(R.id.tv_allmoney);

		tv_allmoney = (TextView) findViewById(R.id.tv_allmoney);
		bank_name = (TextView) findViewById(R.id.bank_name);
		bank_num = (TextView) findViewById(R.id.bank_num);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_time = (TextView) findViewById(R.id.tv_time);

		intent = getIntent();
		if (intent.getStringExtra("bank_num") != null) {

			mNum = intent.getStringExtra("bank_num");

		}

		if (intent.getStringExtra("bank_name") != null) {

			mName = intent.getStringExtra("bank_name");

		}

		if (intent.getStringExtra("all_money").equals("null")) {
			tv_allmoney.setText("0元");
			MyLog.e("提现获取传过来的钱数0元", "tv_allmoney");
		} else {
			mKtje = intent.getStringExtra("all_money") + "元";
			tv_allmoney.setText(mKtje);
		}

		// if(intent.getStringExtra("all_money") != null){
		//
		// mKtje =intent.getStringExtra("all_money")+"元";
		// tv_allmoney.setText(mKtje);
		// MyLog.e("提现获取传过来的钱数", intent.getStringExtra("all_money")+"元");
		// }

		if (intent.getStringExtra("qixian") != null) {

			mTkqx = intent.getStringExtra("qixian");
		}

		if (intent.getStringExtra("real_name") != null) {

			mZsxm = intent.getStringExtra("real_name");

		}

		bank_name.setText(mName);
		bank_num.setText("尾号为" + mNum);

		tv_name.setText(mZsxm);
		// info[3].setText(mSjhm);
		tv_time.setText(mTkqx);

		mEdit_money = (EditText) findViewById(R.id.edit_money);
		mEdit_pass = (EditText) findViewById(R.id.edit_pass);

	}

	public void getEditInfo() {

	}

	/*
	 * public void updateInfo(JSONObject json) { try { mName =
	 * json.getString("bank_name"); mNum = json.getString("bank_num");
	 * mZsxm = json.getString("real_name");
	 * mTkqx = json.getString("qixian");
	 * //mKtje = json.getDouble("all_money");// 可提现金额
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_enter_money:
			// getEditInfo();
			mMoney = mEdit_money.getText().toString();
			mPassword = mEdit_pass.getText().toString();

			if (mMoney.equals("")) {
				showCustomToast(R.string.toast10);
				return;
			} else if (mPassword.equals("")) {
				showCustomToast(R.string.toast11);
				return;
			}
			showDialog();// 我要提现—提交前确认

			break;
		case R.id.back:
			finish();
			break;
		}
	}

	public void clearEnterInfo() {
		mEdit_money.setText("");
		mEdit_pass.setText("");
		// info[5].setText("元");

	}

	// 我要提现—提交前确认
	public void showDialog() {

		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("amount", mMoney);
		jsonBuilder.put("pwd", mPassword);

		BaseHttpClient.post(getBaseContext(), Default.peoInfoWithdrawal_2, jsonBuilder, new JsonHttpResponseHandler() {

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

							MyLog.e(json.toString());
							messageInfo = json.getString("message");

							Message message = new Message();
							// 请求服务器体现
							message.arg1 = 1;
							handler.sendMessage(message);
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

	// 我要提现—最后提交
	public void dohttp() {
		// showPeopleInfoWithdrawalActivityDialog(messageInfo);
		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("amount", mMoney);
		jsonBuilder.put("pwd", mPassword);

		BaseHttpClient.post(getBaseContext(), Default.peoInfoWithdrawal_3, jsonBuilder, new JsonHttpResponseHandler() {

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
						MyLog.e(json.toString());
						if (json.getInt("status") == 1) {

							messageInfo = json.getString("message");
							Message message = new Message();
							message.arg1 = 3;
							Bundle bundel = new Bundle();
							bundel.putString("info", json.getString("message"));
							message.setData(bundel);
							handler.sendMessage(message);
							// clearEnterInfo();
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

	public void showPeopleInfoWithdrawalActivityDialog(String messageInfo) {

		AlertDialog.Builder builder = new Builder(peopleInfoWithdrawalActivity.this);
		builder.setTitle("友情提示");
		builder.setMessage(messageInfo);

		builder.setNegativeButton("取消提现", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setPositiveButton("确认提现", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				Message message = new Message();
				message.arg1 = 2;
				handler.sendMessage(message);
			}
		});
		builder.create().show();

	}

	class peopleInfoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			// 我要提现—提交前确认
			case 1:
				showPeopleInfoWithdrawalActivityDialog(messageInfo);
				break;
			// 我要提现—最后提交
			case 2:
				dohttp();
				break;
			case 3:
				clearEnterInfo();
				getTiXian();
			case 4:
				showCustomToast(msg.getData().getString("info"));

				break;
			}
		}
	}

	// 提现协议
	public void getTiXian() {

		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("uid", Default.userId);

		BaseHttpClient.post(getBaseContext(), Default.peoInfoWithdrawal, jsonBuilder, new JsonHttpResponseHandler() {

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
							MyLog.e(json.toString());
							MyLog.e("获取提现的信息", json.toString());
							mKtje = json.getString("all_money") + "元";
							tv_allmoney.setText(mKtje);

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
}
