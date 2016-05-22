package com.glowingsoft.mystudents.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.glowingsoft.mystudents.GlobalConfiguration;
import com.glowingsoft.mystudents.MainActivity;
import com.glowingsoft.mystudents.NewPostActivity;
import com.glowingsoft.mystudents.R;
import com.glowingsoft.mystudents.TinyDB;
import com.glowingsoft.mystudents.GlobalConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mg on 5/21/2016.
 */
public class ProfileFragment extends Fragment {
    Button logoutButton,postButton;
    TinyDB tinyDB;
    TextView username;
    ProgressDialog progressDialog;
    GlobalConfiguration globalConfiguration;
    public ProfileFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        globalConfiguration = new GlobalConfiguration(getActivity());
        username = (TextView) view.findViewById(R.id.username);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        postButton = (Button) view.findViewById(R.id.postButton);
        progressDialog = new ProgressDialog(getActivity());
        tinyDB = new TinyDB(getActivity().getApplicationContext());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                tinyDB.putInt("token",0);
                tinyDB.putString("username","");
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPost();
            }
        });
        if(tinyDB.getString("username").length() < 1){
            serverRequest();
        }else{
            username.setText(tinyDB.getString("username"));
        }

        return view;
    }

    private void newPost() {
        Toast.makeText(getActivity(),"New Post",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),NewPostActivity.class);
        startActivity(intent);
    }

    private void serverRequest() {
        if(globalConfiguration.isNetworkAvailable() == false){
            Toast.makeText(getActivity(),"Network Not available",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        String urlJsonObj = globalConfiguration.BASE_URL+globalConfiguration.USER_PROFILE+"?token="+tinyDB.getInt("token");
        Log.d("Profile Url",urlJsonObj);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Profile", response.toString());
                progressDialog.dismiss();
                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = null;
                    name = response.getString("name");
                    username.setText(name);
                    tinyDB.putString("username",name);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("Profile", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);
    }

}
