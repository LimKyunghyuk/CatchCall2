package org.devlion.catchCall;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    final String TAG = "CATCH_CALL";

    TextView txv;

//    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        sharedPref = getSharedPreferences(TAG, MODE_PRIVATE);
        String serverUrl = SharedPrefHelper.getInstance(this).getString(SharedPrefHelper.SERVER_URL);
        String serverKey = SharedPrefHelper.getInstance(this).getString(SharedPrefHelper.SERVER_KEY);


//        String serverUrl = sharedPref.getString(SERVER_URL, "");
//        String serverKey = sharedPref.getString(SERVER_KEY, "");

        EditText edtServerUrl = findViewById(R.id.edt_server_url);
        edtServerUrl.setText(serverUrl);

        EditText edtServerKey = findViewById(R.id.edt_server_key);
        edtServerKey.setText(serverKey);

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String serverUrl = edtServerUrl.getText().toString();
                String serverKey = edtServerKey.getText().toString();

                Log.d(TAG, "SERVER_URL1:" + serverUrl);
                Log.d(TAG, "SERVER_KEY1:" + serverKey);

                SharedPrefHelper.getInstance(getApplicationContext()).setString(SharedPrefHelper.SERVER_URL, serverUrl);
                SharedPrefHelper.getInstance(getApplicationContext()).setString(SharedPrefHelper.SERVER_KEY, serverKey);

                Log.d(TAG, "SERVER_URL2:" + serverUrl);
                Log.d(TAG, "SERVER_KEY2:" + serverKey);
                /*
                Intent intent = new Intent();
                intent.putExtra("응답 키", "응답 코드");
                setResult(RESULT_OK, intent);
                */

                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button btnTestPopup = findViewById(R.id.btn_test_popup);
        btnTestPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup.getInstance(getApplicationContext()).open("테스트 회사","장보고 책임", "포세이돈팀", "담당 업무");
            }
        });


        EditText edtCnumber = findViewById(R.id.edt_cnumber);
        EditText edtEnumber = findViewById(R.id.edt_enumber);

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "검색..", Toast.LENGTH_SHORT).show();

                String cNumber = edtCnumber.getText().toString();
                String eNumber = edtEnumber.getText().toString();
                Log.d(TAG, "cNumber:" + cNumber);
                Log.d(TAG, "eNumber:" + eNumber);

                CatchCall catchCall = new CatchCall(getApplicationContext());
                catchCall.check(cNumber, eNumber);
            }
        });

    }


}