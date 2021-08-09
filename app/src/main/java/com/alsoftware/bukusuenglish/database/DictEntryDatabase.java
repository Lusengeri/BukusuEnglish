package com.alsoftware.bukusuenglish.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DictEntry.class}, version=1)
public abstract class DictEntryDatabase extends RoomDatabase {
    public abstract DictEntryDao dictEntryDao();
    private static DictEntryDatabase DATABASE_INSTANCE;
    private static boolean populated = false;

    public static DictEntryDatabase getDatabaseInstance(Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (DictEntryDatabase.class) {
                if (DATABASE_INSTANCE == null) {
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DictEntryDatabase.class, "definitions.db")
                            .createFromAsset("definitions.db")
                            .build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }
}
