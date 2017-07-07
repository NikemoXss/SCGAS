package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

public class ForgotPwdActivity extends BaseActivity implements OnClickListener {

	/**
	 * 用户手机号码
	 */
	private EditText phoneEditor;
	private EditText usernameEditor, user_phone_sx, ver_pwd_sx;

	/**
	 * 用户手机号码
	 */
	private String phoneNum;
	private String username;
	TextView title;
	ImageView iv;
	String uid;
	String iszfpwd;
	public static ForgotPwdActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pwd);
		instance = this;
		title = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		iszfpwd = intent.getStringExtra("iszfpwd");
//		showCustomToast(iszfpwd);
		title.setText("忘记登录密码");
		if(iszfpwd.equals("0")){
			title.setText("忘记支付密码");
		}
		// findViewById(R.id.forget_pwd).setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		// phoneEditor = (EditText) findViewById(R.id.user_phone);
		// usernameEditor = (EditText) findViewById(R.id.user_name);
		findViewById(R.id.edittext_sx).setOnClickListener(this);
		user_phone_sx = (EditText) findViewById(R.id.user_phone_sx);
		findViewById(R.id.forget_pwd_sx).setOnClickListener(this);
		ver_pwd_sx = (EditText) findViewById(R.id.ver_pwd_sx);
	}

	/**
	 * 修改密码第一步
	 */
	private void dohttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("user_name", username);
		builder.put("user_phone", phoneNum);
		BaseHttpClient.post(getBaseContext(), Default.FORGOT_PWD_0, builder, new JsonHttpResponseHandler() {

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
									Intent intent = new Intent(ForgotPwdActivity.this, ForgotPwd2Activity.class);
									intent.putExtra("phone", phoneNum);
									startActivity(intent);
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
				// stop();
				// getPhoneFlagBtn.setEnabled(true);
				// getPhoneFlagBtn.setText("获取验证码");

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		case R.id.forget_pwd_sx:// 校验验证码
			if (user_phone_sx.equals("")) {
				showCustomToast("请输入您的手机号！");
				return;
			}
			if (ver_pwd_sx.equals("")) {
				showCustomToast("请输入验证码！");
				return;
			}
			doHttpyzmyz();
//			Intent intent = new Intent(ForgotPwdActivity.this, AddNewPwd.class);
//			intent.putExtra("uid", 349);
//			startActivity(intent);
			break;
		case R.id.edittext_sx:// 获取验证码
			doHttpyzm();
			break;
		default:
			break;
		}

	}

	private void doHttpyzm() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("phone", user_phone_sx.getText().toString());
		// 填充参数
		BaseHttpClient.post(ForgotPwdActivity.this, Default.getyzm, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				Log.e("yzma", json.toString());
				try {
					String status = json.getString("status");
					if (status.equals("1")) {
						uid = json.getString("uid");
						showCustomToast("验证码发送成功");
					} else {
						showCustomToast("验证码发送失败");
					}
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

	private void doHttpyzmyz() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("code", ver_pwd_sx.getText().toString());
		// 填充参数
		BaseHttpClient.post(ForgotPwdActivity.this, Default.yzmyzsx, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				Log.e("yzmyz", json.toString());
				try {
					String status = json.getString("status");
					if (status.equals("1")) {
//						showCustomToast(json.getString("message"));
						Intent intent = new Intent(ForgotPwdActivity.this, AddNewPwd.class);
						intent.putExtra("uid", uid);
						intent.putExtra("iszf", iszfpwd);
						startActivity(intent);
					} else {
						showCustomToast("验证码验证错误");
					}
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

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
