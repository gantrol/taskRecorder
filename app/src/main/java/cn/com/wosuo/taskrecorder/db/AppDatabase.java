package cn.com.wosuo.taskrecorder.db;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.util.DatabaseUtils;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.TaskFtsEntity;
import cn.com.wosuo.taskrecorder.vo.User;

@Database(entities = {User.class, Task.class, TaskFtsEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase sInstance;
//    private static volatile WordRoomDatabase INSTANCE;

    @VisibleForTesting
    public static final String DATABASE_NAME = "land-manager-db";

    public abstract UserDao userDao();

    public abstract TaskDao taskDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    @Deprecated
    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @Deprecated
    public static AppDatabase getInstance(final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(BasicApp.getAppContext(), executors);
                    sInstance.updateDatabaseCreated(BasicApp.getAppContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
//                .addCallback(sRoomDatabaseCallback)
                .build();
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new DatabaseUtils(sInstance).execute();
            }
        };

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<User> users,
                                   final List<Task> tasks) {
        database.runInTransaction(() -> {
            database.userDao().insertAll(users);
            database.taskDao().insertAll(tasks);
        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `usersFts` USING FTS4("
//                    + "`name` TEXT, `description` TEXT, content=`users`)");
//            database.execSQL("INSERT INTO usersFts (`rowid`, `name`, `description`) "
//                    + "SELECT `id`, `name`, `description` FROM users");
//
//        }
//    };
}
