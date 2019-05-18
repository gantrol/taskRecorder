package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@Entity(
        tableName = "tasks"
)
public class Task implements Serializable {
    @PrimaryKey
    private int taskID;
    private String title;
    private String description;
    private int createAt;
    private int updateAt;
    private int finishAt;
    //    private boolean mSolved;
    private int type; // 0 = Explore 1 = Plot 2 = Sampling
    @Ignore
    private TaskResult mTaskResult;
    @Ignore
    private int resource;
    @Ignore
    private User assignee;
    @Expose(serialize = false, deserialize = false)
    private int assignee_id;
    @Ignore
    private User assigner;
    @Expose(serialize = false, deserialize = false)
    private int assigner_id;
    @Expose(serialize = false, deserialize = false)
    private int executor;
    private int status;  // 任务状态 0 = Created 1 = InProgress 2 = ReadyForTest 3 = Done

    public Task(){
    }

    @Ignore
    public Task(int taskID, String title, String description, User assigner,
                User assignee, int type, TaskResult taskResult, int resource, int createAt,
                int updateAt, int finishAt, int status){
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.assignee_id = (assignee == null) ? -1 : assigner.getUid();
        this.assigner = assigner;
        this.assignee_id = (assignee == null) ? -1 : assigner.getUid();
        this.type = type;
        this.mTaskResult = taskResult;
        this.resource = resource;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.finishAt = finishAt;
        this.status = status;
    }

    @Ignore
    public Task(String title, String description, int assigner,
                int assignee, int type){
        this.title = title;
        this.description = description;
        this.assignee_id = assignee;
        this.assigner_id = assigner;
        this.type = type;
    }

    public Task(Task task){
        taskID = task.getTaskID();
        title = task.getTitle();
        description = task.getDescription();
        assignee = task.getAssignee();
        assigner = task.getAssigner();
        assignee_id = task.getAssignee_id();
        assigner_id = task.getAssigner_id();
        executor = task.getExecutor();
        type = task.getType();
        mTaskResult = task.getTaskResult();
        resource = task.getResource();
        createAt = task.getCreateAt();
        updateAt = task.getUpdateAt();
        finishAt = task.getFinishAt();
        status = task.getStatus();
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreateAt() {
        return createAt;
    }


    public void setCreateAt(int createAt) {
        this.createAt = createAt;
    }

    public int getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(int updateAt) {
        this.updateAt = updateAt;
    }

    public int getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(int finishAt) {
        this.finishAt = finishAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public int getExecutor() {
        return executor;
    }

    public void setExecutor(int executor) {
        this.executor = executor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public TaskResult getTaskResult() {
        return mTaskResult;
    }

    public void setTaskResult(TaskResult taskResult) {
        this.mTaskResult = taskResult;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public int getAssigner_id() {
        return assigner_id;
    }

    public void setAssigner_id(int assigner_id) {
        this.assigner_id = assigner_id;
    }
}
