package com.easyweather.app.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceUtil {
	
		//����Web Service�������ռ�
	  static final String SERVICE_NS ="http://WebXml.com.cn/";  
	  	//����Web Service�ṩ�����URL  
	  static final String SERVICE_URL ="http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx"; 
	 
	  /*
	  *  ͨ��service��������ȡ����������Ϣ
	  */
	  public static SoapObject getWeatherByCity(String cityCode){ 
		  String methodName = "getWeather";  
		  HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);  
		  ht.debug = true;  
		  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
		  SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);  
		  soapObject.addProperty("theCityCode", cityCode);  
		  envelope.bodyOut = soapObject;  
		  // ������.Net�ṩ��Web Service���ֽϺõļ�����  
		  envelope.dotNet = true;  
	      try  
	       {  
	            ht.call(SERVICE_NS + methodName, envelope);  
	             SoapObject result = (SoapObject) envelope.bodyIn;  
	             SoapObject detail = (SoapObject) result.getProperty(methodName+ "Result");  
	              return detail;  
	        }  
	       catch (Exception e)  
	          {  
	              e.printStackTrace();  
	          }  
	          return null;  
	}  
	
}
