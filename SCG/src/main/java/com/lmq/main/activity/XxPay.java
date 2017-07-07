package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class XxPay extends BaseActivity implements OnClickListener {
	TextView title, XX_one, xx_submit, XX_two, XX_thr;
	ImageView imageview;
	RadioGroup radioGroupCricket;
	RadioButton radioBtn1, radioBtn2, radioBtn3;
	Map<String, Object> map;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	EditText xx_czje, xx_lsdzh, xx_czfs;
	int ss = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xxpay);
		title = (TextView) findViewById(R.id.title);
		title.setText("线下充值");
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);
		XX_one = (TextView) findViewById(R.id.XX_one);
		XX_two = (TextView) findViewById(R.id.XX_two);
		XX_thr = (TextView) findViewById(R.id.XX_thr);
		xx_submit = (TextView) findViewById(R.id.xx_submit);
		xx_submit.setOnClickListener(this);
		radioGroupCricket = (RadioGroup) findViewById(R.id.radioGroup1);
		xx_czje = (EditText) findViewById(R.id.xx_czje);
		xx_lsdzh = (EditText) findViewById(R.id.xx_lsdzh);
		xx_czfs = (EditText) findViewById(R.id.xx_czfs);
		radioBtn1 = (RadioButton) findViewById(R.id.radioGroupButton0);
		radioBtn2 = (RadioButton) findViewById(R.id.radioGroupButton1);
		radioBtn3 = (RadioButton) findViewById(R.id.radioGroupButton2);

		radioGroupCricket.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (radioBtn1.getId() == checkedId) {
					ss = 1;
				} else if (radioBtn2.getId() == checkedId) {
					ss = 2;
				} else if (radioBtn3.getId() == checkedId) {
					ss = 3;
				}
				// Toast.makeText(XxPay.this, "" + ss,
				// Toast.LENGTH_SHORT).show();
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
		case R.id.xx_submit:
			// EditText xx_czje, xx_lsdzh, xx_czfs;
			// int selected = radioGroupCricket.getCheckedRadioButtonId();
			// radioBtn1 = (RadioButton) findViewById(selected);
			// int sid = getTypeFromSelectId(selected);
			xx_czje.getText().toString();
			xx_lsdzh.getText().toString();
			xx_czfs.getText().toString();
			// int tran_id,String money_off,String bank,String off_way

			submitFrBankCard(ss, xx_czje.getText().toString(), xx_lsdzh.getText().toString(),
					xx_czfs.getText().toString());

			// Log.e("radioBtn1", selected + "");
			// Toast.makeText(XxPay.this, "" + ss, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	public int getTypeFromSelectId(int selected) {
		int ss = 1;
		switch (selected) {
		case 2131034168:
			ss = 1;
			break;
		case 2131034170:
			ss = 2;
			break;
		case 2131034172:
			ss = 3;
			break;
		default:
			break;
		}
		return ss;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getFrBankCard();
	}

	/**
	 * 获取公司法人银行卡信息
	 */

	public void getFrBankCard() {

		BaseHttpClient.post(XxPay.this, Default.offinecharge, null, new JsonHttpResponseHandler() {

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
						if (response.getInt("status") == 1) {
							MyLog.e("获取提现的信息", response.toString());
							// 获取到用户绑定银行卡信息
							// decoidTheUesrCardInfo(response);
							getBankCard(response);
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
				showCustomToast(responseString);
			}
		});

	}

	/**
	 * 上传线下充值信息
	 */

	public void submitFrBankCard(int bank, String money_off, String tran_id, String off_way) {

		JsonBuilder builder = new JsonBuilder();
		builder.put("money_off", money_off);// 金额
		builder.put("bank", bank);// 银行123
		builder.put("tran_id", tran_id);// 流水单号
		builder.put("off_way", off_way);// 充值方式
		BaseHttpClient.post(XxPay.this, Default.offinepay, builder, new JsonHttpResponseHandler() {

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
						if (response.getInt("status") == 1) {
							MyLog.e("获取提现的信息", response.toString());
							// 获取到用户绑定银行卡信息
							// decoidTheUesrCardInfo(response);
							showCustomToast(response.getString("message"));
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
				showCustomToast(responseString);
			}
		});

	}

	protected void getBankCard(JSONObject response) {
		// TODO Auto-generated method stub
		list.clear();
		try {
			JSONArray jsonArray = response.getJSONArray("BANK");
			for (int i = 0; i < jsonArray.length(); i++) {
				map = new HashMap<String, Object>();
				JSONObject jb = (JSONObject) jsonArray.get(i);
				map.put("bank", jb.optString("bank"));
				map.put("payee", jb.optString("payee"));
				map.put("account", jb.optString("account"));
				map.put("address", jb.optString("address"));
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		radioBtn1 = (RadioButton) findViewById(R.id.radioGroupButton0);
		radioBtn1.setText(list.get(0).get("bank").toString() + "	开户名:" + list.get(0).get("payee").toString());
		radioBtn2 = (RadioButton) findViewById(R.id.radioGroupButton1);
		radioBtn2.setText(list.get(1).get("bank").toString() + "	开户名:" + list.get(1).get("payee").toString());
		radioBtn3 = (RadioButton) findViewById(R.id.radioGroupButton2);
		radioBtn3.setText(list.get(2).get("bank").toString() + "	开户名:" + list.get(2).get("payee").toString());
		XX_one.setText(
				"账户:" + list.get(0).get("account").toString() + "\n开户地址:" + list.get(0).get("address").toString());
		XX_two.setText(
				"账户:" + list.get(1).get("account").toString() + "\n开户地址:" + list.get(1).get("address").toString());
		XX_thr.setText(
				"账户:" + list.get(2).get("account").toString() + "\n开户地址:" + list.get(2).get("address").toString());
	}
}
