package com.lmq.menu.tab;

import com.czscg.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ZcjsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mainView = inflater.inflate(R.layout.activity_zcjsfragment, null);
		TextView tv_js = (TextView) mainView.findViewById(R.id.zcjs);
		Bundle bundle = getArguments();// 从activity传过来的Bundle
		String str = bundle.getString("zcjs_str");
		tv_js.setText(str);
		// mainView.findViewById(R.id.tx_sx);
		return mainView;
	}

}
