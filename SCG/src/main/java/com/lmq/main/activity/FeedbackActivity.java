package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
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
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

public class FeedbackActivity extends BaseActivity implements OnClickListener {

	private EditText feedBackEditText = null;
	private String mMessage;
	TextView title;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		findViewById(R.id.title_right).setOnClickListener(this);
		feedBackEditText = (EditText) findViewById(R.id.feedback_content);
		findViewById(R.id.submit_btn).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("意见反馈");
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;

		case R.id.submit_btn:
			String feedBackStr = feedBackEditText.getText().toString();
			MyLog.e("意见反馈信息--->", feedBackStr);
			if (null == feedBackStr) {
				showCustomToast(R.string.feedback_tip_null);
			} else if (feedBackStr.length() == 0 || feedBackStr.equals("")) {
				showCustomToast(R.string.feedback_tip_null);
			} else {
				doHttp();
			}
			break;
		default:
			break;
		}
	}

	// 提交用户反馈到服务器
	public void doHttp() {

		// 构造请求参数
		JsonBuilder jsonBuilder = new JsonBuilder();

		jsonBuilder.put("message", feedBackEditText.getText().toString());

		MyLog.e("手机标识----->", Default.PHONE_MODEL + Default.OS_VERSION);
		// 获取当前手机系统信息
		jsonBuilder.put("system", Default.PHONE_MODEL + Default.OS_VERSION);
		BaseHttpClient.post(getBaseContext(), Default.FEEDBACK, jsonBuilder, new JsonHttpResponseHandler() {

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
					dismissLoadingDialog();
					if (statusCode == 200) {
						if (json.getInt("status") == 1) {

							// showCustomToast(R.string.feedback_tip_success);
							// mMessage = json.getString("message");
							// handler.sendEmptyMessage(1);
							showCustomToast(json.getString("message"));
						} else {
							showCustomToast(json.getString("message"));
						}
					} else {
						showCustomToast(R.string.toast1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
