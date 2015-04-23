package com.easyweather.app.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceUtil {
	
		//定义Web Service的命名空间
	  static final String SERVICE_NS ="http://WebXml.com.cn/";  
	  	//定义Web Service提供服务的URL  
	  static final String SERVICE_URL ="http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx"; 
	 
	  /*
	  *  通过service服务器获取城市天气信息
	  */
	  public static SoapObject getWeatherByCity(String cityCode){ 
		  String methodName = "getWeather";  
		  HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);  
		  ht.debug = true;  
		  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
		  SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);  
		  soapObject.addProperty("theCityCode", cityCode);  
		  envelope.bodyOut = soapObject;  
		  // 设置与.Net提供的Web Service保持较好的兼容性  
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
