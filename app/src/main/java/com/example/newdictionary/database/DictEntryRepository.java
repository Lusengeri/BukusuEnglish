package com.example.newdictionary.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DictEntryRepository {
    private final LiveData<List<DictEntry>> allEntries;
    private MutableLiveData<DictEntry> currentWord = new MutableLiveData<>();
    private MutableLiveData<List<DictEntry>> suggestions = new MutableLiveData<>();
    private DictEntryDao dictEntryDao;

    public DictEntryRepository(Application application) {
        DictEntryDatabase db;
        db = DictEntryDatabase.getDatabaseInstance(application);
        dictEntryDao = db.dictEntryDao();
        allEntries = dictEntryDao.getAllEntries();
    }

    private void getDefinitionFinished(List<DictEntry> results) {
        currentWord.setValue(results.get(0));
    }

    protected class GetDefinitionAsyncTask extends AsyncTask<String, Void, List<DictEntry>> {

        private DictEntryDao dao;
        private DictEntryRepository delegate;

        public GetDefinitionAsyncTask(DictEntryDao dictEntryDao, DictEntryRepository caller) {
            dao = dictEntryDao;
            delegate = caller;
        }

        @Override
        protected List<DictEntry> doInBackground(String... strings) {
            return dao.getDefinition(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DictEntry> result) {
            delegate.getDefinitionFinished(result);
        }
    }

    private void getSuggestionsFinished(List<DictEntry> result) {
        suggestions.setValue(result);
    }

    protected class GetSuggestionsAsyncTask extends AsyncTask<String, Void, List<DictEntry>> {

        private DictEntryDao dao;
        private DictEntryRepository delegate;

        public GetSuggestionsAsyncTask(DictEntryDao dictEntryDao, DictEntryRepository caller) {
            dao = dictEntryDao;
            delegate = caller;
        }

        @Override
        protected List<DictEntry> doInBackground(String... strings) {
            return dao.getSearchSuggestions(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DictEntry> result) {
            delegate.getSuggestionsFinished(result);
        }
    }

    public LiveData<List<DictEntry>> getAllEntries() {
        return allEntries;
    }

    public LiveData<DictEntry> getCurrentWord() {
        return currentWord;
    }

    public void getDefinition(String query) {
        //Should not be on main thread
        GetDefinitionAsyncTask task = new GetDefinitionAsyncTask(dictEntryDao, this);
        task.execute(query);
    }

    public void getSearchSuggestions(String searchTerm) {
        //Should not be on main thread
        GetSuggestionsAsyncTask task = new GetSuggestionsAsyncTask(dictEntryDao, this);
        task.execute(searchTerm);
    }
}