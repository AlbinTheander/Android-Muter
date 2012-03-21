package com.jayway.muter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MuterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void mute(View view) {
    	Intent intent = new Intent(this, MuterService.class);
    	startService(intent);
	}
    
    public void unMute(View view) {
    	Intent intent = new Intent(this, MuterService.class);
    	intent.setData(Uri.parse("stop"));
    	startService(intent);
    }
}