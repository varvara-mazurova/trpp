package com.example.insense.repository.room;

import java.util.List;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Dao;

@Dao
public interface ActivityDAO {
    @Query("SELECT * FROM Activity")
    List<Activity> getAll();

    @Query("SELECT * FROM Activity WHERE uid IN (:activity_lolIds)")
    List<Activity> loadAllByIds(int[] activity_lolIds);

    @Query("SELECT * FROM Activity WHERE uid IN (:id)")
    Activity loadById(int id);

    @Query("SELECT * FROM Activity WHERE occupation IN (:occupation)")
    List<Activity> loadByOccupation(String occupation);

    @Query("SELECT * FROM Activity WHERE name IN (:name)")
    Activity loadByName(String name);

    @Query("SELECT * FROM Activity WHERE al IN (:al)")
    Activity loadByAl(int al);

    @Query("SELECT * FROM Activity WHERE name IN (:name)")
    Activity loadByDay(String name);

    @Insert
    void insertAll(List<Activity> activitys);

    @Update
    void updateActivity(Activity activitys);

    @Delete
    void delete(Activity activitys);


    /*@Transaction
    @Query("SELECT * FROM Activity")
    public List<ActivityCategory> all_activity_category();*/
}
