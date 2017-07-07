package com.lmq.menu;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.czscg.R;
import com.lmq.http.BaseHttpClient;
import com.lmq.http.JsonHttpResponseHandler;
import com.lmq.main.activity.LendMoney_Choice_kindActivity;
import com.lmq.main.api.BaseFragment;
import com.lmq.main.api.JsonBuilder;
import com.lmq.main.api.MyLog;
import com.lmq.main.util.Default;

public class LendMoneyFragment extends BaseFragment implements OnClickListener {

	private String kindStr;

	// 标题
	private String l_title;

	// 借款金额
	private String amount;

	// 年利率
	private String interest_rate;

	// 借款时间
	private String borrow_time;

	// 还款类型
	private String pay_kind = "1";

	// 是否定向标
	private String direct_flag = "0";

	// 是否有奖励
	private String reward_kind = "0";

	private String repay_due_unit = "2";

	// 定向标密码
	private String direct_pwd_str;

	// 奖励百分比
	private String rewrad_point_str;
	// 借款描述
	private String borrow_info;
	// 奖励金额

	// 组件初始化

	private EditText title_teTextView = null;
	private EditText amount_Editor = null;
	private EditText interest_rate_Editor = null;
	private EditText borrow_time_Editor = null;
	private TextView borrow_time_tips = null;
	private ToggleButton direct_flag_btn = null;
	private RadioGroup rewrad_btn = null;
	private TextView other_tips = null;
	private Button change_unit_btn = null;
	private Spinner pay_kind_sp = null;
	private Spinner pay_kind_sp2 = null;
	private TextView direct_pwd_Edit = null;
	private TextView t = null;

	// 奖励百分比
	private EditText rewrad_point;
	View mainView;

