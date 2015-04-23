package com.example.screenenglish.db;

import com.example.screenenglish.Util;
import com.example.screenenglish.service.StartScreenLockService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenEnglishDB {
	public final String LOGNAME = "youdaoDB";
	public static final String DB_NAME = "notes";
	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private SQLiteDatabase db;
	private long count;
	private int randomId;
	private String dbFilePath;
	private Util util;
	private String randomWord;
	private String randomDetail;
	
	public ScreenEnglishDB(){
		try{
    		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    			this.dbFilePath = Environment.getExternalStorageDirectory()+ "/Youdao/Dict/" + "notes.db" ;
    			if(!StartScreenLockService.ONOFF){Log.d(LOGNAME,"get notes is ok");}
    		}
    		db = SQLiteDatabase.openDatabase(this.dbFilePath, null, 1);
    		String sql = "SELECT COUNT(*) FROM "+ DB_NAME;
    		SQLiteStatement statement = db.compileStatement(sql);
    		count = statement.simpleQueryForLong();//获取表notes的总行数
    		randomId = (int) (Math.random()*count)+ 1;//随机获取notes表中一行
    		String num = Integer.toString(randomId);
    		if(db != null){
    			Cursor cursor = db.query("notes", null, "_id =?",new String[]{num}, null, null, null);
    			if(cursor.moveToFirst()){
    				do{
    					randomWord = cursor.getString(cursor.getColumnIndex("word"));
    					randomDetail = cursor.getString(cursor.getColumnIndex("detail"));
    				
    				}while(cursor.moveToNext());
    			}
    		cursor.close();
    		
    		}
    		db.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
				
		if(!StartScreenLockService.ONOFF){Log.d(LOGNAME,"word changed");}
	}
	public String getRandomWord(){
		return randomWord;
	}
	public String getRandomDetail(){
		return randomDetail;
	}
		
}


