package com.cyberthieves.trialapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GradesListAdapter extends ArrayAdapter<JSONObject> {
    int num;
    ArrayList<JSONObject> grades;
    Context context;

    public GradesListAdapter(Context context, int num, int id, ArrayList<JSONObject> grades){
        super(context, num, id, grades);
        this.num = num;
        this.context = context;
        this.grades = grades;
    }

    public View getView(int position, View converView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(num,parent,false);
        TextView serialno = (TextView)itemView.findViewById(R.id.serialno);
        TextView course = (TextView)itemView.findViewById(R.id.course);
        TextView name = (TextView)itemView.findViewById(R.id.name);
        TextView score = (TextView)itemView.findViewById(R.id.score);
        TextView weight = (TextView)itemView.findViewById(R.id.weight);
        TextView abs_marks = (TextView)itemView.findViewById(R.id.abs_marks);

        try {

            serialno.setText(grades.get(position).getString("s_no"));
            if(course!=null)
                course.setText(grades.get(position).getString("course"));
            name.setText(grades.get(position).getString("name"));
            score.setText(grades.get(position).getString("score"));
            weight.setText(grades.get(position).getString("weight"));
            abs_marks.setText(grades.get(position).getString("abs_marks"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }
}