	// 奖励金额

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.tz_jingzhibiao, null);

		initViews();
		return mainView;
	}

	protected void initViews() {
		// TODO Auto-generated method stub

		direct_flag_btn = (ToggleButton) mainView.findViewById(R.id.direct_flg);
		other_tips = (TextView) mainView.findViewById(R.id.other_tips);
		title_teTextView = (EditText) mainView.findViewById(R.id.l_title);
		amount_Editor = (EditText) mainView.findViewById(R.id.l_amount);
		interest_rate_Editor = (EditText) mainView.findViewById(R.id.l_interest_rate);
		borrow_time_Editor = (EditText) mainView.findViewById(R.id.borrow_time);
		borrow_time_tips = (TextView) mainView.findViewById(R.id.bttps);
		rewrad_btn = (RadioGroup) mainView.findViewById(R.id.reward_kind);
		direct_pwd_Edit = (TextView) mainView.findViewById(R.id.direct_pwd);
		rewrad_btn.setOnCheckedChangeListener(new oncheck(getActivity()));

		change_unit_btn = (Button) mainView.findViewById(R.id.change_time_kind_btn);
		change_unit_btn.setOnClickListener(this);
		mainView.findViewById(R.id.submit_lendmoney).setOnClickListener(this);
		mainView.findViewById(R.id.item_show_kind).setOnClickListener(this);
		t = (TextView) mainView.findViewById(R.id.borrow_time_uin_flag);

		direct_flag_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					direct_flag = "1";
					direct_pwd_Edit.setVisibility(View.VISIBLE);
				} else {
					direct_pwd_Edit.setVisibility(View.GONE);
					direct_flag = "0";
				}

			}
		});

		pay_kind_sp = (Spinner) mainView.findViewById(R.id.pay_kind_list);
		pay_kind_sp2 = (Spinner) mainView.findViewById(R.id.pay_kind_list2);
		pay_kind_sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// showCustomToast("你选择了" + arg2 + "");

				kindStr = arg2 + 1 + "";

				/** 对秒标的特殊定制 */
				if (arg2 == 2) {

					// 关闭借款期限选择
					change_unit_btn.setEnabled(false);
					// 设定借款期限

					borrow_time_tips.setVisibility(View.VISIBLE);
					borrow_time_Editor.setVisibility(View.GONE);
					t.setVisibility(View.GONE);
					// 设置还款方式
					pay_kind_sp.setSelection(1);
					pay_kind_sp.setEnabled(false);

				} else {
					change_unit_btn.setEnabled(true);
					borrow_time_Editor.setVisibility(View.VISIBLE);
					borrow_time_tips.setVisibility(View.GONE);
					pay_kind_sp.setEnabled(true);
					pay_kind_sp.setSelection(0);
					t.setVisibility(View.VISIBLE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		pay_kind_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				// showCustomToast("你选择了" + arg2 + "");

				pay_kind = arg2 + 1 + "";

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	class oncheck implements android.widget.RadioGroup.OnCheckedChangeListener {

		private Context context;

		public oncheck(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub

			int radioButtonId = arg0.getCheckedRadioButtonId();
			switch (radioButtonId) {
			case R.id.b1:
				reward_kind = "0";
				break;
			case R.id.b2:
				reward_kind = "1";
				rewrad_point = new EditText(context);
				new AlertDialog.Builder(context).setTitle("请输入奖励比例").setView(rewrad_point).setPositiveButton("确定", null).show();

				break;
			case R.id.b3:
				reward_kind = "2";
				rewrad_point = new EditText(context);
				new AlertDialog.Builder(context).setTitle("请输入奖励金额").setView(rewrad_point).setPositiveButton("确定", null).show();
				break;

			default:
				break;
			}

		}
	}

	// 提交到服务器
	public void doHttp() {
		MyLog.e("请求服务器    上传数据", "...............2");

		JsonBuilder jsonBuilder = new JsonBuilder();
		// jsonBuilder.put("uid", Default.userId);
		jsonBuilder.put("name", l_title);
		jsonBuilder.put("amount", amount);
		jsonBuilder.put("interest_rate", interest_rate);
		jsonBuilder.put("borrow_time", borrow_time);
		jsonBuilder.put("repay_kind", pay_kind);
		jsonBuilder.put("kind", kindStr);
		jsonBuilder.put("reward_kind", reward_kind);
		jsonBuilder.put("is_targeting", direct_flag);
		jsonBuilder.put("borrow_info", borrow_info);
		jsonBuilder.put("repayment_type", repay_due_unit);

		// 提交定向标密码
		if (direct_flag.endsWith("1")) {

			jsonBuilder.put("targeting_pass", direct_pwd_str);
		}

		// 提交奖励标准参数
		if (reward_kind.endsWith("1") || reward_kind.endsWith("2")) {

			jsonBuilder.put("reward_num", rewrad_point_str);

		}

		BaseHttpClient.post(getActivity(), Default.LENDMONEY_REQUEST, jsonBuilder, new JsonHttpResponseHandler() {

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
					MyLog.e("我发布借款从服务器获得的数据", response.toString());
					if (statusCode == 200) {
						if (response.getInt("status") == 1) {
							showCustomToast("提交成功");
							MainTabActivit_Scg.mainTabActivity.IndexView();

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
		case R.id.change_time_kind_btn:

			if (t.getText().toString().equals("月")) {
				showCustomToast("天标默认还款方式:按天到期还款");
				pay_kind_sp.setSelection(0);

				pay_kind_sp.setEnabled(false);
				repay_due_unit = "1";
				change_unit_btn.setText("按月");
				t.setText("天");

			} else {
				pay_kind_sp.setEnabled(true);
				repay_due_unit = "2";
				change_unit_btn.setText("按天");
				t.setText("月");
			}
			break;

		case R.id.item_show_kind:

			getActivity().startActivity(new Intent(getActivity(), LendMoney_Choice_kindActivity.class));
			break;

		default:
			break;
		}

	}

	// 提交到服务器之前对所有字段的判断
	public void submitTheInfo() {

		MyLog.e("请求服务器", "...............1");
		if (title_teTextView.getText() != null && title_teTextView.getText().length() != 0) {

			l_title = title_teTextView.getText().toString();
		} else {

			showCustomToast("标题不能为空");
			return;
		}

		if (amount_Editor.getText() != null && amount_Editor.getText().length() != 0 && !amount_Editor.getText().equals("0")) {

			amount = amount_Editor.getText().toString();

		} else {

			showCustomToast("请填写借款金额");
			return;
		}
		if (interest_rate_Editor.getText() != null && interest_rate_Editor.getText().length() != 0) {

			interest_rate = interest_rate_Editor.getText().toString();

		} else {

			showCustomToast("年利率不能为空");
			return;
		}

		if (borrow_time_Editor.getVisibility() == View.VISIBLE) {
			if (borrow_time_Editor.getText() != null && borrow_time_Editor.getText().length() != 0) {

				borrow_time = borrow_time_Editor.getText().toString();

			} else {

				showCustomToast("借款期不能为空");
				return;
			}
		} else {

			pay_kind = "1";
			borrow_time = "一个月";
		}

		// 定向标密码
		if (direct_flag.endsWith("1")) {

			if (direct_pwd_Edit.getText() != null && direct_pwd_Edit.getText().toString().length() != 0) {

				direct_pwd_str = direct_pwd_Edit.getText().toString();
			} else {

				showCustomToast("请输入定向标密码!");
				return;

			}
		}

		// 获取奖励百分比值 或 奖金值
		if (reward_kind.endsWith("1") || reward_kind.endsWith("2")) {

			rewrad_point_str = rewrad_point.getText().toString();

		}

		if (other_tips.getText() != null) {

			borrow_info = other_tips.getText().toString();
		}
		MyLog.e("请求服务器    上传数据", "...............1");
		doHttp();

	}

}
