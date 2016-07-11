package com.bit.spring.blink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity

{
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final ViewFlipper loading = (ViewFlipper) findViewById(R.id.viewFlipper);

        Handler handler = new Handler();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // UI를 손대기 위해서는 runOnUiThread를 사용해야 함
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // 다음 이미지 표시
                        loading.showNext();
                    }

                });
            }
        }, 240, 240);

        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        handler.postDelayed(new splashhandler(), 3000);    // ms, 3초후 splashhandler 클래스 생성
    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }
}
