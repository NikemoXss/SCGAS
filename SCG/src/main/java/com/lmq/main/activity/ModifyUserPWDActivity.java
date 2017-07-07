package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

public class ModifyUserPWDActivity extends BaseActivity implements OnClickListener {

	/**
	 * 用户设置新密码
	 */
	private EditText userNewPwd;
	/**
	 * 用户重复新密码
	 */
	private EditText userNewPwdRep;
	private String username = "";;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		username = getIntent().getStringExtra("username");
		setContentView(R.layout.modiy_user_pwd);
		TextView titTextView = (TextView) findViewById(R.id.title);
		titTextView.setText("重置密码");
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.modify_pwd).setOnClickListener(this);
		userNewPwd = (EditText) findViewById(R.id.user_new_pwd);
		userNewPwdRep = (EditText) findViewById(R.id.user_rep_pwd);

	}

	/**
	 * 修改密码第一步
	 */
	private void dohttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("password", userNewPwd.getText().toString());
		builder.put("username", username);
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
						MyLog.e("找回密码---1", response.toString());

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.modify_pwd:
			String userName = userNewPwd.getText().toString();
			if (userName == null && userName.equals("")) {
				showCustomToast("请输入密码");
				return;
			}
			String newpwd = userNewPwdRep.getText().toString();
			if (newpwd == null && newpwd.equals("")) {
				showCustomToast("请输入密码");
				return;
			}

			if (!userName.endsWith(newpwd)) {
				showCustomToast("两次密码输入不一致，请重新输入");
				return;
			}
			dohttp();
			break;

		default:
			break;
		}

	}
}
