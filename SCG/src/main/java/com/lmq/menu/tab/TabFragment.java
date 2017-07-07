package com.lmq.menu.tab;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.czscg.R;
import com.lmq.main.activity.loginActivity;
import com.lmq.main.api.LocationApplication;
import com.lmq.main.api.MyLog;
import com.lmq.main.listener.FragmentUpdateListener;
import com.lmq.main.util.Default;
import com.lmq.menu.HomeFragment;
import com.lmq.menu.IndexFragment_Scg;
import com.lmq.menu.SettingFragment;
import com.lmq.menu.UserInfoFragment;

public class TabFragment extends Fragment implements FragmentUpdateListener, OnClickListener {
	private IndexFragment_Scg mFragmentHome;
	private HomeFragment mFragmentInfoList;
	private SettingFragment mFragmentMore;
	private UserInfoFragment mFragmentPeople;

	private FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;

	private RadioGroup rg;
	private RadioButton rb[];
	private boolean updateRadioGroup = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.page_foot, null);

		initView(mainView);
		mFragmentManager = getFragmentManager();

		if (Default.showNewsList) {
			change(R.id.button4);
		} else {
			change(R.id.button1);
		}
		rg.check(R.id.button1);
		return mainView;
	}

	public void initView(View mainView) {
		rg = (RadioGroup) mainView.findViewById(R.id.group);
		rb = new RadioButton[4];
		rb[0] = (RadioButton) mainView.findViewById(R.id.button1);
		rb[1] = (RadioButton) mainView.findViewById(R.id.button2);
		rb[2] = (RadioButton) mainView.findViewById(R.id.button3);
		rb[3] = (RadioButton) mainView.findViewById(R.id.button4);

		for (int i = 0; i < rb.length; i++) {
			rb[i].setOnClickListener(this);
		}

	}

	/**
	 * 设置隐藏Fragment
	 *
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mFragmentInfoList != null) {
			transaction.hide(mFragmentInfoList);
		}
		if (mFragmentMore != null) {
			transaction.hide(mFragmentMore);
		}
		if (mFragmentPeople != null) {
			transaction.hide(mFragmentPeople);
		}
		if (mFragmentHome != null) {
			transaction.hide(mFragmentHome);
		}

	}

	public void login(int type) {
		startActivityForResult(new Intent(getActivity(), loginActivity.class), type);
		getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.to_left);

	}

	public void changeTextColor(int index) {
		for (int i = 0; i < rb.length; i++) {
			if (index == i) {
				rb[i].setTextColor(getResources().getColor(R.color.red));
			} else {
				rb[i].setTextColor(getResources().getColor(R.color.black));
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Default.LOGIN_TYPE_2:
			if (resultCode == 1) {
				change(R.id.button3);
			} else {
				updateRadioGroup = false;
				change(R.id.button1);
			}
			break;
		case Default.LOGIN_TYPE_3:
			if (resultCode == 1) {
				change(R.id.button2);
			} else {
				updateRadioGroup = false;
				change(R.id.button1);
			}
			break;
		}
	}

	// 登录失败或成功时 返回之前的界面
	@Override
	public void onFramentCallback(int type, JSONObject json) {
		MyLog.e("123", "更新TabBar选项卡.........");
		if (type == 1) {
			updateRadioGroup = false;
			MyLog.e("123", "更新TabBar选项卡......切换到一...");
			rg.check(R.id.button1);
			change(R.id.button1);
		}
	}

	@Override
	public void onClick(View arg0) {
		change(arg0.getId());
	}

	public void change_to_home() {

		mFragmentTransaction = mFragmentManager.beginTransaction();
		hideFragments(mFragmentTransaction);
		changeTextColor(0);
		if (mFragmentHome == null) {
			mFragmentHome = new IndexFragment_Scg();
			mFragmentTransaction.add(R.id.layout, mFragmentHome);
		} else {
			mFragmentTransaction.show(mFragmentHome);
		}
		if (((LocationApplication) getActivity().getApplication()).titleTextView != null) {

			((LocationApplication) getActivity().getApplication()).titleTextView.setText("主页");
		}
		mFragmentTransaction.commit();
		rb[0].setSelected(true);
		rb[2].setSelected(false);
	}

	public void change(int id) {
		if (!updateRadioGroup) {
			updateRadioGroup = true;
		}
		mFragmentTransaction = mFragmentManager.beginTransaction();
		hideFragments(mFragmentTransaction);
		switch (id) {

		case R.id.button1:
			changeTextColor(0);
			if (mFragmentHome == null) {
				mFragmentHome = new IndexFragment_Scg();
				mFragmentTransaction.add(R.id.layout, mFragmentHome);
			} else {
				mFragmentTransaction.show(mFragmentHome);
			}
			if (((LocationApplication) getActivity().getApplication()).titleTextView != null) {

				((LocationApplication) getActivity().getApplication()).titleTextView.setText("主页");
			}
			break;
		case R.id.button2:

			changeTextColor(1);
			if (mFragmentInfoList == null) {
				mFragmentInfoList = new HomeFragment();
				mFragmentTransaction.add(R.id.layout, mFragmentInfoList);
			} else {
				mFragmentTransaction.show(mFragmentInfoList);
			}
			((LocationApplication) getActivity().getApplication()).titleTextView.setText("投资");
			break;
		case R.id.button3:
			changeTextColor(2);
			if (Default.layout_type == Default.pageStyleLogin) {
				login(Default.LOGIN_TYPE_2);
				return;
			} else {

				((LocationApplication) getActivity().getApplication()).titleTextView.setText("我的账户");
				if (mFragmentPeople == null) {
					mFragmentPeople = new UserInfoFragment();
					mFragmentPeople.setListener(this);
					mFragmentTransaction.add(R.id.layout, mFragmentPeople);
				} else {
					mFragmentTransaction.show(mFragmentPeople);
					// mFragmentPeople.setListener(this);
				}
			}
			break;
		case R.id.button4:

			changeTextColor(3);

			if (mFragmentMore == null) {
				mFragmentMore = new SettingFragment();
				mFragmentTransaction.add(R.id.layout, mFragmentMore);
			} else {
				mFragmentTransaction.show(mFragmentMore);
			}

			((LocationApplication) getActivity().getApplication()).titleTextView.setText("更多");
			break;
		}
		mFragmentTransaction.commit();
	}

}
