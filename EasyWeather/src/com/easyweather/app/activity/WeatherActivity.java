package com.easyweather.app.activity;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyweather.app.service.AutoUpdateService;
import com.easyweather.app.util.HttpCallbackListener;
import com.easyweather.app.util.HttpUtil;
import com.easyweather.app.util.Utility;
import com.easyweather.app.R;

public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	/*
	 * ������ʾ������
	 */
	private TextView cityNameText;
	/*
	 * ������ʾ����ʱ��
	 */
	private TextView publishText;
	/*
	 * ������ʾ��������
	 */
	private TextView weatherDespText;
	
	/*
	 * ������ʾ��������
	 */
	private TextView todayDespText;
	private TextView todayTempText;
	/*
	 * ������ʾ��������
	 */
	private TextView tomorrowDespText;
	private TextView tomorrowTempText;
	/*
	 * ������ʾ��������
	 */
	private TextView thirdDayDespText;
	private TextView thirdDayTempText;
	/*
	 * �л����а�ť
	 */
	private Button switchCity;
	/*
	 * ����������Ϣ
	 */
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		todayDespText = (TextView) findViewById(R.id.today_desp);
		todayTempText = (TextView) findViewById(R.id.today_temp);
		tomorrowDespText = (TextView) findViewById(R.id.tomorrow_desp);
		tomorrowTempText = (TextView) findViewById(R.id.tomorrow_temp);
		thirdDayDespText = (TextView) findViewById(R.id.third_day_desp);
		thirdDayTempText = (TextView) findViewById(R.id.third_day_temp);
		String cityCode = getIntent().getStringExtra("city_code");
		
		if(!TextUtils.isEmpty(cityCode)){
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(cityCode);
		}else{
			showWeather();
			}
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("ͬ����...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String cityCode = prefs.getString("cityId", "");
			if(!TextUtils.isEmpty(cityCode)){
				queryWeatherInfo(cityCode);
			}
			break;
		default:
			break;
		}
	}
	/*
	 * ��ѯ���д��Ŷ�Ӧ������
	 */
	private void queryWeatherInfo(String cityCode){
		String address = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theUserID=&theCityCode=" + cityCode;
		queryFromServer(address, "cityCode");
		}
	/*
	 * 
	 */
	
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if("cityCode".equals(type)){
					Utility.parseWeatherXMLWithPull(WeatherActivity.this,response);
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	/*
	 * ��SharedPreferences�ļ��ж�ȡ������Ϣ������ʾ������
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("cityName", ""));
		weatherDespText.setText(prefs.getString("weatherDesp", ""));
		publishText.setText(prefs.getString("publishTime", "")+"����");
		todayDespText.setText(prefs.getString("todayDesp", ""));
		todayTempText.setText(prefs.getString("todayTemp", ""));
		tomorrowDespText.setText(prefs.getString("tomorrowDesp", ""));
		tomorrowTempText.setText(prefs.getString("tomorrowTemp", ""));
		thirdDayDespText.setText(prefs.getString("thirdDayDesp", ""));
		thirdDayTempText.setText(prefs.getString("thirdDayTemp", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
	
	
}
