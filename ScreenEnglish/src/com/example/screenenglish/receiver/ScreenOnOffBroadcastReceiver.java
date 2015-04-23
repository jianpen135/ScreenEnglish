package com.example.screenenglish.receiver;
/*
 * 监视屏幕，来电移除锁屏和黑屏时运行程序
 */
import com.example.screenenglish.StartLockView;
import com.example.screenenglish.activity.WindowActivity;
import com.example.screenenglish.service.StartScreenLockService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class ScreenOnOffBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//来电时移除锁屏界面
		if(arg1.getAction().equals("android.intent.action.PHONE_STATE") && StartLockView.startLockView != null){
			WindowManager wm = (WindowManager)arg0.getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(StartLockView.startLockView);
			//黑屏即添加view，亮屏直接显示，防止延迟
		}else if(arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)&& StartLockView.startLockView == null){
			new StartLockView(arg0);
		}
		Intent service=new Intent();                   //确保服务在每次亮屏都能运行
        service.setClass(arg0,StartScreenLockService.class);
        arg0.startService(service);
	}

}
