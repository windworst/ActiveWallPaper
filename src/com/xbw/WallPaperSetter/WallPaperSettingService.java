package com.xbw.WallPaperSetter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;

public class WallPaperSettingService extends Service{

	static String INTENT_KW_PATHLIST = "path_list";
	static String INTENT_KW_TIMEWAIT = "time_wait";
	String[] picList = null;
	int timeWait = 30;
	volatile boolean isStillRun = false;
	Thread thread = null;
	WallpaperManager wm = null;
	Runnable runner = new Runnable() {
		@Override
		public void run() {
			if (picList==null || picList.length==0)return;
			isStillRun = true;
			int index = 0;
			while(isStillRun) {
				String path = picList[index];
				++index;
				if(index >= picList.length) {
					index = 0;
				}
				try {
					InputStream is = new FileInputStream(path);
					try {
						wm.setStream(is);
						try {
							Thread.sleep(timeWait);
						} catch (InterruptedException e) {
						}
					} catch (IOException e1) {
					}
				} catch (FileNotFoundException e1) {
				}
			}
			isStillRun = false;
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		//当线程仍在运行时,令线程退出
		if(thread!=null && isStillRun){
			thread = null;
			isStillRun = false;
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//从Intent中拿图片路径
		picList = intent.getStringArrayExtra(INTENT_KW_PATHLIST);
		timeWait = intent.getIntExtra(INTENT_KW_TIMEWAIT, timeWait);
		wm = WallpaperManager.getInstance(getApplicationContext());
		
		//开启线程,启动换图片过程
		thread = new Thread(runner);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
}
