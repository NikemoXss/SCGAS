package com.lmq.main.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

public class myjiekuan extends BaseActivity implements OnClickListener {
	private String user_name;
	// 用户名
	private String user_phone;
	// 用户手机号
	private String age;
	// 年龄
	private String Yongtu;
	// 贷款用途
	private String money;
	// 省份
	private String province;
	// 城市
	private String city;
	// 县区
	private String area;

	//
	// private String kindStr;

	// 标题
	// private String l_title;

	// 借款金额
	// private String amount;
	//
	// // 年利率
	// private String interest_rate;
	//
	// // 借款时间
	// private String borrow_time;
	//
	// // 还款类型
	// private String pay_kind = "1";
	// // 借款用途
	// private String borrow_use = "1";
	//
	// // 是否定向标
	// private String direct_flag = "0";
	//
	// // 是否有奖励
	// private String reward_kind = "0";
	//
	// private String repay_due_unit = "2";
	//
	// // 定向标密码
	// private String direct_pwd_str;
	//
	// // 奖励百分比
	// private String rewrad_point_str;
	// // 借款描述
	// private String borrow_info;
	// // 奖励金额

	// 组件初始化

	private EditText username_teTextView = null;
	private EditText user_phone_teTextView = null;
	private EditText age_teTextView = null;
	private EditText Yongtu_teTextView = null;
	private EditText money_teTextView = null;
	private EditText province_teTextView = null;
	private EditText city_teTextView = null;
	private EditText area_teTextView = null;

	//
	// private RadioGroup rewrad_btn = null;
	//
	// private Button change_unit_btn = null;
	// private Spinner pay_kind_sp = null;
	// private Spinner pay_kind_sp2 = null;
	// private Spinner pay_kind_sp3 = null;

