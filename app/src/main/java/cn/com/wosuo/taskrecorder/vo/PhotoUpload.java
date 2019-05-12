package cn.com.wosuo.taskrecorder.vo;

import java.io.File;

public class PhotoUpload {
    int taskID;
    int subID; // 0-3分别代表四种照片：访谈人、座谈会议、敏感对象、地块现场
    long time;
    File file;
    String description;



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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
