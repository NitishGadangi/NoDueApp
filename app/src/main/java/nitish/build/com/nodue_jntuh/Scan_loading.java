package nitish.build.com.nodue_jntuh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Scan_loading extends AppCompatActivity {
    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_loading);

        Intent intent = getIntent();
        link=intent.getStringExtra("link");

        boolean isFound = link.contains("results.jntuhceh.ac.in/verify/memo");
        if (isFound) {
            Intent intent1 = new Intent(getApplicationContext(),ScannedResult.class);
            intent1.putExtra("result",link);
            startActivity(intent1);
            finish();
        }
        else {
            Main2Activity.proceedOrNot = "Please read above instructions and scan Again";
            Intent intent2 =new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(intent2);
            finish();
        }


    }
}
