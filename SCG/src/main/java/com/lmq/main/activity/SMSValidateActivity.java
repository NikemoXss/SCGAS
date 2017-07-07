package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

import android.os.Bundle;
import android.util.Log;

public class SMSValidateActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
//验证短信是否验证
	private void doHttpyzmyz() {
		JsonBuilder builder = new JsonBuilder();
		//
		builder.put("code", "");
		// 填充参数
		BaseHttpClient.post(SMSValidateActivity.this, Default.getyzmyz, builder, new JsonHttpResponseHandler() {

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
						showCustomToast(json.toString());
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
	//发送短信验证
	private void doHttpyzm() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("phone", "");
		// 填充参数
		BaseHttpClient.post(SMSValidateActivity.this, Default.getyzm, builder, new JsonHttpResponseHandler() {

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
}
