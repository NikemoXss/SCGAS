package com.lmq.main.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.gesturelock.GestureLockViewGroup;
import com.lmq.gesturelock.GestureLockViewGroup.OnGestureLockViewListener;
import com.lmq.gesturelock.Utils;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class GestureLockViewActivity extends BaseActivity implements OnClickListener {

	private GestureLockViewGroup mGestureLockViewGroup;
	List<Integer> mAnswerList = new ArrayList<Integer>();
	int mNum = 1;

	TextView title;
	ImageView title_right;

	int isFirst = 0;// 0表示的登录页面跳转的，1表示设置页面跳转的

	Boolean isOne = false;

	String username, pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesturelock);

		title = (TextView) findViewById(R.id.title);

		title_right = (ImageView) findViewById(R.id.title_right);
		title_right.setVisibility(View.VISIBLE);
		title_right.setOnClickListener(this);

		isFirst = getIntent().getIntExtra("isRu", 0);
		if (isFirst == 0) {
			title.setText("手势登录");
			username = getIntent().getStringExtra("username");
			pwd = getIntent().getStringExtra("pwd");
		}else {
			title.setText("手势绘制");
		}
		mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
		mGestureLockViewGroup.setAnswer(isFirst);
		mGestureLockViewGroup.setOnGestureLockViewListener(new OnGestureLockViewListener() {

			@Override
			public void onUnmatchedExceedBoundary() {
				mGestureLockViewGroup.setUnMatchExceedBoundary(5);
			}

			@Override
			public void onGestureEvent(int matched) {
				switch (matched) {
				case 0:// 第一次绘制
					showCustomToast("绘制成功,请再次绘制");
					isOne = true;
					break;
				case 1:// 第二次匹配错误，并且清除SharedPreferences内的数据
					showCustomToast("两次绘制的手势图案不匹配，请重新绘制。");
					clearData();
					break;
				case 2:// 匹配成功
					if (isFirst == 0) {
//						showCustomToast("登录成功");
						doHttp2();

					} else {
						isOne = false;
						showCustomToast("绘制成功,可在登录页进行手势登录");
						finish();
					}

					break;
				case 3:
					showCustomToast("登录失败,请重新登录");
				default:
					break;
				}
			}

			@Override
			public void onBlockSelected(int cId) {
			}
		});
	}

	public void doHttp2() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("sUserName", username);
		builder.put("sPassword", pwd);

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
								// if (cb_login_sx.isChecked()) {
								// saveNP_SX(credits, username);
								// } else {
								// clearNP();
								// }
								LoginActivity_Scg.mLoginActivity_Scg.finish();
								finish();
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

	public boolean isHasList() {
		SharedPreferences sharedPreferences = GestureLockViewActivity.this.getSharedPreferences("scenelist",
				Context.MODE_PRIVATE);
		String liststr = sharedPreferences.getString(Utils.SCENE_LIST, "");
		List showSceneList = null;
		try {
			showSceneList = Utils.String2SceneList(liststr);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (showSceneList.size() > 0) {
			return true;
		}
		return false;
	}

	public void clearData() {
		SharedPreferences sharedPreferences = GestureLockViewActivity.this.getSharedPreferences("scenelist",
				Context.MODE_PRIVATE);
		sharedPreferences.edit().clear().commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			if (isOne) {
				clearData();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isOne) {
			clearData();
		}
	}

}
