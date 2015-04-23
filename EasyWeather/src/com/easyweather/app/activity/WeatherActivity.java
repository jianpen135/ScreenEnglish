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
	 * 用于显示城市名
	 */
	private TextView cityNameText;
	/*
	 * 用于显示发布时间
	 */
	private TextView publishText;
	/*
	 * 用于显示天气描述
	 */
	private TextView weatherDespText;
	
	/*
	 * 用于显示今天天气
	 */
	private TextView todayDespText;
	private TextView todayTempText;
	/*
	 * 用于显示明天天气
	 */
	private TextView tomorrowDespText;
	private TextView tomorrowTempText;
	/*
	 * 用于显示后天天气
	 */
	private TextView thirdDayDespText;
	private TextView thirdDayTempText;
	/*
	 * 切换城市按钮
	 */
	private Button switchCity;
	/*
	 * 更新天气信息
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
			publishText.setText("同步中...");
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
			publishText.setText("同步中...");
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
	 * 查询城市代号对应的天气
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
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	/*
	 * 从SharedPreferences文件中读取天气信息，并显示到界面
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("cityName", ""));
		weatherDespText.setText(prefs.getString("weatherDesp", ""));
		publishText.setText(prefs.getString("publishTime", "")+"发布");
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
