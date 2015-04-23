package com.example.screenenglish;


import android.annotation.SuppressLint;
import android.app.WallpaperManager; 
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.screenenglish.db.ScreenEnglishDB;

public class StartLockView implements Runnable{
	private Context context;
	private TextView detail;
	private EditText word;
	private Button read;
	private Button check;
	public static final String DB_NAME = "notes";
	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private Util util;
	private View view;
	public static View startLockView;
	private WindowManager.LayoutParams wmParams;
	private SharedPreferences settings;
	private String randomWord;
	private String randomDetail;
	private ScreenEnglishDB db;
	private WindowManager wm;
	private DisplayMetrics dm;
	@SuppressLint("InflateParams")
	public StartLockView(final Context context){
		 Log.d("screenenglish", "view");
		this.context = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context);//存储选择项
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = 2003;
		wmParams.format=1;
		wmParams.flags=32;
		wmParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		wmParams.horizontalWeight = 0;
	    wmParams.x = 0;
	    wmParams.y = 0;
	    dm=new DisplayMetrics();
	    wm.getDefaultDisplay().getMetrics(dm);
	    wmParams.width=dm.widthPixels;
	    wmParams.height= dm.heightPixels;
	    view = View.inflate(context,R.layout.window_layout,null);
	    view.setBackground(getWallpaper());
	    startLockView = view;
	    detail = (TextView) view.findViewById(R.id.show_detail);
	    word = (EditText) view.findViewById(R.id.write_word);
	    read = (Button) view.findViewById(R.id.change);
	    check = (Button) view.findViewById(R.id.check);
	    check.setOnClickListener(new View.OnClickListener()
	    {
	    	public void onClick(View v) {
	    		
				String content = word.getText().toString();
				if(content.equals(randomWord)){
					wm.removeView(startLockView);
					startLockView = null;
				}else{
					detail.setText("错误，请重试!\n"+randomDetail);
				}
			}
	    	});
	    read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshWord();
				detail.setText(randomDetail);
			}
		});
	    refreshWord();
    	detail.setText(randomDetail);
    	wm.addView(startLockView, wmParams);
	}
	  @SuppressWarnings("deprecation")
	public BitmapDrawable getWallpaper(){
			 WallpaperManager wallpaperManager = WallpaperManager  
			                    .getInstance(context);  
			          // 获取当前壁纸   
			           Drawable wallpaperDrawable = wallpaperManager.getDrawable();  
			           // 将Drawable,转成Bitmap   
			            Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();  
			           

		  return new BitmapDrawable(bm);
		}
	@Override
	public void run() {
	}
	public void setEnabled(boolean flag){
		read.setEnabled(flag);
		check.setEnabled(flag);
	}
	public void refreshWord(){
		db = new ScreenEnglishDB();
		randomWord = db.getRandomWord();
		randomDetail = db.getRandomDetail();
	}
}
