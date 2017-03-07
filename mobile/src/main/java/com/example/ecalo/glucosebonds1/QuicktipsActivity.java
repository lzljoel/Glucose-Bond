package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class QuicktipsActivity extends AppCompatActivity {

    ImageButton button;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.quick_tips);
        Intent watch = new Intent (this, PhoneToWatchService.class);
        watch.putExtra("data", "quicktips");
        startService(watch);
        button = (ImageButton) findViewById(R.id.med_button);
        Log.d("T", "gets here");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("T", "and here");
                Intent injection = new Intent(context, PhoneToWatchService.class);
                injection.putExtra("data", "media");
                startService(injection);
            }
        });
    }

}