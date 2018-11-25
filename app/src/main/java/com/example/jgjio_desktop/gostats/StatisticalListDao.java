package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface StatisticalListDao {

    @Query("SELECT * FROM list ORDER BY id")
    LiveData<List<StatisticalList>> loadAllLists();

    @Insert
    long insert(StatisticalList statisticalList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(StatisticalList statisticalList);

    @Delete
    void delete(StatisticalList statisticalList);

    @Query("DELETE FROM list WHERE id = :id")
    void deleteListById(int id);

    @Query("SELECT count(*) FROM list")
    LiveData<Long> getListCount();

    @Query("SELECT name FROM list WHERE id = :id LIMIT 1;")
    LiveData<String> getListName(int id);

    @Query("UPDATE list SET name =:newName WHERE id = :id")
    void updateName(String newName, int id);

    @Query("SELECT is_frequency_table FROM list WHERE id = :id LIMIT 1")
    LiveData<Boolean> isFrequencyTable(int id);

}
