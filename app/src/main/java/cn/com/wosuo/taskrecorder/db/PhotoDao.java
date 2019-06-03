package cn.com.wosuo.taskrecorder.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cn.com.wosuo.taskrecorder.vo.PhotoResult;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM PhotoResult WHERE taskID = :taskID")
    LiveData<List<PhotoResult>> getPhotoResultByTaskID(int taskID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PhotoResult> photoResults);

    @Query("DELETE FROM PhotoResult WHERE taskID = :taskID")
    void deleteByTaskID(int taskID);
}