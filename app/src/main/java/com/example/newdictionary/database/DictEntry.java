package com.example.newdictionary.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "definitions")
public class DictEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "pos")
    private String pos;

    @ColumnInfo(name="definition")
    private String definition;

    @ColumnInfo(name="unaccented")
    private String unaccented;

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setUnaccented(String unaccented) {
        this.unaccented = unaccented;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getPos() {
        return pos;
    }

    public String getDefinition() {
        return definition;
    }

    public String getUnaccented() {
        return unaccented;
    }

    public void blankAll() {
        setWord("");
        setPos("");
        setUnaccented("");
        setDefinition("");
    }
}