package com.imp.qq;

import org.json.JSONObject;

import com.lmq.main.api.BaseActivity;
import com.czscg.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QQLogin extends BaseActivity {
	Tencent mTencent;
	Button qqbtn, btn_getUserinfo;
	IUiListener listener;
	IUiListener userinfolister;
	private static final String APPID = "1105681579";
	TextView tv;
	private UserInfo userInfo; // qq用户信息
//http://blog.csdn.net/bear_huangzhen/article/details/46602741
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qqlogin);
		tv = (TextView) findViewById(R.id.tv_userinfo);
		qqbtn = (Button) findViewById(R.id.btn_QQlogin);
		btn_getUserinfo = (Button) findViewById(R.id.btn_getUserinfo);
		qqbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mTencent.isSessionValid()) {
					mTencent.login(QQLogin.this, "all", listener);
				}

			}
		});
		btn_getUserinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userInfo = new UserInfo(QQLogin.this, mTencent.getQQToken());
				userInfo.getUserInfo(userinfolister);
			}
		});
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mTencent = Tencent.createInstance(APPID, QQLogin.this);
		listener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onError", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onComplete", Toast.LENGTH_LONG).show();
				tv.setText(arg0.toString());
				if (arg0 == null) {
					return;
				}

				try {
					JSONObject jo = (JSONObject) arg0;

					int ret = jo.getInt("ret");

					System.out.println("json=" + String.valueOf(jo));

					if (ret == 0) {
						Toast.makeText(QQLogin.this, "登录成功", Toast.LENGTH_LONG).show();

						String openID = jo.getString("openid");
						String accessToken = jo.getString("access_token");
						String expires = jo.getString("expires_in");
						mTencent.setOpenId(openID);
						mTencent.setAccessToken(accessToken, expires);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onCancel", Toast.LENGTH_LONG).show();
			}
		};
		userinfolister = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onError", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onComplete", Toast.LENGTH_LONG).show();
				tv.setText(arg0.toString());
				if (arg0 == null) {
					return;
				}
				try {
					JSONObject jo = (JSONObject) arg0;
					int ret = jo.getInt("ret");
					System.out.println("json=" + String.valueOf(jo));
					String nickName = jo.getString("nickname");
					String gender = jo.getString("gender");

					Toast.makeText(QQLogin.this, "你好，" + nickName, Toast.LENGTH_LONG).show();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(QQLogin.this, "onCancel", Toast.LENGTH_LONG).show();
			}
		};

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 回调一定要加上
		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.REQUEST_LOGIN) {
				mTencent.handleLoginData(data, listener);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}