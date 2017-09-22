package com.lmq.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;

import org.apache.http.Header;
import org.json.JSONObject;

public class registerActivity extends BaseActivity implements OnClickListener {
	private EditText mPassw, mPassw2, mPhone, mPhoneNum, register_people;
	private String info[];
	private TimeCount time;
	/**
	 * 注册同意协议 按钮
	 */
	private Button mSendPhoneNum;
	private boolean mRemember = true;
	TextView title;
	ImageView iv;
	RelativeLayout head_re;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setBackgroundResource(R.drawable.back);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("");
		head_re=(RelativeLayout) findViewById(R.id.head_re);
		head_re.setBackgroundResource(R.color.white);
		findViewById(R.id.show_context).setOnClickListener(this);


		mSendPhoneNum = (Button) findViewById(R.id.sendphonenum);
		mSendPhoneNum.setOnClickListener(this);

		// TextView text = (TextView) findViewById(R.id.title);
		// text.setText(R.string.register);

		mPassw = (EditText) findViewById(R.id.editpassw);
		mPassw2 = (EditText) findViewById(R.id.editpassw2);
		mPhone = (EditText) findViewById(R.id.editem_phone);
		mPhoneNum = (EditText) findViewById(R.id.editem_phonenum);
		register_people = (EditText) findViewById(R.id.editem_people);

		time = new TimeCount(60000, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			info = getInfo();
			if (info == null) {
				return;
			}
			doHttp();
			break;
		case R.id.title_right:
			finish();
			time.cancel();
			break;
		case R.id.sendphonenum:
			String phone = mPhone.getText().toString();
			if (phone.equals("")) {
				showCustomToast(R.string.whitephone);
				return;
			}
			if (!SystenmApi.isMobileNO(phone)) {
				showCustomToast("请输入合法的手机号");
				return;
			}
			time.start();
			doHttpSendPhone(phone);
			break;
		case R.id.show_context:

			startActivity(new Intent(registerActivity.this, ShowRegisterContextActivity.class));
			break;
		}

	}

	public String[] getInfo() {
		String name = mPhone.getText().toString();
		String passw = mPassw.getText().toString();
		String passw2 = mPassw2.getText().toString();
		String phone = mPhone.getText().toString();
		String phonenum = mPhoneNum.getText().toString();
		String em_people = register_people.getText().toString();
		if (name.equals("")) {
			showCustomToast(R.string.toast12);
			return null;
		} else if (SystenmApi.ByteLenth(name) < 4 || SystenmApi.ByteLenth(name) > 20) {
			showCustomToast("4-20个字母、数字、汉字、下划线");
			return null;
		}
		if (phone.equals("")) {
			showCustomToast(R.string.toastphone);

			return null;
		}

		if (phonenum.equals("")) {
			showCustomToast(R.string.toastyzm);

			return null;
		}

		if (passw.equals("")) {
			showCustomToast(R.string.toast14);

			return null;
		} else if (passw.length() < 6) {
			showCustomToast(R.string.toast15);
			return null;
		} else if (passw.length() > 16) {
			showCustomToast(R.string.toast16);
			return null;
		}
		/*
		 * if (email.equals("")) {
		 * showCustomToast(R.string.toast17);
		 * return null;
		 * } else {
		 * if (email.indexOf("@") < 0) {
		 * showCustomToast(R.string.toast18);
		 * return null;
		 * }
		 * if (!SystenmApi.isEmail(email)) {
		 * showCustomToast(R.string.toast18);
		 * return null;
		 * }
		 * }
		 */

		if (!passw.equals(passw2)) {
			showCustomToast(R.string.toast19);
			return null;
		}

		if (!mRemember) {
			showCustomToast("同意注册协义才可以注册");
			return null;
		}

		return new String[] { name, passw, phone, phonenum, em_people };
	}

	public void doHttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("name", info[0]);
		builder.put("password", info[1]);
		builder.put("tel", info[2]);
		builder.put("tel2", info[3]);
		builder.put("register_rec", info[4]);

		BaseHttpClient.post(getBaseContext(), Default.register, builder, new JsonHttpResponseHandler() {

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
						if (json.getInt("status") == 1) {
							/*
							 * Message msg = new Message();
							 * msg.arg1 = 4;
							 * Bundle bundel = new Bundle();
							 * bundel.putString("info", getResources()
							 * .getString());
							 * msg.setData(bundel);
							 * handler.sendMessage(msg);
							 */
							showCustomToast(R.string.toast5);

							Intent intent = new Intent();
							intent.putExtra("name", info[0]);
							intent.putExtra("password", info[1]);

							Default.userId = json.getLong("uid");

							setResult(RESULT_OK, intent);
							finish();
						} else {
							showCustomToast(json.getString

							("message"));
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

	public void doHttpSendPhone(String phone) {

		JsonBuilder builder = new JsonBuilder();
		builder.put("phone", phone);

		BaseHttpClient.post(getBaseContext(), Default.registerPhone, builder, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				showLoadingDialogNoCancle(getResources().getString(R.string.toast2));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				super.onSuccess(statusCode, headers, json);
				dismissLoadingDialog();
				try {
					String msg = json.getString("message");
					showCustomToast(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				dismissLoadingDialog();
				showCustomToast(responseString);

			}

		});

	}

	/* 倒计时类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {
			mSendPhoneNum.setText("发送");
			mSendPhoneNum.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mSendPhoneNum.setClickable(false);
			mSendPhoneNum.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
