package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by joel on 4/27/16.
 */
public class LinksActivity extends AppCompatActivity{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.links_screen);
    }
}
