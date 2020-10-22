package com.example.roomjava2;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDic(Dic dic);

    @Query("select * from words where origin = :word")
    public Dic GetWord(String word);

    @Query("select * from words where origin = :word")
    public List<Dic> GetAllWord(String word);

    @Query("select * from words where origin like  '%' || :word || '%'")
    public List<Dic> GetAllWord2(String word);

    @Query("Delete from words where origin = :word")
    public void DeleteWord(String word);


}


