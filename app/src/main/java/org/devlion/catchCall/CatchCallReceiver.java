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

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(incomingNumber != null){
                Log.d(TAG, "incomingNumber:" + incomingNumber);
                final String phoneNumber = PhoneNumberUtils.formatNumber(incomingNumber);
                Log.d(TAG, "phoneNumber:" + phoneNumber);
                Toast.makeText(context,"Call: " + phoneNumber, Toast.LENGTH_SHORT).show();
            }

//            final String phoneNumber = PhoneNumberUtils.formatNumber(incomingNumber);
//
//            Intent serviceIntent = new Intent(context, CallingService.class);
//            serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
//            context.startService(serviceIntent);
//            Log.d(TAG, "Call:"+incomingNumber);

        }
    }
}
