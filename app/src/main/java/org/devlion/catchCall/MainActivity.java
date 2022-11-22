package org.devlion.catchCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import org.devlion.util.db.DBHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String TAG = "CATCH_CALL";
    TextView txv;


    private DBHelper dbHelper;

    void init(){
        dbHelper = new DBHelper(this);
        dbHelper.open();
        dbHelper.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        checkPermissionDrawOverlay();

        init();

        ImageButton btnConfig = findViewById(R.id.btn_setting);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });


        // ListView 세팅

        List<String> list = new ArrayList<>();

        Cursor csr = dbHelper.selectColumns();
        if(0 < csr.getCount()){
            while(csr.moveToNext()){

                String logId = csr.getString(0);
                String logDt = csr.getString(1);
                String cellPhone = csr.getString(2);
                String companyPhone = csr.getString(3);
                String companyCode = csr.getString(4);
                String addisplayName = csr.getString(5);
                String chargeJob = csr.getString(6);
                String mainDeptCode = csr.getString(7);
                String companyName = csr.getString(8);
                String deptName = csr.getString(9);

                list.add(logDt + " : " + addisplayName + "\n" + deptName);

            }
        }

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