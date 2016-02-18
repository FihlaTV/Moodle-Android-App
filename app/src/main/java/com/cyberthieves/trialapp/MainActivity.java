package com.cyberthieves.trialapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pDialog;
    String  entrynum="";
    String email="";
    String  password="";
    String  type="";
    String  id="";

    TextView user_name;
    TextView user_entry;
    TextView user_email;
    TextView user_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent start = getIntent();
        final String entryNum = start.getStringExtra("Entry_Number");
        final TextView en = (TextView) findViewById(R.id.currentUser);
        pDialog = new ProgressDialog(this);
     

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View en1=navigationView.getHeaderView(0);
        TextView text = (TextView) en1.findViewById(R.id.currentUser);
        text.setText(entryNum);


        String name= getIntent().getExtras().getString("firstname")+" "+getIntent().getExtras().getString("lastname");
       // TextView use=(TextView)(findViewById(R.id.currentUser));
        text.setText(name);


        entrynum=getIntent().getExtras().getString("entry");
        email=getIntent().getExtras().getString("email");
        password=getIntent().getExtras().getString("password");
        type=getIntent().getExtras().getString("type");
        id=getIntent().getExtras().getString("id");

        user_name=(TextView)(findViewById(R.id.name));
        user_entry=(TextView)(findViewById(R.id.entry));
        user_email=(TextView)(findViewById(R.id.email));
        user_type=(TextView)(findViewById(R.id.type));


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





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

         if (id == R.id.nav_gallery) {

             Intent opengrades=new Intent(this,grading.class);
             opengrades.putExtra("pass",password);
             opengrades.putExtra("entry",entrynum);


            startActivity(opengrades);
        } else if (id == R.id.nav_slideshow) {
             showpDialog();
             RequestQueue queue = Volley.newRequestQueue(this);
             String url = "http://10.208.20.32:1805/default/notifications.json";
             StringRequest myReq = new StringRequest(Request.Method.GET,
                     url,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             cancelPDialog();
                             try {
                                 JSONObject response1 = new JSONObject(response);
                                 JSONArray notifications = response1.getJSONArray("notifications");
                                 for(int i=0; i<notifications.length(); i++) {
                                     JSONObject notification = notifications.getJSONObject(i);
                                     int userId = notification.getInt("user_id");
                                 }
                             } catch (JSONException ex) {
                                 AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                 alert.setTitle("Cannot complete the action!!");

                                 final String MessageToSubmit="Please login again...";
                                 alert.setMessage(MessageToSubmit);

                                 alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int whichButton) {
                                         dialog.cancel();
                                         Intent login = new Intent(MainActivity.this,LoginActivity.class);
                                         startActivity(login);
                                     }
                                 });
                                 alert.show();
                             }
                         }

                     }, new Response.ErrorListener() {

                 @Override
                 public void onErrorResponse(VolleyError error) {
                     cancelPDialog();
                     AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                     alert.setTitle("No Internet Connection!");
                     alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int whichButton) {
                             dialog.cancel();
                         }
                     });
                     alert.show();
                 }
             });
             queue.add(myReq);
        }
         else if (id == R.id.nav_sen3) {
             showpDialog();
             RequestQueue queue = Volley.newRequestQueue(this);
             String url = "http://10.208.20.32:1805/default/logout.json";
             StringRequest myReq = new StringRequest(Request.Method.GET,
                     url,
                     new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             cancelPDialog();
                             Intent login = new Intent(MainActivity.this,LoginActivity.class);
                             startActivity(login);
                         }

                     }, new Response.ErrorListener() {

                 @Override
                 public void onErrorResponse(VolleyError error) {
                     cancelPDialog();
                     AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                     alert.setTitle("No Internet Connection!");
                     alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int whichButton) {
                             dialog.cancel();
                         }
                     });
                     alert.show();
                 }
             });
             queue.add(myReq);
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
            pDialog.hide();
    }




//    public void onNavigationDrawerItemSelected(int position) {
//        switch(position) {
//            case 0:
//                startActivity(new Intent(this, Overview.class)); break;
//            case 1:
//                startActivity(new Intent(this, Assignments.class)); break;
//            case 2:
//                startActivity(new Intent(this, Threads.class)); break;
//            default:
//
//        }
//    }



}
