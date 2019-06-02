package cn.com.wosuo.taskrecorder.vo;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import cn.com.wosuo.taskrecorder.db.UserConverter;
import cn.com.wosuo.taskrecorder.db.UserLocConverter;

@Entity
@TypeConverters({UserConverter.class, UserLocConverter.class})
public class PhotoResult {
    @PrimaryKey
    private int photoID;
    private User author;
    private String path;
    private String description;
    private UserLocation location;
    private int photoTime;  // 需要*1000再转时间戳
    private int taskID;
    private int subID;  // 本组序号

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public int getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(int photoTime) {
        this.photoTime = photoTime;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }
}
