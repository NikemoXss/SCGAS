package com.lmq.menu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lmq.gesture.UnlockGesturePasswordActivity;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.http.NetWorkStatusBroadcastReceiver;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.ActivityManager;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.api.res.SyncImageLoader;
import com.lmq.main.dialog.NewVersionDialog;
import com.lmq.main.listener.MenuItemListener;
import com.lmq.main.util.Default;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainMenuActivity extends SlidingFragmentActivity implements MenuItemListener {

	public static MainMenuActivity mainMenuActivity;
	private HomeFragment homeFragment;
	private LendMoneyFragment lendMoneyFragment;
	// private UserInfoFragment userInfoFragment;
	private UserInfoFragment userInfoFragment_new;
	public SettingFragment settingFragment;
	private IndexFragment_Scg indexFragment;
	private SlidingMenu sm;

	public static MenuFragment menuFragment;

	private TextView titleTextView;

	private SyncImageLoader syncImageLoader;
	private int currentSelectIndex = 4;

	private NetWorkStatusBroadcastReceiver netWorkStatusBroadcastReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheExtraOptions(480, 800)
		// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
		// backKeyClickNum = 0;

		// 注册网络监听
		// 注册网络监听
		netWorkStatusBroadcastReceiver = new NetWorkStatusBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netWorkStatusBroadcastReceiver, filter);

		setContentView(R.layout.main);
		titleTextView = (TextView) findViewById(R.id.title_view);
		titleTextView.setText(R.string.app_name);
		mainMenuActivity = this;
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment

		indexFragment = new IndexFragment_Scg();

		menuFragment = new MenuFragment();

		menuFragment.addMenuItemListener(this);

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, indexFragment).commit();

		// customize the SlidingMenu
		sm = getSlidingMenu();

		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeEnabled(false);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		sm.setBackgroundImage(R.drawable.menu_background);
		sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth() / 2, canvas.getHeight() / 2);
			}
		});

		sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
			}
		});

		findViewById(R.id.title_bar_left_menu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				sm.toggle(true);

			}
		});

		// 检测更新
		syncImageLoader = new SyncImageLoader();
		getVersion();

		checkNewVersion();
		// showThePasswordView();
		if (!Default.isActive) {
			// app 从后台唤醒，进入前台
			if (!Default.isgestures) {

				showThePasswordView();
			} else {
				Default.isgestures = false;
			}
		}

	}

	public SyncImageLoader getImageLodader() {
		return syncImageLoader;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 4000:
			if (resultCode == Default.LOGIN_TYPE_2) {

				titleTextView.setText("我要借款");
				if (lendMoneyFragment == null) {
					lendMoneyFragment = new LendMoneyFragment();
				}
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, lendMoneyFragment).commit();
				sm.toggle();
				currentSelectIndex = 1;
			} else {

			}

			break;
		case 5000:
			if (resultCode == Default.LOGIN_TYPE_2) {
				titleTextView.setText("我的账户");
				// if (userInfoFragment == null) {
				// userInfoFragment = new UserInfoFragment();
				// }
				// getSupportFragmentManager().beginTransaction()
				// .replace(R.id.content_frame, userInfoFragment).commit();
				if (userInfoFragment_new == null) {
					userInfoFragment_new = new UserInfoFragment();
				}
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userInfoFragment_new).commit();
				sm.toggle();
				currentSelectIndex = 2;
			} else {

			}
			break;
		case 6000:
			// 设置头型
			if (resultCode == Default.LOGIN_TYPE_2) {

				menuFragment.setUserImage();
				// sm.toggle(true);
			} else {

			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// showCustomToast("ON STOP ");
		super.onStop();
		if (!isAppOnForeground()) {
			// app 进入后台

			// 全局变量isActive = false 记录当前已经进入后台
			Default.isActive = false;

		} else {
			Default.isgestures = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// showCustomToast("ON RESUME ");
		super.onResume();

		if (!Default.isActive) {
			// app 从后台唤醒，进入前台
			if (!Default.isgestures) {

				showThePasswordView();
			}
			Default.isActive = true;
		}
	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	public void showThePasswordView() {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("lmq", 0);

		if (sharedPreferences.getBoolean("sl", false)) {
			Intent intent = new Intent(getApplicationContext(), UnlockGesturePasswordActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplicationContext().startActivity(intent);

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();

	}

	private long lastTime;
	private int closeNum;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {

			sm.toggle(true);
			return false;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (sm.isMenuShowing()) {
				sm.toggle(true);
				return false;
			} else {
				if (currentSelectIndex != 4) {
					if (currentSelectIndex != 4) {
						titleTextView.setText(R.string.app_name);
						getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, indexFragment).commit();
						currentSelectIndex = 4;
					}
					// sm.toggle(false);
				} else {
					closeNum++;
					if (System.currentTimeMillis() - lastTime > 5000) {
						closeNum = 0;
					}
					lastTime = System.currentTimeMillis();

					if (closeNum == 1) {
						doHttp();
						finish();
					} else {
						showCustomToast(R.string.toast6);
					}
				}
			}
			return false;
		}

		// if (keyCode == KeyEvent.KEYCODE_BACK && backKeyClickNum == 1
		// && !sm.isMenuShowing()) {
		// doHttp();
		// finish();
		// return false;
		// }

		return super.onKeyDown(keyCode, event);
	}

	private void doHttp() {

		BaseHttpClient.post(getBaseContext(), Default.exit, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);

				try {

					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							MyLog.d("zzx", "exit成功");
							ActivityManager.closeAllActivity();
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
			}

		});

	}

	private NewVersionDialog dialog;
	private String downloadURL = "";

	// 提交用户反馈到服务器
	public void checkNewVersion() {

		dialog = new NewVersionDialog(MainMenuActivity.this, "");
		dialog.setListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (v.getId()) {
				// 下载更新
				case R.id.download_newapp:

					// dialog.updateBtn.setEnabled(false);
					//
					// dialog.exitbtn.setEnabled(false);
					// dialog.showProgress();
					// downloadNewVersionApp(dialog.getPath());

					ActivityInfo activityInfo = SystenmApi.getBrowserApp(getApplicationContext());
					if (activityInfo != null) {
						Uri uri = Uri.parse(dialog.getPath());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						intent.setClassName(activityInfo.packageName, activityInfo.name);
						startActivity(intent);
					} else {
						showCustomToast("手机没有浏览器，无法更新...");
					}

					break;
				// 退出程序
				case R.id.newversion_exit:
					doHttp();
					finish();

					break;

				default:
					break;
				}

			}

		});

		JsonBuilder jsonBuilder = new JsonBuilder();

		jsonBuilder.put("version", Default.curVersion);// SystenmApi.getVersion(MainMenuActivity.this)

		BaseHttpClient.post(getBaseContext(), Default.version, jsonBuilder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {

					if (statusCode == 200) {
						MyLog.e(response.toString());
						if (response.getInt("status") == 1) {

						} else if (response.getInt("status") == 0) {

							String temp = dialog.versionTextView.getText().toString();
							dialog.versionTextView.setText(temp + "(" + response.getString("version") + ")");
							dialog.show();
							dialog.setPath(response.getString("path").replace("\\/", ""));
						} else {

						}
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

		});

	}

	/**
	 * 根据地址下载最新版本的APP
	 *
	 * @param download_url
	 */
	public void downloadNewVersionApp(String download_url) {
		String filePathStr = Environment.getExternalStorageDirectory() + "/temp/";
		String fileName = "newVersion.apk";
		File file = new File(filePathStr);
		File saveFile = null;
		try {
			saveFile = File.createTempFile("newVersion", ".apk", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BaseHttpClient.downloadFile(MainMenuActivity.this, download_url, new FileAsyncHttpResponseHandler(saveFile) {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				MyLog.e("onStart----------->" + "onStart");

			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);

				int num = getPercent(bytesWritten, totalSize);
				dialog.progressBar.setProgress(num);
				dialog.showProgressView.setText(num + "%");

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, File file) {
				// TODO Auto-generated method stub

				MyLog.e("SUCCESS");
				BaseHttpClient.httpClient.setTimeout(3 * 1000);
				dialog.updateBtn.setText("安裝");
				dialog.updateBtn.setTag(101);

				MyLog.e(file.getPath());
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(file.getPath())), "application/vnd.android.package-archive");
				startActivity(intent);
				finish();

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				// TODO Auto-generated method stub

			}
		});

	}

	public int getPercent(int x, int total) {
		int result = 0;// 接受百分比的值
		double x_double = Double.parseDouble(x + "");
		int tempresult = (int) (x / Double.parseDouble(total + "") * 100);

		return tempresult;
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public void getVersion() {
		try {
			Default.curVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(netWorkStatusBroadcastReceiver); // 取消监听
		super.onDestroy();
	}

	public void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(MainMenuActivity.this).inflate(R.layout.common_toast, null);
		((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(MainMenuActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	public void showCustomToast(int id) {
		showCustomToast(getResources().getString(id).toString());
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// sm.toggle(true);

			return false;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		Default.isAlive = false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		MyLog.e("123", "00------------->");
		switch (arg0.getId()) {

		case R.id.one1:

			if (currentSelectIndex != 4) {
				titleTextView.setText(R.string.app_name);
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, indexFragment).commit();
				currentSelectIndex = 4;
			}
			sm.toggle(true);
			break;
		case R.id.one:// 我要投资

			changeHomeFragment(0);

			sm.toggle(true);

			break;
		case R.id.two:// 我要借款
			MyLog.e("123", "2------------->");
			if (Default.userId == 0) {
				Intent lendMoneyLoginIntent = new Intent();
				lendMoneyLoginIntent.setClass(this, loginActivity.class);
				startActivityForResult(lendMoneyLoginIntent, 4000);

			} else {

				if (currentSelectIndex != 1) {
					titleTextView.setText("我要借款");
					if (lendMoneyFragment == null) {
						lendMoneyFragment = new LendMoneyFragment();
					}
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, lendMoneyFragment).commit();
					currentSelectIndex = 1;
				}

				sm.toggle(true);

			}
			break;
		case R.id.three:
			MyLog.e("123", "3------------->");
			if (Default.userId == 0) {
				Intent userInfoLoginIntent = new Intent();
				userInfoLoginIntent.setClass(this, loginActivity.class);
				startActivityForResult(userInfoLoginIntent, 5000);
			} else {

				if (currentSelectIndex != 2) {

					titleTextView.setText("我的账户");
					// if (userInfoFragment == null) {
					// userInfoFragment = new UserInfoFragment();
					// }
					// getSupportFragmentManager().beginTransaction()
					// .replace(R.id.content_frame, userInfoFragment)
					// .commit();
					if (userInfoFragment_new == null) {
						userInfoFragment_new = new UserInfoFragment();
					}
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, userInfoFragment_new).commit();
					currentSelectIndex = 2;
				}

				sm.toggle(true);

			}
			break;
		case R.id.four:
			MyLog.e("123", "4------------->");
			if (currentSelectIndex != 3) {
				titleTextView.setText("更多");
				if (settingFragment == null) {
					settingFragment = new SettingFragment();
				}
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, settingFragment).commit();
				currentSelectIndex = 3;
			}

			sm.toggle(true);
			break;

		case R.id.company_tel:
			MyLog.e("123", "4------------->");
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.about_info3))));

			break;

		case R.id.menu_login_reg_btn:
			MyLog.e("123", "4------------->");
			if (Default.userId == 0) {
				Intent userInfoLoginIntent = new Intent();
				userInfoLoginIntent.setClass(this, loginActivity.class);
				startActivityForResult(userInfoLoginIntent, 6000);
			} else {
				menuFragment.showDialog();

			}
			break;

		default:
			break;
		}

	}

	public void changeHomeFragment(int type) {
		if (currentSelectIndex != 0) {
			titleTextView.setText("我要投资");
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
			}
			homeFragment.swith_flag = type;

			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
			currentSelectIndex = 0;
		}
	}

}
