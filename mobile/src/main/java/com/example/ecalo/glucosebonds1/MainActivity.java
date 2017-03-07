package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton button;
    Context context;
    private LinearLayout home_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.home_screen);
        home_screen = (LinearLayout) findViewById(R.id.homeScreen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 2 seconds
                Intent loginActivity = new Intent(context, Login.class);
                startActivity(loginActivity);
                finish();
            }
        }, 2000);

//        button = (ImageButton) findViewById(R.id.img_btn);
//        button.setImageResource(R.drawable.gb_loading);
//
//        button.setOnTouchListener(new GestureHelper(context));
    }

}
