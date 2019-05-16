package cn.com.wosuo.taskrecorder.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;

@Dao
public interface LocCenterPointDao {
    @Query("SELECT * FROM LocCenterPoint WHERE taskID = :taskID")
    LiveData<LocCenterPoint> getLocCenterPointByTaskID(int taskID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocCenterPoint locCenterPoint);
}
