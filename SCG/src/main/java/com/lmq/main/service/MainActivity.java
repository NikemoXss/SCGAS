package com.lmq.main.service;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.czscg.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		 Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
         //启动FloatViewService  
         startService(intent);  
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 销毁悬浮窗
		Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
        //终止FloatViewService  
        stopService(intent); 
        super.onStop();
	}
    
}

