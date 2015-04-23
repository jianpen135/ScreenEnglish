package com.example.screenenglish.activity;


import com.example.screenenglish.R;
import com.example.screenenglish.db.ScreenEnglishDB;
import com.example.screenenglish.receiver.ScreenOnOffBroadcastReceiver;
import com.example.screenenglish.service.StartScreenLockService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;





public class WindowActivity extends Activity {
	public final String LOGNAME = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!StartScreenLockService.ONOFF){Log.d(LOGNAME,"windowActivity is onCreate");}
		setContentView(R.layout.activity_main);
		Intent service=new Intent(WindowActivity.this,StartScreenLockService.class);
        startService(service);
        }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!StartScreenLockService.ONOFF){Log.d(LOGNAME,"windowActivity is onDestroy");}
	}
}
