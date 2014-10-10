package com.xbw.WallPaperSetter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startService(new Intent(this,WallPaperSettingService.class)
        .putExtra(WallPaperSettingService.INTENT_KW_PATHLIST,getPicFile("/sdcard/myfile/Pic/"))
        .putExtra(WallPaperSettingService.INTENT_KW_TIMEWAIT, 50));
    }
    
    
    
    @Override
	protected void onDestroy() {
		stopService(new Intent(this,WallPaperSettingService.class));
		super.onDestroy();
	}



	String[] getPicFile(String path){
    	File[] fs = new File(path).listFiles();
    	Log.v("tag", ""+fs.length);
    	int index = 0;
    	String[] result = new String[fs.length];
    	for(File f: fs){
    		if(f.isFile()){
    			result[index] = f.getAbsolutePath();
    			Log.v("result",result[index]);
    			++index;
    		}
    	}
    	return result;
    }
}
