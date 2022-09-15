package org.devlion.catchCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
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

            // API 리스너 정의
            HttpHelper httpHelper = new HttpHelper();
            httpHelper.setHttpListener(new HttpHelper.HttpListener() {
                @Override
                public void onResponse(int resCode, JSONObject res) {
                    Log.d(TAG, "resCode: " + resCode + ", res: " + res);

                    try {
                        if(res != null && "S".equals(res.get("result"))){

                            String team = (String)res.get("team");
                            String name = (String)res.get("name");;

                            Popup p = Popup.getInstance(context);
                            Log.d(TAG, "open p: " + p);
                            p.open(team, name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            
            // API 호출
            Map<String, String> req = new HashMap<String, String>();
            req.put("option",incomingNumber);
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

}
