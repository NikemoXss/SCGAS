package com.lmq.menu;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.czscg.R;
import com.lmq.gesture.GuideGesturePasswordActivity;
import com.lmq.gesture.UnlockGesturePasswordActivity;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.AboutUsNewActivity;
import com.lmq.main.activity.FeedbackActivity;
import com.lmq.main.activity.NewsListActivity;
import com.lmq.main.activity.NoticeActivity;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.LocationApplication;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Data;
import com.lmq.main.util.Default;

public class SettingFragment extends BaseFragment implements OnClickListener {
	private View parentView;
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 5000;
	private static int LOCATION_COUTNS = 0;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private SharedPreferences sharedPreferences;
	private ToggleButton t2;
	private Button exit_button;
	private View finger_pwd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_more, null);
		sharedPreferences = getActivity().getSharedPreferences("lmq", 0);

		initView(mainView);
		return mainView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initView(View view) {

		((LocationApplication) getActivity().getApplication()).resultTextView = (TextView) view.findViewById(R.id.location_text);
		view.findViewById(R.id.userinfo).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_news).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_notice).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_newVersion).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_feedback).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_about).setOnClickListener(new ItemTouchListener());
		view.findViewById(R.id.item_share).setOnClickListener(new ItemTouchListener());
		finger_pwd = view.findViewById(R.id.finger_pwd);
		exit_button = (Button) view.findViewById(R.id.more_exit);
		exit_button.setOnClickListener(new ItemTouchListener());

		ToggleButton t = (ToggleButton) view.findViewById(R.id.toggle_location);
		t.setOnCheckedChangeListener(new SwitchClickListener());
		t2 = (ToggleButton) view.findViewById(R.id.toggle_screenLock);
		t2.setOnClickListener(this);

		// showExitBtn();

		boolean local_flag = sharedPreferences.getBoolean("dw", false);
		if (local_flag) {

			t.setChecked(true);
			getLocation();

		} else {

			t.setChecked(false);
		}

		if (!sharedPreferences.getBoolean("sl", false)) {

			t2.setChecked(false);

		}

	}

	public void showDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("退出");
		builder.setMessage("是否退出该用户");
		builder.setIcon(R.drawable.scg_logo);

		builder.setPositiveButton("退出", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				doHttpExit();
				dialog.dismiss();

			}

		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 退出登录按钮控制
	 */
	// private void showExitBtn() {
	//
	// switch (Default.style) {
	//
	// case 0:
	// if (Default.userId == 0) {
	// exit_button.setVisibility(View.GONE);
	// } else {
	// exit_button.setVisibility(View.VISIBLE);
	// }
	//
	// default:
	// break;
	//
	// }
	// }

	private void doHttpExit() {
		BaseHttpClient.post(getActivity(), Default.exit, null, new JsonHttpResponseHandler() {

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
				MyLog.d("zzx", "exit成功" + json.toString());
				try {

					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							MyLog.d("zzx", "exit成功");
							Default.layout_type = Default.pageStyleLogin;
							Default.userId = 0;
							// showExitBtn();
							// setDefaultImage();
							Data.clearInfo();
							// Intent intent = new Intent();
						} else {
							MyLog.d("zzx", "exit失败");
						}
					} else {
						MyLog.d("zzx", "exit失败");
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
			}

		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 打开开关
		if (Default.passwordSwitchChanged) {

			if (t2 != null) {
				t2.setChecked(true);
			}
		} else if (!Default.passwordSwitchChanged && !sharedPreferences.getBoolean("sl", false)) {
			// 关闭开关
			if (t2 != null) {
				t2.setChecked(false);
			}

		}
		// showExitBtn();
		// showFingerPWDView();
	}

	// private void showFingerPWDView() {
	//
	// if (Default.userId == 0) {
	//
	// finger_pwd.setVisibility(View.GONE);
	// } else {
	//
	// finger_pwd.setVisibility(View.VISIBLE);
	//
	// }
	// }

	// 开关监听事件
	class SwitchClickListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

			switch (arg0.getId()) {
			case R.id.toggle_location:
				if (arg1) {

					Editor editor = sharedPreferences.edit();
					editor.putBoolean("dw", true);
					editor.commit();
					getLocation();
				} else {
					Editor editor = sharedPreferences.edit();
					editor.putBoolean("dw", false);
					editor.commit();
					((LocationApplication) getActivity().getApplication()).locationClient.stop();
					((LocationApplication) getActivity().getApplication()).resultTextView.setText("未定位");

				}

				break;

			case R.id.toggle_screenLock:

				// if (arg1) {
				// MyLog.e("手势解锁", "------------>");
				// if (!sharedPreferences.getBoolean("sl", false)) {
				// startActivityForResult(new Intent(getActivity(),
				// GuideGesturePasswordActivity.class), 1212);
				// }
				//
				// } else {
				//
				// Intent closeIntent = new Intent(getActivity(),
				// UnlockGesturePasswordActivity.class);
				// closeIntent.putExtra("closePwd", true);
				// startActivity(closeIntent);
				//
				// }
				break;

			default:
				break;
			}

		}

	}

	public void setpwdSwithClose() {

		if (t2 != null) {

			t2.setChecked(false);
		}
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("sl", false);
		editor.commit();
	}

	public void setpwdSwitch() {
		if (t2 != null) {

			t2.setChecked(true);
		}

	}

	// 实现Item 点击监听事件
	class ItemTouchListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			switch (arg0.getId()) {
			case R.id.more_exit:
				showDialog();
				break;
			case R.id.item_news:

				getActivity().startActivity(new Intent(getActivity(), NewsListActivity.class));

				break;
			case R.id.item_notice:

				getActivity().startActivity(new Intent(getActivity(), NoticeActivity.class));

				break;

			case R.id.item_newVersion:
				// getLocation();
				// locationClient.start();
				checkNewVersion();

				break;
			case R.id.item_feedback:

				// 检测用户是否登录
				if (Default.userId != 0) {

					getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));

				} else {

					getActivity().startActivity(new Intent(getActivity(), loginActivity.class));
				}

				break;
			case R.id.item_about:
				getActivity().startActivity(new Intent(getActivity(), AboutUsNewActivity.class));
				break;
			case R.id.item_share:
				SystenmApi.showShareView(getActivity(), "德成贷理财APP，随时随地掌握你的财富", "手机移动理财的指尖神器，帮您在“拇指时代”指点钱途，“掌握财富”。", "http://www.dechengdai.com/m");
				break;
			default:

				break;
			}

		}

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.toggle_screenLock:

			if (Default.userId == 0) {
				t2.setChecked(false);
				showCustomToast("请登录后再设置手势密码！");

			} else {
				if (!sharedPreferences.getBoolean("sl", false)) {
					t2.setChecked(false);
					startActivityForResult(new Intent(getActivity(), GuideGesturePasswordActivity.class), 1212);
				} else {
					t2.setChecked(true);
					Intent closeIntent = new Intent(getActivity(), UnlockGesturePasswordActivity.class);
					closeIntent.putExtra("closePwd", true);
					startActivity(closeIntent);

				}
			}

			break;

		default:
			break;
		}
	}

	// 提交用户反馈到服务器
	public void checkNewVersion() {
		JsonBuilder jsonBuilder = new JsonBuilder();
		
		jsonBuilder.put("version", Default.version);

		BaseHttpClient.post(getActivity(), Default.version, jsonBuilder, new JsonHttpResponseHandler() {

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
				Log.e("onSuccess",statusCode+ response.toString());
				try {

					if (statusCode == 200) {
						// 没有新版本
						if (response.getInt("status") == 1) {
//							 initData(json);
//							UpdateChecker updateChecker	=new UpdateChecker(getActivity());
//							updateChecker.setCheckUrl("1111");
//							updateChecker.checkForUpdates();
							showCustomToast(response.getString("message"));
							// 获取新版本
						} else if (response.getInt("status") == 0) {
							// 另起后台线程 下载新版APP

						} else {
							showCustomToast(response.getString("message"));
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
//				showCustomToast(responseString);
			}

		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		MyLog.e("123", "123");
		if (requestCode == 1212) {
			if (!sharedPreferences.getBoolean("sl", false)) {

				t2.setChecked(false);

			} else {

				t2.setChecked(true);
			}

		}
		if (requestCode == 101) {
			if (resultCode == 102) {
				showCustomToast(data.getExtras().getString("msg"));
			}
		}
	}

	/***
	 * 基于百度定位SDK获取地理位置
	 */
	public void getLocation() {

		locationClient = ((LocationApplication) getActivity().getApplication()).locationClient;
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(((LocationApplication) getActivity().getApplication()).myLocationListener);
		locationClient.start();

	}

}
