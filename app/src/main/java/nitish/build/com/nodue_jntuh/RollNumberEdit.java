package nitish.build.com.nodue_jntuh;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RollNumberEdit extends AppCompatActivity {

    Long memoInit;
    TextView tempMemo,tempRoll;
    AdView adView,adView1;

    public  void decreaseRoll(View view){
        memoInit--;
        tempMemo.setText("Memo No.: "+memoInit.toString());
        tempRoll.setText(rollReturn(memoInit).substring(0,10));
    }

    public  void increaseRoll(View view){
        memoInit++;
        tempMemo.setText("Memo No.: "+memoInit.toString());
        tempRoll.setText(rollReturn(memoInit).substring(0,10));
    }

    public void seeResult(View view){
        Intent intent4 = new Intent(getApplicationContext(),WebResult.class);
        intent4.putExtra("memo",memoInit);
        startActivity(intent4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number_edit);

        adView = findViewById(R.id.adViewROLLNO1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView1 = findViewById(R.id.adViewROLLNO2);
        adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        memoInit = intent.getLongExtra("memo",111111);
        tempMemo = findViewById(R.id.tempMemo);
        tempMemo.setText("Memo No.: "+memoInit.toString());
        tempRoll = findViewById(R.id.tempRoll);
        tempRoll.setText(rollReturn(memoInit).substring(0,10));


    }

    public String rollReturn(long memoNo){
        String finString="",rollS="";
        String link="https://results.jntuhceh.ac.in/verify/memo/";
        String sha = ShaGenerator.getSha256(Long.toString(memoNo));
        link+=sha;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(link); // Set the action you want to do
        try {
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line + "\n");

            finString = sb.toString(); // Result is here
            Log.i("HTTP",finString);

            is.close(); // Close the stream
        }catch (Exception e){
            Log.i("Exception",e.toString());
        }



        Pattern rollP = Pattern.compile("Ticket\">\n" +
                "        (.*?)</div>");
        Matcher m = rollP.matcher(finString);
        while (m.find())
            rollS = m.group(1);

        return rollS;
    }

}
