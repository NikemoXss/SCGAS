package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

public class ShowRegisterContextActivity extends BaseActivity implements OnClickListener {

	private WebView webview;
	ImageView iv;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_register_context);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.title);
		tv.setText("注册协议");
//		dohttp();

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
