package com.example.newdictionary.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictEntryDao {
    @Query("SELECT * FROM definitions")
    LiveData<List<DictEntry>> getAllEntries();

    @Query("SELECT * FROM definitions WHERE unaccented = :query")
    List<DictEntry> getDefinition(String query);

    @Query("SELECT * FROM definitions WHERE unaccented LIKE :searchTerm")
    List<DictEntry> getSearchSuggestions(String searchTerm);
}
