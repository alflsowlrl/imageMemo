package com.example.tab2_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class FragmentTab extends Fragment {
    String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_tab, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(name);

        return view;
    }


}

