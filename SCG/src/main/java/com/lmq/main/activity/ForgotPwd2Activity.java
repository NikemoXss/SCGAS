package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

public class ForgotPwd2Activity extends BaseActivity implements OnClickListener {

	/**
	 * 用户手机验证码
	 */
	private EditText userYzm;
	private String strYzm;
	/**
	 * 验证码
	 */
	private EditText userPwd, userPwd2;
	private String strPwd;

	/**
	 * 发送验证码
	 */
	private TextView mText;
	private String phoneNum;

	private Handler mhandle = new Handler();
	private Runnable runnbale = new Runnable() {

		@Override
		public void run() {
			changeSendMessageBtn();
		}
	};

	private int timeMax = 60;
	private int time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pwd2);
		TextView titTextView = (TextView) findViewById(R.id.title);
		titTextView.setText("找回密码");
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.forget_pwd).setOnClickListener(this);

		userYzm = (EditText) findViewById(R.id.user_yzm);
		userPwd = (EditText) findViewById(R.id.user_pwd);
		userPwd2 = (EditText) findViewById(R.id.user_pwd2);

		mText = (TextView) findViewById(R.id.send);
		mText.setOnClickListener(this);
		mText.setEnabled(false);

		Intent intent = getIntent();
		phoneNum = intent.getExtras().getString("phone");
		TextView text = (TextView) findViewById(R.id.phone);
		text.setText(phoneNum);

		mhandle.postDelayed(runnbale, 1000);
	}

	/**
	 * 修改密码第一步
	 */
	private void dohttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("phone", phoneNum);
		BaseHttpClient.post(getBaseContext(), Default.FORGOT_PWD_1, builder, new JsonHttpResponseHandler() {

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
						if (response != null) {
							if (response.has("status")) {
								if (response.getInt("status") == 1) {
									showCustomToast(response.getString("message"));

									mhandle.postDelayed(runnbale, 1000);
								} else {
									showCustomToast(response.getString("message"));
								}
							}
						} else {

							showCustomToast(R.string.toast1);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
			}
		});

	}

	/**
	 * 修改密码第二步
	 */
	private void dohttp2() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("code", strYzm);
		builder.put("password", strPwd);
		BaseHttpClient.post(getBaseContext(), Default.FORGOT_PWD_3, builder, new JsonHttpResponseHandler() {

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
						if (response != null) {
							if (response.has("status")) {
								if (response.getInt("status") == 1) {
									showCustomToast(response.getString("message"));
									finish();
								} else {
									showCustomToast(response.getString("message"));
								}
							}
						} else {

							showCustomToast(R.string.toast1);
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
			}
		});

	}

	private void changeSendMessageBtn() {
		time = time + 1000;

		if (time < 1000 * timeMax) {
			mText.setText((1000 * timeMax - time) / 1000 + "秒后重试");
			mhandle.postDelayed(runnbale, 1000);
		} else {
			time = 0;
			mText.setText("获取验证码");
			mText.setEnabled(true);
			stop();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.send:

			dohttp();
			mText.setEnabled(false);
			break;

		case R.id.forget_pwd:
			strYzm = userYzm.getText().toString();
			strPwd = userPwd.getText().toString();
			String pwd2 = userPwd2.getText().toString();
			if (strYzm.equals("")) {
				showCustomToast("请输入短信验证码！");
				return;
			}

			if (strPwd.equals("")) {
				showCustomToast("请输入新登录密码！");
				return;
			}
			if (!strPwd.equals(pwd2)) {
				showCustomToast("两次输入密码不一样，请重新输入！");
				return;
			}

			dohttp2();

			break;

		default:
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		stop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stop();
	}

	public void stop() {
		mhandle.removeCallbacks(runnbale);
	}
}
