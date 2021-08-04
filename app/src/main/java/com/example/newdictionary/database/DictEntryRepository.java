package com.example.newdictionary.database;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DictEntryRepository {
    private Cursor wordList;
    private MutableLiveData<DictEntry> currentWord = new MutableLiveData<>();
    private MutableLiveData<Cursor> suggestionsList = new MutableLiveData<>();
    private DictEntryDao dictEntryDao;

    public DictEntryRepository(Application application) {
        DictEntryDatabase db;
        db = DictEntryDatabase.getDatabaseInstance(application);
        dictEntryDao = db.dictEntryDao();
        wordList = db.getOpenHelper().getWritableDatabase().query("SELECT unaccented FROM definitions");
    }

    public Cursor getWordList() {
        return wordList;
    }

    protected class GetCurrentWordAsyncTask extends AsyncTask<String, Void, List<DictEntry>> {
        private DictEntryDao dao;
        private DictEntryRepository delegate;

        public GetCurrentWordAsyncTask(DictEntryDao dictEntryDao, DictEntryRepository caller) {
            dao = dictEntryDao;
            delegate = caller;
        }

        @Override
        protected List<DictEntry> doInBackground(String... strings) {
            return dao.getDefinition(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DictEntry> result) {
            delegate.getCurrentWordFinished(result);
        }
    }

    private void getCurrentWordFinished(List<DictEntry> results) {
        if (!results.isEmpty()) {
            currentWord.setValue(results.get(0));
        } else {
            DictEntry blankWord = new DictEntry();
            blankWord.blankAll();
            currentWord.setValue(blankWord);
        }
    }

    public MutableLiveData<DictEntry> getCurrentWord() {
        return currentWord;
    }

    protected class GetSuggestionsAsyncTask extends AsyncTask<String, Void, Cursor> {
        private DictEntryDao dao;
        private DictEntryRepository delegate;

        public GetSuggestionsAsyncTask(DictEntryDao dictEntryDao, DictEntryRepository caller) {
            dao = dictEntryDao;
            delegate = caller;
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            return dao.getSearchSuggestions(strings[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            delegate.getSuggestionsFinished(result);
        }
    }

    private void getSuggestionsFinished(Cursor result) {
        suggestionsList.setValue(result);
    }

    public LiveData<Cursor> getSuggestionsList() {
        return suggestionsList;
    }

    public void searchForWord(String query) {
        GetCurrentWordAsyncTask task = new GetCurrentWordAsyncTask(dictEntryDao, this);
        task.execute(query);
    }

    public void searchForSuggestions(String searchTerm) {
        GetSuggestionsAsyncTask task = new GetSuggestionsAsyncTask(dictEntryDao, this);
        task.execute(searchTerm);
    }
}