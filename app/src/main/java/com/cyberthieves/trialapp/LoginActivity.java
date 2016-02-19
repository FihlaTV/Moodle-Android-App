package com.cyberthieves.trialapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {

    static private final String TAG = "Moodle-App";
    public static final String PREFS_NAME = "MoodleApp_Settings";
    private TextInputLayout mUserIdLayout;
    private EditText mUserId;
    private TextInputLayout mPasswordLayout;
    private EditText mPassword;
    private Button mlogIn;
    private TextView mforgotPasswd;
    private ProgressDialog pDialog;

    private SharedPreferences _preferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

        pDialog = new ProgressDialog(this);

        mUserIdLayout = (TextInputLayout) findViewById(R.id.email);
        mUserId = (EditText) findViewById(R.id.emailf);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password);
        mPassword = (EditText) findViewById(R.id.passwordf);
        mlogIn = (Button) findViewById(R.id.email_sign_in_button);
        mforgotPasswd= (TextView) findViewById(R.id.forgPass);
        mforgotPasswd.setMovementMethod(LinkMovementMethod.getInstance());

        mUserId.addTextChangedListener(new CustomTextWatcher(mUserId));
        mPassword.addTextChangedListener(new CustomTextWatcher(mPassword));


        mUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        mlogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                proceed(view);
            }
        });

    }

    private void proceed(View view) {
        Log.i(TAG, "Entered proceed()");
        if (!checkUserId()) {
            checkPasswd();
            return;
        }
        else if (!checkPasswd()) {
            checkUserId();
            return;
        }
        Log.i(TAG, "Input correct...");

        final String userId = mUserId.getText().toString();
        final String passwd = mPassword.getText().toString();
        final View view1 = view;

        showpDialog();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.208.20.32:1805/default/login.json?userid=" + userId + "&password=" + passwd;
        Log.d(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelPDialog();
                        try {
                            JSONObject response1 = new JSONObject(response);
                            boolean Success = response1.getBoolean("success");
                            if (Success){
                                Intent login = new Intent(LoginActivity.this,MainActivity.class);
                                JSONObject person=response1.getJSONObject("user");
                                String last_name = person.getString("last_name");
                                int id = person.getInt("id");
                                String first_name = person.getString("first_name");
                                String entry_no = person.getString("entry_no");
                                String email = person.getString("email");
                                String username = person.getString("username");
                                String password = person.getString("password");
                                int type = person.getInt("type_");

                                login.putExtra("firstname", first_name);
                                login.putExtra("lastname",last_name);
                                login.putExtra("entry",entry_no);
                                login.putExtra("email",email);
                                login.putExtra("username",username);
                                login.putExtra("password",password);
                                login.putExtra("id",id);
                                if(type==0)
                                {
                                    login.putExtra("type","0");
                                }
                                else
                                {
                                    login.putExtra("type","1");
                                }




                                startActivity(login);
                            }
                            else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                alert.setTitle("Invalid UserID or Password!!");

                                final String MessageToSubmit="Please enter the details again...";
                                alert.setMessage(MessageToSubmit);

                                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                            }
                        } catch (JSONException ex) {
                            Log.e(TAG, "Cannot parse response!!");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                cancelPDialog();
                Snackbar.make(view1, "No Internet Connection!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);

        //Intent start = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(start);
    }



    private boolean checkUserId() {
        Pattern pat = Pattern.compile("[a-zA-Z]{2}[a-zA-Z0-9]{1}[0-1]{1}[0-9]{5}");
        Matcher mat = pat.matcher(mUserId.getText().toString());
        if (mUserId.getText().toString().trim().isEmpty()) {
            mUserIdLayout.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            mUserIdLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean checkPasswd() {
        if (mPassword.getText().toString().trim().isEmpty()) {
            mPasswordLayout.setError(getString(R.string.err_msg_passwd));
            return false;
        } else {
            mPasswordLayout.setErrorEnabled(false);
        }
        return true;
    }

    private class CustomTextWatcher implements TextWatcher {

        private View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailf:
                    mUserIdLayout.setErrorEnabled(false);
                    break;
                case R.id.passwordf:
                    mPasswordLayout.setErrorEnabled(false);
                    break;
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void cancelPDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
}




