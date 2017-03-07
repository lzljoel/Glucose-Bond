package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

/**
 * Created by joel on 4/19/16.
 */
public class FindMentorActivity extends AppCompatActivity {

    ImageButton button;
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.mentor);
//        button = (ImageButton) findViewById(R.id.img_btn);
//        button.setImageResource(R.drawable.find_mentor);

    }
}