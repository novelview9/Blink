package com.bit.spring.blink.fra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bit.spring.blink.MainActivity;
import com.bit.spring.blink.R;
import com.bit.spring.blink.Tab2.IconTextItem;
import com.bit.spring.blink.Tab2.IconTextListAdapter;
import com.bit.spring.blink.Tab2.SwipeDismissListViewTouchListener;
import com.bit.spring.blink.server.DeleteHistory;

import java.util.Map;


public class HomeTab2 extends Fragment implements OnClickListener {
	private  IconTextListAdapter adapter;
	private  Resources res;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.index_2, null);
		float a = (float) 100.00;
		float b = (float) 80.00;

		res = getResources();
		listView = (ListView) v.findViewById(R.id.listView);  // 리스트뷰 객체 참조
		adapter = new IconTextListAdapter(getActivity()); // 어댑터 객체 생성

		if (MainActivity.list.size() > 1){
			for (Map<String, Object> item : MainActivity.list) {

				if (Float.valueOf(String.valueOf(item.get("SUCCESSRATE"))) == a) {
					adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.icon01), String.valueOf(item.get("STARTDATE")), String.valueOf(item.get("STARTTIME")) + "~", String.valueOf(item.get("ENDTIME")), String.valueOf(item.get("CNTAVG")), String.valueOf(item.get("SUCCESSRATE")) + "%", String.valueOf(item.get("HISTORYNO"))));
				} else if (Float.valueOf(String.valueOf(item.get("SUCCESSRATE"))) >= b && Float.valueOf(String.valueOf(item.get("SUCCESSRATE"))) < a) {
					adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.icon02), String.valueOf(item.get("STARTDATE")), String.valueOf(item.get("STARTTIME")) + "~", String.valueOf(item.get("ENDTIME")), String.valueOf(item.get("CNTAVG")), String.valueOf(item.get("SUCCESSRATE")) + "%", String.valueOf(item.get("HISTORYNO"))));
				} else {
					adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.icon03), String.valueOf(item.get("STARTDATE")), String.valueOf(item.get("STARTTIME")) + "~", String.valueOf(item.get("ENDTIME")), String.valueOf(item.get("CNTAVG")), String.valueOf(item.get("SUCCESSRATE")) + "%", String.valueOf(item.get("HISTORYNO"))));
				}
			}

		/*리스트뷰에 어댑터 설정*/
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					IconTextItem curItem = (IconTextItem) adapter.getItem(position);
					((MainActivity)getActivity()).detailListView(curItem);
				}
			});


		}else{
			Toast.makeText(getContext(), "내역이 없습니다.",Toast.LENGTH_SHORT).show();
		}
		swipeDelete();

		return v;
	}

	@Override
	public void onClick(View v) {
	}

	public void swipeDelete(){
		SwipeDismissListViewTouchListener touchListener =
				new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}
					@Override
					public void onDismiss(ListView listView2, int[] reverseSortedPositions) {
						for (final int position : reverseSortedPositions) {
							AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
							alert_confirm.setMessage("리스트를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

											IconTextItem curItem = (IconTextItem) adapter.getItem(position); //통신 데이터
											new DeleteHistory(curItem).execute(); //통신 연결!

											adapter.remove(position);
											adapter.notifyDataSetChanged();
										}
									}).setNegativeButton("CANCEL",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// 'No'
											return;
										}
									});
							AlertDialog alert = alert_confirm.create();
							alert.show();
						}
					}
				});
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());
	}
}
