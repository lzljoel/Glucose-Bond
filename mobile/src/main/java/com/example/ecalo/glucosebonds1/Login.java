package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

/**
 * Created by joel on 4/27/16.
 */
public class Login extends AppCompatActivity {
    Context context;
    private Button loginBtn;
    private TextView createNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.login_screen);



        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                BaasUser user = BaasUser.withUserName(username).setPassword(password);

                user.login(new BaasHandler<BaasUser>() {
                    @Override
                    public void handle(BaasResult<BaasUser> baasResult) {
                        if (baasResult.isSuccess()) {
                            Intent welcomeActivity = new Intent(context, WelcomeActivity.class);
                            startActivity(welcomeActivity);
                        } else {
                            Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                            Log.e("T", baasResult.error().toString());
                        }
                    }
                });

            }
        });

        createNew = (TextView) findViewById(R.id.create_new);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signActivity = new Intent(context, SignUpActivity.class);
                startActivity(signActivity);
            }
        });
    }
}