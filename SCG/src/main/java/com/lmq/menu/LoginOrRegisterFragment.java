package com.lmq.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.czscg.R;
import com.lmq.main.activity.LoginActivity_Scg;
import com.lmq.main.api.BaseFragment;


//苏常账户
@SuppressLint("NewApi")
public class LoginOrRegisterFragment extends BaseFragment implements OnClickListener {

    Button login_register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layout_lor, null);
        login_register=(Button)mainView.findViewById(R.id.login_register);
        login_register.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.login_register:
                Intent intent=new Intent(getActivity(), LoginActivity_Scg.class);
                startActivity(intent);
                break;
        }
    }

}
