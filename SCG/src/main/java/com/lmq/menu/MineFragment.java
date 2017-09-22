package com.lmq.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.czscg.R;
import com.lmq.main.api.BaseFragment;



//苏常账户
@SuppressLint("NewApi")
public class MineFragment extends BaseFragment implements OnClickListener {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_mine, null);

        return mainView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }

}
