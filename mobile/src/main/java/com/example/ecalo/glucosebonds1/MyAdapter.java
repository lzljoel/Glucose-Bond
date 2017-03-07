package com.example.ecalo.glucosebonds1;

/**
 * Created by Kangsik on 3/2/16.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import retrofit.http.GET;
//import retrofit.http.Query;


public class MyAdapter extends ArrayAdapter<String> {


    private Activity context;
    private final List<String> mentorsNames;



    public MyAdapter(Activity context, ArrayList<String> mentorsNames) {
        super(context,R.layout.row_mentors_layout, mentorsNames);
        //super(context, R.layout.congressional_list_view_cell, legislators);
        this.context= context;
        this.mentorsNames=mentorsNames;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.row_mentors_layout, parent, false);
        //View rowView=inflater.inflate(R.layout.congressional_list_view_cell, null, true);

        final String mentorName = getItem(position);

        //ImageView in row



        //textView in row
        TextView textViewMentorName = (TextView) theView.findViewById(R.id.textViewMentorName);

        textViewMentorName.setText(mentorName);

        return theView;

    }



}
