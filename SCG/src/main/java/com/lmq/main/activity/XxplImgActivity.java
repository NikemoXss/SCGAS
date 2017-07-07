package com.lmq.main.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;

//信息披露
public class XxplImgActivity extends BaseActivity implements OnClickListener {
	ImageView imageview;
	TextView title;
	ArrayList list;
	int pos;
	PhotoView xxpl_photo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxplimg_activity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		imageview = (ImageView) findViewById(R.id.title_right);
		imageview.setVisibility(View.VISIBLE);
		imageview.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setText("信息披露图片");
		xxpl_photo=(PhotoView) findViewById(R.id.xxpl_photo);
		Bundle bundle = this.getIntent().getExtras();
		list = bundle.getParcelableArrayList("list");
		pos = bundle.getInt("position");
		Map<String, String> map = null;
		map = (Map<String, String>) list.get(pos);
		String img = map.get("image");
//		for (int i = 0; i < list.size(); i++) {
//			map = (Map<String, String>) list.get(i);
//			String img = map.get("image");
//		}
		Picasso.with(this).load(img)
		.placeholder(R.drawable.activityloading).error(R.drawable.activityloading).fit()
		.into(xxpl_photo);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
