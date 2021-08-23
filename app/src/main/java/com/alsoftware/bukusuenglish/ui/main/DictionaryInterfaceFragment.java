package com.alsoftware.bukusuenglish.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.alsoftware.bukusuenglish.MainActivity;
import com.alsoftware.bukusuenglish.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DictionaryInterfaceFragment extends Fragment {
    private ViewPager2 viewPager;
    private MainActivity parentActivity;

    public DictionaryInterfaceFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            parentActivity = (MainActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must be ....");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_interface, container, false);

        TabLayout tabLayout = parentActivity.getTabLayout();
        viewPager = view.findViewById(R.id.mainViewPager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(parentActivity.getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(mainPagerAdapter);

        TabLayoutMediator.TabConfigurationStrategy strategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("DEFINITION");
                } else {
                    tab.setText("LOOKUP");
                }
            }
        };
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, strategy);
        mediator.attach();
        return view;
    }

    public void resetPager() {
        viewPager.setCurrentItem(0);
    }
}