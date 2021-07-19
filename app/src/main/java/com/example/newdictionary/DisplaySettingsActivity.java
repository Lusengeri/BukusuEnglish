package com.example.newdictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class DisplaySettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);

        Toolbar toolbar = findViewById(R.id.displaySettingsToolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.display_settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private DisplaySettingsActivity activityCallback;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            try {
                activityCallback = (DisplaySettingsActivity) context;
            } catch (ClassCastException e) {
                throw new ClassCastException((context.toString() + "must implement ChangeListener"));
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.display_preferences, rootKey);

            SwitchPreferenceCompat themePreference = findPreference("theme");
            themePreference.setOnPreferenceChangeListener(new SwitchPreferenceCompat.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean value = (boolean) newValue;

                    if (!value) {
                        //Set app theme to light-mode
                        getPreferenceManager().getSharedPreferences().edit().putInt("Theme", R.style.CustomAppTheme).apply();
                    } else {
                        //Set app theme to dark-mode
                        getPreferenceManager().getSharedPreferences().edit().putInt("Theme", R.style.CustomAppTheme2).apply();
                    }
                    activityCallback.recreate();
                    return true;
                }
            });
        }
    }
}