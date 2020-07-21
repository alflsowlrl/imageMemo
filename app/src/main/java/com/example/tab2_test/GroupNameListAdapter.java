package com.example.tab2_test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GroupNameListAdapter extends ArrayAdapter<String> {
    private int resourceID;
    private ArrayList<String> items = new ArrayList<>();
    public GroupNameListAdapter(Context context, int ResourceId, ArrayList<String> group_name_list){
        super(context, ResourceId, group_name_list);
        this.items = group_name_list;
        this.resourceID = ResourceId;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
        }

        String group_name = items.get(position);

        if(group_name != null){
            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            if(tv != null){
                tv.setText(group_name);
            }
        }

        return v;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public String getItem(int position){
        return items.get(position);
    }
}
