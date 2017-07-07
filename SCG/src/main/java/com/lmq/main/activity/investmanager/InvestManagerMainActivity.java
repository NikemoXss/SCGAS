package com.lmq.main.activity.investmanager;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.util.Default;

/**
 * 投资管理
 *
 * @author sunjianchao
 */
public class InvestManagerMainActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inverst_manager_layout);

		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.invest_manager);

		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.invest_manager_sb).setOnClickListener(this);
		// findViewById(R.id.invest_manager_qyzt).setOnClickListener(this);
		// findViewById(R.id.invest_manager_dtb).setOnClickListener(this);
		// findViewById(R.id.invest_manager_zqzr).setOnClickListener(this);
		// findViewById(R.id.invest_manager_zdtb).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back:
			finish(0);
			break;

		case R.id.invest_manager_sb:
			// TODO 散标界面跳转
			intent.setClass(InvestManagerMainActivity.this, InvestManagerStandardActivity.class);
			startActivity(intent);
			break;
		// case R.id.invest_manager_qyzt:
		// //TODO 企业直投界面跳转
		// intent.setClass(InvestManagerMainActivity.this,InvestManagerDirectlyActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.invest_manager_dtb:
		// //TODO 定投宝界面跳转
		// // intent.setClass(InvestManagerMainActivity.this,InvestManagerActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.invest_manager_zqzr:
		// //TODO 债券转让界面跳转
		// intent.setClass(InvestManagerMainActivity.this,InvestManagerDebetActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.invest_manager_zdtb:
		// //TODO 自动投标界面跳转
		// intent.setClass(InvestManagerMainActivity.this,AutoTenderActivity.class);
		// startActivity(intent);
		// break;

		}
	}

	// 提交用户反馈到服务器
	public void checkNewVersion() {

		BaseHttpClient.post(getBaseContext(), Default.version, null, new JsonHttpResponseHandler() {

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
						// 没有新版本
						if (json.getInt("status") == 1) {
							// initData(json);
							// 获取新版本
							showCustomToast(json.getString("message"));
						} else if (json.getInt("status") == 0) {
							// 另起后台线程 下载新版APP

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

	@Override
	public void finish() {
		super.finish();
	}

}
