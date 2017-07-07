package com.lmq.main.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.SystenmApi;
import com.lmq.main.util.Default;

/**
 * 第一次绑定银行卡
 */

public class PeopleInfo_backcard extends BaseActivity implements OnClickListener {
	private LayoutInflater mInflater;

	private Spinner mBank, mProvince, mCity;
	private EditText mOldCardNumber, mCardNumber, mBankName;

	private List<BankItem> mBankList = new ArrayList<PeopleInfo_backcard.BankItem>();
	private List<BankItem> mProvinceList = new ArrayList<PeopleInfo_backcard.BankItem>();
	private List<BankItem> mCityList = new ArrayList<PeopleInfo_backcard.BankItem>();

	private bankAdapter mBankAdapter;
	private provinceAdapter mProvinceAdapter;
	private cityAdapter mCityAdapter;

	private String strOldBankNum, str_sfz, str_dh, str_name, ip;// 卡号

	private String strBank;// 银行卡
	private String strBankNum;// 卡号
	private String strProvince;// 开户省
	private String strCity;// 城市
	private String strBranch;// 开户行
	private EditText ed_cardnumber_sx, et_sfz_sx, et_dh_sx, et_xm_sx;
	private boolean isUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_bind_bankcard);
		mInflater = LayoutInflater.from(this);

		Intent intent = getIntent();
		isUpdate = intent.getBooleanExtra("up", false);

		initView();

		dohttpBank();
	}

	public void initView() {

		ed_cardnumber_sx = (EditText) findViewById(R.id.ed_cardnumber_sx);
		et_sfz_sx = (EditText) findViewById(R.id.ed_sfz_sx);
		et_dh_sx = (EditText) findViewById(R.id.ed_dh_sx);
		et_xm_sx = (EditText) findViewById(R.id.ed_rn_sx);

		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.btn_tijiao).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(isUpdate ? R.string.update_banckcard : R.string.bind_banckcard);

		// TextView text = (TextView) findViewById(R.id.textcard);
		// text.setText(isUpdate ? R.string.new_kahao : R.string.kahao);

		// findViewById(R.id.lay1).setVisibility(isUpdate ? View.VISIBLE :
		// View.GONE);

		mCardNumber = (EditText) findViewById(R.id.ed_cardnumber);
		mBankName = (EditText) findViewById(R.id.ed_oldbank);
		mOldCardNumber = (EditText) findViewById(R.id.ed_oldcardnumber);

		mBank = (Spinner) findViewById(R.id.sp_chosebank_list);
		mProvince = (Spinner) findViewById(R.id.province_spinner);
		mCity = (Spinner) findViewById(R.id.city_spinner);

		mBankAdapter = new bankAdapter();
		mBank.setAdapter(mBankAdapter);

		mCityAdapter = new cityAdapter();
		mCity.setAdapter(mCityAdapter);

		mProvinceAdapter = new provinceAdapter();
		mProvince.setAdapter(mProvinceAdapter);
		mProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BankItem item = mProvinceList.get(arg2);
				dohttpCity(item.id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_tijiao:
			// Boolean b = SystenmApi.isWifiConnected(PeopleInfo_backcard.this);
			// ip = SystenmApi.getIPStr(PeopleInfo_backcard.this, b);// ip
			// ip=SystenmApi。GetHostIp();
			ip = SystenmApi.getLocalIpAddress();
			strBank = "" + mBank.getSelectedItem();// 银行卡
			strBankNum = ed_cardnumber_sx.getText().toString();// 卡号
			strProvince = "" + mProvince.getSelectedItem();// 开户省
			strCity = "" + mCity.getSelectedItem();// 城市
			strBranch = mBankName.getText().toString();// 开户行
			str_sfz = et_sfz_sx.getText().toString();// 身份证
			str_dh = et_dh_sx.getText().toString();// 电话
			str_name = et_xm_sx.getText().toString();// 姓名
			if (!SystenmApi.isMobileNO(str_dh)) {
				showCustomToast("请输入合法的手机号");
				return;
			}
			// if (!SystenmApi.personIdValidation(str_sfz)) {
			// showCustomToast("请输入合法的身份证");
			// return;
			// }
			doHttp();
			break;
		case R.id.back:
			finish();
			break;
		}
	}

	/**
	 * 获取银行列表 获取省份列表
	 */
	private void dohttpBank() {

		BaseHttpClient.post(getBaseContext(), Default.getBankInfo, null, new JsonHttpResponseHandler() {

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
							getBankInfo(json);
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
				showCustomToast(responseString);
			}

		});
	}

	/**
	 * 根据省份id获取 城市列表
	 *
	 * @param id
	 */
	private void dohttpCity(String id) {

		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("id", id);

		BaseHttpClient.post(getBaseContext(), Default.getCity, jsonBuilder, new JsonHttpResponseHandler() {

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
							getCityInfo(json);
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
				showCustomToast(responseString);
			}

		});

	}

	/**
	 * 解析获得数据
	 *
	 * @param json
	 */
	public void getCityInfo(JSONObject json) {
		try {
			mCityList.clear();
			JSONArray citys = json.getJSONArray("city");

			for (int i = 0; i < citys.length(); i++) {
				JSONObject temp = citys.getJSONObject(i);
				BankItem item = new BankItem(temp);
				mCityList.add(item);
			}

			mCityAdapter.notifyDataSetChanged();
		} catch (Exception e) {

		}
	}

	class BankItem {
		String id;
		String name;

		public BankItem(JSONObject json) {
			try {
				id = json.getString("id");
				name = json.getString("value");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getBankInfo(JSONObject json) {
		try {
			JSONArray banks = json.getJSONArray("bank");
			JSONArray province = json.getJSONArray("province");

			for (int i = 0; i < banks.length(); i++) {
				JSONObject temp = banks.getJSONObject(i);
				BankItem item = new BankItem(temp);
				mBankList.add(item);
			}

			for (int i = 0; i < province.length(); i++) {
				JSONObject temp = province.getJSONObject(i);
				BankItem item = new BankItem(temp);
				mProvinceList.add(item);
			}

			mBankAdapter.notifyDataSetChanged();
			mProvinceAdapter.notifyDataSetChanged();
		} catch (Exception e) {

		}
	}

	class bankAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mBankList.size();
		}

		@Override
		public Object getItem(int position) {
			BankItem item = mBankList.get(position);
			return item.name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			BankItem item = mBankList.get(position);
			name.setText(item.name);
			return convertView;
		}

	}

	class provinceAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mProvinceList.size();
		}

		@Override
		public Object getItem(int position) {
			BankItem item = mProvinceList.get(position);
			return item.name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			BankItem item = mProvinceList.get(position);
			name.setText(item.name);
			return convertView;
		}

	}

	class cityAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCityList.size();
		}

		@Override
		public Object getItem(int position) {
			BankItem item = mCityList.get(position);
			return item.name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.adapter_item_bank, null);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			BankItem item = mCityList.get(position);
			name.setText(item.name);
			return convertView;
		}

	}

	public void doHttp() {

		JsonBuilder jsonBuilder = new JsonBuilder();
		// if (isUpdate) {
		// strOldBankNum = mOldCardNumber.getText().toString();
		// jsonBuilder.put("original_debit_id", strOldBankNum);// 原银行卡
		// }

		jsonBuilder.put("bankname", strBank);// 银行卡
		jsonBuilder.put("bankcard", strBankNum);// 卡号
		jsonBuilder.put("province", strProvince);// 开户省
		jsonBuilder.put("city", strCity);// 城市
		jsonBuilder.put("bankaddress", strBranch);// 开户行
		jsonBuilder.put("idcard", str_sfz);// 身份证
		jsonBuilder.put("mobile", str_dh);// 电话
		jsonBuilder.put("realname", str_name);// 姓名
		jsonBuilder.put("ip", ip);// 姓名

		BaseHttpClient.post(getBaseContext(), Default.peoInfobindbankcard_sx, jsonBuilder,
				new JsonHttpResponseHandler() {

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
									showCustomToast("信息绑定成功");
									finish();
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
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, responseString, throwable);
						dismissLoadingDialog();
						showCustomToast(responseString);
					}

				});

	}
}
