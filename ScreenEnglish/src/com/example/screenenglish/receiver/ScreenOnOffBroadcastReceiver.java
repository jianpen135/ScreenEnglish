package com.example.screenenglish.receiver;
/*
 * ������Ļ�������Ƴ������ͺ���ʱ���г���
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
		//����ʱ�Ƴ���������
		if(arg1.getAction().equals("android.intent.action.PHONE_STATE") && StartLockView.startLockView != null){
			WindowManager wm = (WindowManager)arg0.getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(StartLockView.startLockView);
			//���������view������ֱ����ʾ����ֹ�ӳ�
		}else if(arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)&& StartLockView.startLockView == null){
			new StartLockView(arg0);
		}
		Intent service=new Intent();                   //ȷ��������ÿ��������������
        service.setClass(arg0,StartScreenLockService.class);
        arg0.startService(service);
	}

}
