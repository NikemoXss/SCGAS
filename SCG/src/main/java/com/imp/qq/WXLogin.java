package com.imp.qq;

import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.LocationApplication;
import com.czscg.R;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WXLogin extends BaseActivity {
	private static final String APP_ID = "wxc03c6d2f5324e25e";
	// private static IWXAPI wxapi=LocationApplication.wxapi;
	Button btn_wxlogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wxlogin);
		btn_wxlogin = (Button) findViewById(R.id.btn_wxlogin);
		btn_wxlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regToWx();
			}
		});

	}

	private void regToWx() {
		// TODO Auto-generated method stub
		LocationApplication.wxapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
		LocationApplication.wxapi.registerApp(APP_ID);
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		LocationApplication.wxapi.sendReq(req);
	}

}
