package com.lmq.menu;

import org.apache.http.Header;
import org.json.JSONObject;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.AboutUsActivity;
import com.lmq.main.activity.ContactUsActivity;
import com.lmq.main.activity.FeedbackActivity;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
//苏常设置
public class SetFragment_Scg extends BaseFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.layout_set_sx, null);
		mainView.findViewById(R.id.item_feedback).setOnClickListener(this);
		mainView.findViewById(R.id.item_check_update).setOnClickListener(this);
		mainView.findViewById(R.id.item_shareapp).setOnClickListener(this);
		mainView.findViewById(R.id.item_contactus).setOnClickListener(this);
		mainView.findViewById(R.id.item_aboutus).setOnClickListener(this);
		return mainView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void stop() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_feedback:
			// 检测用户是否登录
			if (Default.userId != 0) {
				getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
			} else {
				getActivity().startActivity(new Intent(getActivity(), loginActivity.class));
			}
			break;
		case R.id.item_check_update:
			checkNewVersion();
			break;
		case R.id.item_shareapp:
			SystenmApi.showShareView(getActivity(), "苏常网APP，随时随地掌握你的财富", "手机移动理财的指尖神器，帮您在“拇指时代”指点钱途，“掌握财富”。",
					"http://www.czsuchang.com/Member/Common/AppRegister?invite="+Default.userId);//http://www.czsuchang.com/Member/Common/AppRegister?invite=881
			break;
		case R.id.item_contactus:
			Intent intent = new Intent(getActivity(), ContactUsActivity.class);
			startActivity(intent);
			break;
		case R.id.item_aboutus:
			Intent intent1 = new Intent(getActivity(), AboutUsActivity.class);
			startActivity(intent1);
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
				Log.e("onSuccess", statusCode + response.toString());
				try {

					if (statusCode == 200) {
						// 没有新版本
						if (response.getInt("status") == 1) {
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
				// showCustomToast(responseString);
			}

		});

	}

}
