package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.List;

//@Entity(
//        tableName = "tracks"
//)
public class Tracks {

    private int trackID;
    private int taskID;
    private int userID;
    @Ignore
    private List<Track> tracks;
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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

}
