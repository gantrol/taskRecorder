package cn.com.wosuo.taskrecorder.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.com.wosuo.taskrecorder.db.TrackConverter;


@Entity(
        tableName = "track"
)
public class Tracks {
    @PrimaryKey
    private int trackID;
    private int taskID;
    private int userID;
    @TypeConverters({TrackConverter.class})
    @SerializedName(alternate = {"tracks"}, value = "track")
    private Track track;

    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}
