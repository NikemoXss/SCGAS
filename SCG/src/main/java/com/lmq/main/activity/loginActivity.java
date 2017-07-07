package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.gesture.CreateGesturePasswordActivity;
import com.lmq.gesture.UnlockGesturePasswordActivity;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;

public class loginActivity extends BaseActivity implements OnClickListener {

	private EditText mEditName, mEditPassword;
	private Button mCheckbox;
	private boolean mRemember = true;

	private String mName;
	private String mPassword;
	private boolean modify_password_flag = false;
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		if (intent.getStringExtra("check_pass") != null && intent.getStringExtra("check_pass").equals("1")) {

			modify_password_flag = true;

		} else {

			modify_password_flag = false;
		}
		if (intent.getBooleanExtra("exit", false)) {
			handler.sendEmptyMessage(1);
		}

		setContentView(R.layout.login);
		mEditName = (EditText) findViewById(R.id.editname);
		mEditPassword = (EditText) findViewById(R.id.editpassw);

		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.peo_info8);

		mCheckbox = (Button) findViewById(R.id.remember);
		mCheckbox.setBackgroundResource(R.drawable.b_chech_1_1);
		mCheckbox.setOnClickListener(this);

		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.forget_pwd).setOnClickListener(this);

		if (getNP() && !modify_password_flag) {
			mEditName.setText(mName);
			mEditPassword.setText(mPassword);

		}

		mCheckbox.setBackgroundResource(mRemember && !modify_password_flag ? R.drawable.b_chech_1_1 : R.drawable.b_chech_1_0);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showCustomToast("已掉线，请重新登录");
				doHttpExit();
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Default.RESULT_REGISTER_TO_LOGIN) {
			mEditName.setText(data.getExtras().getString("name"));
			mEditPassword.setText(data.getExtras().getString("password"));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if (modify_password_flag) {

				Intent intent = new Intent(loginActivity.this, UnlockGesturePasswordActivity.class);
				startActivity(intent);
			}
			finish();
			break;
		case R.id.login:
			mName = mEditName.getText().toString();
			mPassword = mEditPassword.getText().toString();

			if (mName.equals("") || mPassword.equals("")) {
				showCustomToast(R.string.erro_login1);
				return;
			}
			// 是否保存密码
			if (mRemember) {
				saveNP();
			} else {
				clearNP();
			}
			doHttp2();
			// showLogin();
			break;
		case R.id.register:
			startActivityForResult(new Intent(loginActivity.this, registerActivity.class), 0);
			break;
		case R.id.remember:
			if (mRemember) {
				mRemember = false;
				mCheckbox.setBackgroundResource(R.drawable.b_chech_1_0);
			} else {
				mRemember = true;
				mCheckbox.setBackgroundResource(R.drawable.b_chech_1_1);
			}
			break;

		case R.id.forget_pwd:
			startActivity(new Intent(loginActivity.this, ForgotPwdActivity.class));
			break;
		}
	};

	public void clearInfo() {
		mEditName.setText("");
		mEditPassword.setText("");
		mRemember = false;
	}

	/**
	 * 存储用户信息
	 */
	public void saveNP() {

		/**
		 * 加密存储用户信息
		 */
		SystenmApi.saveUserLoginInfo(getApplicationContext(), mName, mPassword);
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		Editor edit = sp.edit();
		edit.putString(Default.userName, mName);
		edit.putString(Default.userPassword, mPassword);
		edit.putBoolean(Default.userRemember, mRemember);
		edit.commit();
	}

	public void clearNP() {
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		Editor edit = sp.edit();
		edit.putString(Default.userName, "");
		edit.putString(Default.userPassword, "");
		edit.putBoolean(Default.userRemember, false);
		edit.commit();
	}

	public boolean getNP() {
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		mName = sp.getString(Default.userName, "");
		mPassword = sp.getString(Default.userPassword, "");
		mRemember = sp.getBoolean(Default.userRemember, false);

		if (mName.equals("") || mPassword.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 自动登陆
	 *
	 * @param name
	 * @param pwd
	 * @return
	 */

	public void doHttp2() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("sUserName", mName);
		builder.put("sPassword", mPassword);

		BaseHttpClient.post(getBaseContext(), Default.login, builder, new JsonHttpResponseHandler() {

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

				dismissLoadingDialog();
				if (statusCode == 200) {
					try {
						if (json.has("status")) {
							if (json.getInt("status") == 1) {
								MyLog.e("123", "登陆后的数据:" + json.toString());
								getJsonInfo(json);
								if (modify_password_flag) {

									Intent intent = new Intent(loginActivity.this, CreateGesturePasswordActivity.class);

									loginActivity.this.startActivity(intent);
								}
								finish();
							} else {
								showCustomToast(json.getString("message"));
							}
						} else {
							showCustomToast(json.getString("message"));
						}

					} catch (Exception e) {

						e.printStackTrace();
						showCustomToast("登录异常");
					}

				} else {
					showCustomToast(R.string.toast1);
				}
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (modify_password_flag) {

				Intent intent = new Intent(loginActivity.this, UnlockGesturePasswordActivity.class);
				startActivity(intent);
				finish();
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public void getJsonInfo(JSONObject json) {
		try {
			int type = json.getInt("status");
			if (type == 1) {
				Default.userId = json.getLong("uid");
				Default.username = json.getString("username");
				Default.user_photo_path = json.getString("head");
				if (json.has("phoneverify_manual")) {
					Default.phoneverif = json.getInt("phoneverify_manual");
				}
				// MainMenuActivity.menuFragment.setUserImage();
				Intent intent = new Intent();

				try {
					// 获取用户登录成功之后 服务器端返回的数据
					Bundle bundle = new Bundle();
					bundle.putDouble("collect", json.getDouble("collect"));
					bundle.putDouble("mayuse", json.getDouble("mayuse"));
					bundle.putDouble("freeze", json.getDouble("freeze"));
					bundle.putDouble("total", json.getDouble("total"));
					bundle.putString("head", json.getString("head"));
					bundle.putString("credits", json.getString("credits"));
					bundle.putString("username", json.getString("username"));
					System.out.println("bundle获得冻结金额:" + json.getDouble("freeze") + "bundle获得余额金额:" + json.getDouble("mayuse"));
					intent.putExtras(bundle);

					SharedPreferences sp = getSharedPreferences("user", 0);
					Editor edit = sp.edit();
					// 代收
					edit.putString("collect", json.getDouble("collect") + "");
					// 可用余额
					edit.putString("mayuse", json.getDouble("mayuse") + "");
					// 冻结金额
					edit.putString("freeze", json.getDouble("freeze") + "");
					// 总额
					edit.putString("total", json.getDouble("total") + "");
					edit.putString("head", json.getString("head"));
					edit.putString("credits", json.getString("credits"));
					// 用户名保存
					edit.putString("username", json.getString("username"));
					// 用户ID 保存
					edit.putString("userid", json.getString("uid"));

					// 用户头像保存
					edit.putString("phoneverif", json.getString("head"));

					System.out.println("存取总额到本地^^^^^^^^^^^^冻结：" + json.getDouble("freeze") + "余额：" + json.getDouble("mayuse"));

					edit.commit();

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setResult(Default.LOGIN_TYPE_2, intent);

				Default.layout_type = Default.pageStyleInfo;

				overridePendingTransition(R.anim.left_to_right, R.anim.to_right);
			} else {
				showCustomToast(json.getString("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doHttpExit() {
		JsonBuilder builder = new JsonBuilder();

		BaseHttpClient.post(getBaseContext(), Default.exit, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				try {

					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							MyLog.d("zzx", "exit成功");
						} else {
							MyLog.d("zzx", "exit失败");
						}
					} else {
						MyLog.d("zzx", "exit失败");
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
			}

		});
	}
}
