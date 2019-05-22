package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_ID;


@Entity(
        tableName = "results",
        foreignKeys = @ForeignKey(
                entity = Task.class,
                parentColumns = TASK_ID,
                childColumns = TASK_ID
        )
)
public class TaskResult {
    @PrimaryKey
    private int taskID;
    private int positionX;
    private int positionY;
    private int coordinate;
    @Ignore
    private List<Tracks> trasksList;
    @Ignore
    private List<PhotoResult> mPhotoResults;

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public List<Tracks> getTrasksList() {
        return trasksList;
    }

    public void setTrasksList(List<Tracks> trasksList) {
        this.trasksList = trasksList;
    }

    public List<PhotoResult> getPhotoResults() {
        return mPhotoResults;
    }

    public void setPhotoResults(List<PhotoResult> photoResults) {
        this.mPhotoResults = photoResults;
    }
}
