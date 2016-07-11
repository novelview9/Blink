package com.bit.spring.blink.fra;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.spring.blink.MainActivity;
import com.bit.spring.blink.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Map;

public class HomeTab3 extends Fragment implements OnChartValueSelectedListener {

	protected BarChart Chart;
	private Typeface mTf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.index_3, null);

		TextView avgCountView = (TextView) v.findViewById(R.id.avgConunt);

		TextView successRateView = (TextView) v.findViewById(R.id.successRate);

		getAvgOf7records(avgCountView, successRateView);


		Chart = (BarChart) v.findViewById(R.id.chart1);
		Chart.setOnChartValueSelectedListener(this);

		Chart.setDescription("");

		Chart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
		Chart.setPinchZoom(false);
		Chart.setDrawGridBackground(false);

		mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf"); //글씨체

		XAxis xAxis = Chart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setSpaceBetweenLabels(1);
		xAxis.setAxisLineColor(Color.rgb(153, 204, 255));

		YAxis leftAxis = Chart.getAxisLeft();
		leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(8, false);
		leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setSpaceTop(10f);
		leftAxis.setGridColor(Color.rgb(153, 204, 255));
		leftAxis.setAxisLineColor(Color.rgb(153, 204, 255));

		YAxis rightAxis = Chart.getAxisRight();
		rightAxis.setDrawGridLines(false);
		rightAxis.setTypeface(mTf);
		rightAxis.setLabelCount(8, false);
		rightAxis.setSpaceTop(10f);
		rightAxis.setAxisLineColor(Color.rgb(153, 204, 255));

		Legend l = Chart.getLegend();
		l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
		l.setForm(Legend.LegendForm.SQUARE);
		l.setFormSize(10f);
		l.setTextSize(10f);
		l.setXEntrySpace(8f);

		setData();

		return v;
	}

	private void setData() {
		int j = 0;
		int index = 0;

		String a="",b="",c="",d="",e="",f="",g="";

		for(Map<String, Object> item:MainActivity.statisticsList){

			if(index == 0 ) {
				a = (String) item.get("STARTDATE");
			}else if(index ==1){
				b = (String) item.get("STARTDATE");
			}else if(index == 2){
				c = (String) item.get("STARTDATE");
			}else if(index == 3){
				d = (String) item.get("STARTDATE");
			}else if(index == 4){
				e = (String) item.get("STARTDATE");
			}else if(index == 5){
				f = (String) item.get("STARTDATE");
			}else if(index == 6){
				g = (String) item.get("STARTDATE");
			}
			index++;
		}

		String[] recordDate = new String[] {a, b, c, d, e, f, g};

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			xVals.add(recordDate[i % 7]);
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

		for (Map<String, Object> item: MainActivity.statisticsList) {
			yVals1.add(new BarEntry(Float.parseFloat(String.valueOf(item.get("CNTAVG"))), j));
			j++;
		}

		BarDataSet set1 = new BarDataSet(yVals1, "Avg. Blink per record");
		set1.setBarSpacePercent(35f);
		set1.setColor(Color.rgb(0, 128, 255)); //그래프 색

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(xVals, dataSets);
		data.setValueTextSize(10f);
		data.setValueTypeface(mTf);

		Chart.setData(data);
	}

	@SuppressLint("NewApi")
	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

		if (e == null)
			return;

		RectF bounds = Chart.getBarBounds((BarEntry) e);
		PointF position = Chart.getPosition(e, YAxis.AxisDependency.LEFT);

		Log.i("bounds", bounds.toString());
		Log.i("position", position.toString());

		Log.i("x-index",
				"low: " + Chart.getLowestVisibleXIndex() + ", high: "
						+ Chart.getHighestVisibleXIndex());
	}

	private void getAvgOf7records(TextView avgCountView, TextView successRateView){
		float cntAvg=0, successRate= 0;
		for (Map<String, Object> item:  MainActivity.statisticsList) {
			cntAvg += Float.parseFloat(String.valueOf(item.get("CNTAVG")));
			successRate += Float.parseFloat(String.valueOf(item.get("SUCCESSRATE")));
		}
		cntAvg = (float) (Math.round(cntAvg/7*10)/10.0);
		successRate = (float) (Math.round(successRate/7*10)/10.0);

		Log.d("cntAvg =", String.valueOf(cntAvg));
		Log.d("successRate =", String.valueOf(successRate));

		avgCountView.setText(String.valueOf(cntAvg));
		successRateView.setText(String.valueOf(successRate));
	}

	public void onNothingSelected() {
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

}