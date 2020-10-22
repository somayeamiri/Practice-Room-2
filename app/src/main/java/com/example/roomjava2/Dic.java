package com.example.roomjava2;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class Dic {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "origin")
    String origin;

    @ColumnInfo(name = "translate")
    String translate;



}
