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
    int counterMy = 0;
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
        getCounter();
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
                if(messages.size() == 2){
                    sendMessage(login,enterText.getText().toString(),false);
                    adapter.notifyDataSetChanged();
                    String ans =enterText.getText().toString();
                    enterText.setText("");
                    login = ans;
                    adapter.notifyDataSetChanged();
                    blockingInput.setClickable(true);
                    blockingInput.setBackgroundColor(R.color.black);
                    botImage.setImageResource(R.drawable.bot_write);

                    new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            //sendMessage("Сигналл", savedMessage.getText().toString(),true);
                            sendMessage("Сигналл", "В темном лесу, где лучи света едва проникали сквозь густую листву, жил одинокий странник по имени Элиас. Он был мудрым и наблюдательным, всегда готовым к новым испытаниям. Однажды, прогуливаясь по лесу, Элиас услышал загадочный шепот, словно призывающий его к чему-то важному.",true);
                            sendMessage("Сигналл", "Следуя за звуками, странник наткнулся на яркое озеро, на поверхности которого пляшущие отблески света создавали завораживающий образ. Но среди этой красоты он заметил странное движение под водой, словно что-то пыталось его привлечь. Подойдя ближе, Элиас разглядел зазубренные крючья, скрытые под блестящими лучами." ,true);
                            sendMessage("Сигналл", "\"Фишинг\", - прошептал он, осознавая, что это ловушка для ничего не подозревающих жертв. Сквозь метафору озера и его мерцающей поверхности он понял, что в интернете тоже есть хитроумные уловки, способные обмануть даже самых бдительных. Элиас решил остерегаться и быть бдительным, как в лесу, так и в виртуальном мире.",true);
                            sendMessage("Сигналл", "Это конечно всё хорошо, но всё же что такое фишинг в наше время?",true);
                            blockingInput.setBackgroundColor(Color.TRANSPARENT);
                            blockingInput.setClickable(false);
                            botImage.setImageResource(R.drawable.base_bot);
                        }
                    }.start();
                }else{
                    sendMessage(login,enterText.getText().toString(),false);
                    adapter.notifyDataSetChanged();
                    String ans =enterText.getText().toString();
                    enterText.setText("");
                    adapter.notifyDataSetChanged();
                    blockingInput.setClickable(true);
                    blockingInput.setBackgroundColor(R.color.black);
                    botImage.setImageResource(R.drawable.bot_write);
                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            blockingInput.setBackgroundColor(Color.TRANSPARENT);
                            blockingInput.setClickable(false);
                            if(ans.toLowerCase().contains("вид кибератаки")){
                                sendMessage("Сигналл","Правильно! Следующий вопрос",true);
                                botImage.setImageResource(R.drawable.bot_happy);
                                rewriteToFile("2",MainActivity.this,"counter");
                            }else{
                                getResponse("Напиши подсказку на вопрос - Что такое фишинг?  используя ключевые слова - вид кибератаки(ключевые слова нельзя менять)", true);
                                sendMessage("Сигналл",savedMessage.getText().toString(),true);
                                botImage.setImageResource(R.drawable.bot_q);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(login != null && messages.size() == 0) {
            sendMessage("Сигналл", "Приветствую, путешественник! Я - бот \"Сигналл\", твой верный проводник в мире цифровых тайн и опасностей. Однако, случился неприятный инцидент - из-за бага в моей системе я временно потерял свои знания о кибербезопасности цифровизации и даже искусственному интеллекту ! Теперь я могу лишь подсказывать тебе, но без твоей помощи я не смогу восстановить свои данные.", true);
            sendMessage("Сигналл", "Привет! Как мне к тебе обращаться? Напиши своё имя, пожалуйста.", true);
        }
        if(!messages.contains("Что такое программное обеспечение?") && messages.size() == 6){
            switchCase("Что такое фишинг");
            writeToFile("1",this,"counter");

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
    }private void rewriteToFile(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName +".txt",MODE_PRIVATE));
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
    }private void getCounter(){
        InputStream fin = null;
        try {

            fin = openFileInput("counter.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF8"));
            String counter = "";
            String i;
            while((i=in.readLine()) != null){
                counter += i;
            }
            if(counter.equals("1")){
                counterMy = 1;
            }else if(counter.equals("2")){
                counterMy = 2;
            }else if(counter.equals("3")){
                counterMy = 3;
            }
            in.close();
        }catch (FileNotFoundException e){
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
                        Log.e("123",textResult);
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
    void switchCase(String question){
        switch (question) {
            case "Что такое программное обеспечение?":
                sendMessage("Сигналл", "Что такое программное обеспечение?", true);
                break;
            case "Что такое кибербезопасность?":
                sendMessage("Сигналл", "Что такое кибербезопасность?", true);
                break;
            case "Что такое облачные технологии?":
                sendMessage("Сигналл", "Что такое облачные технологии?", true);
                break;
        }
    }
}