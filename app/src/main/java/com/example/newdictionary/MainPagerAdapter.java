package com.example.newdictionary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class MainPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public MainPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabCount = numberOfTabs;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchResultFragment();
            case 1:
                return new WordListFragment();
            default:
                return null;
        }
    }
}
