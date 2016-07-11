package com.bit.spring.blink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bit.spring.blink.Tab2.DetailViewFragment;
import com.bit.spring.blink.Tab2.IconTextItem;
import com.bit.spring.blink.fra.HomeTab1;
import com.bit.spring.blink.fra.HomeTab2;
import com.bit.spring.blink.fra.HomeTab3;
import com.bit.spring.blink.fra.HomeTab4;
import com.bit.spring.blink.server.DetailView;
import com.bit.spring.blink.server.HistoryList;
import com.bit.spring.blink.server.Statistics;

import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnClickListener {
    private ImageView mSelBg;
    public LinearLayout content_container, mTab_item_container;
    private int mSelectIndex = 0;

    public static List<Map<String, Object>> list;               //Tab2 화면에 보여줄 리스트
    public static List<Map<String, Object>> detailViewList;     //Tab2 상세페이지의 리스트
    public String[] curData;                                    //Tab2 상세페이지에 보여줄 데이터
    public static List<Map<String,Object>> statisticsList;      //Tab3 통계화면 보여줄 리스트

    public static String deviceId;                              //안드로이드 고유 ID

    private final int REQUEST_CODE_ANOTHER = 1001;          //액티비티 실행시 요청번호
    private final int REQUEST_CODE_DETAILVIEW = 2001;
    private final int REQUEST_CODE_TAB3 = 3001;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //메인액티비티 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();                           //변수로 레이아웃 참조 및 리스너 정의

        changeFragment(new HomeTab1(), true);  //첫 화면 탭1

        getAndroidID();                   //안드로이드 ID 가져오기

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void init() {
        mTab_item_container = (LinearLayout) findViewById(R.id.tab_item_container);

        findViewById(R.id.tab_bt_1).setOnClickListener(this);         //클릭리스너 등록
        findViewById(R.id.tab_bt_2).setOnClickListener(this);
        findViewById(R.id.tab_bt_3).setOnClickListener(this);
        findViewById(R.id.tab_bt_4).setOnClickListener(this);

        mSelBg = (ImageView) findViewById(R.id.tab_bg_view);                     //tab_bg_view 'ID'를 이미지뷰타입의 변수로 참조
        mSelBg.getLayoutParams().width = mTab_item_container.getWidth() / 4;   //tab_bg_view의 가로길이를 mTab_item_container길이의 4분의1로 지정

        content_container = (LinearLayout) findViewById(R.id.content_container); //프래그먼트가 올라갈 컨테이너 지정
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mSelBg.getLayoutParams().width = mTab_item_container.getWidth() / 4;  // 중복?
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.tab_bt_1:
                startAnimation(0);                    //현재의 인덱스를 파라미터로 넘겨 애니메이션 요청
                changeFragment(new HomeTab1(), true);      //프래그먼트 전환

                break;

            case R.id.tab_bt_2:
                startAnimation(1);
                Intent intent = new Intent(getApplicationContext(),HistoryList.class);   //HistoryList 인텐트 생성 및 참조
                startActivityForResult(intent, REQUEST_CODE_ANOTHER);                  //응답을 받기위한 요청 , 서버와 통신 후 돌아옴

                break;

            case R.id.tab_bt_3:
                startAnimation(2);
                Intent intentTab3 = new Intent(getApplicationContext(),Statistics.class);
                startActivityForResult(intentTab3, REQUEST_CODE_TAB3);

                break;

            case R.id.tab_bt_4:
                startAnimation(3);
                changeFragment(new HomeTab4(), true);

                break;

            default:
                break;
        }
    }

    private void startAnimation(int tabBtnIndex) {

        View last = mTab_item_container.getChildAt(mSelectIndex); //이전 위치의 뷰
        View now = mTab_item_container.getChildAt(tabBtnIndex);     //현재 위치의 뷰

        TranslateAnimation tA = new TranslateAnimation(last.getLeft(), now.getLeft(), 0, 0);        //TranslateAnimation(x,x,y,y) 생성자인자로 위치지정(X -> X , Y -> Y)
        tA.setDuration(0);                                                                        //이동시간
        tA.setFillAfter(true);                                                                     //이동한 좌표에 고정

        mSelBg.startAnimation(tA);                                                                 //스타트
        mSelectIndex = tabBtnIndex;                                                               //현재 tabBtn 인덱스
    }

    private void changeFragment(Fragment fragment, boolean temp) {  //프래그먼트 전환
        FragmentManager fM = getSupportFragmentManager();              //액티비티에서 프래그먼트 메니저 요청
        FragmentTransaction fT = fM.beginTransaction();                //프래그먼트 트랜잭션 참조

        if(temp == true ){
            fM.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            fT.addToBackStack(null);                                              //커밋을 호출하기전 프래그먼트의 모든정보를 트랙잭션단위로 백스택에 추가, 이전버튼 누를시 백스택에 추가된 정보로 되돌림, 인자는 저장되는 정보의 Key값
        }
        fT.replace(R.id.content_container, fragment).commit();  //하나의 Fragment가 존재하며 바꿔치기 한다. *add로 Fragment를 추가하면 View가 계속 쌓여 성능이 느려짐
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //startActivityForResult의 응답을 받아준다. setResult로 요청 된다.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ANOTHER ){    //requestCode로 startActivityForResult의 요청을 구분
            changeFragment(new HomeTab2(), true);             //데이터 통신 후, 프레그먼트 전환

        }else if(resultCode == 99){                     //resultCode로 setResult의 요청을 구분
            Toast.makeText(getApplicationContext(), "테스트가 완료되었습니다.",Toast.LENGTH_SHORT).show();

        }else if(requestCode == REQUEST_CODE_DETAILVIEW){
            changeFragment(new DetailViewFragment(), false);

        }else if(requestCode == REQUEST_CODE_TAB3){
            changeFragment(new HomeTab3(), true);
        }
    }

    @Override
    public void onBackPressed(){      //이전버튼 누를 시
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack();
        }
        else {

            backPressCloseHandler.onBackPressed();
        }
    }

    private void getAndroidID(){    //안드로이드 ID를 얻는 메서드
        TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);    //getSystemService 메서드를 사용하려면 매니페스트에 uses-permission를 추가해야한다,
        deviceId = tm.getDeviceId();
    }

    public void detailListView(IconTextItem curItem){   //HomeTab2 프래그먼트에서 요청하는 메서드, 프래그먼트에서 액티비티를 띄울 수 없기에 사용함.
        curData = curItem.getData();  //다른 액티비티에서 사용할 변수이므로 클래스변수로 선언

        Intent detailViewintent = new Intent(this, DetailView.class);
        detailViewintent.putExtra("curData", curData);  //DetailView에 넘길 데이터

        detailViewintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);  //Activity Stack 내에 다른 Activity는 모두 onDestory() 시킴(RootActivity 제외)
        startActivityForResult(detailViewintent, REQUEST_CODE_DETAILVIEW);
    }

}