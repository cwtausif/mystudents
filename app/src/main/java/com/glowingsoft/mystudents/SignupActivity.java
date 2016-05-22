package com.glowingsoft.mystudents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nameET,emailET,passwordET;
    ImageView signupButton;
    GlobalConfiguration globalconf;
    String name,email,password;
    TinyDB tinyDb;
    ProgressDialog progress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getViews();
        tinyDb = new TinyDB(getApplicationContext());
        globalconf = new GlobalConfiguration(getApplicationContext());
        progress = new ProgressDialog(this);
    }
    

    private void getViews() {
    nameET = (EditText) findViewById(R.id.name);
    emailET = (EditText) findViewById(R.id.email);
    passwordET = (EditText) findViewById(R.id.password);
    signupButton = (ImageView) findViewById(R.id.signupButton);    
    signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                if(name.length() < 1 || email.length() < 1 || password.length() < 1){
                    Toast.makeText(getApplicationContext(),"Fields can't be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.matches(emailPattern) == false){
                    Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(globalconf.isNetworkAvailable() == false){
                    Toast.makeText(getApplicationContext(),"Network Not available",Toast.LENGTH_SHORT).show();
                }else {
                    signupServerRequest();
                }

        }
    }


    private void signupServerRequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        progress.show();

        String url = globalconf.BASE_URL+globalconf.SIGNUP_URL+"?name="+name+"&email="+email+"&password="+password;
        Log.d("url signup",url);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response ",response);
                        progress.dismiss();

                        // Display the first 500 characters of the response string.
                        try{
                            if(response.contains("failure")){
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed or Email already exists",Toast.LENGTH_LONG).show();
                            }else if(response.length() > 20) {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed or Email already exists",Toast.LENGTH_LONG).show();
                            }else {
                                //successfull login
                                String tokenString = response;
                                int token = Integer.parseInt(tokenString);
                                tinyDb.putInt("token",token);
                                Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
                                startActivity(intent);
                                progress.dismiss();
                                finish();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","volley error");
                progress.hide();
            }

        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
