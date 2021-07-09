package com.example.newdictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int SCHEMA_VERSION;
    private static final String DB_NAME;
    private static final String TAG;
    private final Context context;

    static {
        TAG = DatabaseOpenHelper.class.getSimpleName();
        DB_NAME = "definitions.db";
        SCHEMA_VERSION = 1;
    }

    public DatabaseOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, SCHEMA_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open("all_entries.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder statement = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                db.execSQL(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS definitions");
        onCreate(db);
    }
}
