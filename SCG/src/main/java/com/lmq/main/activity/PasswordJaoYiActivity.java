package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

/**
 * 修改交易密码
 */
public class PasswordJaoYiActivity extends BaseActivity implements OnClickListener {
	private EditText mOldd, mPassww, mPassww2;
	private String info[];
	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_update_jiaoyipsw);
		initView();
	}

	public void initView() {
		iv_back = (ImageView) findViewById(R.id.title_right);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		// findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_jiayoyi).setOnClickListener(this);
		findViewById(R.id.getPw).setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.updatezfpsd_title);

		mOldd = (EditText) findViewById(R.id.ed_itold);
		mPassww = (EditText) findViewById(R.id.ed_itpassw);
		mPassww2 = (EditText) findViewById(R.id.ed_itpassw2);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_jiayoyi:
			info = getInfo();
			if (info == null) {
				return;
			}
			doHttp();
			break;
		case R.id.title_right:
			finish();
			break;
		case R.id.getPw:
			Intent intent = new Intent(PasswordJaoYiActivity.this, ForgotPwdActivity.class);
			intent.putExtra("iszfpwd", "0");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	public String[] getInfo() {
		String old = mOldd.getText().toString();
		String passw = mPassww.getText().toString();
		String passw2 = mPassww2.getText().toString();

		if (old.equals("")) {
			showCustomToast(R.string.toast7);
			return null;
		}
		if (passw.equals("")) {
			showCustomToast(R.string.toast8);
			return null;
		}
		if (passw.length() < 6) {
			showCustomToast(R.string.toast8_2);
			return null;
		}

		if (passw.length() > 16) {
			showCustomToast(R.string.toast8_3);
			return null;
		}
		if (passw2.equals("")) {
			showCustomToast(R.string.toast8_1);
			return null;
		}
		if (passw2.equals(passw) == false) {
			showCustomToast(R.string.toast9);
			return null;
		}

		return new String[] { old, passw, passw2 };
	}

	public void doHttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("oldpwd", info[0]);
		builder.put("newpwd", info[1]);

		BaseHttpClient.post(getBaseContext(), Default.peoInfoxsjiaoyipsw, builder, new JsonHttpResponseHandler() {

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
							finish();
							showCustomToast("修改支付密码成功！");
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

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			showCustomToast(msg.getData().getString("info"));
		}

	};

}
