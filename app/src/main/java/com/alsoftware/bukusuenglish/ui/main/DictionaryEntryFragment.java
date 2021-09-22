package com.alsoftware.bukusuenglish.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsoftware.bukusuenglish.MainActivity;
import com.alsoftware.bukusuenglish.R;
import com.alsoftware.bukusuenglish.database.DictEntry;

public class DictionaryEntryFragment extends Fragment {
    private CardView definitionCard;
    private TextView posView;
    private TextView colonView;
    private TextView wordView;
    private TextView pronunciationView;
    private TextView definitionNum;
    private TextView definitionView;
    private TextView messageView;
    private View dividingLine;

    private MainActivity parentActivity;
    private MainViewModel mainViewModel;

    public DictionaryEntryFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_entry, container, false);
        posView = view.findViewById(R.id.posView);
        colonView = view.findViewById(R.id.colonView);
        wordView = view.findViewById(R.id.wordView);
        pronunciationView = view.findViewById(R.id.pronunciationView);
        definitionNum = view.findViewById(R.id.definitionNum);
        definitionView = view.findViewById(R.id.definitionView);
        dividingLine = view.findViewById(R.id.dividingLine);
        messageView = view.findViewById(R.id.messageView);
        definitionCard = view.findViewById(R.id.cardView);
        setDefaultDefinitionText();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = parentActivity.getMainViewModel();

        Observer<DictEntry> currentWordObserver = new Observer<DictEntry>() {
            @Override
            public void onChanged(DictEntry dictEntry) {
                if (dictEntry.getUnaccented().equals("") || dictEntry.getDefinition().equals("")
                    || dictEntry.getPos().equals("") || dictEntry.getWord().equals("")) {
                    showUnsuccessful(getResources().getString(R.string.word_not_found_placeholder));
                } else {
                    showSelectedDefinition(dictEntry.getWord(), dictEntry.getPos(), dictEntry.getDefinition(),
                            dictEntry.getUnaccented());
                }
            }
        };
        mainViewModel.getCurrentWord().observe(getViewLifecycleOwner(), currentWordObserver);
    }

    public void showSelectedDefinition(String word, String pos, String definition, String unaccented) {
        definitionCard.setVisibility(View.VISIBLE);
        messageView.setVisibility(View.INVISIBLE);

        if (word.equals(null)) {
            setDefaultDefinitionText();
        } else {
            posView.setText(pos);
            wordView.setText(unaccented);
            pronunciationView.setText("|" + word + "|");
            definitionNum.setText("1.");
            definitionView.setText(definition);

            SharedPreferences.Editor editor = parentActivity.getPreferences(MainActivity.MODE_PRIVATE).edit();
            editor.putString("pos", posView.getText().toString());
            editor.putString("word", wordView.getText().toString());
            editor.putString("definition", definitionView.getText().toString());
            editor.putString("pron", pronunciationView.getText().toString());
            editor.commit();
        }
    }

    private void setDefaultDefinitionText() {
        SharedPreferences preferences = parentActivity.getPreferences(MainActivity.MODE_PRIVATE);
        String prev_pos = preferences.getString("pos", "");
        String prev_word = preferences.getString("word", "");
        String prev_pron = preferences.getString("pron", "");
        String prev_definition = preferences.getString("definition", "");

        if (prev_word == "") {
            showUnsuccessful(getResources().getString(R.string.default_view_text));
        } else {
            posView.setText(prev_pos);
            wordView.setText(prev_word);
            pronunciationView.setText(prev_pron);
            definitionNum.setText("1.");
            definitionView.setText(prev_definition);
        }
    }

    public void showUnsuccessful(String message) {
        definitionCard.setVisibility(View.INVISIBLE);
        messageView.setVisibility(View.VISIBLE);
        messageView.setText(message);
    }
}