package com.bit.spring.blink.Tab1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bit.spring.blink.MainActivity;
import com.bit.spring.blink.R;
import com.bit.spring.blink.server.InsertMiPerCnt;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class RealTimeGraphFragment extends Fragment {

    private CombinedChart chart1;
    private RelativeLayout frameLayout;
    private Button pauseBtn;
    private Button reStartBtn;
    private Button stopBtn;
    private ProgressBar progressBar;

    public TextView textview;
    public TextView mText;

    public List<String> minutesList = new ArrayList<String>(); //1분마다 리스트로 담음
    public ArrayList<Entry> entries = new ArrayList<Entry>(); //1분마다 눈깜빡임 카운트를 리스트로 담음

    public float blink =0;
    public CombinedChart mChart;         //그래프 인스턴스 변수

    public String minutes = String.valueOf(Float.valueOf((float) 0.5)); //진행시간(분)
    public int xIndex = 0;                       //X축 index 초기값

    public Handler graphHandler;
    public Handler updateCntHandler;

    ToggleButton toggleButton2;
    ImageButton alaramquestionmark;

    CountDownTimer mCountDown;
    int value= 0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        frameLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_graph, container, false);
        chart1 = (CombinedChart)frameLayout.findViewById(R.id.chart1);
        pauseBtn = (Button)frameLayout.findViewById(R.id.pauseBtn);
        reStartBtn= (Button)frameLayout.findViewById(R.id.reStartBtn);
        stopBtn = (Button)frameLayout.findViewById(R.id.stopBtn);
        progressBar = (ProgressBar)frameLayout.findViewById(R.id.progressBar);
        mText = (TextView)frameLayout.findViewById(R.id.timetext2);    //시간 나타내는 텍스트
        textview = (TextView) frameLayout.findViewById(R.id.textview);  //count값 프로그레스바에 나타내는 텍스트

        toggleButton2 = (ToggleButton)frameLayout.findViewById(R.id.toggleButton2);
        alaramquestionmark =  (ImageButton) frameLayout.findViewById(R.id.alaramquestionmark);

        progressBar.setIndeterminate(false);

        blinkHandler(); // 그래프 그리기 및 데이터베이스 통신

        FuntionProgressBar(); //progressBar 메서드

        blinkStopBtn(); //stopBtn 클릭

        blinkPause(); // pauseBtn 클릭

        blinkRestart(); //restartBtn 클릭

        AlarmQuestionMark(); // 알람 물음표 클릭 설명

        return frameLayout;
    }

    void AlarmQuestionMark(){
        alaramquestionmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("제목");
                builder.setMessage("사용설명서");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "닫혔다.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

    }

    /*progressBar*/
    void FuntionProgressBar(){
        mCountDown = new CountDownTimer(300 * 100, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                value = (int) (30000 - millisUntilFinished);
                mText.setText(value / 1000 + "/30s");
                progressBar.setProgress(value);
            }
            @Override
            public void onFinish() {

                mCountDown.start();
            }
        }.start();
    }

    //그래프 그리기 핸들러
    private void blinkHandler(){
        graphHandler = new Handler(); //핸들러 생성
        graphHandler.postDelayed(graphRun, 30000); //grahpRun 메서드 요청


        updateCntHandler = new Handler();
        updateCntHandler.post(update);
    }

    //쓰레드 생성 및 그래프 그리기에 대한 반복작업
    private Runnable graphRun = new Runnable() {

        @Override
        public void run() {

            if(toggleButton2.isChecked()&&FaceGraphic.count<8) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
                new AlarmReceiver().onReceive(getContext(), intent);
            }
            else {
            }
            Log.d("myTask", "run(), blink = " + blink);
            minutesList.add(minutes);
            drawGrahp(); //그래프 그리는 함수 요청

            new InsertMiPerCnt(blink).execute(); //서버에 인설트!

            FaceGraphic.count=0;
            minutes = Float.toString((float)(Float.parseFloat(minutes) + 0.5));

            //알람 울리는 클릭리스너




            graphHandler.postDelayed(graphRun, 30000); //30초마다 grahpRun메서드 반복
        }
    };

    private Runnable update = new Runnable() {

        @Override
        public void run() {
            blink =  FaceGraphic.count;
            //textView.setText(String.valueOf(FaceGraphic.count));
            Log.d("------->", "update blink" + String.valueOf(blink));

            if(FaceGraphic.count<10){
                textview.setText(0+String.valueOf(FaceGraphic.count));
            }
            else{
                textview.setText(String.valueOf(FaceGraphic.count));
            }

            updateCntHandler.postDelayed(update, 30); //0.03초마다 grahpRun메서드 반복
        }
    };

    public void drawGrahp(){
        mChart = (CombinedChart) chart1; //레이아웃
        mChart.setDescription("");

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        // 그래프 x,y축 동적 증가
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.LINE}); //라이브러리

        YAxis rightAxis = mChart.getAxisRight(); //그래프 축 오른쪽
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();  //그래프 축 왼쪽
        leftAxis.setDrawGridLines(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        CombinedData data = new CombinedData(minutesList);

        data.setData(generateLineData());

        mChart.setData(data);
        mChart.invalidate();
    }

    //그래프 UI 기능
    public LineData generateLineData() {

        LineData d = new LineData();

        Log.d("--------------->", "graph blink"+String.valueOf(blink));
        entries.add(new Entry(blink, xIndex));

        xIndex++;

        LineDataSet set = new LineDataSet(entries, "Blink count per minute");
        set.setColor(Color.rgb(210, 105, 30));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(210, 105, 30));
        set.setCircleSize(0f);
        set.setFillColor(Color.rgb(210, 105, 30));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(0f);
        set.setValueTextColor(Color.rgb(210, 105, 30));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

    private void blinkRestart(){
        reStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDown.start();
                final Button startBtn = (Button) reStartBtn;
                FaceGraphic.count = 0;
                graphHandler.postDelayed(graphRun, 30000);
                updateCntHandler.post(update);
                startBtn.setVisibility(View.INVISIBLE);
                pauseBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void blinkPause(){
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDown.cancel();
                FaceGraphic.count = 0;                                 //깜빡임 횟수 초기화
                graphHandler.removeCallbacksAndMessages(null); //handler.postDelayed() 중지
                updateCntHandler.removeCallbacksAndMessages(null); //updateCntHandler.postDelayed() 중지

                final Button startBtn = (Button) pauseBtn;
                startBtn.setVisibility(View.INVISIBLE);
                reStartBtn.setVisibility(View.VISIBLE);
            }
        });
    }
    private void blinkStopBtn(){
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                graphHandler.removeCallbacksAndMessages(null); //handler.postDelayed() 중지
                updateCntHandler.removeCallbacksAndMessages(null); //updateCntHandler.postDelayed() 중지

                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        graphHandler.removeCallbacksAndMessages(null);
        updateCntHandler.removeCallbacksAndMessages(null);
        mCountDown.cancel();
        FaceGraphic.count = 0;

    }
}

