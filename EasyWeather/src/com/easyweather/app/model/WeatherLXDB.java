package com.easyweather.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easyweather.app.db.WeatherLXOpenHelper;
import com.easyweather.app.model.City;
import com.easyweather.app.model.Province;

public class WeatherLXDB {

	/*
	 * ���ݿ���
	 */
	public static final String DB_NAME = "weather_lx";
	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	private static WeatherLXDB weatherLXDB;
	private SQLiteDatabase db;
	/*
	 * �����췽��˽�л�
	 */
	private WeatherLXDB(Context context){
		WeatherLXOpenHelper dbHelper = new WeatherLXOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	/*
	 * ��ȡweatherLXDB��ʵ��
	 */
	public synchronized static WeatherLXDB getInstance(Context context){
		if(weatherLXDB == null){
			weatherLXDB = new WeatherLXDB(context);
		}
		return weatherLXDB;
	}
	/*
	 * ��provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province){
		 if(province != null){
			 ContentValues values = new ContentValues();
			 values.put("province_name", province.getProvinceName());
			 values.put("province_code", province.getProvinceCode());
			 db.insert("province", null, values);
		 }
	}
	 /*
	  * �����ݿ��ȡĳʡ�����еĳ�����Ϣ
	  */
	 
	 public List<Province> loadProvinces(){
		 List<Province> list = new ArrayList<Province>();
		 Cursor cursor = db.query("Province", null, null, null, null, null, null);
		 if(cursor.moveToFirst()){
			 do{
				 	Province province = new Province();
				 	province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				 	province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				 	province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				 	list.add(province);
			 }while(cursor.moveToNext());
		 }
		 if(cursor != null){
			 cursor.close();
		 }
		 return list;
	 }
	 /*
	  * ��cityʵ���洢�����ݿ�
	  */
	 public void saveCity(City city){
		 if(city != null){
			 ContentValues values = new ContentValues();
			 values.put("city_name", city.getCityName());
			 values.put("city_code", city.getCityCode());
			 values.put("province_id", city.getProvinceId());
			 db.insert("city", null, values);
		 }
	 }
	 /*
	  * �����ݿ��ȡĳʡ�����еĳ�����Ϣ
	  */
	 public List<City> loadCities(int provinceId){
		 List<City> list = new ArrayList<City>();
		 Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		 if(cursor.moveToFirst()){
			 do{
				 	City city = new City();
				 	city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				 	city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				 	city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				 	city.setProvinceId(provinceId);
				 	list.add(city);
			 }while(cursor.moveToNext());
		 }
		 if(cursor != null){
			 cursor.close();
		 }
		 return list;
	 }
	

}
