package org.devlion.catchCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.devlion.util.cmn.HttpHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String TAG = "CATCH_CALL";
    TextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        checkPermissionDrawOverlay();


        ImageButton btnConfig = findViewById(R.id.btn_setting);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        List<String> list = new ArrayList<>();
        list.add("사과");
        list.add("딸기");
        list.add("바나나");

        ListView listView = findViewById(R.id.list);
        ArrayAdapter<String> adpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adpater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "List position : " + position + ", data : " + data , Toast.LENGTH_SHORT).show();
            }
        });


        /*
        HttpHelper httpHelper = new HttpHelper();
        httpHelper.setHttpListener(new HttpHelper.HttpListener() {
            @Override
            public void onResponse(int resCode, JSONObject res) {
                Log.d(TAG, "resCode: " + resCode + ", res: " + res);
                txv.setText("JSON: " + res);
            }
        });

        txv = findViewById(R.id.tv_hello);
        txv.setText("Hello");

        Button btnRequest = findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();

                Map<String, String> req = new HashMap<String, String>();
                req.put("option", "01012341234");
                httpHelper.doGet(req);
            }
        });

        Button btnPopupOpen = findViewById(R.id.btn_popup_open);
        btnPopupOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup.getInstance(getApplicationContext()).open("1","2");
            }
        });

        Button btnPopupClose = findViewById(R.id.btn_popup_close);
        btnPopupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup.getInstance(getApplicationContext()).close();
            }
        });


        Button crashButton = findViewById(R.id.btn_crash);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

         */
    }

    public void checkPermission(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{
                              Manifest.permission.READ_CALL_LOG
                            , Manifest.permission.READ_PHONE_STATE
                    }
                    ,1);
        }
    }

    public void checkPermissionDrawOverlay(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getApplicationContext())){
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(overlayIntent);
            }
        }
    }

}