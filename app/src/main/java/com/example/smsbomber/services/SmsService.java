package com.example.smsbomber.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SmsService extends Service {
    private static final String TAG = "SmsService";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    
    private static SmsManager smsManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        registerReceiver(new SmsReceiver(), filter);
        smsManager = SmsManager.getDefault();
    }

    public static void sendSms(String phoneNumber, String message) {
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private static class SmsReceiver extends BroadcastReceiver {
        public SmsReceiver() {
            Log.v("SmsReceiver", "SmsReceiver()");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            StringBuilder str = new StringBuilder();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    str.append("SMS from ").append(msgs[i].getOriginatingAddress());
                    str.append(" :");
                    str.append(msgs[i].getMessageBody());
                    str.append("\n");
                }
                Toast.makeText(context, str.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
