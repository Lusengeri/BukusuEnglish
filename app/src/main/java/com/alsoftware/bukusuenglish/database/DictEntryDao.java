package com.alsoftware.bukusuenglish.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictEntryDao {
    @Query("SELECT _id, unaccented FROM definitions")
    Cursor getAllEntries();

    @Query("SELECT * FROM definitions WHERE unaccented = :query")
    List<DictEntry> getDefinition(String query);

    @Query("SELECT _id, unaccented FROM definitions WHERE unaccented LIKE :searchTerm")
    Cursor getSearchSuggestions(String searchTerm);
}
