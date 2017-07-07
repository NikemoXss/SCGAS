package com.lmq.pay;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.fuiou.pay.FyPay;
import com.fuiou.pay.activity.RequestOrder;
import com.fuiou.pay.util.AppConfig;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.util.Default;

/***
 * 我要充值 富友充值
 */
public class FyPayActivity extends BaseActivity implements OnClickListener {
	public RequestOrder requestOrder;
	public EditText user_pay;
	TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.peopleinfo_pay);
		user_pay = (EditText) findViewById(R.id.user_pay_amount);
		requestOrder = new RequestOrder(this);
		findViewById(R.id.user_pay_btn).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("线上充值");
		FyPay.init(FyPayActivity.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.user_pay_btn:
			getPayInfo();
			break;

		default:
			break;
		}
	}

	public void update(JSONObject json) {
		try {

			String MCHNTCD = json.optString("MCHNTCD");// 商户号
			String MCHNTORDERID = json.optString("MCHNTORDERID");// 商户订单号
			String USERID = json.optString("USERID");// 用户id
			String AMT = json.optString("AMT");// 订单金额
			String BANKCARD = json.optString("BANKCARD");// 银行卡号
			String BACKURL = json.optString("BACKURL");// 支付结果回调地址
			String NAME = json.optString("NAME");// 姓名
			String IDNO = json.optString("IDNO");// 证件号
			String IDTYPE = json.optString("IDTYPE");// 证件类型
			String SIGNTP = json.optString("SIGNTP");// 签名类型(MD5)
			String SIGN = json.optString("SIGN");// 签名
			String VERSION = json.optString("VERSION");// 版本号定值
			String TYPE = json.optString("TYPE");// 交易类型

			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_CD, MCHNTCD);// 商户号
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_ORDER_ID,// 商户订单号
					MCHNTORDERID);
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_USER_ID, USERID);// 用户id
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_AMT, AMT);// 订单金额
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_BANK_NUMBER, BANKCARD);// 银行卡号
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_BACK_URL, BACKURL);// 支付结果回调地址
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_USER_NAME, NAME);// 姓名
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_USER_IDNU, IDNO);// 证件号
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_USER_IDCARD_TYPE, IDTYPE);// 证件类型
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_SDK_SIGNTP, SIGNTP);// 签名类型(MD5)
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_SING_KEY, SIGN);// 签名
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_SDK_TYPE, TYPE);// 交易类型
			AppConfig.setData(FyPayActivity.this, AppConfig.MCHNT_SDK_VERSION, VERSION);// 版本号定值

			requestOrder.Request();

		} catch (Exception e) {

		}
	}

	public void getPayInfo() {
		JsonBuilder builder = new JsonBuilder();

		builder.put("money", user_pay.getText().toString());

		BaseHttpClient.post(getBaseContext(), Default.peopleinfoPay_fy, builder, new JsonHttpResponseHandler() {

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

				dismissLoadingDialog();
				try {
					if (json.optInt("status") == 1) {
						update(json);
					} else {
						showCustomToast(json.optString("message"));
					}
				} catch (Exception e) {
					showCustomToast(R.string.toast1);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				showCustomToast(responseString);
				dismissLoadingDialog();
			}

		});

	}

	/**
	 * 返回订单相关信息(手动退出)
	 */
	@Override
	protected void onStart() {
		Log.i("zls", "应答码：" + AppConfig.getData(this, AppConfig.RSP_CODE));
		Log.i("zls", "描述：" + AppConfig.getData(this, AppConfig.RSP_DESC));
		/**
		 * 请求发送成功的返回数据 发起支付
		 */
		Log.i("zls", "发送成功请求的返回数据：" + AppConfig.getData(this, AppConfig.RSP_SDK_DATA));
		String s = AppConfig.getData(this, AppConfig.RSP_SDK_DATA);
		if (s != null && !"".equals(s)) {
			String code = s.substring(s.indexOf("<RESPONSECODE>") + "<RESPONSECODE>".length(), s.indexOf("</RESPONSECODE>"));
			if (code.equals("0000")) {
				Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				String message = s.substring(s.indexOf("<RESPONSEMSG>") + "<RESPONSEMSG>".length(), s.indexOf("</RESPONSEMSG>"));
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
		}
		reset();
		super.onStart();
	}

	/**
	 * 清除数据
	 */
	private void reset() {
		AppConfig.setData(this, AppConfig.RSP_CODE, "");
		AppConfig.setData(this, AppConfig.RSP_DESC, "");
		AppConfig.setData(this, AppConfig.RSP_SDK_DATA, "");
	}

}
