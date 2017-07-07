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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddNewPwd extends BaseActivity implements OnClickListener {
	Button conmit_bt;
	EditText newpwd_et;
	TextView title;
	ImageView back_iv;
	String uid, iszf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_addnewpwd);
		Intent intent = getIntent();
		conmit_bt = (Button) findViewById(R.id.subpwd_sx);
		newpwd_et = (EditText) findViewById(R.id.newpwd_sx);
		title = (TextView) findViewById(R.id.title);
		iszf = intent.getStringExtra("iszf");
		title.setText("添加新登录密码");
		if (iszf.equals("0")) {
			title.setText("添加新支付密码");
		}
		back_iv = (ImageView) findViewById(R.id.title_right);
		back_iv.setVisibility(View.VISIBLE);
		conmit_bt.setOnClickListener(this);
		back_iv.setOnClickListener(this);

		uid = intent.getStringExtra("uid");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.subpwd_sx:
			if (newpwd_et.getText().toString() == null || newpwd_et.getText().toString().equals("")) {
				showCustomToast("请输入新的密码");
			} else {
				doHttaddnewpwd();
			}
			break;

		case R.id.title_right:
			finish();
			break;

		default:
			break;
		}
	}

	private void doHttaddnewpwd() {
		JsonBuilder builder = new JsonBuilder();
		String url = Default.addloginnewpwdsx;
		if (iszf.equals("0")) {
			url = Default.addzhifunewpwdsx;
			builder.put("uid", Default.userId);
		} else {
			builder.put("uid", uid);
		}
		builder.put("password", newpwd_et.getText().toString());

		// 填充参数
		BaseHttpClient.post(AddNewPwd.this, url, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				Log.e("pinpassword", json.toString());
				try {
					// showCustomToast(json.toString()+Default.userId);
					String status = json.getString("status");
					if (status.equals("1")) {
						// if(iszf.equals("0")){
						// showCustomToast(json.getString("message"));
						// }
						showCustomToast(json.getString("message"));
						// showCustomToast("修改成功,请重新登录");
						ForgotPwdActivity.instance.finish();
						finish();
					} else {
						showCustomToast("修改失败");
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
