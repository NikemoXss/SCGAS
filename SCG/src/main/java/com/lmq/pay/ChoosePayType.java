package com.lmq.pay;

import com.lmq.main.activity.XxPay;
import com.lmq.main.api.BaseActivity;
import com.czscg.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosePayType extends BaseActivity implements OnClickListener {
	TextView title;
	ImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosepaytype);
		title = (TextView) findViewById(R.id.title);
		title.setText("充值方式选择");
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);

		findViewById(R.id.pay_xs).setOnClickListener(this);
		findViewById(R.id.pay_xx).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		case R.id.pay_xs:
			Intent intent = new Intent(ChoosePayType.this, FyPayActivity.class);
			startActivity(intent);
			break;
		case R.id.pay_xx:
			Intent intent1 = new Intent(ChoosePayType.this, XxPay.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

}
