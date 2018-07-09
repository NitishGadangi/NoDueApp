package com.example.nitishpc.nodue;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity {
    TextView link,roll,name,memoNo;
    String rollS,nameS,memoNoS,finString="";
    public void proceed(View view){

    }
    public void search(View view){
        EditText editText = findViewById(R.id.editText);
        String rollEntered = editText.getText().toString();
        TextView expected = findViewById(R.id.expected);

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String res ="";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1){
                    char current = (char) data;
                    res+= current;
                    data= reader.read();
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Failed";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        link=findViewById(R.id.link);
        roll=findViewById(R.id.Roll);
        name=findViewById(R.id.Name);
        memoNo=findViewById(R.id.MemoNo);

        Intent intent = getIntent();
        String scannedUrl = intent.getStringExtra("result");

        Log.i("Test",scannedUrl);

        DownloadTask task = new DownloadTask();
        try {
            finString = task.execute(scannedUrl).get();
            Log.i("Test",finString);
        } catch (Exception e) {
            e.printStackTrace();
        }



        Pattern memoP = Pattern.compile("Serial\">(.*?)</div>"),
                nameP = Pattern.compile("Name\">(.*?)</div>"),
                rollP = Pattern.compile("Ticket\">(.*?)</div>");
        Matcher m = memoP.matcher(finString);
        while (m.find())
            memoNoS = m.group(1);
        memoNo.setText(memoNoS);
        m = nameP.matcher(finString);
        while (m.find())
            nameS = m.group(1);
        name.setText(memoNoS);
        m= rollP.matcher(finString);
        while (m.find())
            rollS = m.group(1);
        roll.setText(rollS);

    }
}
