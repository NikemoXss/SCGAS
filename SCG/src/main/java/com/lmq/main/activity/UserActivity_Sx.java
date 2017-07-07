package com.lmq.main.activity;

import com.lmq.main.activity.investmanager.InvestManagerStandardActivity;
import com.lmq.main.api.BaseActivity;
import com.lmq.main.util.Default;
import com.czscg.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
//个人中心
public class UserActivity_Sx extends BaseActivity implements OnClickListener {

	TextView tv_title, peo_name, tv_members;
	ImageView iv;
	String mName, mPassword, credits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_sx);
		iv = (ImageView) findViewById(R.id.title_right);
		tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText("个人中心");
		peo_name = (TextView) findViewById(R.id.peo_name);
		tv_members = (TextView) findViewById(R.id.tv_members);
		iv.setVisibility(View.VISIBLE);
		iv.setOnClickListener(this);
		findViewById(R.id.peopleinfo_deal).setOnClickListener(this);
		findViewById(R.id.peopleinfo_invest_sx).setOnClickListener(this);
		findViewById(R.id.peopleinfo_zhgl_sx).setOnClickListener(this);
		findViewById(R.id.my_jxq_sx).setOnClickListener(this);
		getNP();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right:
			finish();
			break;
		case R.id.peopleinfo_deal://交易管理
			startActivity(new Intent(UserActivity_Sx.this, PeopleInfoJiaoYi_New.class));
			break;
		case R.id.peopleinfo_invest_sx://投资管理
			startActivity(new Intent(UserActivity_Sx.this, InvestManagerStandardActivity.class));
			break;
		case R.id.peopleinfo_zhgl_sx://賬戶管理
			Intent intent = new Intent(UserActivity_Sx.this, peopleInfoDataActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.my_jxq_sx://加息劵
			Intent intent2=new Intent(UserActivity_Sx.this,MyCaupon.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	public void getNP() {
		SharedPreferences sp = getSharedPreferences(Default.userPreferences, 0);
		mName = sp.getString(Default.userName, "");
		mPassword = sp.getString(Default.userPassword, "");
		credits = sp.getString("credits", "");
		peo_name.setText(mName);
		tv_members.setText(credits);
	}
	

}
