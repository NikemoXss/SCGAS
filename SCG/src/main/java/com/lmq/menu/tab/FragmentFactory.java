package com.lmq.menu.tab;

import android.support.v4.app.Fragment;

import com.lmq.menu.AccountFragment_Scg;
import com.lmq.menu.IndexFragment_Scg;
import com.lmq.menu.SetFragment_Scg;
import com.lmq.menu.TouziFragment;
import com.lmq.menu.ZxFragment;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
	public static Fragment getInstanceByIndex(int index) {
		Fragment fragment = null;
		switch (index) {
		case 1:
			fragment = new IndexFragment_Scg();// 主页
			break;
		case 2:
			fragment = new AccountFragment_Scg();// 账户
			break;
		case 3:
			fragment = new SetFragment_Scg();// 设置
			break;
		case 4:
//			fragment = new Crowd_Sx();// 众筹
			fragment = new ZxFragment();// 众筹
			break;
		case 5:
			fragment = new TouziFragment();//投资
			break;
		}
		return fragment;
	}
}
