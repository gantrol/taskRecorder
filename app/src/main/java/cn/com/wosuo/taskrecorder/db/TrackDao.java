package cn.com.wosuo.taskrecorder.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cn.com.wosuo.taskrecorder.vo.Tracks;

@Dao
public interface TrackDao {
    @Query("SELECT * FROM track WHERE taskID = :taskID")
    LiveData<List<Tracks>> getTracksByTaskID(int taskID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Tracks> tracks);
}
