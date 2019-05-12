package cn.com.wosuo.taskrecorder.repository;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.wosuo.taskrecorder.AppExecutors;
import cn.com.wosuo.taskrecorder.BasicApp;
import cn.com.wosuo.taskrecorder.api.ApiResponse;
import cn.com.wosuo.taskrecorder.api.BigkeerSerivice;
import cn.com.wosuo.taskrecorder.db.AppDatabase;
import cn.com.wosuo.taskrecorder.db.UserDao;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.RateLimiter;
import cn.com.wosuo.taskrecorder.util.Resource;
import cn.com.wosuo.taskrecorder.vo.ArrayResult;
import cn.com.wosuo.taskrecorder.vo.BigkeerResponse;
import cn.com.wosuo.taskrecorder.vo.GroupInfoResult;
import cn.com.wosuo.taskrecorder.vo.User;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_LOGIN;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_ME;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_NOTHING;

/**
 * Repository handling the work with users.
 */
public class UserRepository {
//  TODO: 3翻页。。
    private static UserRepository sInstance;
    private BigkeerSerivice mBigkeerSerivice;
    private AppDatabase mDatabase;
    private UserDao mUserDao;
    private AppExecutors mAppExecutors;
    private RateLimiter<String> userListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    private RateLimiter<String> userMeRateLimit = new RateLimiter<>(5, TimeUnit.MINUTES);
    private final static List<String> sUserLocalStatus = FinalMap.getUserLocalStatusList();
    private final static int loginStatus = sUserLocalStatus.indexOf(USER_LOGIN);
    private final static int noStatus = sUserLocalStatus.indexOf(USER_NOTHING);
    private final static List<String> sUserType = FinalMap.getUserTypeList();

    private UserRepository(final AppDatabase database) {
        mBigkeerSerivice = BasicApp.getBigkeerService();
        mAppExecutors = new AppExecutors();
        mDatabase = database;
        mUserDao = database.userDao();
    }

    public static UserRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of users from the database and get notified when the data changes.
     */
    public LiveData<Resource<List<User>>> getAllUsers() {
        return (new NetworkBoundResource<List<User>, BigkeerResponse<ArrayResult<User>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<User>> item) {
                mUserDao.insertAll(item.getResult().getData());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> data) {
                return data == null || data.isEmpty() || userListRateLimit.shouldFetch(USER_ME);
            }

            @NonNull
            protected LiveData<List<User>> loadFromDb() {
                return mUserDao.loadAllUsers();
            }

            @NonNull
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<User>>>> createCall() {
                return mBigkeerSerivice.getAllUsers();
            }

            @Override
            protected void onFetchFailed() {
                userListRateLimit.reset(USER_ME);
            }
        }).getAsLiveData();
    }

    public LiveData<Resource<List<User>>> getUsersInCompany(int type, int companyID){
        return (new NetworkBoundResource<List<User>, BigkeerResponse<GroupInfoResult>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<GroupInfoResult> item) {
                GroupInfoResult result = item.getResult();
                int companyID = result.getUid();
                User company = new User(companyID, result.getName(), result.getMail());
                company.setCompany_id(company.getUid());
                mUserDao.insert(company);
                List<User> users = result.getUsers();
                if (users != null)
                    setUsersCompany(users, companyID);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> data) {
                return data == null || data.isEmpty() || userListRateLimit.shouldFetch(USER_ME);
            }

            @NonNull
            protected LiveData<List<User>> loadFromDb() {
                return mUserDao.loadUserByGroupID(companyID);
            }

            @NonNull
            public LiveData<ApiResponse<BigkeerResponse<GroupInfoResult>>> createCall() {
                if (type == sUserType.indexOf(GROUP_GROUP))
                    return mBigkeerSerivice.companyGetUsersInGroup();
                else return mBigkeerSerivice.userGetUsersInGroup();
            }

            @Override
            protected void onFetchFailed() {
                userListRateLimit.reset(USER_ME);
            }
        }).getAsLiveData();
    }

    public LiveData<Resource<List<User>>> getCompanys(){
        return (new NetworkBoundResource<List<User>, BigkeerResponse<ArrayResult<User>>>(mAppExecutors) {
            @Override
            protected void saveCallResult(@NonNull BigkeerResponse<ArrayResult<User>> item) {
                mUserDao.insertAll(item.getResult().getData());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> data) {
                return data == null || data.isEmpty() || userListRateLimit.shouldFetch(USER_ME);
            }

            @NonNull
            protected LiveData<List<User>> loadFromDb() {
                return mUserDao.loadUserByType(sUserType.indexOf(GROUP_GROUP));
            }

            @NonNull
            public LiveData<ApiResponse<BigkeerResponse<ArrayResult<User>>>> createCall() {
                return mBigkeerSerivice.getAllUsers();
            }

            @Override
            protected void onFetchFailed() {
                userListRateLimit.reset(USER_ME);
            }
        }).getAsLiveData();
    }

    public LiveData<Resource<User>> getUserByID(int id){
        return (new NetworkBoundResource<User, BigkeerResponse<User>>(mAppExecutors) {

            protected void saveCallResult(@NonNull BigkeerResponse<User> item) {
                User user = item.getResult();
                if (user.getCompany() != null)
                    setUserCompany(user, user.getCompany().getCompany_id());
                mUserDao.insert(user);
            }

            protected boolean shouldFetch(@Nullable User data) {
                return data == null || userMeRateLimit.shouldFetch(USER_ME);
            }

            @NonNull
            protected LiveData<User> loadFromDb() {
                return mUserDao.loadUser(id);
            }

            @NonNull
            public LiveData<ApiResponse<BigkeerResponse<User>>> createCall() {
                return mBigkeerSerivice.getUserById(id);
            }

            @Override
            protected void onFetchFailed() {
                userMeRateLimit.reset(USER_ME);
            }
        }).getAsLiveData();
    }



    public LiveData<List<User>> searchUsers(String query) {
        return mDatabase.userDao().loadAllUsers();
    }

    /**
     * All CRUD operation should be done in background thread.
     * Room database do not allow to do database operation on main thread,
     * so we have to create {@link AsyncTask classes for each crud operation.
     */
    public void insert(User user) {
        new InsertUserAsyncTask(mUserDao).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(mUserDao).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(mUserDao).execute(user);
    }

    public void deleteAllUsers() {
        new DeleteAllUsersAsyncTask(mUserDao).execute();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao dao) {
            this.userDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao dao) {
            this.userDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao dao) {
            this.userDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private DeleteAllUsersAsyncTask(UserDao dao) {
            this.userDao = dao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

    private void setUsersCompany(List<User> users, int companyID){
        for (User user: users){
            setUserCompany(user, companyID);
        }
    }

    private void setUserCompany(User user, int companyID){
        user.setCompany_id(companyID);
        mUserDao.insert(user);
    }
}

