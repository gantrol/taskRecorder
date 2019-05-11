package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Ignore;

import java.util.List;

public class Track {
    private int coordinate;
    private List<TrackData> data;

    @Ignore
    public Track(int coordinate, List<TrackData> data){
        this.coordinate = coordinate;
        this.data = data;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public List<TrackData> getData() {
        return data;
    }

    public void setData(List<TrackData> data) {
        this.data = data;
    }
}
