package com.example.roomjava;



import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Delete
    void reset(List<MainData> mainData);

    @Query("UPDATE table_name SET text = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    @Query("DELETE FROM table_name WHERE ID = :sID")
    void deleteTest(int sID);

    @Query("SELECT * FROM table_name") //@Query("SELECT * FROM table_name ORDER BY text ASC")
    List<MainData> getAll();
}
