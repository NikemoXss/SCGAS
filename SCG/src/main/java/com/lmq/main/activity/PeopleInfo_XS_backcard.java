package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

/**
 * 显示绑定银行卡信息
 */

public class PeopleInfo_XS_backcard extends BaseActivity implements OnClickListener {

	private TextView info[];
	private String mbank;
	private String mdebit_id;
	private String maccount_province;
	private String maccount_city;
	private String maccount_branch;
	private Button btn_yhbind;
	private SharedPreferences sp;
	private Boolean flag;
	private String bank_num = null;
	private Intent intent;

	private boolean mCanmodify = true;
	TextView title;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_yhk);

		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);

		title = (TextView) findViewById(R.id.title);
		title.setText("银行卡认证");

		btn_yhbind = (Button) findViewById(R.id.btn_yhbind);

		findViewById(R.id.btn_yhbind).setOnClickListener(this);

		info = new TextView[5];
		info[0] = (TextView) findViewById(R.id.tv_yhang);
		info[1] = (TextView) findViewById(R.id.tv_yhzh);
		info[2] = (TextView) findViewById(R.id.tv_szsf);
		info[3] = (TextView) findViewById(R.id.tv_szcity);
		info[4] = (TextView) findViewById(R.id.tv_szzh);

		intent = getIntent();
		flag = intent.getIntExtra("card_status", 0) == 1;
		// btn_yhbind.setText(flag ? "修改银行卡" : "进行绑定");
		if (flag) {
			btn_yhbind.setVisibility(View.GONE);
		}
		doHttp2();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yhbind: {
			if (flag && !mCanmodify) {
				showCustomToast("不允许修改银行卡信息！");
				return;
			}
		}
			Intent intent = new Intent(new Intent(PeopleInfo_XS_backcard.this, PeopleInfo_backcard.class));
			intent.putExtra("up", flag);
			startActivity(intent);
			finish();
			break;
		case R.id.title_right:
			finish();
			break;
		}

	}

	public void updateInfo(JSONObject json) {

		try {
			if (json == null) {
				return;
			}
			if (json.getInt("status") == 1) {

				// 银行
				mbank = json.getString("bank");
				// 卡号
				mdebit_id = json.getString("debit_id");
				// 开户省
				maccount_province = json.getString("account_province");
				// 开户市
				maccount_city = json.getString("account_city");
				// 开户支行
				maccount_branch = json.getString("account_branch");

				if (json.optInt("can_modify") == 0) {
					mCanmodify = false;
				} else {
					mCanmodify = true;
				}

				info[0].setText(mbank);
				info[1].setText(mdebit_id);
				info[2].setText(maccount_province);
				info[3].setText(maccount_city);
				info[4].setText(maccount_branch);

			} else {
				showCustomToast(json.getString("message"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void doHttp2() {

		BaseHttpClient.post(getBaseContext(), Default.peoInfoxsbankcard, null, new JsonHttpResponseHandler() {

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

				if (statusCode == 200) {
					try {

						if (json != null) {
							if (json.getInt("status") == 1) {

								updateInfo(json);
							} else {
								showCustomToast(json.getString("message"));
							}
						} else {
							MyLog.e("获取用户绑定信息.......", "Null   null  null");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					showCustomToast(R.string.toast1);
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

		doHttp2();
	}

}
