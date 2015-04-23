package com.easyweather.app.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.easyweather.app.model.City;
import com.easyweather.app.model.Province;
import com.easyweather.app.model.WeatherLXDB;

public class Utility {

	/*
	 * 解析和处理服务器返回的省级数据
	 */
	
	
	public synchronized static boolean parseProvinceXMLWithPull(WeatherLXDB weatherLXDB, String response){
		if(!TextUtils.isEmpty(response)){
			try{
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				String RegionID = "";
				String RegionName = "";
				while(eventType != XmlPullParser.END_DOCUMENT){
					String nodeName = xmlPullParser.getName();
					switch(eventType){
					case XmlPullParser.START_TAG:{
						if("RegionID".equals(nodeName)){
							RegionID = xmlPullParser.nextText();
						}else if("RegionName".equals(nodeName)){
							RegionName = xmlPullParser.nextText();
						}
						break;
					}
					case XmlPullParser.END_TAG:{
						if("Province".equals(nodeName)){
							Province province = new Province();
							province.setProvinceCode(RegionID);
							province.setProvinceName(RegionName);
							weatherLXDB.saveProvince(province);
						}
						break;
					}
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return false;
	}
	/*
	 * 解析和处理服务器返回的省级数据
	 */
	
	
	public synchronized static boolean parseCityXMLWithPull(WeatherLXDB weatherLXDB, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			try{
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				String CityID = "";
				String CityName = "";
				while(eventType != XmlPullParser.END_DOCUMENT){
					String nodeName = xmlPullParser.getName();
					switch(eventType){
					case XmlPullParser.START_TAG:{
						if("CityID".equals(nodeName)){
							CityID = xmlPullParser.nextText();
						}else if("CityName".equals(nodeName)){
							CityName = xmlPullParser.nextText();
							Log.d("weather", CityName);
							
						}
						break;
					}
					case XmlPullParser.END_TAG:{
						if("City".equals(nodeName)){
							City city = new City();
							city.setCityCode(CityID);
							city.setCityName(CityName);
							city.setProvinceId(provinceId);
							weatherLXDB.saveCity(city);
						}
						break;
					}
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
				return true;
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*
	 * 使用pull方法解析和处理服务器返回的xml格式的天气数据
	 */
	
	public static void parseWeatherXMLWithPull(Context context, String response){
		if(!TextUtils.isEmpty(response)){
			try{
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(response));
				int eventType = xmlPullParser.getEventType();
				String[] array = new String[32];
				int num = 0;
				while(eventType != XmlPullParser.END_DOCUMENT){
					String nodeName = xmlPullParser.getName();
					switch(eventType){
					case XmlPullParser.START_TAG:{
						if("string".equals(nodeName)){
							
								array[num] = xmlPullParser.nextText();
								num++; 
							
						}
						break;
					}
					case XmlPullParser.END_TAG:{
						if("ArrayOfString".equals(nodeName)){
							
						}
						break;
						
					}
					default:
						break;
					}
					eventType = xmlPullParser.next();
					
				}
				String cityName = array[1];
				String cityId = array[2];
				String weatherDesp = array[4];
				String publishTime = array[3];
				String todayDesp = array[7];
				String todayTemp = array[8];
				String tomorrowDesp = array[12];
				String tomorrowTemp = array[13];
				String thirdDayDesp = array[17];
				String thirdDayTemp = array[18];
				Log.d("aaa", weatherDesp);
				SaveWeatherInfo(context,cityName, cityId, weatherDesp, publishTime, todayDesp, todayTemp, tomorrowDesp, tomorrowTemp, thirdDayDesp, thirdDayTemp);
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * 存储天气数据到SharedPreferences文件中
	 */
	public static void SaveWeatherInfo(Context context, String cityName,
			String cityId, String weatherDesp, String publishTime, String todayDesp, String todayTemp, String tomorrowDesp, String tomorrowTemp, String thirdDayDesp, String thirdDayTemp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
.edit();
		editor.putBoolean("city_selected", true);
		editor.putString("cityName", cityName);
		editor.putString("cityId", cityId);
		editor.putString("weatherDesp", weatherDesp);
		editor.putString("publishTime", publishTime);
		editor.putString("currentDate", sdf.format(new Date()));
		editor.putString("todayDesp", todayDesp);
		editor.putString("todayTemp", todayTemp);
		editor.putString("tomorrowDesp", tomorrowDesp);
		editor.putString("tomorrowTemp", tomorrowTemp);
		editor.putString("thirdDayDesp", thirdDayDesp);
		editor.putString("thirdDayTemp", thirdDayTemp);
		editor.commit();
		
	}

}
