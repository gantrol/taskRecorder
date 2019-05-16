package cn.com.wosuo.taskrecorder.api;

public final class Urls {
    public static final String BASE_URL = "https://land.bigkeer.cn/api/";
    public static final String BASE_URL_minus = "https://land.bigkeer.cn";
    public static final String LOGIN = "Session";

    public static final String GET_USER_ME = "User/me";

    public static final String GET_OR_CREATE_TASKS = "Task";
        public static final String TASK_TITLE = "title";
        public static final String TASK_DESCRIPTION = "description";
        public static final String TASK_TYPE = "assignee";
        public static final String ASSIGNEE = "assignee";
        public static final String RESOURCE = "resource";
        public static final String TASK_STATUS = "status";
        public static final String TASK_TRACK = "Track";
        public static final String TASK_TRACK_DATA = "strData";
        public static final String TASK_ID = "taskID";
        public static final String TASK_X = "PositionX";
        public static final String TASK_Y = "PositionY";
        public static final String TASK_COOR = "coordinate";
    public static final String photo__ = "photo";
        public static final String PHOTO_FILE = "file";
        public static final String PHOTO_TASKID = "taskID";
        public static final String PHOTO_SUBID = "subID";
        public static final String PHOTO_TIME = "time";
        public static final String PHOTO_LOCATION= "locationStr";
        public static final String PHOTO_DESC = "description";



    public static final String GET_COMPANY_TASKS = "Task/assignee/{assigneeID}";
    public static final String COMPANY_ID_IN_TASK = "assigneeID";
    public static final String GET_MANAGER_TASKS = "Task/assignee/{assignerID}";
    public static final String MANAGER_ID_IN_TASK = "assignerID";
    public static final String GET_USER_TASKS = "Assign/task/active";
    public static final String USER_GET_USERS_BY_GROUP = "UserGroup/user";
    public static final String COMPANY_GET_USERS_BY_GROUP = "UserGroup";

    public static final String COMPANY_GET_EXECUTOR = "Assign/";

}
