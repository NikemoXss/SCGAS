package com.lmq.main.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowBorrowContextActivity extends BaseActivity implements OnClickListener {

	private WebView webview;
	ImageView iv;
	TextView tv;
	TextView jkbh_sx, yfjkr_sx, sfzh_sx, shxydm_sx, rmb_sx, jkqx_sx, yifang_sx, riqi_sx;
	String sfz, rmb, yifang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_borrow_context);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.title);
		tv.setText("借款协议");
		Intent intent = getIntent();
		sfz = intent.getStringExtra("sfz");
		rmb = intent.getStringExtra("rmb");
		yifang = intent.getStringExtra("yifang");
		// dohttp();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		riqi_sx = (TextView) findViewById(R.id.riqi_sx);// 借款编号
		jkbh_sx = (TextView) findViewById(R.id.jkbh_sx);// 借款编号
		yfjkr_sx = (TextView) findViewById(R.id.yfjkr_sx);// 乙方借款人
		sfzh_sx = (TextView) findViewById(R.id.sfzh_sx);// 身份证
		shxydm_sx = (TextView) findViewById(R.id.shxydm_sx);// 统一代码
		rmb_sx = (TextView) findViewById(R.id.rmb_sx);// 金融
		jkqx_sx = (TextView) findViewById(R.id.jkqx_sx);// 借款期限
		yifang_sx = (TextView) findViewById(R.id.yifang_sx);// 乙方
		jkbh_sx.setText("SDX" + Default.userId + "" + System.currentTimeMillis());
		yfjkr_sx.setText(yifang);
		sfzh_sx.setText(sfz);
		rmb_sx.setText(rmb+"元");
		yifang_sx.setText(yifang);
		jkqx_sx.setText("70天");
		riqi_sx.setText("日期："+getDate());
	}

	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(new Date());
	}

	/**
	 * 获取数据
	 */
	private void dohttp() {

		JsonBuilder builder = new JsonBuilder();
		BaseHttpClient.post(getBaseContext(), Default.GET_REGISTER_CONTEXT, builder, new JsonHttpResponseHandler() {

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
				try {

					if (statusCode == 200) {

						if (response != null) {
							if (response.has("message")) {
								String html = "<html><head><style type='text/css'>p{text-left: center;border-style:"
										+ " none;border-top-width: 2px;border-right-width: 2px;border-bottom-width: 2px;border-left-width: 2px;}"
										+ "img{height:auto;width: auto;width:100%;}</style></head><body>"
										+ response.getString("message") + "</body></html>";

								webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
							}
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				dismissLoadingDialog();
				// stop();
				// getPhoneFlagBtn.setEnabled(true);
				// getPhoneFlagBtn.setText("获取验证码");

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		default:
			break;
		}

	}

}
