package com.example.ecalo.glucosebonds1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class WatchListenerService extends WearableListenerService {

    private static final String DATA = "/Data";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if(messageEvent.getPath().equalsIgnoreCase(DATA)) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.e("T", "data value is: " + value);

            Intent intent = null;
//            SharedPreferences sp;
//            sp = getSharedPreferences(value, MODE_PRIVATE);
//            Log.e("T", "sp contains " + value + " " + sp.contains(value));
//            if (sp.getBoolean(value, false)){
//                return;
//            }

            switch (value) {
                case "quicktips":
                    intent = new Intent(this, QuicktipsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case "msg":
                    intent = new Intent(this, MessageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case "media":
                    intent = new Intent(this, MediaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                default:
                    Log.e("T", "unidentified data retrieved in watch");
            }

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}