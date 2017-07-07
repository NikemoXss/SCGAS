package com.lmq.http;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.ActivityManager;
import com.lmq.main.api.MyLog;
import com.lmq.menu.MainMenuActivity;

public class JsonHttpResponseHandler extends com.loopj.android.http.JsonHttpResponseHandler {

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub

		MyLog.e("123", response.toString() + "");
		if (statusCode == 200) {

			try {
				if (response != null && response.has("session_expired")) {
					if (response.getInt("session_expired") == 1) {
						Activity a = ActivityManager.getCurrentActivit();
						if (a == null) {
							a = MainMenuActivity.mainMenuActivity;
						}
						Intent intent = new Intent(a, loginActivity.class);
						intent.putExtra("exit", true);
						a.startActivity(intent);

					}

				} else {
					super.onSuccess(statusCode, headers, response);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		// TODO Auto-generated method stub
		super.onFailure(statusCode, headers, responseString, throwable);
	}

}
