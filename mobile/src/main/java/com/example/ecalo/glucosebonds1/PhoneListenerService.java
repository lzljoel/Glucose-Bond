package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String PDATA = "/PDATA";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if(messageEvent.getPath().equalsIgnoreCase(PDATA)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent;
            Log.d("T", "data value is: " + value);
            switch (value) {
                case "pmsg":
                    Log.d("T", "it gets here");
                    intent = new Intent(this, MessageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("activity", "Message");
                    startActivity(intent);
            }
        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}