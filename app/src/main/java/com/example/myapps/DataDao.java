package com.example.myapps;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataDao {

    @Insert
    void insert(DataEntity data);

    @Update
    void update(DataEntity data);

    @Delete
    void delete(DataEntity data);


    @Query("SELECT * FROM data_table ORDER BY id DESC")
    LiveData<List<DataEntity>> getAllData();

    //list activity
   @Query("SELECT * FROM data_table WHERE date LIKE '%' || :month || '/' || :year")
    LiveData<List<DataEntity>> getDataByMonth(String month, String year);



 /*   @Query("SELECT * FROM data_table")
    List<DataEntity> getAllData();*/


}
