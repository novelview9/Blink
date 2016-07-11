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

    public class HistoryList extends Activity{

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static boolean temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = getIntent();
        new HistoryListAjax(resultIntent).execute();
    }

    public class HistoryListAjax extends SafeAsyncTask<Object> {
        public Intent resultIntent;

        HistoryListAjax(Intent resultIntent){
            this.resultIntent = resultIntent;
        }

        @Override
        public List<Map<String, Object>> call() throws Exception {

            HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/history/");

            request.connectTimeout(2000).readTimeout(2000);
            request.send("androidId=" + MainActivity.deviceId);

            if (request.code() != HttpURLConnection.HTTP_OK) {
                Log.d("---> historyListEroor :", String.valueOf(request.code()));
                MainActivity.list = null;
            }


            //4. JSON 파싱
            Reader reader = request.bufferedReader();
            Map<String, Object> map = GSON.fromJson(reader, Map.class);

            reader.close();
            Log.d("-----> historyList", (String) map.get("getList")); // 통신 성공

            MainActivity.list = (List<Map<String, Object>>) map.get("list");


            return MainActivity.list;
        }

        @Override
        protected void onSuccess(List<Map<String, Object>> list) throws Exception {
            super.onSuccess(list);
            Log.d("-------->","onSuccess");

            setResult(RESULT_OK, resultIntent);
            finish();

        }
    }
}
