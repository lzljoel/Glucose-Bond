package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

/**
 * Created by joel on 4/27/16.
 */
public class SignUpActivity extends AppCompatActivity {
    Context context;
    private Spinner age_list;
    private String[] ages;
    private Button sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.sign_up);
        age_list = (Spinner) findViewById(R.id.age_list);
        ages = getResources().getStringArray(R.array.child_age);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.child_age, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        age_list.setAdapter(adapter);
        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goWelcome = new Intent(context, WelcomeActivity.class);
                startActivity(goWelcome);
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = ((EditText) findViewById(R.id.first_name)).getText().toString();
                String lastname = ((EditText) findViewById(R.id.last_name)).getText().toString();

                String address = ((EditText) findViewById(R.id.address)).getText().toString();

                String childname = ((EditText) findViewById(R.id.child_name)).getText().toString();

                String month = ((EditText) findViewById(R.id.diag_month)).getText().toString();
                String day = ((EditText) findViewById(R.id.diag_day)).getText().toString();
                String year = ((EditText) findViewById(R.id.diag_year)).getText().toString();



                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                BaasUser user = BaasUser.withUserName(username).setPassword(password);

                user.getScope(BaasUser.Scope.REGISTERED).put("Name", firstname + " " + lastname);
                user.getScope(BaasUser.Scope.REGISTERED).put("Address", address);
                user.getScope(BaasUser.Scope.REGISTERED).put("Childname", childname);
                user.getScope(BaasUser.Scope.REGISTERED).put("DiagnosisDate", month + "/" + day + "/" + year);

                user.getScope(BaasUser.Scope.REGISTERED).put("img", R.drawable.img3);

                user.signup(new BaasHandler<BaasUser>() {
                    @Override
                    public void handle(BaasResult<BaasUser> baasResult) {
                        if (baasResult.isSuccess()) {
                            //Intent goWelcome = new Intent(context, WelcomeActivity.class);
                            //startActivity(goWelcome);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}