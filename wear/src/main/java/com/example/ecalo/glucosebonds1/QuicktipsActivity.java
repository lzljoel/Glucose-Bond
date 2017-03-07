package com.example.ecalo.glucosebonds1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by Allen on 4/16/2016.
 */
public class QuicktipsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicktips);
        ImageButton imgbtn = (ImageButton) findViewById(R.id.imagebtn);
        imgbtn.setImageResource(R.drawable.quicktips_watch);
        Log.e("W", "in quicktips activity");
    }

}
