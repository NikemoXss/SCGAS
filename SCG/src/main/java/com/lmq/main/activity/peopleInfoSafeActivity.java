/**
 *
 */
package com.lmq.main.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.util.Data;

/**
 * @author zzx
 */
public class peopleInfoSafeActivity extends BaseActivity {
	private String mReal_name;
	private String mSafequestion_status;
	private String mPin_pass;
	private String mUser_phone;
	private String mUser_email;
	private String mDenglu_pass;
	private TextView mInfo[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_safe);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		updateInfo(Data.peopleSafeJson);

		mInfo = new TextView[6];
		mInfo[0] = (TextView) findViewById(R.id.smrz);
		mInfo[1] = (TextView) findViewById(R.id.dlmm);
		mInfo[2] = (TextView) findViewById(R.id.zfmm);
		mInfo[3] = (TextView) findViewById(R.id.aqwt);
		mInfo[4] = (TextView) findViewById(R.id.sjhm);
		mInfo[5] = (TextView) findViewById(R.id.dzyx);

		mInfo[0].setText(mReal_name);
		mInfo[1].setText(mDenglu_pass);
		mInfo[2].setText(mPin_pass);
		mInfo[3].setText(mSafequestion_status);
		mInfo[4].setText(mUser_phone);
		mInfo[5].setText(mUser_email);
	}

	public void updateInfo(JSONObject json) {
		if (json == null) {
			return;
		}
		try {
			// 实名认证
			mReal_name = json.getString("real_name");
			// 安全问题
			mSafequestion_status = json.getString("safequestion_status");
			// 支付密码
			mPin_pass = json.getString("pin_pass");
			// 手机号码
			mUser_phone = json.getString("user_phone");
			// 电子邮箱
			mUser_email = json.getString("user_email");
			// 登录密码
			mDenglu_pass = json.getString("denglu_pass");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
