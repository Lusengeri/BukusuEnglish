package com.example.newdictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsAndHelpActivity extends BaseActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TITLE_TAG = "settingsActivityTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);

        Toolbar toolbar = findViewById(R.id.helpAndFeedbackToolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.help_and_feedback, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            setTitle(R.string.title_activity_settings_and_help);
                        }
                    }
                });

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.help_and_feedback, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }

    public static class HeaderFragment extends PreferenceFragmentCompat {

        private Activity activityCallback;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            try {
                activityCallback = (Activity) context;
            } catch (ClassCastException e) {
                throw new ClassCastException((context.toString() + "must be activity"));
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_help_preferences, rootKey);

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

            Preference feedback_link = findPreference("feedback_link");
            feedback_link.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(SettingsAndHelpActivity.getEmailIntent());
                    return false;
                }
            });
        }
    }

    public static class AboutFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.about_menu, rootKey);

            Preference feedback_link = findPreference("feedback_link");
            feedback_link.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(SettingsAndHelpActivity.getEmailIntent());
                    return false;
                }
            });

            Preference rate_link = findPreference("rate_link");
            rate_link.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(SettingsAndHelpActivity.getAppStoreRatingIntent());
                    return false;
                }
            });
        }
    }

    public static class HelpFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.help_menu_sections, rootKey);
        }
    }

    private static Intent getEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"al.software.engineering@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RE: BUKUSU-ENGLISH DICTIONARY - USER FEEDBACK");
        return emailIntent;
    }

    public static Intent getAppStoreRatingIntent() {
        Uri uri = Uri.parse("market://details?id=$packageName");
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
        return rateIntent;
    }

    public static Intent getAppShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Bukusu-English Dictionary");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=$packageName");
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}