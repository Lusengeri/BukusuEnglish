package com.alsoftware.bukusuenglish.ui.main;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alsoftware.bukusuenglish.database.DictEntry;
import com.alsoftware.bukusuenglish.database.DictEntryRepository;

public class MainViewModel extends AndroidViewModel {
    private DictEntryRepository repository;
    private MutableLiveData<DictEntry> currentWord;
    private MutableLiveData<Cursor> suggestionsList;
    private Cursor wordList;

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
            repository.searchForSuggestions(query);
    }
}