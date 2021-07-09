package com.example.newdictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {

    private TextView wordView;
    private TextView pronunciationView;
    private TextView definitionView;
    private View dividingLine;
    private MainActivity parent;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            parent = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must be ....");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        wordView = view.findViewById(R.id.wordView);
        pronunciationView = view.findViewById(R.id.pronunciationView);
        definitionView = view.findViewById(R.id.definitionView);
        dividingLine = view.findViewById(R.id.dividingLine);
        setDefaultDefinitionText();
        return view;
    }

    public void showSelectedDefinition(String word, String pos, String definition, String spelled) {
        wordView.setVisibility(View.VISIBLE);
        pronunciationView.setVisibility(View.VISIBLE);
        definitionView.setVisibility(View.VISIBLE);
        dividingLine.setVisibility(View.VISIBLE);

        if (word.equals(null)) {
            setDefaultDefinitionText();
        } else {
            wordView.setText(pos + ": " + spelled);
            pronunciationView.setText("|" + word + "|");
            definitionView.setText("1. " + definition);

            SharedPreferences.Editor editor = parent.getPreferences(MainActivity.MODE_PRIVATE).edit();
            editor.putString("word", wordView.getText().toString());
            editor.putString("definition", definitionView.getText().toString());
            editor.putString("pos", pronunciationView.getText().toString());
            editor.commit();
        }
    }

    private void setDefaultDefinitionText() {
        SharedPreferences preferences = parent.getPreferences(MainActivity.MODE_PRIVATE);
        String prev_word = preferences.getString("word", null);
        String prev_pos = preferences.getString("pos", null);
        String prev_definition = preferences.getString("definition", null);

        if (prev_word == null) {
            wordView.setText(getResources().getString(R.string.default_view_text));
        } else {
            wordView.setText(prev_word);
            pronunciationView.setText(prev_pos);
            definitionView.setText(prev_definition);
        }
    }

    public void showUnsuccessful(){
        wordView.setText(getResources().getString(R.string.word_not_found_placeholder));
        pronunciationView.setVisibility(View.INVISIBLE);
        definitionView.setVisibility(View.INVISIBLE);
        dividingLine.setVisibility(View.INVISIBLE);
    }
}