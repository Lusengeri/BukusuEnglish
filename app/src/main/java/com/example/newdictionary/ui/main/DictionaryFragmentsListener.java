package com.example.newdictionary.ui.main;

import android.database.Cursor;

public interface DictionaryFragmentsListener {
    void onListFragmentInteraction(String word);
    Cursor getWordList();
}
