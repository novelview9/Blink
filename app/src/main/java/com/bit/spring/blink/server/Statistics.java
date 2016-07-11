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

public class Statistics extends Activity{

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = getIntent();
        new StatisticsAjax(resultIntent).execute();
    }

    public class StatisticsAjax extends SafeAsyncTask<Object> {
        public Intent resultIntent;

        StatisticsAjax(Intent resultIntent){
            this.resultIntent = resultIntent;
        }

        @Override
        public List<Map<String, Object>> call() throws Exception {

            HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/history/statistics/");

            request.connectTimeout(2000).readTimeout(2000);
            request.send("androidId=" + MainActivity.deviceId);

            if (request.code() != HttpURLConnection.HTTP_OK) {
                Log.d("---> StatisticsErorr :", String.valueOf(request.code()));
                return MainActivity.statisticsList = null;
            }


            //4. JSON 파싱
            Reader reader = request.bufferedReader();
            Map<String, Object> map = GSON.fromJson(reader, Map.class);

            reader.close();
            Log.d("-----> Statistics", (String) map.get("statisticsCheck")); // 통신 성공

            MainActivity.statisticsList = (List<Map<String, Object>>) map.get("getStatistics");


            return MainActivity.statisticsList;
        }

        @Override
        protected void onSuccess(List<Map<String, Object>> statisticsList) throws Exception {
            super.onSuccess(statisticsList);
            Log.d("-------->","onSuccess");

            setResult(RESULT_OK, resultIntent);
            finish();

        }
    }
}
