package com.glowingsoft.mystudents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView loginButton,signupButton;
    EditText emailET,passwordET;
    String TAG = "MainActivity ";
    ProgressDialog pDialog;
    GlobalConfiguration globalConfiguration;
    String email,password;
    TinyDB tinyDB;
    ProgressDialog progress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //get Reference to Views
        getViews();
        progress = new ProgressDialog(this);


        globalConfiguration = new GlobalConfiguration(getApplicationContext());
        tinyDB = new TinyDB(getApplicationContext());
        int token = tinyDB.getInt("token");
        if(token > 0){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getViews() {
    emailET = (EditText) findViewById(R.id.email);
    passwordET = (EditText) findViewById(R.id.password);

    loginButton = (ImageView) findViewById(R.id.loginButton);
    signupButton = (ImageView) findViewById(R.id.signupButton);
    loginButton.setOnClickListener(this);
    signupButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                    if(globalConfiguration.isNetworkAvailable() == false){
                        Toast.makeText(getApplicationContext(),"Network Not available",Toast.LENGTH_SHORT).show();
                    }else {
                        loginServerRequest(email,password);
                    }
                break;

            case R.id.signupButton:
                Intent i = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void loginServerRequest(String e, String p) {


        //check email and password
        if(e.length() < 1 || p.length() < 1){
            Toast.makeText(getApplicationContext(),"Fields Can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(e.matches(emailPattern) == false){
            Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();
        String url = globalConfiguration.BASE_URL+globalConfiguration.LOGIN_URL+"?email="+e+"&password="+p;
        Log.d(TAG,"url"+url);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        Log.d("Login response",response);

                        try{
                            if(response.contains("failure")){
                                Toast.makeText(getApplicationContext(),"Account not exists",Toast.LENGTH_SHORT).show();

                            }else if(response.length() > 9){
                                Toast.makeText(getApplicationContext(),"Failed to Login",Toast.LENGTH_SHORT).show();
                            }else {
                                //successfull login

                                int token = Integer.parseInt(response);
                                tinyDB.putInt("token",token);

                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                                progress.dismiss();
                                finish();
                            }
                            progress.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // passwordET.setText("That didn't work!");
                progress.dismiss();
            }

        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
