package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageActivity extends AppCompatActivity {

    ImageButton button;
    Context context;
    String mCollection;
    String mentorName = "mentor";
    String menteeName = "mentee";
    boolean isMentor;

    private DiscussArrayAdapter adapter;
    private ListView lv;
    private EditText editText1;
    private static Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        context = this;
        mCollection = "messages";

        //Get Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //String mentorName = extras.getString("MENTOR_NAME");
        isMentor = extras.getBoolean("IS_MENTOR");

        TextView textViewMentorName = (TextView) findViewById(R.id.textViewMentorName);
        textViewMentorName.setText(isMentor ? menteeName : mentorName);


        lv = (ListView) findViewById(R.id.listView1);
        adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
        lv.setAdapter(adapter);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    createDoc(editText1.getText().toString());

                    adapter.add(new OneComment(false, editText1.getText().toString()));
                    editText1.setText("");
                    return true;
                }
                return false;
            }
        });

        fetchFromCollection(mCollection);
        //addItems();
    }

    private void addItems() {
        adapter.add(new OneComment(true, "Hello bubbles!"));

        for (int i = 0; i < 4; i++) {
            boolean left = i % 2 == 0;

            String words = "TEST"+ Integer.toString(i);

            adapter.add(new OneComment(left, words));
        }
    }

    private void createDoc(String msg) {
        BaasUser user = BaasUser.current();

        BaasDocument doc = new BaasDocument(mCollection);
        doc.put("title", user.getScope(BaasUser.Scope.REGISTERED).getString("Name"));
        doc.put("body", msg);

        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                if (baasResult.isSuccess()) {
                    try {
                        baasResult.value().grantAll(Grant.ALL, Role.REGISTERED, new BaasHandler<Void>() {
                            @Override
                            public void handle(BaasResult<Void> baasResult) {
                                if (baasResult.isSuccess()) {
                                    Log.e("T", "successfully granted permission");
                                } else {
                                    Log.e("T", "unable to grant permission");
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Log.e("F", "successfully saved document");
                } else {
                    Log.e("F", "failed to create document");
                }
            }
        });
    }

    private void fetchFromCollection(String collection) {
        Log.e("T", "getting documents from " + collection);
        BaasDocument.fetchAll(collection, new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> baasResult) {
                if (baasResult.isSuccess()) {
                    adapter.clear();
                    ArrayList<String> items = new ArrayList<String>();
                    for (BaasDocument doc : baasResult.value()) {
                        Log.e("F", "document is: " + doc);
                        //item.collection = doc.getString("collection");
                        String name = doc.getString("title");
                        String msg = doc.getString("body");

                        String myname = BaasUser.current().getScope(BaasUser.Scope.REGISTERED).getString("Name");

                        if (!name.equals(myname)) {
                            adapter.add(new OneComment(true, msg));
                        } else {
                            adapter.add(new OneComment(false, msg));
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("F", "failed to retrieve documents from forums-general");
                }
            }
        });
    }



}
