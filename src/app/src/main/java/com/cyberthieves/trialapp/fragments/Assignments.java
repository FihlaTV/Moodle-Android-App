package com.cyberthieves.trialapp.fragments;

import android.app.ListActivity;
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
import com.cyberthieves.trialapp.GradesListAdapter;
import com.cyberthieves.trialapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Assignments extends Fragment{

    public String course_code;
    static private final String TAG = "Moodle-App";
    private TextView no_assign;

    public Assignments() {
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
        final View view;
        view = inflater.inflate(R.layout.assignments_list, container, false);

        no_assign = (TextView) view.findViewById(R.id.no_assign);

        final ArrayList<JSONObject> assign_list = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getString(R.string.domain)+"/courses/course.json/"+course_code+"/assignment";
        Log.d(TAG,url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response1 = new JSONObject(response);
                            JSONArray assignments = response1.getJSONArray("assignments");

                            for(int i=0; i<assignments.length(); i++) {
                                JSONObject c = assignments.getJSONObject(i);
                                JSONObject temp = new JSONObject();
                                temp.put("s_no", i + 1 + ".");

                                String name = c.getString("name");
                                temp.put("description", name);

                                String deadline = c.getString("deadline");
                                temp.put("created_at", "Deadline: "+deadline);
                                assignments.put(temp);
                            }
                            Log.d(TAG,assignments.toString());
                            if(assignments.length()==0)
                                no_assign.setVisibility(View.VISIBLE);
                            else
                                no_assign.setVisibility(View.GONE);

                            for (int j = 0; j < assignments.length(); j++)
                                assign_list.add(assignments.getJSONObject(j));

                        } catch (JSONException ex) {
                            Log.e(TAG, ex.toString());
                        }

                        ListView listV = (ListView) view.findViewById(R.id.notifiList);
                        GradesListAdapter adapter = new GradesListAdapter(getContext(), R.layout.fragment_assignments, R.id.serialno, assign_list);
                        listV.setAdapter(adapter);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        queue.add(myReq);
    return  view;
    }
}
