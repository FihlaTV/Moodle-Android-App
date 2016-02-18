package com.cyberthieves.trialapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class grading extends Activity {

    String entry_n;
    String pass_n;
    String result="ghsidh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);
        entry_n=getIntent().getExtras().getString("entry");
        pass_n=getIntent().getExtras().getString("pass");


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.208.20.32:1805/default/grades.json";

        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            result = "";
                            JSONObject response1 = new JSONObject(response);
                            JSONArray courses = response1.getJSONArray("courses");
                            JSONArray grades = response1.getJSONArray("grades");
                            Log.i("asf",response);
                            for (int i = 0; i < grades.length(); i++) {
                                JSONObject c = courses.getJSONObject(i);
                                JSONObject g = grades.getJSONObject(i);
                                result = result + (i + 1) + ".   ";
                                String code = c.getString("code");
                                code = code.toUpperCase();
                                result += (code + "   ");
                                String name = g.getString("name");
                                result += (name + "   ");
                                double score = g.getDouble("score");
                                double out_of = g.getDouble("out_of");
                                result += (score + "/" + out_of + "   ");
                                double weightage = g.getDouble("weightage");
                                result += (weightage + "   ");
                                double abs_marks = score / out_of * weightage;
                                abs_marks = (int) (abs_marks * 100);
                                abs_marks /= 100;
                                result += (abs_marks + "\n");
                                Log.i("asf",result);
                                TextView vi=(TextView)findViewById(R.id.t1);
                                Log.i("asf",result+"bingo\n");
                                vi.setText(result);
                            }
                        } catch (JSONException ex) {
Log.e("asff",ex.toString());
                        }
                    }

                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                // hide the progress dialog
            Log.e("asff",error.toString());

            }
        });
        queue.add(myReq);


        Toast.makeText(getApplicationContext(),"bingo",Toast.LENGTH_LONG).show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
