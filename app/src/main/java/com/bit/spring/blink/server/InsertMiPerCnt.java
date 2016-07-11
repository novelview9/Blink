package com.bit.spring.blink.server;

import android.util.Log;

import com.bit.spring.blink.MainActivity;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Map;

public class InsertMiPerCnt extends SafeAsyncTask<Object> {

    private float blink;

    public InsertMiPerCnt(float blink){
        this.blink = blink;
    }

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public Object call() throws Exception {


        HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/blinkcnt/insertMiPerCnt");
        request.connectTimeout(2000).readTimeout(2000);

        request.send("androidId=" + MainActivity.deviceId + "&cnt=" + blink);

        if (request.code() != HttpURLConnection.HTTP_OK) {
            Log.d("---> initialValue :", String.valueOf(request.code()));
            return null;
        }

        Reader reader= request.bufferedReader();
        Map<String, Object> map2 =  GSON.fromJson(reader, Map.class);

        reader.close();

        Log.d("-----> insertMiPerCnt", (String) map2.get("insertMiPerCnt"));

        return null;
    }

}
