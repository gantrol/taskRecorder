package cn.com.wosuo.taskrecorder.api;

public final class Urls {

    public static class HostString {
        public static final String API = "api";
        public static final String API_HOST = "land.bigkeer.cn";
        public static final String BASE_URL = "https://land.bigkeer.cn/api/";
    }

    public static class LoginSignUpApi {
        public static final String LOGIN = "Session";
        public static final String SESSION = "Session";
    }

    public static class UserApi {
        public static final String GET_USER_ME = "User/me";
        public static final String USER = "User";
        public static final String USER_GET_USERS_BY_GROUP = "UserGroup/user";
        public static final String COMPANY_GET_USERS_BY_GROUP = "UserGroup";
        public static final String COMPANY_GET_EXECUTOR = "Assign/";
        public static final String signupAPI = "User";
    }

    public static class TaskApi {
        public static final String GET_OR_CREATE_TASKS = "Task";
        public static final String PUT_EDIT_TASK = "Task/{taskID}";
        public static final String GET_COMPANY_TASKS = "Task/assignee/{assigneeID}";
        public static final String COMPANY_ID_IN_TASK = "assigneeID";
        public static final String GET_MANAGER_TASKS = "Task/assigner/{assignerID}";
        public static final String MANAGER_ID_IN_TASK = "assignerID";
        public static final String GET_USER_TASKS = "Assign/task/active";
    }


    public static class PhotoApi {
        public static final String PHOTO = "Photo";
    }
    public static class ExploreApi {
        public static final String GET_EXPLORE = "Explore/";
        public static final String GET_LOC_EXPLORE = "Explore/{taskID}";
    }

}
