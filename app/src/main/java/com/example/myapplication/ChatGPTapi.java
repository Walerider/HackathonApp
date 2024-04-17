package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTapi {

    public void getResponse(String question, boolean isBot) {
        String apiKey = "sk-proj-KIhl4rjQ8L1ybWXgtaY0T3BlbkFJdxPaDmhtW1UsLu7y4nQ3";
        String url = "https://api.openai.com/v1/chat/completions";
        String s;
        if(isBot){
            String model = "gpt-3.5-turbo";
            String requestBody ="{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"system\", \"content\": \"" + question + "\"}]}";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error", "API failed", e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    if (body != null) {
                        Log.v("data", body);
                    } else {
                        Log.v("data", "empty");
                    }
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(body);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String textResult = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");

                    } catch (JSONException e) {
                        Log.e("123",body);
                    }
                }
            });
        }
        else{
            String response;
            String model = "gpt-3.5-turbo";
            String requestBody = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]}";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error", "API failed", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    if (body != null) {
                        Log.v("data", body);
                    } else {
                        Log.v("data", "empty");
                    }
                    JSONObject jsonObject = null;
                    String textResult = "Привет";
                    try {
                        jsonObject = new JSONObject(body);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        textResult = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        Log.e("123123",textResult);
                    } catch (JSONException e) {


                    }

                }
            });

    }
}
}
