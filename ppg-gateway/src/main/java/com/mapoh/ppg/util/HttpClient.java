package com.mapoh.ppg.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author mabohv
 * @date 2024/12/26 17:18
 */

@Component
public class HttpClient {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static JSONObject get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = CLIENT.newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }
}
