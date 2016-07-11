package com.bit.spring.blink.Tab1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bit.spring.blink.R;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String TAG_LOG	= "AlarmReceiver";
	
	public static MySoundPlay mplay = null;
	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT);
		try {
			if(mplay == null) {
				mplay = new MySoundPlay(context, R.raw.dingdong);
			}
		    mplay.play();
			mplay = null;
		}catch( Exception e ) {
			Output.e(TAG_LOG, e.toString());
		}
	}
}
