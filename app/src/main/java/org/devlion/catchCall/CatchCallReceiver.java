package org.devlion.catchCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.devlion.util.cmn.HttpHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class CatchCallReceiver extends BroadcastReceiver {

    final String TAG = "CATCH_CALL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        // 전화밸이 울리면 Receiver가 2번씩 수신되므로 incomingNumber == null 일 경우 종료
        if(incomingNumber == null) return;

        if(TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

            final String phoneNumber = PhoneNumberUtils.formatNumber(incomingNumber);
            Log.d(TAG, "incomingNumber:" + incomingNumber);
            Log.d(TAG, "phoneNumber:" + phoneNumber);

//
//            SharedPreferences sharedPref = context.getSharedPreferences(TAG, context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(SERVER_URL, edtServerUrl.getText().toString());
//            editor.putString(SERVER_KEY, edtServerKey.getText().toString());
//            editor.commit();

            String serverUrl = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_URL);
            String serverKey = SharedPrefHelper.getInstance(context).getString(SharedPrefHelper.SERVER_KEY);

            serverUrl = "https://" + serverUrl;
            serverUrl += "/monitoring.app/api/getUser";

            Log.d(TAG, "SERVER_URL:" + serverUrl);
            Log.d(TAG, "SERVER_KEY:" + serverKey);

            // API 리스너 정의
            HttpHelper httpHelper = new HttpHelper(serverUrl, serverKey);
            httpHelper.setHttpListener(new HttpHelper.HttpListener() {
                @Override
                public void onResponse(int resCode, JSONObject res) {
                    Log.d(TAG, "resCode: " + resCode + ", res: " + res);

                    try {
                        if(res != null){

                            String cellPhone = (String)res.get("cellPhone");
                            String companyPhone = (String)res.get("companyPhone");
                            String companyCode = (String)res.get("companyCode");
                            String addisplayName = (String)res.get("addisplayName");
                            String chargeJob = (String)res.get("chargeJob");
                            String mainDeptCode = (String)res.get("mainDeptCode");
                            String companyName = (String)res.get("companyName");
                            String deptName = (String)res.get("deptName");

                            Popup p = Popup.getInstance(context);
                            Log.d(TAG, "open p: " + p);

                            p.open(companyName, addisplayName, deptName, chargeJob);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            List<String> numberList = getTwoNumberFromBack(phoneNumber);
            if(numberList.size() < 3) return;

            // API 호출
            Map<String, String> req = new HashMap<String, String>();
            req.put("enumber",numberList.get(0));
            req.put("cnumber",numberList.get(1));
            httpHelper.doGet(req);

        }else if(TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)){
            Log.d("CatchCall", "EXTRA_STATE_OFFHOOK 전화수신");
            Popup p = Popup.getInstance(context);
            Log.d(TAG, "close p: " + p);
            p.close();

        }else if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)){
            Log.d("CatchCall", "EXTRA_STATE_IDLE 전화거절/통화밸 종료");
            Popup p = Popup.getInstance(context);
            Log.d(TAG, "close p: " + p);
            p.close();
        }
    }

    List<String> getTwoNumberFromBack(String phoneNumber){

        List<String> numberList = new ArrayList<String>();
        String[] numberAry = phoneNumber.split("-");

        for(int i = numberAry.length - 1 ; 0 <= i ; i--) {
            numberList.add(numberAry[i]);
        }

        return numberList;
    }

}
