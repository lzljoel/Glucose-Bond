package com.example.ecalo.glucosebonds1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Allen on 4/18/2016.
 */
public class TestLoginActivity extends Activity {

    final String collection = "myCollection";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_login);

        Button login = (Button) findViewById(R.id.login_btn);
        Button signUp = (Button) findViewById(R.id.signup_btn);
        Button createDoc = (Button) findViewById(R.id.create_doc);
        Button getDoc = (Button) findViewById(R.id.get_doc);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.login_user)).getText().toString();
                String password = ((EditText) findViewById(R.id.login_pass)).getText().toString();
                BaasUser user = BaasUser.withUserName(username).setPassword(password);

                user.login(new BaasHandler<BaasUser>() {
                    @Override
                    public void handle(BaasResult<BaasUser> baasResult) {
                        if (baasResult.isSuccess()) {
                            BaasUser me = baasResult.value();
                            String name = me.getName();
                            Toast.makeText(TestLoginActivity.this, "Success. Logged in as: " + name, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TestLoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                            Log.e("T", baasResult.error().toString());
                        }
                    }
                });

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.login_user)).getText().toString();
                String password = ((EditText) findViewById(R.id.login_pass)).getText().toString();
                BaasUser user = BaasUser.withUserName(username).setPassword(password);

                user.signup(new BaasHandler<BaasUser>() {
                    @Override
                    public void handle(BaasResult<BaasUser> baasResult) {
                        if (baasResult.isSuccess()) {
                            Toast.makeText(TestLoginActivity.this, "Success, new User signed up", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TestLoginActivity.this, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        createDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.input_title)).getText().toString();
                String body = ((EditText) findViewById(R.id.input_body)).getText().toString();

                BaasDocument doc = new BaasDocument(collection);
                doc.put("title", title);
                doc.put("body", body);
                doc.save(new BaasHandler<BaasDocument>() {
                    @Override
                    public void handle(BaasResult<BaasDocument> baasResult) {
                        if (baasResult.isSuccess()) {
                            Toast.makeText(TestLoginActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TestLoginActivity.this, "Failed to save doc", Toast.LENGTH_SHORT).show();
                            Log.e("T", "" + baasResult.error());
                        }
                    }
                });

            }
        });

        getDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaasDocument.fetchAll(collection, new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> baasResult) {
                        if (baasResult.isSuccess()) {
                            TextView title = (TextView) findViewById(R.id.get_title);
                            TextView body = (TextView) findViewById(R.id.get_body);

                            BaasDocument doc = baasResult.value().get(0);

                            title.setText(doc.getString("title"));
                            body.setText(doc.getString("body"));

                        } else {
                            Toast.makeText(TestLoginActivity.this, "Failed to retrieve doc", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}
