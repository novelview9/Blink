package com.bit.spring.blink.fra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bit.spring.blink.R;
import com.bit.spring.blink.Tab1.BlinkTestActivity;
import com.bit.spring.blink.Tab1.FaceGraphic;
import com.bit.spring.blink.Tab1.StartActivity;


public class HomeTab1 extends Fragment implements OnClickListener{

	Button blinkTestBtn;
	Button startBtn;
	public static final int REQUEST_CODE_ANOTHER = 1002;
	public static boolean temp = true;
	public static int num;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.index_1, null);

		blinkTestBtn = (Button) v.findViewById(R.id.blinkTestBtn);
		blinkTestBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				num = 1;
				FaceGraphic.count=0;
				Intent blinkTestIntent = new Intent(getActivity().getApplicationContext(), BlinkTestActivity.class);
				blinkTestIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

				startActivityForResult(blinkTestIntent, REQUEST_CODE_ANOTHER);
			}
		});

		startBtn = (Button) v.findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				num = 0;
				if (temp == false) {
					Intent startIntent = new Intent(getActivity().getApplicationContext(), StartActivity.class);
					startIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					FaceGraphic.count = 0;
					startActivity(startIntent);
				} else {
					Toast.makeText(getActivity().getApplicationContext(), "TEST를 먼저 실행해주세요.", Toast.LENGTH_LONG).show();
				}
			}
		});

		init(v);
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);


}
	private void init(View v) {

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

}
