package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//活动页
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class Stretch_sx extends BaseActivity implements OnClickListener {

	WebView webView;
	ImageView iv_back;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stretch_sx);
		title = (TextView) findViewById(R.id.title);
		title.setText("活动详情");
		iv_back = (ImageView) findViewById(R.id.title_right);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.stretch_ac_sx);
		webView.setVerticalScrollbarOverlay(true);
		// 设置WebView支持JavaScript
		// webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");

		String url = "http://www.czsxcf.com/Home/appnewale/activity";
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// yz();
	}

	private class MyWebViewClient extends WebViewClient {
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			showLoadingDialogNoCancle("");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			dismissLoadingDialog();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	public void yz() {
		JsonBuilder builder = new JsonBuilder();

		// builder.put("type",11);
		builder.put("category", 0);
		// 填充参数
		BaseHttpClient.post(Stretch_sx.this, Default.syList_sx, builder, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showLoadingDialogNoCancle("");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);

				dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
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
