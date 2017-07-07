package com.lmq.main.api;

import com.czscg.R;

public class GameApi {

	public static int getBitmapFromType(int type) {
		// 标种类型 2 流转标 3表示信用标 4担保标 5秒还标
		// 6净值标
		// 7抵押标
		int value = 0;
		switch (type) {
		case 2:
			value = R.drawable.b_tz_type_2;
			break;
		case 3:
			value = R.drawable.b_tz_type_3;
			break;
		case 4:
			value = R.drawable.b_tz_type_4;
			break;
		case 5:
			value = R.drawable.b_tz_type_5;
			break;
		case 6:
			value = R.drawable.b_tz_type_6;
			break;
		case 7:
			value = R.drawable.b_tz_type_7;
			break;
		}
		return value;

	}

}
