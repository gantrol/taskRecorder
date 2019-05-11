package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "tasksFts")
@Fts4(contentEntity = Task.class)
public class TaskFtsEntity {
    private String title;
    private String description;

    public TaskFtsEntity(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
