package com.example.ecalo.glucosebonds1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joel on 4/19/16.
 */
public class DetailedForumActivity extends AppCompatActivity{
    ImageButton button;
    Context context;

    ListAdapter listAdapter;
    ArrayList<String> listItems;

    String mCollection;

    public String FirstName;
    public String LastName;
    public String DayPosted;
    public String likes;
    public String question;
    public String questionDescription;
    public String numberOfComments;
    public String commentName;
    public String secondComment;
    public String  secondCommentName;
    public String firstComment;

    public TextView title;
    public TextView questionContent;
    public TextView nameDate;
    public ImageView m_img;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_forum_details);
//        button = (ImageButton) findViewById(R.id.img_btn);
//        button.setImageResource(R.drawable.education_screen);
        title = (TextView) findViewById(R.id.title);
        questionContent = (TextView) findViewById(R.id.questionContent);
        nameDate = (TextView) findViewById(R.id.name_date);
        m_img = (ImageView) findViewById(R.id.main_img);

        mCollection = getIntent().getStringExtra("collection");
        listItems = new ArrayList<>();

        ListView lv = (ListView) findViewById(R.id.listview);

        listAdapter = new ListAdapter(context, R.layout.activity_forum_details, listItems);

        lv.setAdapter(listAdapter);

        try {
            testJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fetchFromCollection(mCollection);

        String contentTitle = getIntent().getStringExtra("title");
        String contentQuestion = getIntent().getStringExtra("question");

        title.setText(contentTitle);
        questionContent.setText(contentQuestion);

        View createNew = (View) findViewById(R.id.new_question);

        int main_img = getIntent().getIntExtra("img", -1);

        String name = getIntent().getStringExtra("user");

        m_img.setImageResource(main_img);
        title.setText(contentTitle);
        questionContent.setText(contentQuestion);
        nameDate.setText(name + " Posted 4/28/16");

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Input Comment");

                final EditText comment = new EditText(context);
                comment.setInputType(InputType.TYPE_CLASS_TEXT);
                comment.setHint("Comment text");

                builder.setView(comment);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //createDoc(title.getText().toString(), comment.getText().toString(), "");
                        createDoc(comment.getText().toString(), "", "");
                        fetchFromCollection(mCollection);
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



    }


    public void testJSON() throws JSONException {

        //This creates the new json object which represents the specific question that you want to see in
        //forum page
        JSONObject forumQuestion = new JSONObject();
        forumQuestion.put("firstName", "blah");
        forumQuestion.put("lastName", "Smith");
        forumQuestion.put("dayPosted", "4/28/16");
        forumQuestion.put("likes", "6");
        forumQuestion.put("question", "What are some ways to get your child to exercise more?");
        forumQuestion.put("comments", 7);
        forumQuestion.put("questionDescription", "My son is currently 10 years old, diagnosed with type 1 diabetes over a year ago. He was never an athletic child and mostly spends his time in front of the tv. I have tried to get him to go outside but can’t motivate him to exercise for long. Any thoughts?");
        //This is adding the json object in an array that we will parse through in order to get this information onto
        //the actual screen.
        JSONArray forumQuestionArray = new JSONArray();
        forumQuestionArray.put(forumQuestion);

        //you use this to extract information and population the text boxes
        JSONObject subObject = forumQuestionArray.getJSONObject(0);
        //FirstName = subObject.getString("firstName");
        //LastName = subObject.getString("lastName");
        DayPosted = subObject.getString("dayPosted");
        likes = subObject.getString("likes");
        question = subObject.getString("question");
        numberOfComments = subObject.getString("comments");
        questionDescription = subObject.getString("questionDescription");

        title.setText(question);
        questionContent.setText(questionDescription);
    }

    private void createDoc(String title, String body, String collection) {
        BaasDocument doc = new BaasDocument(mCollection);
        doc.put("title", title);

        BaasUser user = BaasUser.current();

        try {
            JSONObject bodyJSON = new JSONObject();
            bodyJSON.put("user", user.getScope(BaasUser.Scope.REGISTERED).getString("Name"));
            bodyJSON.put("img", user.getScope(BaasUser.Scope.REGISTERED).getInt("img"));
            bodyJSON.put("body", title);

            doc.put("body", bodyJSON.toString());

        } catch (Exception e) {
            e.printStackTrace();
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

    private void updateListView(ArrayList<String> items) {
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
                    ArrayList<String> items = new ArrayList<String>();
                    for (BaasDocument doc : baasResult.value()) {
                        Log.e("F", "document is: " + doc);
                        //item.collection = doc.getString("collection");
                        items.add(doc.getString("body"));
                    }
                    updateListView(items);
                } else {
                    Log.e("F", "failed to retrieve documents from forums-general");
                }
            }
        });
    }

    private class ListAdapter extends ArrayAdapter<String> {
        private List<String> items;

        public ListAdapter(Context context, int textViewResourceId, List<String> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            Holder holder = null;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.comments, null);

                holder = new Holder();
                holder.img = (ImageView) v.findViewById(R.id.img);
                holder.name = (TextView) v.findViewById(R.id.name);
                holder.content = (TextView) v.findViewById(R.id.comment);

                v.setTag(holder);

            } else {
                holder = (Holder) v.getTag();
            }

            String stringJSON = items.get(position);
            try {
                JSONObject bodyJSON = new JSONObject(stringJSON);
                String name = bodyJSON.getString("user");
                int img = bodyJSON.getInt("img");
                String body = bodyJSON.getString("body");

                holder.img.setImageResource(img);
                holder.name.setText(name);
                holder.content.setText(body);

            } catch (Exception e) {
                Log.e("T", e.getMessage());
                e.printStackTrace();
            }

            return v;
        }
    }

    private class Holder {
        ImageView img;
        TextView name;
        TextView content;
    }

}
