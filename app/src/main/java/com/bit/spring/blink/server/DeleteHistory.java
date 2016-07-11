package com.bit.spring.blink.server;

import android.util.Log;

import com.bit.spring.blink.Tab2.IconTextItem;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class DeleteHistory extends SafeAsyncTask<Object> {

    String[] curData;

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    //String[] data = curData;
    public DeleteHistory(IconTextItem curItem){
        this.curData = curItem.getData();
    }

    @Override
    public List<Map<String, Object>> call() throws Exception {

        String no2 = curData[5];
        int no = (int) Float.parseFloat(no2);

        HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/history/deletehistory/");

        request.connectTimeout(2000).readTimeout(2000);
        request.send("no=" + no);

        if (request.code() != HttpURLConnection.HTTP_OK) {
            Log.d("---> DeleteError :", String.valueOf(request.code()));
            return null;
        }

        //4. JSON 파싱
        Reader reader = request.bufferedReader();
        Map<String, Object> map = GSON.fromJson(reader, Map.class);
        reader.close();

        Log.d("DeleteHistory", (String) map.get("DeleteHistory"));

        return null;
    }

}
