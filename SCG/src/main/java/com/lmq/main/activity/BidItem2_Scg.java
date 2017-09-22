package com.lmq.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;

//投资详情-->我要投资
public class BidItem2_Scg extends BaseActivity implements OnClickListener {
	ImageView imageview;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biditem2);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);

		title = (TextView) findViewById(R.id.title);
		title.setText("我要投资");

		initView();
	}

	private void initView() {

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.title_right:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		getPageInfoHttp();
	}

}
