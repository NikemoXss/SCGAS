package com.lmq.main.activity;

import com.lmq.main.api.BaseActivity;
import com.czscg.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalSetActivity extends BaseActivity implements OnClickListener {
	TextView title;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalset);
		TextView register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(this);
		iv = (ImageView) findViewById(R.id.title_right);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("用户设置");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		}

	}
}
