package com.example.myapplication;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String login;
    BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    ImageButton sendButton;
    EditText enterText;
    View blockingInput;
    RecyclerView recyclerView;
    ArrayList<Message> messages;
    MessageListAdapter adapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messages = savedChat();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLogin();
        enterText = findViewById(R.id.enter_text);
        sendButton = findViewById(R.id.send_button);
        adapter = new MessageListAdapter(this,messages);
        adapter.notifyDataSetChanged();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        blockingInput = findViewById(R.id.blocking_input);
        sendButton.setOnClickListener(v ->{
            if(enterText.getText().toString().length() != 0){
                sendMessage(login,enterText.getText().toString(),false);
                adapter.notifyDataSetChanged();
                enterText.setText("");
                blockingInput.setBackgroundColor(R.color.black);
                blockingInput.setClickable(true);
                sendMessage("Сигналл","Привет!",true);
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        blockingInput.setBackgroundColor(Color.TRANSPARENT);
                        blockingInput.setClickable(false);
                    }
                }.start();
            }
        });
    }
    private void sendMessage(String sender, String message,boolean isBot){
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

}