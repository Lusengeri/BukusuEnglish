package com.example.newdictionary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(entities = {DictEntry.class}, version=1)
public abstract class DictEntryDatabase extends RoomDatabase {
    public abstract DictEntryDao dictEntryDao();
    private static DictEntryDatabase DATABASE_INSTANCE;
    private static boolean populated = false;

    public static DictEntryDatabase getDatabaseInstance(Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (DictEntryDatabase.class) {
                if (DATABASE_INSTANCE == null) {
                    //DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            //DictEntryDatabase.class, "definitions_database").build();
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DictEntryDatabase.class, "definitions.db")
                            .createFromAsset("demo.db")
                            .build();

                    /*
                        try {
                            InputStream inputStream = context.getAssets().open("all_entries.sql");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder statement = new StringBuilder();
                            String line;
                            SupportSQLiteDatabase dbase = DATABASE_INSTANCE.getOpenHelper().getWritableDatabase();
                            //dbase.query("DROP * FROM definitions;");
                            while ((line = reader.readLine()) != null) {
                                dbase.execSQL(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            populated = true;
                        }

                     */
                }
            }
        }
        return DATABASE_INSTANCE;
    }
}
