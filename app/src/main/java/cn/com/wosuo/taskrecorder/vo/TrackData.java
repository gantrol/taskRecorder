package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Ignore;

public class TrackData {
    private double pointX;
    private double pointY;
    private int user;
    private long timestamp;

    @Ignore
    public TrackData(double pointX, double pointY, int user, long timestamp){
        this.pointX = pointX;
        this.pointY = pointY;
        this.user = user;
        this.timestamp = timestamp;
    }

    public double getPointX() {
        return pointX;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
