package com.example.tab2_test;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentStatePagerAdapter{


    private ArrayList<FragmentTab> fragments = new ArrayList<FragmentTab>();

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Override
    public FragmentTab getItem(int position){
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    void addItems(FragmentTab fragment){
        fragments.add(fragment);
    }
}