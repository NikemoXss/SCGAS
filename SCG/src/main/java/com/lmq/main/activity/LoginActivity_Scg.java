package com.lmq.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.gesturelock.Utils;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;
import com.lmq.menu.MainTabActivit_Scg;
import com.lmq.view.EditTextWithDel;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.tencent.bugly.crashreport.CrashReport;

import org.apache.http.Header;
import org.json.JSONObject;



@SuppressLint("NewApi")
public class LoginActivity_Scg extends BaseActivity implements OnClickListener {

	EditTextWithDel login_zh_sx, login_mm_sx;
	Button login_sx;
	String login_zh_sx_str, login_mm_sx_str;
	TextView title, register_sx, forget_pwd_sx;
	ImageView iv, title_left1;
	CheckBox cb_login_sx;
	LinearLayout et_dh, login_zh_ll, login_pwd_ll, remember_ll, login_ed_ll;
	Button login_sx_bt;
	static LoginActivity_Scg mLoginActivity_Scg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_scg);
		LoginActivity_Scg.mLoginActivity_Scg = this;
		login_sx_bt = (Button) findViewById(R.id.login_sx_bt);
		login_sx_bt.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("登录");
		login_zh_sx = (EditTextWithDel) findViewById(R.id.login_zh_sx);
		login_mm_sx = (EditTextWithDel) findViewById(R.id.login_mm_sx);
		login_zh_sx.setPic(R.drawable.icon_03);
		login_mm_sx.setPic(R.drawable.icon_05);
		
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);

		title_left1 = (ImageView) findViewById(R.id.title_left1);
		title_left1.setOnClickListener(this);

		findViewById(R.id.forget_pwd_sx).setOnClickListener(this);
		findViewById(R.id.register_scg).setOnClickListener(this);
		cb_login_sx = (CheckBox) findViewById(R.id.cb_login_sx);
		cb_login_sx.setChecked(true);

		et_dh = (LinearLayout) findViewById(R.id.et_dh);
		login_zh_ll = (LinearLayout) findViewById(R.id.login_zh_ll);
		login_pwd_ll = (LinearLayout) findViewById(R.id.login_pwd_ll);
		remember_ll = (LinearLayout) findViewById(R.id.remember_ll);
		login_ed_ll = (LinearLayout) findViewById(R.id.login_ed_ll);

		// ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
		// animator.setTarget(et_dh);
		// animator.setDuration(1000).start();
		// animator.addUpdateListener(new AnimatorUpdateListener() {
		// @Override
		// public void onAnimationUpdate(ValueAnimator animation) {
		// et_dh.setTranslationY((Float) animation.getAnimatedValue());
		// }
		// });

		ZxAnimator(login_sx_bt, 100);
		ZxAnimator(remember_ll, 300);
		ZxAnimator(login_ed_ll, 400);
		// ZxAnimator(login_pwd_ll, 800);
		// ZxAnimator(login_zh_ll, 1000);

		getNP();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isHasSh()) {
			title_left1.setVisibility(View.VISIBLE);
		}
	}

	public void ZxAnimator(final View view, long time) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				zxxxx(view);
			}
		}, time);

	}

	public void zxxxx(final View view) {
		ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
		animator.setTarget(view);
		animator.setDuration(800).start();
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				view.setTranslationY((Float) animation.getAnimatedValue());
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_sx_bt:// 登录
			login_zh_sx_str = login_zh_sx.getText().toString();
			login_mm_sx_str = login_mm_sx.getText().toString();
			doHttp2();
			break;
		case R.id.title_right:
			Intent intent = getIntent();
			setResult(Default.LOGIN_TYPE_2, intent);
			MainTabActivit_Scg.mainTabActivity.IndexView();
			finish();
			break;
		case R.id.forget_pwd_sx:
			Intent intent1 = new Intent(LoginActivity_Scg.this, ForgotPwdActivity.class);
			intent1.putExtra("iszfpwd", "1");
			startActivity(intent1);
			break;
		case R.id.register_scg:
			 Intent i = new Intent(LoginActivity_Scg.this,
			 registerActivity.class);
			 startActivityForResult(i, 0);
			
//			Intent i = new Intent(LoginActivity_Scg.this, Card.class);
//			startActivity(i);
			break;
		case R.id.title_left1:
			Intent i1 = new Intent(LoginActivity_Scg.this, GestureLockViewActivity.class);
			i1.putExtra("isRu", 0);
			i1.putExtra("username", login_zh_sx.getText().toString());
			i1.putExtra("pwd", login_mm_sx.getText().toString());
			startActivity(i1);
			break;
		default:
			break;
		}
	}

	public boolean isHasSh() {
		SharedPreferences sharedPreferences = LoginActivity_Scg.this.getSharedPreferences("scenelist",
				Context.MODE_PRIVATE);
		// String liststr = sharedPreferences.getString(Utils.SCENE_LIST, "");
		String user = sharedPreferences.getString(Utils.SCENE_USER, "");
		String pwd = sharedPreferences.getString(Utils.SCENE_PWD, "");
		String mUser = login_zh_sx.getText().toString();
		String mPwd = login_mm_sx.getText().toString();
		Log.e("isHasShisHasSh", user + ";" + pwd + ";" + mUser + ";" + mPwd);
		if ((!"".equals(user)) && (user.equals(mUser) && (pwd.equals(mPwd)))) {
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0 && data != null) {
			login_zh_sx.setText(data.getExtras().getString("name"));
			login_mm_sx.setText(data.getExtras().getString("password"));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				finish();
				MainTabActivit_Scg.mainTabActivity.IndexView();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 存储用户信息
	 */
	public void saveNP_SX(String credits, String username) {

		/**
		 * 加密存储用户信息
		 */
		SystenmApi.saveUserLoginInfo(getApplicationContext(), login_zh_sx_str, login_mm_sx_str);
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		Editor edit = sp.edit();
		edit.putString(Default.userName, login_zh_sx_str);
		edit.putString(Default.userPassword, login_mm_sx_str);
		edit.putString("credits", credits);
		edit.putString("username", username);
		edit.commit();

	}

	public void getNP() {
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		if (!sp.getString(Default.userName, "").equals("")) {
			login_zh_sx.setText(sp.getString(Default.userName, ""));
			login_mm_sx.setText(sp.getString(Default.userPassword, ""));
			// mName = sp.getString(Default.userName, "");
			// mPassword = sp.getString(Default.userPassword, "");
		}

	}

	public void clearNP() {
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		Editor edit = sp.edit();
		edit.putString(Default.userName, "");
		edit.putString(Default.userPassword, "");
		edit.commit();
	}

	public void doHttp2() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("sUserName", login_zh_sx_str);
		builder.put("sPassword", login_mm_sx_str);

		BaseHttpClient.post(getBaseContext(), Default.login, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				// showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
				showLoadingDialogNoCancle("");
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
								String username = json.optString("username");
								Long uid = json.getLong("uid");
								Default.userId = uid;
								String credits = json.optString("credits");
								if (cb_login_sx.isChecked()) {
									saveNP_SX(credits, username);
								} else {
									clearNP();
								}
								LogoActicity.loginActivity.finish();
								finish();
								//腾讯bugly设置发生crash时，传给后台
								CrashReport.putUserData(getApplicationContext(), "uid", Default.userId+"");
								CrashReport.putUserData(getApplicationContext(), "version", Default.curVersion+"");
								CrashReport.putUserData(getApplicationContext(), "username", username);
								// Intent i = new Intent(LoginActivity_Scg.this,
								// MainTabActivit_Scg.class);
								// startActivity(i);
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

}
