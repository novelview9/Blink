package com.bit.spring.blink.Tab1;

import android.content.Context;
import android.media.MediaPlayer;

public class MySoundPlay {
	MediaPlayer mp = null;
	public MySoundPlay ( Context context, int id ) {
		mp = MediaPlayer.create(context, id);
	}    	  
	public void play() {
		mp.seekTo(0);
		mp.start();
	}
}
