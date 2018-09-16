package com.cyberthieves.trialapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyberthieves.trialapp.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Grades extends Fragment{

    static private final String TAG = "Moodle-App";
    public String course_code;
    private TextView no_grades;

    public Grades() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_grades, container, false);
        final View view1 = view;

        no_grades = (TextView) view.findViewById(R.id.no_grades);
        final JSONArray grades = new JSONArray();
        final ArrayList<JSONObject> grades_list = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getString(R.string.domain)+"/courses/course.json/"+course_code+"/grades";

        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject response1 = new JSONObject(response);

                            JSONArray grades1 = response1.getJSONArray("grades");
                            if (grades1.length() == 0) {

                                for (int i = 0; i < grades1.length(); i++) {
                                    JSONObject g = grades1.getJSONObject(i);
                                    JSONObject temp = new JSONObject();

                                    temp.put("s_no", i + 1 + ".");

                                    String name = g.getString("name");
                                    temp.put("name", name);

                                    double score = g.getDouble("score");
                                    double out_of = g.getDouble("out_of");
                                    temp.put("score", score + "/" + out_of);

                                    double weightage = g.getDouble("weightage");
                                    temp.put("weight", weightage);

                                    double abs_marks = score / out_of * weightage;
                                    abs_marks = (int) (abs_marks * 100);
                                    abs_marks /= 100;
                                    temp.put("abs_marks", abs_marks);
                                    grades.put(temp);
                                }

                                Log.d(TAG,grades.toString());
                                if(grades.length()==0)
                                    no_grades.setVisibility(View.VISIBLE);
                                else
                                    no_grades.setVisibility(View.GONE);
                                for (int j = 0; j < grades.length(); j++)
                                    grades_list.add(grades.getJSONObject(j));
                            }
                        } catch (JSONException ex) {
                            Log.e("Cannot parse response!!", ex.toString());
                        }

                        ListView listV = (ListView) view1.findViewById(R.id.notifiList);
                        GradesListAdapter adapter = new GradesListAdapter(getContext(), R.layout.fragment_grades, R.id.serialno, grades_list);
                        listV.setAdapter(adapter);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Can't Access Server", error.toString());
            }
        });
        queue.add(myReq);
        return view;
    }
}
