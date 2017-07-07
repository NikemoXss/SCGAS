package com.lmq.main.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;

public class LendMoney_Choice_kindActivity extends BaseActivity implements OnClickListener {

	// 标介绍Title
	private TextView intro_title;
	// 标介绍内容
	private TextView intro_det;

	// 选择标类型 标示符
	private String kind_flag = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tz_gerenzhongxin);

		TextView text = (TextView) findViewById(R.id.title);
		text.setText(R.string.lendmoney_title);

		findViewById(R.id.credit_btn).setOnClickListener(this);

		findViewById(R.id.second_btn).setOnClickListener(this);

		findViewById(R.id.mortgage_btn).setOnClickListener(this);

		findViewById(R.id.circulation_btn).setOnClickListener(this);

		findViewById(R.id.guarantee_btn).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);

		// 初始化
		intro_title = (TextView) findViewById(R.id.title_intro);
		intro_det = (TextView) findViewById(R.id.title_det);
		// findViewById(R.id.submit_post).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		/**
		 * 标类型 1信用标 2担保标 3秒还标 4净值标 5抵押标 6企业直投
		 */
		switch (arg0.getId()) {
		case R.id.credit_btn:

			kind_flag = "1";
			intro_title.setText(getResources().getString(R.string.title_intro_1));
			intro_det.setText(getResources().getString(R.string.title_det_1));

			break;
		case R.id.second_btn:

			kind_flag = "3";
			intro_title.setText(getResources().getString(R.string.title_intro_3));
			intro_det.setText(getResources().getString(R.string.title_det_3));
			break;
		case R.id.mortgage_btn:

			kind_flag = "5";
			intro_title.setText(getResources().getString(R.string.title_intro_5));
			intro_det.setText(getResources().getString(R.string.title_det_5));
			break;
		case R.id.circulation_btn:

			kind_flag = "4";
			intro_title.setText(getResources().getString(R.string.title_intro_4));
			intro_det.setText(getResources().getString(R.string.title_det_4));
			break;
		case R.id.guarantee_btn:
			kind_flag = "2";
			intro_title.setText(getResources().getString(R.string.title_intro_2));
			intro_det.setText(getResources().getString(R.string.title_det_2));
			break;

		case R.id.back:

			finish();

			break;
		/*
		 * case R.id.submit_post:
		 * Intent intent = new Intent();
		 * intent.setClass(LendMoney_Choice_kindActivity.this,
		 * LendMoneyInfoActivity.class);
		 * intent.putExtra("kind", kind_flag);
		 * startActivity(intent);
		 * break;
		 */

		default:
			break;
		}

	}

	@Override
	public void finish() {

		super.finish();
	}

}
