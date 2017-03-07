package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ForumsDetailedServer extends AppCompatActivity {

    ImageButton button;
    Context context;
    String mCollection;
    ListAdapter listAdapter;

    ArrayList<ForumItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.forums_detail);

        mCollection = getIntent().getStringExtra("collection");

        Button post = (Button) findViewById(R.id.create_question);

        listItems = new ArrayList<>();

        listAdapter = new ListAdapter(context, R.layout.forums, listItems);

        ListView lv = (ListView) findViewById(R.id.listview);

        lv.setAdapter(listAdapter);

        fetchFromCollection(mCollection);

        final EditText title = (EditText) findViewById(R.id.post_title);
        final EditText body = (EditText) findViewById(R.id.body);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaasDocument doc = new BaasDocument(mCollection);
                Log.e("CV", title.getText().toString());
                doc.put("title", title.getText().toString());
                doc.put("body", body.getText().toString());

                ForumItem item = new ForumItem();
                item.title = title.getText().toString();
                item.body = body.getText().toString();

                listItems.add(item);
                listAdapter.notifyDataSetChanged();
                //updateListView(listItems);

                doc.save(new BaasHandler<BaasDocument>() {
                    @Override
                    public void handle(BaasResult<BaasDocument> baasResult) {
                        if (baasResult.isSuccess()) {
                            fetchFromCollection(mCollection);
                            Log.e("F", "successfully saved document");
                        } else {
                            Log.e("F", "failed to create document");
                        }
                    }
                });

                doc.save(new BaasHandler<BaasDocument>() {
                    @Override
                    public void handle(BaasResult<BaasDocument> baasResult) {
                        if (baasResult.isSuccess()) {
                            try {
                                baasResult.value().grantAll(Grant.ALL, Role.REGISTERED, new BaasHandler<Void>() {
                                    @Override
                                    public void handle(BaasResult<Void> baasResult) {
                                        if (baasResult.isSuccess()) {
                                            Log.e("F", "successfully granted permission");
                                        } else {
                                            Log.e("F", "failed to grant permission");
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            fetchFromCollection(mCollection);
                            Log.e("F", "successfully saved document");
                        } else {
                            Log.e("F", "failed to create document");
                        }
                    }
                });

            }
        });

    }

    private void updateListView(ArrayList<ForumItem> items) {
        listItems.clear();
        listItems.addAll(items);
        listAdapter.notifyDataSetChanged();
    }

    private void fetchFromCollection(String collection) {
        BaasDocument.fetchAll(collection, new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> baasResult) {
                if (baasResult.isSuccess()) {
                    ArrayList<ForumItem> items = new ArrayList<ForumItem>();
                    for (BaasDocument doc : baasResult.value()) {
                        Log.e("F", "document is: " + doc);
                        ForumItem item = new ForumItem();
                        Log.e("T", doc.getString("title"));
                        item.title = doc.getString("title");
                        item.body = doc.getString("body");
                        items.add(item);
                    }
                    updateListView(items);
                } else {
                    Log.e("F", "failed to retrieve documents from forums-general");
                }
            }
        });
    }

    private class ListAdapter extends ArrayAdapter<ForumItem> {
        private List<ForumItem> items;

        public ListAdapter(Context context, int textViewResourceId, List<ForumItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            Holder holder = null;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.forums_item, null);

                holder = new Holder();
                holder.title = (TextView) v.findViewById(R.id.forum_title);
                holder.body = (TextView) v.findViewById(R.id.forum_body);

                v.setTag(holder);

            } else {
                holder = (Holder) v.getTag();
            }

            ForumItem item = items.get(position);

            if (item != null) {
                TextView title = (TextView) v.findViewById(R.id.forum_title);
                TextView body = (TextView) v.findViewById(R.id.forum_body);
                holder.title.setText(item.title);
                holder.body.setText(item.body);
            }

            return v;
        }
    }

    public class Holder {
        TextView title;
        TextView body;
    }

    public class ForumItem {
        public String title;
        public String body;
    }

}
