package com.example.newdictionary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.preference.PreferenceManager;

import java.security.KeyStore;
import java.util.jar.Attributes;

public class SuggestionCursorAdapter extends CursorAdapter {

    public SuggestionCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.suggestion_list_item,
                parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.suggestView);
        int currentTheme = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt("Theme", R.style.CustomAppTheme);

        if (currentTheme == R.style.CustomAppTheme) {
            textView.setTextColor(context.getResources().getColor(R.color.primaryLight));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.secondaryDark));
        }

        textView.setText(cursor.getString(1));
    }
}
