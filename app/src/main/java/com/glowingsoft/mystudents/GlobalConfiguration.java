package com.glowingsoft.mystudents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mg on 5/21/2016.
 */
public class GlobalConfiguration {
    Context mContext;
    public String BASE_URL = "http://glowingsoft.com/mystudents/";
    public String LOGIN_URL = "login.php";
    public String SIGNUP_URL = "signup.php";
    public String USER_PROFILE ="user_profile.php";
    public String NEW_POST ="new_post.php";

    public GlobalConfiguration(Context applicationContext) {
        mContext = applicationContext;
    }
    public GlobalConfiguration(){

    };

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
