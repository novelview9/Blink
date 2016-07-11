package com.bit.spring.blink.Tab2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.spring.blink.MainActivity;
import com.bit.spring.blink.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.LineDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bit-user on 2015-12-09.
 */
public class DetailViewFragment extends Fragment {

    LineChart mChart;
    List<Map<String, Object>> list = MainActivity.detailViewList;
    int size = list.size();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detailview, null);


        String[] data2 = ((MainActivity) getActivity()).curData;
        TextView mText01 = (TextView) v.findViewById(R.id.datetext);
        mText01.setText(data2[0]);
        TextView mText02 = (TextView) v.findViewById(R.id.timetext1);
        mText02.setText(data2[1]);
        TextView mText03 = (TextView) v.findViewById(R.id.timetext2);
        mText03.setText(data2[2]);
        TextView mText04 = (TextView) v.findViewById(R.id.timetext3);
        mText04.setText(""+size/2);

        TextView mText05 = (TextView) v.findViewById(R.id.blinktext);
        mText05.setText(data2[3]);
        TextView mText06 = (TextView) v.findViewById(R.id.successtext);
        mText06.setText(data2[4]);

        mChart = (LineChart) v.findViewById(R.id.chart2);
        mChart.setDrawGridBackground(false);

        mChart.setDescription("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(true);

        //X축
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.rgb(153, 204, 255));
        xAxis.setGridColor(Color.rgb(153, 204, 255));
        xAxis.setTextColor(Color.rgb(153, 204, 255));
        //Y축 평행 점선
        LimitLine ll1 = new LimitLine(8f, "");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(5f, 5f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(12f);


        ll1.setLineColor(Color.rgb(168, 255, 174));
        ll1.setTextColor(Color.rgb(168, 255, 174));

        //y축
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMaxValue(20f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisLineColor(Color.rgb(153, 204, 255));
        leftAxis.setGridColor(Color.rgb(153, 204, 255));
        leftAxis.setTextColor(Color.rgb(153, 204, 255));

        YAxis rightAxis =         mChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);

        setData(size);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        return v;
    }
    private void setData(int count) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            float y = (float) 0.5f+i * 0.5f;

            xVals.add((y) + "");

        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            Map<String, Object> item = list.get(i);
            yVals.add(new Entry(Float.valueOf(String.valueOf(item.get("CNT"))), i));
        }



        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "깜빡임 횟수");
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleSize(4f);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(173, 197, 221));
        set1.setFillColor(Color.rgb(19, 94, 172));
        set1.setFillAlpha(85);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
// set data
        mChart.setData(data);

    }

}
