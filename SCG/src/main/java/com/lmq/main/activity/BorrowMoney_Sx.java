package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BorrowMoney_Sx extends BaseActivity implements OnClickListener {
	TextView tv_title, spinner_tv, city_sx, jl_xgtl_sx;
	// ImageView spinner_tv;
	Spinner spinner;
	EditText real_name_sx, phone_sx, sfz_sx, bor_je_sx, xx_dz_sx, yue_bor_money;
	CheckBox checkBox1_sx;
	Button submit_now_sx;
	String fbdx_str = "";

	// Button city_sx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrowmoney_sx);
		findViewById(R.id.title_right).setVisibility(View.VISIBLE);
		findViewById(R.id.title_right).setOnClickListener(this);
		city_sx = (TextView) findViewById(R.id.city_sx);
		city_sx.setOnClickListener(this);
		jl_xgtl_sx = (TextView) findViewById(R.id.jl_xgtl_sx);
		jl_xgtl_sx.setOnClickListener(this);
		submit_now_sx = (Button) findViewById(R.id.submit_now_sx);
		submit_now_sx.setOnClickListener(this);
		checkBox1_sx = (CheckBox) findViewById(R.id.checkBox1_sx);
		real_name_sx = (EditText) findViewById(R.id.real_name_sx);
		phone_sx = (EditText) findViewById(R.id.phone_sx);
		sfz_sx = (EditText) findViewById(R.id.sfz_sx);
		bor_je_sx = (EditText) findViewById(R.id.bor_je_sx);
		xx_dz_sx = (EditText) findViewById(R.id.xx_dz_sx);
		yue_bor_money = (EditText) findViewById(R.id.yue_bor_money);
		spinner_tv = (TextView) findViewById(R.id.spinner_tv);
		spinner_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText("我要借款");
		spinner = (Spinner) findViewById(R.id.spinner1);
		Clickspinner(spinner);
	}

	private void Clickspinner(Spinner spinner2) {
		// TODO Auto-generated method stub
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String[] languages = getResources().getStringArray(R.array.fbdx);
				// spinner_tv.setText(languages[position] + "");
				spinner_tv.setVisibility(View.GONE);
				fbdx_str = languages[position];
				// Toast.makeText(BorrowMoney_Sx.this, "你点击的是:" +
				// languages[position], 1000).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

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
		case R.id.spinner_tv:
			spinner.setVisibility(View.VISIBLE);
			spinner.performClick();
			break;
		case R.id.city_sx:
			Intent intent = new Intent(BorrowMoney_Sx.this, CitiesActivity.class);
			// startActivity(intent);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.light_to_dark, R.anim.dark_to_light);
			break;
		case R.id.submit_now_sx:
			doHttp();
			break;
		case R.id.jl_xgtl_sx:
			Intent intent2=new Intent(BorrowMoney_Sx.this,ShowBorrowContextActivity.class);
			intent2.putExtra("sfz", sfz_sx.getText().toString());
			intent2.putExtra("rmb", bor_je_sx.getText().toString());
			intent2.putExtra("yifang", real_name_sx.getText().toString());
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			String result = data.getStringExtra("result");
			city_sx.setText(result);
			// editText3.setText(result);
		}
	}

	// 提交到服务器
	public void doHttp() {
		if (!SystenmApi.isMobileNO(phone_sx.getText().toString())) {
			showCustomToast("请输入合法的手机号");
			return;
		}
		if (!checkBox1_sx.isChecked()) {
			showCustomToast("请阅读完相关条例");
			return;
		}
		if (fbdx_str.equals("")) {
			showCustomToast("请选择发布对象");
			return;
		}
		MyLog.e("请求服务器    上传数据", "...............2");
		// bor_je real_name_sx
		JsonBuilder jsonBuilder = new JsonBuilder();
		// jsonBuilder.put("uid", Default.userId);
		jsonBuilder.put("user_name", real_name_sx.getText().toString());
		jsonBuilder.put("user_phone", phone_sx.getText().toString());
		jsonBuilder.put("age", sfz_sx.getText().toString());
		jsonBuilder.put("money", bor_je_sx.getText().toString());// 借款金额
		jsonBuilder.put("releaseobject", fbdx_str);// 发布对象
		jsonBuilder.put("monthmomey", yue_bor_money.getText().toString());// 月收入
		jsonBuilder.put("address", city_sx.getText().toString() + xx_dz_sx.getText().toString());
		// jsonBuilder.put("city", city_sx.getText().toString());
		// jsonBuilder.put("area", xx_dz_sx.getText().toString());

		BaseHttpClient.post(this, Default.borrow_sx, jsonBuilder, new JsonHttpResponseHandler() {

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
				// MyLog.e("测试状态"+statusCode, response.toString());
				try {
					MyLog.e("我发布借款从服务器获得的数据", response.toString());
					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							showCustomToast("提交成功");
							finish();

						} else {
							showCustomToast(response.getString("message"));
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

		MyLog.e("请求服务器    上传数据", "...............3");
	}
}
