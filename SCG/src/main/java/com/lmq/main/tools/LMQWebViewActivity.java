package com.lmq.main.tools;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.MyLog;

/**
 * 优惠券列表
 */
public class LMQWebViewActivity extends BaseActivity implements OnClickListener {

	private String title;
	private String url;
	private WebView webView;
	TextView tv;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lmq_webview_layout);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		
		tv=(TextView) findViewById(R.id.title);
		tv.setText("项目描述");
		
		Intent intent = getIntent();
		if (null != intent) {

			if (intent.hasExtra("title")) {

				title = intent.getStringExtra("title");
			}
			if (intent.hasExtra("url")) {

				url = intent.getStringExtra("url");
				url.replace("\\", "");
			}
			MyLog.e("1111111111111111111111", url);

		}

		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void initView() {


		webView = (WebView) findViewById(R.id.lmq_webView);
		webView.loadUrl(url);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setSupportMultipleWindows(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setAppCacheEnabled(true);

		List<Cookie> cookies = BaseHttpClient.getCookie();
		// DefaultHttpClient httpClient=new DefaultHttpClient();
		// httpClient.getCookieStore().getCookies();
		if (!cookies.isEmpty()) {
			CookieSyncManager.createInstance(webView.getContext());

			CookieManager cookieManager = CookieManager.getInstance();
			// cookieManager.removeSessionCookie();

			cookieManager.setAcceptCookie(true);
			cookieManager.removeAllCookie();
			// sync all the cookies in the httpclient with the webview by
			// generating cookie string
			for (Cookie cookie : cookies) {
				String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain()
						+ ";path=" + cookie.getPath();
				MyLog.e("cookieStr === >", cookieString);
				cookieManager.setCookie(url, cookieString);

				CookieSyncManager.getInstance().sync();

			}
		}

		webView.loadUrl(url);

	}

	@Override
	public void finish() {

		super.finish();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.title_right:
			finish();
			break;

		}

	}
}
