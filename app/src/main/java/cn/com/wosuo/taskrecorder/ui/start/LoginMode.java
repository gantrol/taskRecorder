package cn.com.wosuo.taskrecorder.ui.start;

@Deprecated
public enum LoginMode {
    LOGGED_IN_MODE_LOGGED_OUT(-1),
    LOGGED_IN_MODE_ADMIN(0),
    LOGGED_IN_MODE_USER(1),
    LOGGED_IN_MODE_GROUP(2),
    LOGGED_IN_MODE_MANAGER(3);

    private final int mType;

    LoginMode(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }
}
