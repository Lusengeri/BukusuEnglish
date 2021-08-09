package com.alsoftware.bukusuenglish;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class BaseActivity extends AppCompatActivity {
    int currentTheme = R.style.CustomAppTheme;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        currentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt("Theme", R.style.CustomAppTheme);
        setTheme(currentTheme);
        super.onCreate(savedInstanceState);
    }
}
