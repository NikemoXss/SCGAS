/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.czscg.wxapi;

import org.apache.http.Header;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.LocationApplication;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
//import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LocationApplication.wxapi.handleIntent(getIntent(), WXEntryActivity.this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		LocationApplication.wxapi.handleIntent(intent, WXEntryActivity.this);
	}

	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(WXEntryActivity.this, "+++BaseResp+++" + arg0.getType(), 1).show();
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		// bundle = getIntent().getExtras();
		// SendAuth.Resp resp = new SendAuth.Resp(bundle);

		// 获取到code之后，需要调用接口获取到access_token
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			// String code = resp.code;
			String code = ((SendAuth.Resp) resp).code;
			// if(LocationApplication.wxapi.){
			//
			// } else{
			// WXEntryActivity. this.finish();
			// }
			Toast.makeText(WXEntryActivity.this, "BaseResp+++" + code, 1).show();

			getToken(code);
		} else {
			WXEntryActivity.this.finish();
		}
	}

	private void getToken(String code) {
		// {
		// "access_token":"ACCESS_TOKEN",
		// "expires_in":7200,
		// "refresh_token":"REFRESH_TOKEN",
		// "openid":"OPENID",
		// "scope":"SCOPE"
		// }

		// JsonBuilder builder = new JsonBuilder();
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxc03c6d2f5324e25e&secret=ba1ad5154d1b7ad671b16cfbe44790fb&code="
				+ code + "&grant_type=authorization_code";
		BaseHttpClient.getL(WXEntryActivity.this, url, null, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				// showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, json);
				Toast.makeText(WXEntryActivity.this, "getToken+onSuccess++" + json.toString(), 1).show();

				try {
					getUserInfo(json.optString("access_token"), json.optString("openid"));
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(WXEntryActivity.this, "getToken+Exception++" + e.toString(), 1).show();
				}
				// dismissLoadingDialog();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				// dismissLoadingDialog();
				Toast.makeText(WXEntryActivity.this, "getToken+onFailure++" + responseString.toString(), 1).show();
				// showCustomToast(responseString);
			}

		});

	}

	protected void getUserInfo(final String token, final String openID) {
		// TODO Auto-generated method stub
//		{ 
//			"openid":"OPENID",
//			"nickname":"NICKNAME",
//			"sex":1,
//			"province":"PROVINCE",
//			"city":"CITY",
//			"country":"COUNTRY",
//			"headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
//			"privilege":[
//			"PRIVILEGE1", 
//			"PRIVILEGE2"
//			],
//			"unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
//
//			}
		String uil1 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openID;
		BaseHttpClient.getL(WXEntryActivity.this, uil1, null, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				Toast.makeText(WXEntryActivity.this, token + "+getUserInfo+onStart++" + openID, 1).show();
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				Toast.makeText(WXEntryActivity.this, "getUserInfo+onSuccess+++" + response.toString(), 1).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.makeText(WXEntryActivity.this, "getUserInfo+onFailure+++" + responseString.toString(), 1).show();
			}
		});
	}

}
