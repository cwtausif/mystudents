package com.glowingsoft.mystudents.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.glowingsoft.mystudents.R;

/**
 * Created by mg on 5/21/2016.
 */
public class ChatFragment extends Fragment {
    ListView ideasList;
    String[] values = new String[] {"My Students",
            "Teletporst Offfer",
            "AIA",
            "Let it Go",
            "Smart Clinic",
            "Team Khaday",
            "Bhook"
    };
    public ChatFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        ideasList = (ListView) view.findViewById(R.id.ideasList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        ideasList.setAdapter(adapter);
        return view;

    }
}
