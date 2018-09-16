package com.cyberthieves.trialapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static private final String TAG = "Moodle-App";
    private Toolbar toolbar;
    private static int current_id=R.id.overview;;
    private TextView user_name;
    private TextView user_entry;
    private TextView user_email;
    private TextView user_type;
    private TextView head;
    private TextView mNotifications;
    private ListView listVw;
    private Menu menu;
    private Menu menu2;
	private int no_of_courses;
    private ArrayList<String> course_list;
    private ArrayList<String> course_name;
    private ArrayList<String> course_description;

    private Toast toast =null;
    private ProgressDialog pDialog;
    String entrynum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Overview");
        pDialog = new ProgressDialog(this);

        user_name=(TextView)(findViewById(R.id.name));
        user_entry=(TextView)(findViewById(R.id.entry));
        user_email=(TextView)(findViewById(R.id.email));
        user_type=(TextView)(findViewById(R.id.type));
        head=(TextView)(findViewById(R.id.head));
        mNotifications = (TextView) findViewById(R.id.notifications);
        listVw = (ListView) findViewById(R.id.notifiList);
        course_list = new ArrayList<>();
        course_name = new ArrayList<>();
        course_description = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu().getItem(3).getSubMenu();
        menu2=navigationView.getMenu();

        final View view1 = findViewById(android.R.id.content);

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = getString(R.string.domain)+"/courses/list.json";
        Log.d(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject response1;
                        try {
                            response1 = new JSONObject(response);
                            JSONArray courses = response1.getJSONArray("courses");
							
                            for(no_of_courses=0; no_of_courses<courses.length(); no_of_courses++) {
                                JSONObject c = courses.getJSONObject(no_of_courses);
                                String code = c.getString("code");
                                code=code.toUpperCase();
                                course_list.add(code);
                                course_name.add(c.getString("name"));
                                course_description.add(c.getString("description"));
                                MenuItem item = menu.add(1, no_of_courses, Menu.NONE, code);
                                item.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
                            }

                        } catch (JSONException ex) {
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                cancelPDialog();
                Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);

        View en1 = navigationView.getHeaderView(0);
        TextView text = (TextView) en1.findViewById(R.id.currentUser);

        String name = getIntent().getExtras().getString("firstname") + " " + getIntent().getExtras().getString("lastname");
        text.setText(name);

        entrynum=getIntent().getExtras().getString("entry");
        String  email=getIntent().getExtras().getString("email");
        String  password=getIntent().getExtras().getString("password");
        String  type=getIntent().getExtras().getString("type");

        if(type.equals("0"))
        {
            user_type.setText("Student Login");
        }
        else
        {
            user_type.setText("Professor Login");
        }
        user_name.setText(name);
        user_entry.setText(entrynum);
        user_email.setText(email);

        onNavigationItemSelected(menu2.findItem(current_id));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notifications) {
            current_id=id;
            toolbar.setTitle("Notifications");
            user_name.setVisibility(View.GONE);
            user_entry.setVisibility(View.GONE);
            user_email.setVisibility(View.GONE);
            user_type.setVisibility(View.GONE);
            head.setVisibility(View.GONE);
            mNotifications.setVisibility(View.VISIBLE);
            listVw.setVisibility(View.VISIBLE);

            final View view1 = findViewById(android.R.id.content);
            final JSONArray notifications = new JSONArray();
            final ArrayList<JSONObject> noti_list = new ArrayList<JSONObject>();

            RequestQueue queue = Volley.newRequestQueue(this);
            final String url = getString(R.string.domain)+"/default/notifications.json";
            Log.d(TAG, url);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mNotifications.setText("");
                            JSONObject response1;
                            try {
                                response1 = new JSONObject(response);
                                JSONArray noti = response1.getJSONArray("notifications");

                                if (noti.length() == 0) {
                                    mNotifications.append("No Notifications Yet!");
                                } else {
                                    for (int i = 0; i < noti.length(); i++) {
                                        JSONObject c = noti.getJSONObject(i);
                                        JSONObject temp = new JSONObject();
                                        temp.put("s_no", i + 1 + ".");
                                        String notification = c.getString("description");
                                        Spanned sp = Html.fromHtml(notification);
                                        temp.put("description", sp);
                                        String created_at = c.getString("created_at");
                                        temp.put("created_at", created_at);
                                        notifications.put(temp);
                                    }
                                }

                                if (notifications != null) {
                                    for (int i = 0; i < notifications.length(); i++) {
                                        noti_list.add(notifications.getJSONObject(i));
                                    }
                                }

                            } catch (JSONException ex) {
                            }
                            ListView listV = (ListView) findViewById(R.id.notifiList);
                            ListAdapter adapter = new ListAdapter(getBaseContext(), R.layout.list_item, R.id.serialno, noti_list);
                            listV.setAdapter(adapter);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    cancelPDialog();
                    Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });


            queue.add(myReq);
        } else if (id == R.id.logout) {
            showpDialog();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getString(R.string.domain)+"/default/logout.json";
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cancelPDialog();
                            Intent login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(login);
                            finish();
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    cancelPDialog();
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Unable to Access Server!");
                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            });
            queue.add(myReq);
        } else if (id == R.id.grades) {
            current_id=id;
            toolbar.setTitle("Grades");
            user_name.setVisibility(View.GONE);
            user_entry.setVisibility(View.GONE);
            user_email.setVisibility(View.GONE);
            user_type.setVisibility(View.GONE);
            head.setVisibility(View.GONE);
            mNotifications.setVisibility(View.GONE);
            listVw.setVisibility(View.VISIBLE);

            final JSONArray grades = new JSONArray();
            final ArrayList<JSONObject> grades_list = new ArrayList<>();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getString(R.string.domain)+"/default/grades.json";

            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject response1 = new JSONObject(response);

                                JSONArray courses = response1.getJSONArray("courses");
                                JSONArray grades1 = response1.getJSONArray("grades");
                                if (courses.length() == 0) {

                                } else {
                                    for (int i = 0; i < courses.length(); i++) {
                                        JSONObject c = courses.getJSONObject(i);
                                        JSONObject g = grades1.getJSONObject(i);
                                        JSONObject temp = new JSONObject();

                                        temp.put("s_no", i + 1 + ".");

                                        String code = c.getString("code");
                                        temp.put("course", code.toUpperCase());

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

                                    if (grades != null) {
                                        for (int j = 0; j < grades.length(); j++) {
                                            grades_list.add(grades.getJSONObject(j));
                                        }
                                    }
                                }
                            } catch (JSONException ex) {
                                Log.e("Cannot parse response!!", ex.toString());
                            }

                            ListView listV = (ListView) findViewById(R.id.notifiList);
                            GradesListAdapter adapter = new GradesListAdapter(getBaseContext(), R.layout.grades_list, R.id.serialno, grades_list);
                            listV.setAdapter(adapter);
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Can't Access Server", error.toString());
                }
            });
            queue.add(myReq);
        }
        else if (id == R.id.overview) {
            current_id=id;
            toolbar.setTitle("Overview");
            user_name.setVisibility(View.VISIBLE);
            user_entry.setVisibility(View.VISIBLE);
            user_email.setVisibility(View.VISIBLE);
            user_type.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
            mNotifications.setVisibility(View.GONE);
            listVw.setVisibility(View.GONE);
        }

        for(int i=0; i<no_of_courses; i++) {
            if (id == i) {
                user_name.setVisibility(View.GONE);
                user_entry.setVisibility(View.GONE);
                user_email.setVisibility(View.GONE);
                user_type.setVisibility(View.GONE);
                head.setVisibility(View.GONE);
                mNotifications.setVisibility(View.GONE);
                listVw.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(),CourseDetails.class);
                intent.putExtra("course_name",course_name.get(i));
                intent.putExtra("course_description",course_description.get(i));
                intent.putExtra("course_code",course_list.get(i));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void cancelPDialog() {
        if (pDialog.isShowing())
            pDialog.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("id", current_id);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        current_id = savedInstanceState.getInt("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, current_id + "      ****       " + menu2.findItem(current_id).toString());
        onNavigationItemSelected(menu2.findItem(current_id));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, current_id + "      ****       " + menu2.findItem(current_id).toString());
        onNavigationItemSelected(menu2.findItem(current_id));
    }
}