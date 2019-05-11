package cn.com.wosuo.taskrecorder.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.db.TaskDao;
import cn.com.wosuo.taskrecorder.db.UserDao;
import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.User;

/**
 * Read data from json files and import to database
 */

@Deprecated
public class DatabaseUtils extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "DatabaseUtils";
    public static final boolean isDebugMode = true;
    private UserDao userDao;
    private TaskDao taskDao;

    public DatabaseUtils(AppDatabase db) {
        this.userDao = db.userDao();
        this.taskDao = db.taskDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (isDebugMode) Log.d(TAG, "DatabaseUtils.doInBackground: running...");
        importUsers();
        importTasks();
        if (isDebugMode) Log.d(TAG, "DatabaseUtils.doInBackground: finished.");
        return null;
    }

    private void importUsers() {
        Log.d(TAG, "importUsers: START");
        InputStream inputStream = BasicApp.getAppContext().getResources().openRawResource(R.raw.app_data_users);
        Writer writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        char[] buffer = new char[1024];
        try {
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.d(TAG, "importUsers: Error occurred " );
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d(TAG, "inputStream.close() wrong");
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        Pair<Integer, List<User>> pair = JsonParser.parseAllUserJson(jsonString);
        List<User> users = pair.b;
        userDao.insertAll(users);
        if (isDebugMode) Log.d(TAG, "importUsers: " + users.size() + " rows imported.");
        Log.d(TAG, "importUsers: END");
    }

    private void importTasks() {
        Log.d(TAG, "importTasks: START");
        InputStream inputStream = BasicApp.getAppContext().getResources().openRawResource(R.raw.app_data_tasks);
        Writer writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        char[] buffer = new char[1024];
        try {
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.d(TAG, "importUsers: Error occurred " );
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d(TAG, "inputStream.close() wrong");
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        Pair<Integer, List<Task>> pair = JsonParser.parseAllTaskJson(jsonString);
        List<Task> tasks = pair.b;
        taskDao.insertAll(tasks);
        if (isDebugMode) Log.d(TAG, "importUsers: " + tasks.size() + " rows imported.");
        Log.d(TAG, "importUsers: END");
    }


}