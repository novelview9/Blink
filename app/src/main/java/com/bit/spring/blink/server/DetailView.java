package com.bit.spring.blink.server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bit.spring.blink.MainActivity;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by bit-user on 2015-12-03.
 */

    public class DetailView extends Activity{

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private int no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = getIntent();

        String[] data = resultIntent.getStringArrayExtra("curData");

        String no2 = data[5];
        no = (int) Float.parseFloat(no2);

        new DetailViewAjax(resultIntent).execute();
    }

    public class DetailViewAjax extends SafeAsyncTask<Object> {
        public Intent resultIntent;

        DetailViewAjax(Intent resultIntent){
            this.resultIntent = resultIntent;
        }

        @Override
        public List<Map<String, Object>> call() throws Exception {

            HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/blinkcnt/");

            request.connectTimeout(2000).readTimeout(2000);
            request.send("no=" + no);

            if (request.code() != HttpURLConnection.HTTP_OK) {
                Log.d("---> DetailViewErorr :", String.valueOf(request.code()));
                return  MainActivity.detailViewList = null;
            }

            //4. JSON 파싱
            Reader reader = request.bufferedReader();
            Map<String, Object> map = GSON.fromJson(reader, Map.class);

            reader.close();
            Log.d("-----> DetailViewList", (String) map.get("getDetailViewList")); // 통신 성공

            MainActivity.detailViewList = (List<Map<String, Object>>) map.get("detailView");

            for (Map<String, Object> item : MainActivity.detailViewList) {
                Log.d("11111111111111", String.valueOf(item.get("CNT")));
                Log.d("22222222222222", String.valueOf(item.get("REGDATE")));
            }

            return MainActivity.detailViewList;

            }

        @Override
        protected void onSuccess(List<Map<String, Object>> detailViewList) throws Exception {
            super.onSuccess(detailViewList);
            Log.d("-------->","onSuccess");

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
