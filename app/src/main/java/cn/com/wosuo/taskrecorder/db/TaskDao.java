package cn.com.wosuo.taskrecorder.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.Task;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface TaskDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    /**
     * Select Company's tasks by assigneeId.
     *
     * @param assigneeId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks where assignee_id = :assigneeId")
    LiveData<List<Task>> getCompanyTasks(int assigneeId);

    /**
     * Select Manager's tasks by assignerId.
     *
     * @param assignerId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks where assigner_id = :assignerId")
    LiveData<List<Task>> getManagerTasks(int assignerId);

    /**
     * Select User's tasks by assignerId.
     *
     * @param userId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks where executor = :userId")
    LiveData<List<Task>> getUserTasks(int userId);


    /**
     * Select a task by id.
     *
     * @param Id the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks WHERE taskID = :Id")
    LiveData<Task> getTask(int Id);


    @Query("SELECT tasks.* From tasks JOIN tasksFts ON (tasks.taskID = tasksFts.rowid) " +
            "WHERE tasksFts MATCH :query")
    LiveData<List<Task>> searchAllTasks(String query);


    /**
     * Insert tasks in the database. If the task already exists, replace it.
     *
     * @param tasks the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Task> tasks);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    void update(Task task);

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Delete
    void delete(Task task);
//    @Query("DELETE FROM tasks WHERE mId = :taskId")
//    int deleteTaskById(String taskId);


    /**
     * Update the complete status of a task
     *
     * @param taskId    id of the task
     * @param mStatus status to be updated
     */
    @Query("UPDATE tasks SET status = :mStatus WHERE taskID = :taskId")
    void updateStatus(int taskId, int mStatus);

    @Query("UPDATE tasks SET title = :title, assignee_id = :assignee_id, type = :type, " +
            "description = :detail WHERE taskID = :taskID")
    void updateTaskInfo(String title, int assignee_id, int type, String detail, int taskID);

//    @Query("UPDATE tasks SET executor = NULL WHERE taskID = :taskID")
//    void deleteExecutor(int taskID);

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM tasks")
    void deleteAllTasks();

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("DELETE FROM tasks WHERE status = :TaskFinishStatusNumber")
    int deleteCompletedTasks(int TaskFinishStatusNumber);
}