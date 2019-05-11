package cn.com.wosuo.taskrecorder.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import cn.com.wosuo.taskrecorder.vo.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();


    @Query("SELECT * FROM users")
    LiveData<List<User>> loadAllUsers();

    @Query("SELECT * FROM users where uid = :userId")
    LiveData<User> loadUser (int userId);

    @Query("SELECT * FROM users where type = :type")
    LiveData<List<User>> loadUserByType (int type);

    @Query("SELECT * FROM users where company_id = :id")
    LiveData<List<User>> loadUserByGroupID (int id);

    @Query("SELECT * FROM users where uid = :userId")
    User loadUserSync(int userId);

    @Query("DELETE FROM users")
    void deleteAllUsers();

//    @Query("SELECT users.* FROM users JOIN usersFts ON (users.id = usersFts.rowid) "
//            + "WHERE usersFts MATCH :query")
//    List<User> searchAllUserEntitys(String query);
}
