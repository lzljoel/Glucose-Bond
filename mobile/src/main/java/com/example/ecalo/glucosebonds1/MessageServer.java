package com.example.ecalo.glucosebonds1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 4/26/2016.
 */

public class MessageServer extends AppCompatActivity {

    ImageButton button;
    Context context;
    ListAdapter listAdapter;

    final String mCollection = "messages";

    ArrayList<ForumItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.message);

        Button post = (Button) findViewById(R.id.post_message);

        listItems = new ArrayList<>();

        listAdapter = new ListAdapter(context, R.layout.message, listItems);

        ListView lv = (ListView) findViewById(R.id.msg_listview);

        lv.setAdapter(listAdapter);

        fetchFromCollection(mCollection);

        final String currName = BaasUser.current().getName();
        final String name = ((TextView) findViewById(R.id.mentor_name)).getText().toString();

        final EditText title = (EditText) findViewById(R.id.message);
        //final EditText body = (EditText) findViewById(R.id.body);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaasDocument doc = new BaasDocument(mCollection);
                if (currName.equals(name)) {
                    doc.put("title", title.getText().toString());
                } else {
                    doc.put("body", title.getText().toString());
                }

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
                            fetchFromCollection(mCollection);
                            Log.e("F", "successfully saved document");
                        } else {
                            Log.e("F", "failed to create document");
                        }
                    }
                });

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsActivity = new Intent(context, ForumsDetailedServer.class);
                detailsActivity.putExtra("collection", mCollection);
                startActivity(detailsActivity);
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
                        item.title = doc.getString("title");
                        item.body = doc.getString("body");
                        items.add(item);
                    }
                    updateListView(items);
                } else {
                    Log.e("F", "failed to retrieve documents from collection");
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
                v = vi.inflate(R.layout.message_items, null);

                holder = new Holder();
                holder.title = (TextView) v.findViewById(R.id.left);
                holder.body = (TextView) v.findViewById(R.id.right);

                v.setTag(holder);

            } else {
                holder = (Holder) v.getTag();
            }

            ForumItem item = items.get(position);

            if (item != null) {
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
