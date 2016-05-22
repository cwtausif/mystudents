package com.glowingsoft.mystudents.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glowingsoft.mystudents.R;

/**
 * Created by mg on 5/21/2016.
 */
public class FeedFragment extends Fragment {
    public FeedFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        serverRequest();

        return inflater.inflate(R.layout.feed_fragment, container, false);

    }

    public void serverRequest(){

    }

}
