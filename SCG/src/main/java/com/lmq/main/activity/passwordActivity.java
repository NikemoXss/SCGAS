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
import com.lmq.main.util.Default;

/**
 * 修改登录密码
 *
 * @author 孙建超
 */
public class passwordActivity extends BaseActivity implements OnClickListener {
	private EditText mOld, mPassw, mPassw2;
	private String info[];
	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_update_loginpsw);

		initView();
	}

	public void initView() {
		findViewById(R.id.enter).setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.title_right);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.peo_password_1);

		mOld = (EditText) findViewById(R.id.editold);
		mPassw = (EditText) findViewById(R.id.editpassw);
		mPassw2 = (EditText) findViewById(R.id.editpassw2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter:
			info = getInfo();
			if (info == null) {
				return;
			}
			doHttp();
			break;
		case R.id.title_right:
			finish();
			break;
		}

	}

	public String[] getInfo() {
		String old = mOld.getText().toString();
		String passw = mPassw.getText().toString();
		String passw2 = mPassw2.getText().toString();

		if (old.equals("")) {
			showCustomToast(R.string.toast7);
			return null;
		}
		if (passw.equals("")) {
			showCustomToast(R.string.toast8);
			return null;
		}
		if (passw.length() < 4) {
			showCustomToast(R.string.toast13);

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

	// public void doHttp()
	// {
	// showLoadingDialogNoCancle(getResources().getString(
	// R.string.toast2));
	//
	// JsonBuilder builder = new JsonBuilder();
	// builder.put("oldpwd", info[0]);
	// builder.put("newpwd", info[1]);
	//
	// new BaseModel(null, Default.changepass, builder)
	// .setConnectionResponseLinstener(new ConnectResponseListener()
	// {
	// public void onConnectResponseCallback(JSONObject json)
	// {
	// dismissLoadingDialog();
	// finish();
	// }
	//
	// @Override
	// public void onFail(JSONObject json)
	// {
	// dismissLoadingDialog();
	// try
	// {
	// Message msg = new Message();
	// Bundle bundel = new Bundle();
	// bundel.putString("info", json.getString("message"));
	// msg.setData(bundel);
	// handler.sendMessage(msg);
	// }
	// catch (JSONException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	public void doHttp() {

		JsonBuilder builder = new JsonBuilder();
		// builder.put("uid", Default.userId);
		builder.put("oldpwd", info[0]);
		builder.put("newpwd", info[1]);

		BaseHttpClient.post(getBaseContext(), Default.changepass, builder, new JsonHttpResponseHandler() {

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
							showCustomToast("修改登陆密码成功！");
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
