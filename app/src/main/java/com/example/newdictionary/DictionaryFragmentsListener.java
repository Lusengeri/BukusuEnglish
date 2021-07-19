package com.example.newdictionary;

import android.database.Cursor;

public interface DictionaryFragmentsListener {
    void onListFragmentInteraction(String word);
    Cursor getWordList();
}
