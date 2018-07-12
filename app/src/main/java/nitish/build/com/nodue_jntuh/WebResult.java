package nitish.build.com.nodue_jntuh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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



public class WebResult extends AppCompatActivity {

    long memoNo;
    String sha,finString="",rollS,link="https://results.jntuhceh.ac.in/verify/memo/";
    WebView web;
    AdView add1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_result);

        web=findViewById(R.id.web);

        Intent intent = getIntent();
        memoNo = intent.getLongExtra("memo",111111);
        sha = ShaGenerator.getSha256(Long.toString(memoNo));
        link+=sha;

        add1 = findViewById(R.id.adViewWEBRESULT);
        AdRequest adRequest = new AdRequest.Builder().build();
        add1.loadAd(adRequest);


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

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Is this your Roll.No?")
                .setCancelable(false)
                .setMessage(rollS)
                .setPositiveButton("Yes",null)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 =new Intent(getApplicationContext(),RollNumberEdit.class);
                        intent2.putExtra("memo",memoNo);
                        startActivity(intent2);

                    }
                })
                .show();

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(link);

    }
}
