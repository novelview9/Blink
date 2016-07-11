package com.bit.spring.blink.fra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bit.spring.blink.R;
import com.bit.spring.blink.Tab4.SettingList1;
import com.bit.spring.blink.Tab4.SettingList3;
import com.bit.spring.blink.Tab4.SettingList4;

public class HomeTab4 extends Fragment implements AdapterView.OnItemClickListener {

	private ListView listview;
	private ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.index_4, container, false);


		ImageView banner = (ImageView) v.findViewById(R.id.banner);
		banner.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse("http://www.bitacademy.com/"));
				startActivity(intent);
			}
		});


		adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.setting_list);

		listview = (ListView) v.findViewById(R.id.settinglist);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(onClickListItem);

		adapter.add(("도움말, 앱소개"));
		adapter.add(("메일로 문의"));
		adapter.add(("개발자 정보"));
		adapter.add(("유료구매하기"));

		return v;
	}

	private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			switch (position){
				case 0:
					FragmentTransaction setting1 = getFragmentManager().beginTransaction();
					setting1.replace(R.id.content_container, new SettingList1());
					setting1.addToBackStack(null);
					setting1.commit();
					break;
				case 1:
					Intent intent = new Intent(Intent.ACTION_SEND);
					// 받는 사람 (받는 사람은 스트링 배열로 표현해야 함)
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "daeun_k@naver.com" });
					// HTML 형식으로 이메일을 보낼 경우 Html 클래스로 표현
					intent.putExtra(Intent.EXTRA_SUBJECT, "");
					intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(""));
					intent.setType("message/rfc822");
					startActivity(intent);
					break;

				case 2:
					FragmentTransaction setting3 = getFragmentManager().beginTransaction();
					setting3.replace(R.id.content_container, new SettingList3());
					setting3.addToBackStack(null);
					setting3.commit();
					break;
				case 3:
					FragmentTransaction setting4 = getFragmentManager().beginTransaction();
					setting4.replace(R.id.content_container, new SettingList4());
					setting4.addToBackStack(null);
					setting4.commit();
					break;

			}
		}
	};


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}