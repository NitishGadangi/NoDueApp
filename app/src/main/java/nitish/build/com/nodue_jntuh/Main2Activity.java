package nitish.build.com.nodue_jntuh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Main2Activity extends AppCompatActivity {
    Button scanbtn;
    TextView result,proceedtext;
    String resultUrl="";
    static  String proceedOrNot="";
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    private AdView mAdView;

    /*public void proceed(View view){
        Intent intent1 = new Intent(getApplicationContext(),ScannedResult.class);
        intent1.putExtra("result",resultUrl);
        startActivity(intent1);
    }*/

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {

        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent a1_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(a1_intent);
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MobileAds.initialize(this, "ca-app-pub-5703381258854980~8222585081");

        mAdView = findViewById(R.id.adViewROLLNO1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Consider Reading This..!")
                .setCancelable(false)
                .setMessage("This App Contains Ads.Please consider clicking on those Ads as they " +
                        "generate revenue that further help me in adding more features to this app.")
                .setPositiveButton("Ok",null).show();

        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);
        //proceedbtn = findViewById(R.id.proceed);
        proceedtext = findViewById(R.id.proceedtext);
        proceedtext.setText(proceedOrNot);



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent);
            }
        });


    }


    /*
    public void runTest(){

        boolean isFound = resultUrl.contains("results.jntuhceh.ac.in/verify/memo");
        if (isFound) {
            proceedOrNot = "You Can Proceed";
            scanbtn.setVisibility(View.INVISIBLE);
            proceedbtn.setVisibility(View.VISIBLE);
        }
        else {
            proceedOrNot = "please Scan Again";
            scanbtn.setVisibility(View.VISIBLE);
            scanbtn.setText("SCAN AGAIN");
        }
        proceedtext.setText(proceedOrNot);
    }
    */
}

