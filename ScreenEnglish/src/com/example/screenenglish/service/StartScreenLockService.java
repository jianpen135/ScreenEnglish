package com.example.screenenglish.service;

import com.example.screenenglish.receiver.ScreenOnOffBroadcastReceiver;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class StartScreenLockService extends Service{
	public static boolean ONOFF = false;
	public final String LOGNAME = "service";
	@Override
	public void onCreate() {
		super.onCreate();
		if(!ONOFF){Log.d(LOGNAME,"service is onCreate");}
		KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);   //��������ʱ���������
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");  
		keyguardLock.disableKeyguard();
		ScreenOnOffBroadcastReceiver sOnBroadcastReciver=new ScreenOnOffBroadcastReceiver();  
		IntentFilter recevierFilter=new IntentFilter();  
		recevierFilter.addAction(Intent.ACTION_SCREEN_ON);  
		recevierFilter.addAction(Intent.ACTION_SCREEN_OFF); 
		recevierFilter.setPriority(1000);
		registerReceiver(sOnBroadcastReciver, recevierFilter); 
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!ONOFF){Log.d(LOGNAME,"service is onDestroy");}
		startService(new Intent(StartScreenLockService.this, StartScreenLockService.class)); 
	}
}
