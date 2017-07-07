package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;

public class itemInfo3_2Activity extends BaseActivity implements OnClickListener {
	// private LinearLayout mLayout;
	private TextView mTextView[];
	private long tv_account_money;

	// private EditText mEditdx;

	private EditText mEdittb;
	private EditText mEditzf;

	// 定向标Flag
	private boolean mShowDx;

	private TextView tv_addtime;
	private TextView tv_borrow_name;

	// 定向标密码
	private TextView dxTextView;
	// 输入定向标
	private EditText dxpsswdEdit;

	private long id;
	private int type;

	private String messageInfo;
	private itemInfo2Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tz_item2_2);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("投标支付");
		handler = new itemInfo2Handler();

		Intent intent = getIntent();
		if (intent.getLongExtra("id", 0) != 0 && intent.getIntExtra("type", 0) != 0) {

			id = intent.getLongExtra("id", 0);
			type = intent.getIntExtra("type", 0);

		}

		initView();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.enter_money).setOnClickListener(this);

		mTextView = new TextView[4];
		mTextView[0] = (TextView) findViewById(R.id.kyye);
		mTextView[1] = (TextView) findViewById(R.id.zxtbje);
		mTextView[2] = (TextView) findViewById(R.id.zdtbje);// 最大投标
		mTextView[3] = (TextView) findViewById(R.id.hxjk);

		// mEditdx = (EditText) findViewById(R.id.edit_dxpass);
		mEdittb = (EditText) findViewById(R.id.edit_money);
		mEditzf = (EditText) findViewById(R.id.edit_pass);

		dxTextView = (TextView) findViewById(R.id.dx_textView);
		dxpsswdEdit = (EditText) findViewById(R.id.dxb_pwd);

		tv_addtime = (TextView) findViewById(R.id.addtime);// 时间
		tv_borrow_name = (TextView) findViewById(R.id.borrow_name);// 借款名称
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		doHttp();
	}

	public void updateInfo(JSONObject json) {
		try {
			mShowDx = json.getInt("password") == 1;
			if (json.has("addtime")) {
				tv_addtime.setText(json.getString("addtime"));
			}
			if (json.has("borrow_name")) {
				tv_borrow_name.setText(json.getString("borrow_name"));
			}
			// mTextView[0].setText(json.getLong("account_money") + "元");
			tv_account_money = json.getLong("account_money");
			mTextView[0].setText(SystenmApi.getMoneyInfo(tv_account_money) + "元");

			mTextView[1].setText(json.getDouble("borrow_min") + "元");

			// mTextView[2].setText(json.getString("borrow_max") + "元");
			if (json.getString("borrow_max").equals("无")) {
				mTextView[2].setText("无限制");
			} else {
				mTextView[2].setText(json.getString("borrow_max") + "元");
			}

			mTextView[3].setText(json.getDouble("need") + "元");
			if (json.has("toubiao")) {
				mEdittb.setText(json.getDouble("toubiao") + "");
			}
			id = json.getLong("id");
			type = json.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mShowDx) {
			dxpsswdEdit.setVisibility(View.VISIBLE);
			dxTextView.setVisibility(View.VISIBLE);
		} else {
			// mLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.enter_money:
			doHttpCheck();
			break;
		}
	}

	public void doHttpCheck() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("borrow_id", id);
		builder.put("type", type);
		builder.put("pin", mEditzf.getText().toString());
		builder.put("money", mEdittb.getText().toString());

		if (mShowDx) {
			builder.put("borrow_pass", dxpsswdEdit.getText().toString());
		}

		BaseHttpClient.post(getBaseContext(), Default.tzListItem3, builder, new JsonHttpResponseHandler() {

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
					MyLog.e("123", "确认支付之后返回参数---" + json.toString());
					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							Message message = new Message();
							message.arg1 = 1;
							handler.sendMessage(message);
							messageInfo = json.getString("message");
						} else {
							showCustomToast(json.getString("message"));
							finish();
						}
					} else {
						showCustomToast(R.string.toast1);
						finish();
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

	public void showCheckDialog(String messageInfo) {
		AlertDialog.Builder builder = new Builder(itemInfo3_2Activity.this);
		builder.setTitle("友情提示");
		builder.setMessage(messageInfo);

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Message message = new Message();
				message.arg1 = 2;
				handler.sendMessage(message);
			}
		});
		builder.create().show();

	}

	private void doHttp() {

		JsonBuilder builder = new JsonBuilder();
		builder.put("id", id);
		builder.put("type", type);
		MyLog.e("123", "传递数据参数..." + builder.toJsonString().toString());

		BaseHttpClient.post(getBaseContext(), Default.tzListItem2, builder, new JsonHttpResponseHandler() {

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
					MyLog.e("123", json.toString());
					if (statusCode == 200) {
						if (json.getInt("status") == 1) {
							updateInfo(json);
						} else {
							showCustomToast(json.getString("message"));
							finish();
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
			}

		});

	}

	public void doHttpMoney() {
		JsonBuilder builder = new JsonBuilder();
		builder.put("borrow_id", id);
		builder.put("type", type);
		builder.put("pin", mEditzf.getText().toString());
		builder.put("money", mEdittb.getText().toString());
		if (mShowDx) {
			builder.put("borrow_pass", dxpsswdEdit.getText().toString());
		}

		BaseHttpClient.post(getBaseContext(), Default.tzListItem4, builder, new JsonHttpResponseHandler() {

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
							{
								Message message = new Message();
								message.arg1 = 3;
								handler.sendMessage(message);
								messageInfo = json.getString("message");
							}
						} else {
							showCustomToast(json.getString("message"));
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
			}

		});

	}

	public void showMoneyDialog(String messageInfo) {
		AlertDialog.Builder builder = new Builder(itemInfo3_2Activity.this);
		builder.setTitle("友情提示");
		builder.setMessage(messageInfo);

		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.create().show();
	}

	class itemInfo2Handler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 1:
				showCheckDialog(messageInfo);
				break;
			case 2:
				doHttpMoney();
				break;
			case 3:
				showMoneyDialog(messageInfo);
				break;
			case 4:
				showCustomToast(msg.getData().getString("info"));
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int code = data.getIntExtra("code", -1);
		String message = data.getStringExtra("message");
		int code1 = data.getIntExtra("code1", -1);
		String message1 = data.getStringExtra("message1");
		int code2 = data.getIntExtra("code2", -1);
		String message2 = data.getStringExtra("message2");
		if (code == 88) {
			Toast.makeText(this, "恭喜您，投标成功！", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, code + ":" + message, Toast.LENGTH_SHORT).show();
		}
	}
}