	// 奖励金额
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myjiekuang);
		findViewById(R.id.back).setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我要借款");
		initViews();

	}

	protected void initViews() {
		// TODO Auto-generated method stub

		// direct_flag_btn = (ToggleButton) findViewById(R.id.direct_flg);
		// other_tips = (TextView) findViewById(R.id.other_tips);
		username_teTextView = (EditText) findViewById(R.id.user_name);
		user_phone_teTextView = (EditText) findViewById(R.id.user_phone);
		age_teTextView = (EditText) findViewById(R.id.age);
		Yongtu_teTextView = (EditText) findViewById(R.id.Yongtu);
		money_teTextView = (EditText) findViewById(R.id.money);
		province_teTextView = (EditText) findViewById(R.id.province);
		city_teTextView = (EditText) findViewById(R.id.city);
		area_teTextView = (EditText) findViewById(R.id.area);
		// borrow_time_tips = (TextView) findViewById(R.id.bttps);
		// rewrad_btn = (RadioGroup) findViewById(R.id.reward_kind);
		// direct_pwd_Edit = (TextView) findViewById(R.id.direct_pwd);
		// rewrad_btn.setOnCheckedChangeListener(new oncheck(
		// myjiekuan.this));

		// change_unit_btn = (Button) findViewById(R.id.change_time_kind_btn);
		// change_unit_btn.setOnClickListener(this);
		findViewById(R.id.submit_lendmoney).setOnClickListener(this);
		// findViewById(R.id.item_show_kind).setOnClickListener(this);
		// t = (TextView) findViewById(R.id.borrow_time_uin_flag);
		//
		// direct_flag_btn
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0,
		// boolean arg1) {
		// // TODO Auto-generated method stub
		// if (arg1) {
		//
		// direct_flag = "1";
		// direct_pwd_Edit.setVisibility(View.VISIBLE);
		// } else {
		// direct_pwd_Edit.setVisibility(View.GONE);
		// direct_flag = "0";
		// }
		//
		// }
		// });

		// pay_kind_sp = (Spinner) findViewById(R.id.pay_kind_list);
		// pay_kind_sp2 = (Spinner) findViewById(R.id.pay_kind_list2);
		// pay_kind_sp3 = (Spinner) findViewById(R.id.pay_kind_list3);
		// pay_kind_sp2.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// // showCustomToast("你选择了" + arg2 + "");
		//
		// if (arg2 == 0) {
		// kindStr = "1";
		// } else if (arg2 == 1) {
		// kindStr = "2";
		// } else if (arg2 == 2) {
		// kindStr = "4";
		// } else if (arg2 == 3) {
		// kindStr = "5";
		// }

		// kindStr = (arg2 + 1) + "";

		/** 对秒标的特殊定制 */
		// if (arg2 == 2) {
		//
		// // 关闭借款期限选择
		// change_unit_btn.setEnabled(false);
		// // 设定借款期限
		//
		// borrow_time_tips.setVisibility(View.VISIBLE);
		// Yongtu_teTextView.setVisibility(View.GONE);
		// t.setVisibility(View.GONE);
		// // 设置还款方式
		// pay_kind_sp.setSelection(1);
		// pay_kind_sp.setEnabled(false);
		//
		// } else {
		// change_unit_btn.setEnabled(true);
		// Yongtu_teTextView.setVisibility(View.VISIBLE);
		// borrow_time_tips.setVisibility(View.GONE);
		// // Yongtu_teTextView.setHint("借款期限:");
		// pay_kind_sp.setEnabled(true);
		// pay_kind_sp.setSelection(0);
		// t.setVisibility(View.VISIBLE);
		//
		// }

		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// pay_kind_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// // showCustomToast("你选择了" + arg2 + "");
		//
		// pay_kind = (arg2 + 1) + "";
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// pay_kind_sp3.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// // showCustomToast("你选择了" + arg2 + "");
		//
		// borrow_use = (arg2 + 1) + "";
		//
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//

	}

	// class oncheck implements android.widget.RadioGroup.OnCheckedChangeListener {
	//
	// private Context context;
	//
	// public oncheck(Context context) {
	// super();
	// // TODO Auto-generated constructor stub
	// this.context = context;
	// }

	// @Override
	// public void onCheckedChanged(RadioGroup arg0, int arg1) {
	// // TODO Auto-generated method stub
	//
	// int radioButtonId = arg0.getCheckedRadioButtonId();
	// switch (radioButtonId) {
	// case R.id.b1:
	// reward_kind = "0";
	// break;
	// case R.id.b2:
	// reward_kind = "1";
	// rewrad_point = new EditText(context);
	// new AlertDialog.Builder(context).setTitle("请输入奖励比例")
	// .setView(rewrad_point).setPositiveButton("确定", null)
	// .show();
	//
	// break;
	// case R.id.b3:
	// reward_kind = "2";
	// rewrad_point = new EditText(context);
	// new AlertDialog.Builder(context).setTitle("请输入奖励金额")
	// .setView(rewrad_point).setPositiveButton("确定", null)
	// .show();
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	// }

	// 提交到服务器
	public void doHttp() {
		MyLog.e("请求服务器    上传数据", "...............2");

		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.put("uid", Default.userId);
		jsonBuilder.put("user_name", user_name);
		jsonBuilder.put("user_phone", user_phone);
		jsonBuilder.put("age", age);
		jsonBuilder.put("Yongtu", Yongtu);
		jsonBuilder.put("money", money);
		jsonBuilder.put("province", province);
		jsonBuilder.put("city", city);
		jsonBuilder.put("area", area);
		// jsonBuilder.put("borrow_use", borrow_use);
		// jsonBuilder.put("kind", kindStr);
		// jsonBuilder.put("reward_kind", reward_kind);
		// jsonBuilder.put("is_targeting", direct_flag);
		// jsonBuilder.put("borrow_info", borrow_info);
		// jsonBuilder.put("repayment_type", repay_due_unit);

		// // 提交定向标密码
		// if (direct_flag.endsWith("1")) {
		//
		// jsonBuilder.put("targeting_pass", direct_pwd_str);
		// }
		//
		// // 提交奖励标准参数
		// if (reward_kind.endsWith("1") || reward_kind.endsWith("2")) {
		//
		// jsonBuilder.put("reward_num", rewrad_point_str);
		//
		// }

		BaseHttpClient.post(this, Default.LENDMONEY_REQUEST, jsonBuilder, new JsonHttpResponseHandler() {

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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.submit_lendmoney:
			MyLog.e("请求...............", "...........");
			submitTheInfo();
			break;
		// case R.id.change_time_kind_btn:
		//
		// if (t.getText().toString().equals("月")) {
		// showCustomToast("天标默认还款方式:按天到期还款");
		// pay_kind_sp.setSelection(0);
		//
		// pay_kind_sp.setEnabled(false);
		// repay_due_unit = "1";
		// change_unit_btn.setText("按月");
		// t.setText("天");
		//
		// } else {
		// pay_kind_sp.setEnabled(true);
		// repay_due_unit = "2";
		// change_unit_btn.setText("按天");
		// t.setText("月");
		// }
		// break;
		//
		// case R.id.item_show_kind:
		//
		// startActivity(
		// new Intent(this,
		// LendMoney_Choice_kindActivity.class));
		// break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}

	}

	// 提交到服务器之前对所有字段的判断
	public void submitTheInfo() {

		MyLog.e("请求服务器", "...............1");
		if (username_teTextView.getText() != null && username_teTextView.getText().length() != 0) {

			user_name = username_teTextView.getText().toString();
		} else {

			showCustomToast("用户名不能为空");
			return;
		}

		// if (amount_Editor.getText() != null
		// && amount_Editor.getText().length() != 0
		// && !amount_Editor.getText().equals("0")) {
		//
		// amount = amount_Editor.getText().toString();
		//
		// } else {
		//
		// showCustomToast("请填写借款金额");
		// return;
		// }
		// if (age_teTextView.getText() != null
		// && age_teTextView.getText().length() != 0) {
		//
		// interest_rate = age_teTextView.getText().toString();
		//
		// } else {
		//
		// showCustomToast("年利率不能为空");
		// return;
		// }
		if (Yongtu_teTextView.getText() != null && Yongtu_teTextView.getText().length() != 0) {

			Yongtu = Yongtu_teTextView.getText().toString();
		} else {

			showCustomToast("用途不能为空");
			return;
		}

		if (user_phone_teTextView.getText() != null && user_phone_teTextView.getText().length() != 0) {

			user_phone = user_phone_teTextView.getText().toString();
		} else {

			showCustomToast("用途不能为空");
			return;
		}
		if (age_teTextView.getText() != null && age_teTextView.getText().length() != 0) {

			age = age_teTextView.getText().toString();
		} else {

			showCustomToast("年龄不能为空");
			return;
		}
		if (money_teTextView.getText() != null && money_teTextView.getText().length() != 0) {

			money = money_teTextView.getText().toString();
		} else {

			showCustomToast("金额不能为空");
			return;
		}
		if (province_teTextView.getText() != null && province_teTextView.getText().length() != 0) {

			province = province_teTextView.getText().toString();
		} else {

			showCustomToast("金额不能为空");
			return;
		}
		if (city_teTextView.getText() != null && city_teTextView.getText().length() != 0) {

			city = city_teTextView.getText().toString();
		} else {

			showCustomToast("城市不能为空");
			return;
		}
		if (area_teTextView.getText() != null && area_teTextView.getText().length() != 0) {

			area = area_teTextView.getText().toString();
		} else {

			showCustomToast("县区不能为空");
			return;
		}

		// 定向标密码
		// if (direct_flag.endsWith("1")) {
		//
		// if (direct_pwd_Edit.getText() != null
		// && direct_pwd_Edit.getText().toString().length() != 0) {
		//
		// direct_pwd_str = direct_pwd_Edit.getText().toString();
		// } else {
		//
		// showCustomToast("请输入定向标密码!");
		// return;
		//
		// }
		// }

		// 获取奖励百分比值 或 奖金值
		// if (reward_kind.endsWith("1") || reward_kind.endsWith("2")) {
		//
		// rewrad_point_str = rewrad_point.getText().toString();
		//
		// }
		//
		// if (other_tips.getText() != null) {
		//
		// borrow_info = other_tips.getText().toString();
		// }
		MyLog.e("请求服务器    上传数据", "...............1");
		doHttp();

	}

}
