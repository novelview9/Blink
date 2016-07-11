package com.bit.spring.blink.server;

import android.util.Log;

import com.bit.spring.blink.MainActivity;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Map;

public class InitialValue extends SafeAsyncTask<Object> {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public Object call() throws Exception {

        HttpRequest request = HttpRequest.get("http://192.168.1.99:9000/blink/history/initialValue");
        request.connectTimeout(2000).readTimeout(2000);

        request.send("androidId="  +MainActivity.deviceId  );

        if (request.code() != HttpURLConnection.HTTP_OK) {
            Log.d("---> initialValue :", String.valueOf(request.code()));
            return null;
        }

        Reader reader = request.bufferedReader();
        Map<String, Object> map = GSON.fromJson(reader, Map.class);

        reader.close();

        Log.d("-----> initialValue", (String) map.get("initialValue"));

        return null;
    }

}
