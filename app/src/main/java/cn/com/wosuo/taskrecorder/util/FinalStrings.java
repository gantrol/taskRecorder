package cn.com.wosuo.taskrecorder.util;

public final class FinalStrings {

    public static final String LOGIN_PREF = "login_pref";
    public static final String USER_LIST = "USER_LIST";

    public static class ResourceField {
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int MISSINGREQ = -1;
        public static final int INVALIDREQ = -2;
        public static final String CODE = "code";
        public static final String COUNT = "count";
        public static final String DATA = "data";
        public static final String RESULT = "result";
        public static final String SOON = "很快就好...";
        //    Status Code
        public static final int LOGIN_SUCCESS = 0;
        public static final int LOGIN_FAILED = 401;
    }

    public static class LoginSignUpField {
        //    Request string
        public static final String USERNAME = "UserName";
        public static final String PASSWORD = "PassWord";
        public static final String EMAIL = "Mail";
        public static final String NICKNAME = "NickName";
    }

    public static class CoordinateType{
        public static final String WGS84 = "WGS84";
        public static final String GCJ02 = "GCJ02";
        public static final String bd09ll = "bd09ll";
    }

    public static class UserField {
        public static final String USER_ID = "UserID";
        public static final String NAME = "name";
        public static final String MAIL = "mail";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String GROUP = "group";
        public static final String GROUP_NAME = "group_name";
        public static final String GROUP_FOR_DB = "group_";
        public static final String MANAGER_GROUP = "管理部门";
        public static final String USER_NORMAL = "Normal";
        public static final String USER_NONVERIFY = "NonVerify";
        public static final String USER_BLOCK = "Block";
        public static final String USER_DISABLE = "Disable";
        public static final String USER_WEAKPASSWORD = "WeakPassword";
        public static final String USER_ME = "me";
        public static final String USER_NOTHING = "nothing";
        public static final String USER_LOGIN = "login";
        public static final String USER_EDITED = "edit";
        public static final String UID = "uid";
        public static final String ADMIN_GROUP = "admin";
        public static final String USER_GROUP = "普通用户";
        public static final String GROUP_GROUP = "公司用户";
    }

    public static class TaskField {
        public static final String RESOURCE = "resource";
        public static final String TASK_DESCRIPTION = "description";
        public static final String TASK_TYPE = "taskType";
        public static final String ASSIGNEE = "assignee";
        public static final String TASK_STATUS = "status";
        public static final String TASK_TRACK = "Track";
        public static final String TASK_TRACK_DATA = "strData";
        public static final String TASK_ID = "taskID";
        public static final String TASK_X = "PositionX";
        public static final String TASK_Y = "PositionY";
        public static final String TASK_COOR = "coordinate";
        public static final String TASK_TITLE = "title";
        public static final String TASK_EXPLORE = "初步调查";
        public static final String TASK_PLOT = "初布点";
        public static final String TASK_SAMPLING = "采样";
        public static final String TASK_ASSIGNER = "assigner";
        public static final String TASK_ASSIGNEE = "assignee";
        public static final String TASK_DESC = "description";
        public static final String TASK_RESULT = "result";
        public static final String TASK_RESOURCE = "resource";
        public static final String TASK_CREATE_AT = "createAt";
        public static final String TASK_UPDATE_AT = "updateAt";
        public static final String TASK_FINISH_AT = "finishAt";
        public static final String TASK_CREATE = "创建中";
        public static final String TASK_PROGRESS = "进行中";
        public static final String TASK_TEST = "测试中";
        public static final String TASK_DONE = "已完成";
        public static final String TASK_NONE = "取消";
    }

    public static class PhotoField {
        public static final String photo__ = "photo";
        public static final String PHOTO_FILE = "file";
        public static final String PHOTO_TASKID = "taskID";
        public static final String PHOTO_SUBID = "subID";
        public static final String PHOTO_TIME = "time";
        public static final String PHOTO_LOCATION= "locationStr";
        public static final String PHOTO_DESC = "description";
        public static final String PHOTO_TYPE = "subID";
        public static final String INTERVIEWEE = "访谈人";
        public static final String MEETING = "座谈会议";// 访谈人、座谈会议、敏感对象、地块现场
        public static final String SENSITIVE_OBJECT = "敏感对象";
        public static final String SITE_SCENE = "地块现场";
    }

    public static class ToastShowField {
        //    提示语
        public static final String YOU_LOGIN_SUCCESS = "登录成功";
        public static final String YOU_LOGIN_FAILED = "账号或密码错误";
        public static final String YOU_SIGNUP = "注册";
        public static final String YOU_SIGNUP_FAILED = "注册失败";
        public static final String YOU_SIGNUP_SUCCESS = "注册成功";
        public static final String YOU_TASK_UPDATE_FAIL = "任务更新失败";
        public static final String YOU_TASK_UPDATE_SUCCESS = "任务更新成功";
    }

    public static class TextViewField {
        //    UI
        public static final String UPDATE_LASTLY = "最后更新于：";
        public static final String WITHOUT_NOW = "暂无";
    }
}

