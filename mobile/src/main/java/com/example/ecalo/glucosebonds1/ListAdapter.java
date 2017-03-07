package com.example.ecalo.glucosebonds1;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by tototones24 on 4/26/16.
 */
public class ListAdapter extends ArrayAdapter<String> {
    Context context;
    String[] mentorNames;
    int[] mentorImages;
    String[] mentorMessage;
    LayoutInflater inflater;

    public ListAdapter(Context context, String[] mentNames, int[] mentPics, String[] menMessages) {
        super(context, R.layout.mentors_list, mentNames);
        this.context = context;
        this.mentorImages = mentPics;
        this.mentorNames = mentNames;
        this.mentorMessage = menMessages;
    }

    public class ViewHolder {
        ImageView pic;
        TextView message;
        TextView name;
    }

    public View getView(int position, View convertView, ViewGroup vgroup) {
        if (convertView == null) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mentors_list, null);
        }
        final ViewHolder holder = new ViewHolder();


        return convertView;
    }
}
