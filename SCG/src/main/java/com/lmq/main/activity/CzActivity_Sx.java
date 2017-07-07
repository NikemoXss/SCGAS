package com.lmq.main.activity;

import com.lmq.main.api.BaseActivity;
import com.czscg.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CzActivity_Sx extends BaseActivity implements OnClickListener {
	TextView tv;
	ImageView iv;
	RadioGroup radiogroup_sx;
	RadioButton radiobtn_zfb, radiobtn_wx;
	private int mcheckedId = R.id.radio_wx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.czlayout_sx);
		tv = (TextView) findViewById(R.id.title);
		tv.setText("充值");
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		radiogroup_sx = (RadioGroup) findViewById(R.id.radioGroup_sx);
		radiobtn_zfb = (RadioButton) findViewById(R.id.radio_zfb);
		radiobtn_wx = (RadioButton) findViewById(R.id.radio_wx);
		radiobtn_wx.setChecked(true);
		findViewById(R.id.submit_qr_sx).setOnClickListener(this);
		radiogroup_sx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				mcheckedId=checkedId;
				switch (checkedId) {
				case R.id.radio_zfb:
					Toast.makeText(CzActivity_Sx.this, "您选择了支付宝充值", 1).show();
					break;
				case R.id.radio_wx:
					Toast.makeText(CzActivity_Sx.this, "您选择了微信充值", 1).show();
					break;
				default:
					break;
				}
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
		case R.id.submit_qr_sx:
			switch (mcheckedId) {
			case R.id.radio_zfb:
				Toast.makeText(CzActivity_Sx.this, "您选择了支付宝充值", 1).show();
				break;
			case R.id.radio_wx:
				Toast.makeText(CzActivity_Sx.this, "您选择了微信充值", 1).show();
				CallWxApi();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void CallWxApi() {
		// TODO Auto-generated method stub
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(CzActivity_Sx.this, null);
		
		// 将该app注册到微信
		msgApi.registerApp("wxd930ea5d5a258f4f");

	}

}
