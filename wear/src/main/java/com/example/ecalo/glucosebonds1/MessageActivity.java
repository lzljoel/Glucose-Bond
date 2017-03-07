package com.example.ecalo.glucosebonds1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Allen on 4/16/2016.
 */
public class MessageActivity extends Activity {
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicktips);
        ImageButton imgbtn = (ImageButton) findViewById(R.id.imagebtn);
        imgbtn.setImageResource(R.drawable.mentor_msg);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMsg = new Intent(context, WatchToPhoneService.class);
                Log.d("T", "context was the problem?");
                openMsg.putExtra("pdata", "pmsg");
                startService(openMsg);
            }
        });
    }
}
