package nitish.build.com.nodue_jntuh;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ScannedResult extends AppCompatActivity {
    private AdView add1,add2;
    private InterstitialAd mInterstitialAd;
    TextView link,roll,name,memoNo,rollStart;

    String rollS,nameS,memoNoS,finString="";
    long finMemo;
    public void proceed(View view){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            Intent intent2 = new Intent(getApplicationContext(), WebResult.class);
            intent2.putExtra("memo", finMemo);
            startActivity(intent2);
        }
    }
    public void search(View view){
        //view.setVisibility(View.INVISIBLE);
        EditText editText = findViewById(R.id.last3digits);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        Button proceedBtn = findViewById(R.id.procdBtn);
        proceedBtn.setVisibility(View.VISIBLE);


        Log.i("start",rollS.substring(0,7));


        String rollEntered = editText.getText().toString();
        TextView expected = findViewById(R.id.expected);
        String scannedLast = rollS.substring(8,10);
        long exptMemo =Long.parseLong(rollEntered)-Long.parseLong(scannedLast);
        finMemo = Integer.parseInt(memoNoS.substring(0,6))+exptMemo;
        expected.setText("Your MemoNo (Expected):"+Long.toString(finMemo));

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_result);

        link=findViewById(R.id.link);
        roll=findViewById(R.id.Roll);
        name=findViewById(R.id.Name);
        memoNo=findViewById(R.id.MemoNo);
        rollStart = findViewById(R.id.rollStart);

        Intent intent = getIntent();
        String scannedUrl = intent.getStringExtra("result");
        link.setText(scannedUrl);

        add1 = findViewById(R.id.adViewScannedResult1);
        AdRequest adRequest = new AdRequest.Builder().build();
        add1.loadAd(adRequest);

        add2 = findViewById(R.id.adViewScannedResult2);
        adRequest = new AdRequest.Builder().build();
        add2.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5703381258854980/9442908233");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                Intent intent3 = new Intent(getApplicationContext(), WebResult.class);
                intent3.putExtra("memo", finMemo);
                startActivity(intent3);
            }
        });

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Disclaimer!")
                .setCancelable(false)
                .setMessage("This App requires an active internet connection to view your result.")
                .setPositiveButton("Ok",null).show();

        //Log.i("Test",scannedUrl);

        /*
        DownloadTask task = new DownloadTask();
        try {
            finString = task.execute(scannedUrl).get();
            Log.i("Test",finString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(scannedUrl); // Set the action you want to do
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



        Pattern memoP = Pattern.compile("Serial\">\n" +
                "         (.*?)</div>"),
                nameP = Pattern.compile("Name\">\n" +
                        "(.*?)</div>"),
                rollP = Pattern.compile("Ticket\">\n" +
                        "        (.*?)</div>");
        Matcher m = memoP.matcher(finString);
        while (m.find())
            memoNoS = m.group(1);
        memoNo.setText(memoNoS);
        //Log.i("Test",memoNoS);

        m = nameP.matcher(finString);
        while (m.find())
            nameS = m.group(1);
        name.setText(nameS);
        m= rollP.matcher(finString);
        while (m.find())
            rollS = m.group(1);
        roll.setText(rollS);
        rollStart.setText(rollS.substring(0,8));
        //Log.i("Test",rollS);


    }
}
