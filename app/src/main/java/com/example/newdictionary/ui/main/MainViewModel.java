package com.example.newdictionary.ui.main;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newdictionary.database.DictEntry;
import com.example.newdictionary.database.DictEntryRepository;

public class MainViewModel extends AndroidViewModel {
    private DictEntryRepository repository;
    public MutableLiveData<DictEntry> currentWord;
    public LiveData<Cursor> suggestionsList;
    public Cursor wordList;

    public MainViewModel(Application application) {
        super(application);
        repository = new DictEntryRepository(application);
        wordList = repository.getWordList();
        currentWord = repository.getCurrentWord();
        suggestionsList = repository.getSuggestionsList();
    }

    public Cursor getWordList() {
        return wordList;
    }

    public LiveData<DictEntry> getCurrentWord() {
        return currentWord;
    }

    public LiveData<Cursor> getSuggestionsList() {
        return suggestionsList;
    }

    public void searchForWord(String query) {
        repository.searchForWord(query);
    }

    public void searchForSuggestions(String query) {
        repository.searchForSuggestions(query + "%");
    }
}