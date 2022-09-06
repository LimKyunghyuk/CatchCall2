package org.devlion.catchCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.devlion.util.cmn.Receiver;
import org.devlion.util.cmn.HttpHelper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String TAG = "CATCH_CALL";
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        HttpHelper httpHelper = new HttpHelper();
        httpHelper.setReceiver(new Receiver() {
            @Override
            public void onResponse(int resCode, JSONObject res) {
                Log.d(TAG, "resCode: " + resCode + ", res: " + res);
                txv.setText("JSON: " + res);
            }
        });

        txv = findViewById(R.id.txv);
        txv.setText("Hello");

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();

                Map<String, String> req = new HashMap<String, String>();
                req.put("option", "01012341234");
                httpHelper.doGet(req);
            }
        });
    }

    public void checkPermission(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.READ_CALL_LOG
                            , Manifest.permission.READ_PHONE_STATE}
                    ,1);
        }
    }

}