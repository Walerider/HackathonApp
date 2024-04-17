package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import kotlin.Unit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String login;
    ImageButton sendButton;
    EditText enterText;
    View blockingInput;
    RecyclerView recyclerView;
    ArrayList<Message> messages;
    MessageListAdapter adapter;
    TextView savedMessage;
    final String[] textResult = new String[1];
    ImageView botImage;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String start = "";
        new ChatGPTapi().getResponse(start,true);//copy + paste только с изменением первого аргумента
        messages = savedChat();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLogin();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        enterText = findViewById(R.id.enter_text);
        sendButton = findViewById(R.id.send_button);
        adapter = new MessageListAdapter(this,messages);
        adapter.notifyDataSetChanged();
        botImage = findViewById(R.id.bot_view);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        blockingInput = findViewById(R.id.blocking_input);
        savedMessage = findViewById(R.id.for_message);
        savedMessage.setTextColor(Color.TRANSPARENT);
        botImage.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new PersonalAccountFragment()).commit();
        });
        sendButton.setOnClickListener(v ->{
            if(enterText.getText().toString().length() != 0){
                sendMessage(login,enterText.getText().toString(),false);
                adapter.notifyDataSetChanged();
                String ans =enterText.getText().toString();
                enterText.setText("");
                getResponse(ans, true);
                adapter.notifyDataSetChanged();
                blockingInput.setClickable(true);
                blockingInput.setBackgroundColor(R.color.black);
                botImage.setImageResource(R.drawable.bot_write);
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        sendMessage("Сигналл", savedMessage.getText().toString(),true);
                        blockingInput.setBackgroundColor(Color.TRANSPARENT);
                        blockingInput.setClickable(false);
                        botImage.setImageResource(R.drawable.base_bot);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(login != null && messages.size() == 0) {
            sendMessage("Сигналл", "Приветствую, путешественник! Я - бот \"Сигналл\", твой верный проводник в мире цифровых тайн и опасностей. Однако, случился неприятный инцидент - из-за бага в моей системе я временно потерял свои знания о кибербезопасности цифровизации и даже искусственному интеллекту ! Теперь я могу лишь подсказывать тебе, но без твоей помощи я не смогу восстановить свои данные.", true);
        }
    }

    private void sendMessage(String sender, String message, boolean isBot){
        messages.add(new Message(message,sender,isBot));
        String isbot = isBot ? "1" : "0";
        writeToFile(message + "\\" + sender + "\\" + isbot + "|",this,"chat");
        adapter.notifyDataSetChanged();
    }
    private ArrayList<Message> savedChat(){
        ArrayList<Message> list = new ArrayList<>();
        InputStream fin = null;
        try {
            fin = this.openFileInput("chat.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin));
            String text = "";
            Message message = new Message("","",false);
            int count = 0;
            String i;
            while((i=in.readLine()) != null){
                for (int j = 0; j < i.length(); j++) {
                    if(i.charAt(j) =='\\' && count == 0){
                        message.setMessage(text);
                        text = "";
                        count++;
                    }else if(i.charAt(j) =='\\' && count == 1){
                        message.setSender(text);
                        text = "";
                        count++;
                    }else if(i.charAt(j) =='|' && count == 2){
                        if(text.equals("1")){
                            message.setBot(true);
                        }else{
                            message.setBot(false);
                        }
                        count = 0;
                        text = "";
                        list.add(message);
                        Log.e("1233",message.toString());
                        message = new Message("","",false);
                    } else{
                        text += i.charAt(j);
                    }
                }
            }
            in.close();
        }catch (FileNotFoundException e){
            Log.e("login123123", "Can not read file: " + e);
            //startActivity(new Intent(this, SliderActivity.class));
        } catch (UnsupportedEncodingException e) {
            Log.e("login123123", "Can not read file: " + e);
        } catch (IOException e) {
            Log.e("login123123", "Can not read file: " + e);
        }
        return list;
    }
    private void writeToFile(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName +".txt",MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
    private void getLogin(){
        InputStream fin = null;
        try {

            fin = openFileInput("login.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF8"));
            String name = "";
            String i;
            while((i=in.readLine()) != null){
                name += i;
            }
            if(name.equals("")){
                login = "Гость";
            }else{
                login = name;
            }
            in.close();
        }catch (FileNotFoundException e){
            startActivity(new Intent(this, SliderActivity.class));
        }
        catch (IOException e) {
            Log.e("login123123", "Can not read file: " + e);
        }
        finally {
            try {
                if(fin != null)
                    fin.close();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                savedMessage.setText(textResult);
                            }
                        });
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
                    String textRes = "Привет";
                    try {
                        jsonObject = new JSONObject(body);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        textResult[0] = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        Log.e("123",textResult[0]);
                    } catch (JSONException e) {
                    }
                }
            });
        }
    }

}