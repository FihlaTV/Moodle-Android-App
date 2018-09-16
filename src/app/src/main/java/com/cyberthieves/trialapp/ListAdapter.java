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

public class ListAdapter extends ArrayAdapter<JSONObject> {

    int num;
    ArrayList<JSONObject> notification;
    Context context;

    public ListAdapter(Context context, int num, int id, ArrayList<JSONObject> notification){
        super(context, num, id, notification);
        this.num = num;
        this.context = context;
        this.notification = notification;
    }

    public View getView(int position, View converView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(num,parent,false);
        TextView serialno = (TextView)itemView.findViewById(R.id.serialno);
        TextView notific = (TextView)itemView.findViewById(R.id.notification);
        TextView time = (TextView)itemView.findViewById(R.id.time);

        try {

            serialno.setText(notification.get(position).getString("s_no"));

            notific.setText(notification.get(position).getString("description"));

            time.setText(notification.get(position).getString("created_at"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }
}
