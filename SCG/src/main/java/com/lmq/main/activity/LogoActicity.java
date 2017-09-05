package com.lmq.main.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.czscg.R;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.lmq.menu.MainTabActivit_Scg;

import java.io.File;

//scw
public class LogoActicity extends Activity implements OnClickListener {

	private SharedPreferences sharedPreferences;

	static LogoActicity loginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		LogoActicity.loginActivity = this;
		sharedPreferences = getSharedPreferences("lmq", 0);

		findViewById(R.id.rs_register).setOnClickListener(this);
		findViewById(R.id.rs_login).setOnClickListener(this);

		if (!sharedPreferences.contains("fist_into")) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("fist_into", true);
			editor.commit();
		}

		MyLog.e("手机型号:", android.os.Build.MODEL);
		// 获取手机系统信息
		Default.PHONE_MODEL = android.os.Build.MODEL;
		Default.OS_VERSION = android.os.Build.VERSION.RELEASE + "";
		MyLog.e("手机型号:", "" + Default.PHONE_MODEL + "Android版本:" + Default.OS_VERSION);

		// 获取分辨率
		Default.dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(Default.dm);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				stop();
			}
		}, 800);

		File f = new File(Environment.getExternalStorageDirectory() + Default.PIC_PATH);
		if (!f.exists()) {
			f.mkdir();
		}

		// PushManager.startWork(getApplicationContext(),
		// PushConstants.LOGIN_TYPE_API_KEY,
		// Utils.getMetaValue(LogoActicity.this, "api_key"));

		Default.isAlive = true;


		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
				"wDATlcHjOxqIDYVvk1kZzH1iOp6FrBeV");

		// String pkgName = this.getPackageName();
		// Resources resource = this.getResources();

		CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(R.layout.notification_custom_builder,
				R.id.notification_icon, R.id.notification_title, R.id.notification_text);
		cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
		cBuilder.setStatusbarIcon(R.drawable.scg_logo);
		cBuilder.setLayoutDrawable(R.drawable.bg_common_toast);
		// cBuilder.setNotificationSound(Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI,
		// "6").toString());

		// 推送高级设置，通知栏样式设置为下面的ID
		PushManager.setNotificationBuilder(this, 10, cBuilder);


	}


	public void stop() {
		// time.cancel();
		// time.purge();

		// 判断用户是否开启了手势密码功能
		Intent intent = null;
		if (sharedPreferences.getBoolean("fist_into", false)) {
			// intent = new Intent(LogoActicity.this,
			// LoadIntroduceActivity.class);
			intent = new Intent(LogoActicity.this, MainTabActivit_Scg.class);
		} else {
			switch (Default.style) {
			case 0:// tab样式
				intent = new Intent(LogoActicity.this, MainTabActivit_Scg.class);
				// intent = new Intent(LogoActicity.this, QQLogin.class);
				// intent = new Intent(LogoActicity.this, WXLogin.class);
				break;
			case 1:
				intent = new Intent(LogoActicity.this, MainTabActivit_Scg.class);
				break;

			default:
				break;
			}

		}

		// Intent ;
		startActivity(intent);
		LogoActicity.this.finish();
		overridePendingTransition(R.anim.light_to_dark, R.anim.dark_to_light);
		// overridePendingTransition(android.R.anim.fade_in,
		// android.R.anim.fade_out);

		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rs_login:
			Intent intent = new Intent(LogoActicity.this, LoginActivity_Scg.class);
			startActivity(intent);
			// overridePendingTransition(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			break;
		case R.id.rs_register:
			Intent intent1 = new Intent(LogoActicity.this, registerActivity.class);
			startActivity(intent1);
			// overridePendingTransition(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			break;

		default:
			break;
		}
	}
}
