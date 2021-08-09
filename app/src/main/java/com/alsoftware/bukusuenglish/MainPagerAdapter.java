package com.alsoftware.bukusuenglish;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alsoftware.bukusuenglish.ui.main.DictionaryEntryFragment;
import com.alsoftware.bukusuenglish.ui.main.DictionaryLookupFragment;


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
                return new DictionaryEntryFragment();
            case 1:
                return new DictionaryLookupFragment();
            default:
                return null;
        }
    }
}
