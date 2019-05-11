package cn.com.wosuo.taskrecorder.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.ADMIN_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.MANAGER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_CREATE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_DONE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_EXPLORE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_NONE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_PLOT;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_PROGRESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_SAMPLING;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TASK_TEST;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_BLOCK;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_DISABLE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_EDITED;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_LOGIN;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_NONVERIFY;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_NORMAL;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_NOTHING;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.USER_WEAKPASSWORD;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.WITHOUT_NOW;

public final class FinalMap {

    public static List<String> getUserTypeList() {
        return Lists.newArrayList(
                ADMIN_GROUP, USER_GROUP,
                GROUP_GROUP, MANAGER_GROUP,
                WITHOUT_NOW
        );
    }

    public static List<String> getUserStatusList(){
        return Lists.newArrayList(USER_NORMAL, USER_NONVERIFY,
                USER_BLOCK, USER_DISABLE, USER_WEAKPASSWORD);
    }

    public static List<String> getUserLocalStatusList() {
        return Lists.newArrayList(USER_NOTHING, USER_LOGIN, USER_EDITED);
    }


    /**
     * getTaskTypeList()
     * 0 = Explore
     * 1 = Plot
     * 2 = Sampling
     * @return SparseArray
     */
    public static ArrayList<String> getTaskTypeList() {
        return Lists.newArrayList(
                TASK_EXPLORE, TASK_PLOT, TASK_SAMPLING);
    }


    private static final Map<String, Integer> TaskTypeNumMap = ImmutableMap.of(
            TASK_EXPLORE, 0,
            TASK_PLOT, 1,
            TASK_SAMPLING, 2
    );


    /**
     * getTaskStatusList()
     * 0 = Created
     * 1 = InProgress
     * 2 = ReadyForTest
     * 3 = Done
     * @return SparseArray
     */
    public static String[] getTaskStatusList() {
        String[] sTaskStatus = {TASK_CREATE, TASK_PROGRESS, TASK_TEST, TASK_DONE};
        return sTaskStatus;
    }

    /**
     * getTaskStatusList()
     * 0 = Created
     * 1 = InProgress
     * 2 = ReadyForTest
     * 3 = Done
     * @return SparseArray
     */
    public static Map<String, Integer> getTaskStatusNumMap() {
        return TaskStatusNumMap;
    }


    @Deprecated
    private static final Map<String, Integer> TaskStatusNumMap = ImmutableMap.of(
            TASK_CREATE, 0,
            TASK_PROGRESS, 1,
            TASK_TEST, 2,
            TASK_DONE, 3
            );

    public final Integer TaskFinishStatusNumber = TaskStatusNumMap.get(TASK_DONE);

    public static Map<Integer, String> getStatusCodeMap(){
        return ImmutableMap.<Integer, String>builder()
                .put(0, "请求成功")
                .put(1, "请求失败")
                .put(401, "需要登录")
                .put(405, "不能这么做")
                .put(-5, "参数超出界限")
                .put(-4, "未找到资源")
                .put(-3, "无权限")
                .put(-2, "参数错误")
                .put(-1, "参数缺失")
                .build();
    }

    public static final String statusCodeFail = getStatusCodeMap().get(1);
    public static final String statusCodeLost = getStatusCodeMap().get(-1);

    public static ArrayList<String> getCoorTypeList(){
        return Lists.newArrayList("WGS84", "GCJ02", "bd09ll");
    }
}
