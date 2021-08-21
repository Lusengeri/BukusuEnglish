package com.alsoftware.bukusuenglish.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {
    private final int TAB_COUNT = 2;

    public MainPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DictionaryEntryFragment();
            case 1:
                return new DictionaryLookupFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }
}
