package com.easyweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyweather.app.R;
import com.easyweather.app.model.City;
import com.easyweather.app.model.Province;
import com.easyweather.app.model.WeatherLXDB;
import com.easyweather.app.util.HttpCallbackListener;
import com.easyweather.app.util.HttpUtil;
import com.easyweather.app.util.Utility;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private WeatherLXDB weatherLXDB;
	private List<String> dataList = new ArrayList<String>();
	/*
	 *ʡ�б� 
	 */
	private List<Province> provinceList;
	/*
	 * ���б�
	 */
	private List<City> cityList;
	/*
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/*
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	/*
	 * �Ƿ��WeatherActivity����ת����
	 */
	private Boolean isFromWeatherActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//�Ѿ�ѡ������Ҳ��Ǵ�WeatherActivity��ת�������Ż�ֱ����ת��WeatherActivity
		if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView =(ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weatherLXDB = WeatherLXDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					String cityCode = cityList.get(index).getCityCode();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("city_code", cityCode);
					startActivity(intent);
					finish();
				}
				
			}
		});
		queryProvinces();
	}
	
	/*
	 * ��ѯȫ������ʡ�����Ȳ�ѯ���ݿ⣬û�����ѯ��������
	 */
	private void queryProvinces() {
		provinceList = weatherLXDB.loadProvinces();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("ȫ��");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	

	/*
	 * ��ѯ��ʡ�����У����Ȳ�ѯ���ݿ⣬û�����ѯ��������
	 */
	private void queryCities() {
		cityList = weatherLXDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel= LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
			
		}
	}
	/*
	 * ���ݴ�����ź����ʹӷ�������ѯ����
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if(!TextUtils.isEmpty(code)){
			address ="http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getSupportCityDataset?theRegionCode="+code;
		}else{
			address =  "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionDataset";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type)){
					result = Utility.parseProvinceXMLWithPull(weatherLXDB, response);
				}else if("city".equals(type)){
					result = Utility.parseCityXMLWithPull(weatherLXDB, response, selectedProvince.getId());
				}
				if(result){
					runOnUiThread(new Runnable(){
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}
						}
					});
				}
			}
			public void onError(Exception e) {
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	/*
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
	}
			
	
	
	/*
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	/*
	 * ����back���������ݵ�ǰ�����жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
	 */
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_CITY){
			queryProvinces();			
		} 
		else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
