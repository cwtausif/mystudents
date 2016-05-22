package com.glowingsoft.mystudents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity{
    Button postButton;
    EditText postMessage;
    String message;
    GlobalConfiguration globalConfiguration;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        globalConfiguration = new GlobalConfiguration(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        postButton = (Button) findViewById(R.id.publishButton);
        postMessage = (EditText) findViewById(R.id.postMessage);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = postMessage.getText().toString();
                if(message.length()<5){
                    Toast.makeText(getApplicationContext(),"Insufficient Content",Toast.LENGTH_SHORT);
                    return;
                }
                if(globalConfiguration.isNetworkAvailable() == false){
                    Toast.makeText(getApplicationContext(),"Network Not available",Toast.LENGTH_SHORT).show();
                }else{
                    serverRequest(message);
                }

            }
        });
    }

    private void serverRequest(final String message) {
        progressDialog.show();
        String url = globalConfiguration.BASE_URL + globalConfiguration.NEW_POST;
        Log.d("New post Url", "url" + url);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        Log.d("New post", response);
                        progressDialog.dismiss();
                        try {
                            if (response.contains("failure")) {
                                Toast.makeText(getApplicationContext(), "Failed To Post", Toast.LENGTH_SHORT).show();

                            } else if (response.length() > 9) {
                                Toast.makeText(getApplicationContext(), "Failed To Post", Toast.LENGTH_SHORT).show();
                            } else {
                                //successfull login
                                Intent intent = new Intent(NewPostActivity.this, HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Successfully posted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // passwordET.setText("That didn't work!");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
