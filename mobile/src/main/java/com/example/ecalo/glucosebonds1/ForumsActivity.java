package com.example.ecalo.glucosebonds1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.baasbox.android.BaasACL;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ForumsActivity extends AppCompatActivity {

    Context context;
    ListAdapter listAdapter;

    ArrayList<ForumItem> listItems;
    private TextView question;
    private String mCollection = "forumsGeneral";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.general_forum);

        View createNew = (View) findViewById(R.id.new_question);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Input Question Title and Content");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);


                // Set up the input
                final EditText title = new EditText(context);
                title.setInputType(InputType.TYPE_CLASS_TEXT);
                title.setHint("Question Title");

                final EditText question = new EditText(context);
                question.setInputType(InputType.TYPE_CLASS_TEXT);
                question.setHint("Question Details");

                layout.addView(title);
                layout.addView(question);

                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createDoc(title.getText().toString(), question.getText().toString(), "");
                        //fetchFromCollection(mCollection);
                        //m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        listItems = new ArrayList<>();

        listAdapter = new ListAdapter(context, R.layout.general_forum, listItems);

        ListView lv = (ListView) findViewById(R.id.listview);

        lv.setAdapter(listAdapter);

        fetchFromCollection(mCollection);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent detailsActivity = new Intent(context, ForumsDetailedServer.class);
                Intent detailsActivity = new Intent(context, DetailedForumActivity.class);
                detailsActivity.putExtra("title", listItems.get(position).title);

                try {
                    JSONObject bodyJSON = new JSONObject(listItems.get(position).body);
                    detailsActivity.putExtra("question", bodyJSON.getString("body"));
                    detailsActivity.putExtra("user", bodyJSON.getString("user"));
                    detailsActivity.putExtra("img", bodyJSON.getInt("img"));
                } catch (Exception e) {
                    Log.e("adsfl", e.getMessage());
                    e.printStackTrace();
                }
                //detailsActivity.putExtra("question", listItems.get(position).body);

                // WARNING: CAN ONLY USE UP TO 3 FORUMS AS OF NOW
                detailsActivity.putExtra("collection", "forum" + String.valueOf(position));
                startActivity(detailsActivity);
            }
        });

    }

    private void createDoc(String title, String body, String collection) {
        BaasDocument doc = new BaasDocument(mCollection);
        BaasUser user = BaasUser.current();
        doc.put("title", title);
        //doc.put("body", body);
        try {
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("user", user.getScope(BaasUser.Scope.REGISTERED).getString("Name"));
            Log.e("Tadsf", "" + user.getScope(BaasUser.Scope.REGISTERED).getInt("img"));
            bodyJSON.put("img", user.getScope(BaasUser.Scope.REGISTERED).getInt("img"));
            bodyJSON.put("body", body);

            doc.put("body", bodyJSON.toString());

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

        } catch (Exception e) {
            Log.e("FFFF", e.getMessage());
            e.printStackTrace();
        }

    }

    private void updateListView(ArrayList<ForumItem> items) {
        listItems.clear();
        listItems.addAll(items);
        listAdapter.notifyDataSetChanged();
    }

    private void fetchFromCollection(String collection) {
        Log.e("T", "getting documents from " + collection);
        BaasDocument.fetchAll(collection, new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> baasResult) {
                if (baasResult.isSuccess()) {
                    ArrayList<ForumItem> items = new ArrayList<ForumItem>();
                    for (BaasDocument doc : baasResult.value()) {

                        ForumItem item = new ForumItem();
                        item.title = doc.getString("title");
                        String stringJSON = doc.getString("body");
                        try {
                            JSONObject bodyJSON = new JSONObject(stringJSON);
                            item.img = bodyJSON.getInt("img");
                            item.body = bodyJSON.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                v = vi.inflate(R.layout.general_forum_item, null);

                holder = new Holder();
                holder.title = (TextView) v.findViewById(R.id.question);
                holder.img = (ImageView) v.findViewById(R.id.img);
                //holder.body = (TextView) v.findViewById(R.id.forum_body);

                v.setTag(holder);

            } else {
                holder = (Holder) v.getTag();
            }

            ForumItem item = items.get(position);

            if (item != null) {
                holder.title.setText(item.title);
                holder.img.setImageResource(item.img);
            }

            return v;
        }
    }

    public class Holder {
        TextView title;
        ImageView img;
        TextView body;
    }

    public class ForumItem {
        public int img;
        public String title;
        public String body;
        public String collection;
    }

}