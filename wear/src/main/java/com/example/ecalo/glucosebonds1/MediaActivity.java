package com.example.ecalo.glucosebonds1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.HashMap;
//import android.widget.ImageButton;


public class MediaActivity extends Activity {
    public HashMap dict = new HashMap<String, Integer>();
    private static final String DEBUG_TAG = "Media";
    Integer firstImgId = R.drawable.step1;
    Integer scndImgId = R.drawable.step2;
    Integer thrdImgId = R.drawable.step3;
    Integer fourthImgId = R.drawable.step4;
    Integer fifthImgId = R.drawable.step5;
    Integer sixthImgId = R.drawable.step6;
    Context context = this;
    String currimg = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dict.put("1", firstImgId);
        dict.put("2", scndImgId);
        dict.put("3", thrdImgId);
        dict.put("4", fourthImgId);
        dict.put("5", fifthImgId);
        dict.put("6", sixthImgId);

        setContentView(R.layout.activity_media);
        final ImageButton imgbutton = (ImageButton) findViewById(R.id.view);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currimg = Integer.toString(Integer.parseInt(currimg) + 1);
                if (Integer.parseInt(currimg) == 7){
                    currimg = "1";
                }
                imgbutton.setImageResource((Integer) dict.get(currimg));
            }

        });
    }
}