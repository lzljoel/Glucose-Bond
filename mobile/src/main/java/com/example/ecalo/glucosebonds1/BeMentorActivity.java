package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

/**
 * Created by joel on 4/19/16.
 */
public class BeMentorActivity extends AppCompatActivity {
    private BaasBox client;
    ImageButton buttonRegister;
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.be_a_mentor);
        buttonRegister = (ImageButton) findViewById(R.id.buttonRegister);
        //buttonRegister.setImageResource(R.drawable.be_mentor);


        BaasBox.Builder builder = new BaasBox.Builder(this);
        client =builder.setApiDomain("10.0.3.2")
                .setPort(9000)
                .setAppCode("1234567890")
                .setHttpConnectionTimeout(3000)
                .init();

        BaasUser user = BaasUser.current();
        String address = user.getScope(BaasUser.Scope.REGISTERED).getString("Address");
        String name = user.getScope(BaasUser.Scope.REGISTERED).getString("Name");

        BaasDocument doc = new BaasDocument("mentorAddresses");
        doc.putString("Name",name)
                .putString("Address",address);
        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if (res.isSuccess()) {
                    Log.d("LOG", "Saved: " + res.value());
                } else {

                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registeredIntent = new Intent(context, RegisteredMentorActivity.class);
                startActivity(registeredIntent);
                finish();
            }
        });
    }
}