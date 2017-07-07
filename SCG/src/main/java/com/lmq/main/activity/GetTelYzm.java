package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GetTelYzm extends BaseActivity implements OnClickListener {

	Button conmit;
	TextView tel_tv, getyzm_tv, title;
	EditText yzm_et;
	ImageView back_iv;
	public static GetTelYzm instance = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_gettelyzm);
		instance = this;
		initView();
	}

	private void initView() {
		SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		String tel = preferences.getString("tel", "defaultname");
		tel_tv = (TextView) findViewById(R.id.tel);
		tel_tv.setText(tel);
		title = (TextView) findViewById(R.id.title);
		title.setText("找回支付密码");
		getyzm_tv = (TextView) findViewById(R.id.getyzm_tv);
		conmit = (Button) findViewById(R.id.next);
		yzm_et = (EditText) findViewById(R.id.yzm_et);
		back_iv = (ImageView) findViewById(R.id.back);
		back_iv.setOnClickListener(this);
		getyzm_tv.setOnClickListener(this);
		conmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next:// 提交下一步
			if (yzm_et.getText().toString() == null || yzm_et.getText().toString().equals("")) {
				showCustomToast("请输入验证码");
			} else {
				 doHttpyzmyz();
//				Intent intent = new Intent(GetTelYzm.this, AddNewPwd.class);
//				startActivity(intent);
			}

			break;

		case R.id.getyzm_tv:// 获取验证码
			doHttpyzm();
			break;
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	private void doHttpyzm() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("phone", tel_tv.getText().toString());
		// 填充参数
		BaseHttpClient.post(GetTelYzm.this, Default.getyzm, builder, new JsonHttpResponseHandler() {

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

		builder.put("phone", tel_tv.getText().toString());
		builder.put("verify_code", yzm_et.getText().toString());
		// 填充参数
		BaseHttpClient.post(GetTelYzm.this, Default.yzmyz, builder, new JsonHttpResponseHandler() {

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
						Intent intent = new Intent(GetTelYzm.this, AddNewPwd.class);
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

}
