package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    //    ImageButton button;
    Context context;
    private LinearLayout forums, education, mentorship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.welcome_screen);
        forums = (LinearLayout) findViewById(R.id.forums);
        education = (LinearLayout) findViewById(R.id.education);
        mentorship = (LinearLayout) findViewById(R.id.mentorship);
        forums.setOnClickListener(this);
        education.setOnClickListener(this);
        mentorship.setOnClickListener(this);

//        button = (ImageButton) findViewById(R.id.img_btn);
//        button.setImageResource(R.drawable.gb_main);
//
//        button.setOnTouchListener(new GestureHelper(context));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forums:
                Intent forumsActivity = new Intent(context, ForumsActivity.class);
                startActivity(forumsActivity);
                break;
            case R.id.education:
                Intent educationActivity = new Intent(context, EducationActivity.class);
                startActivity(educationActivity);
                break;
            case R.id.mentorship:
                Intent mentorActivity = new Intent(context, MentorshipActivity.class);
                startActivity(mentorActivity);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        BaasUser.current().logout(new BaasHandler<Void>() {
            @Override
            public void handle(BaasResult<Void> baasResult) {
                if (baasResult.isSuccess()) {
                    Log.e("T", "successfully logged out");
                    goBack();
                } else {
                    Log.e("T", "Failed to logout");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.messages:
                Intent messageActivity = new Intent(context, MessageActivity.class);
                messageActivity.putExtra("MENTOR_NAME", BaasUser.current().getScope(BaasUser.Scope.REGISTERED).getString("Name"));
                messageActivity.putExtra("IS_MENTOR", true);
                startActivity(messageActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2, menu);
        return true;
    }

    public void goBack() {
        super.onBackPressed();
    }
}
