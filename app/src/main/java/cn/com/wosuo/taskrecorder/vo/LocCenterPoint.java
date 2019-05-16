package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class LocCenterPoint {
    @PrimaryKey
    private int taskID;
    private double positionX;
    private double positionY;
    private int coordinate;
    @Ignore
    @SerializedName("tracks")
    private List<Tracks> mTracks;
    @Ignore
    @SerializedName("photos")
    private List<PhotoResult> mPhotoResults;

    public LocCenterPoint() {
    }

    @Ignore
    public LocCenterPoint(int taskID, double positionX, double positionY, int coordinate, List<Tracks> tracks, List<PhotoResult> photoResults) {
        this.taskID = taskID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.coordinate = coordinate;
        mTracks = tracks;
        mPhotoResults = photoResults;
    }


    @Ignore
    public LocCenterPoint(int taskID, double positionX, double positionY, int coordinate) {
        this.taskID = taskID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.coordinate = coordinate;
    }

    public LocCenterPoint(LocCenterPoint locCenterPoint){
        this.taskID = locCenterPoint.getTaskID();
        this.positionX = locCenterPoint.getPositionX();
        this.positionY = locCenterPoint.getPositionY();
        this.coordinate = locCenterPoint.getCoordinate();
        mTracks = locCenterPoint.getTracks();
        mPhotoResults = locCenterPoint.getPhotoResults();
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public List<Tracks> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Tracks> tracks) {
        mTracks = tracks;
    }

    public List<PhotoResult> getPhotoResults() {
        return mPhotoResults;
    }

    public void setPhotoResults(List<PhotoResult> photoResults) {
        mPhotoResults = photoResults;
    }
}
