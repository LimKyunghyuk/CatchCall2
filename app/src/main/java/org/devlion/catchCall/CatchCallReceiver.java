package org.devlion.catchCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CatchCallReceiver extends BroadcastReceiver {

    final String TAG = "CATCH_CALL";
    private static String mLastState;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        // 전화밸이 울리면 Receiver가 2번씩 수신되므로 incomingNumber == null 일 경우 종료
        if(incomingNumber == null){
            Log.d(TAG, "incomingNumber is null");
            return;
        }

        if(TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

            final String phoneNumber = PhoneNumberUtils.formatNumber(incomingNumber);
            Log.d(TAG, "phoneNumber:" + phoneNumber);
            Toast.makeText(context, "Call: " + phoneNumber, Toast.LENGTH_SHORT).show();

        }else if(TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)){
            Log.d("CatchCall", "EXTRA_STATE_OFFHOOK 전화수신");


        }else if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)){
            Log.d("CatchCall", "EXTRA_STATE_IDLE 전화거절/통화밸 종료");

        }
    }
}
