package cn.com.wosuo.taskrecorder.util;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.ADMIN_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.CoordinateType.GCJ02;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.CoordinateType.WGS84;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.CoordinateType.bd09ll;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.GROUP_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.INTERVIEWEE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.MANAGER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.MEETING;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.SENSITIVE_OBJECT;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.PhotoField.SITE_SCENE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_CREATE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_DONE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_EXPLORE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_PLOT;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_PROGRESS;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_SAMPLING;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TaskField.TASK_TEST;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_BLOCK;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_DISABLE;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_EDITED;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_GROUP;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_LOGIN;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_NONVERIFY;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_NORMAL;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_NOTHING;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.UserField.USER_WEAKPASSWORD;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TextViewField.WITHOUT_NOW;

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


    /**
     * getTaskStatusList()
     * 0 = Created
     * 1 = InProgress
     * 2 = ReadyForTest
     * 3 = Done
     * @return SparseArray
     */
    public static String[] getTaskStatusList() {
        return new String[]{TASK_CREATE, TASK_PROGRESS, TASK_TEST, TASK_DONE};
    }

    public static ArrayList<String> getTaskStatusArray() {
        return Lists.newArrayList(TASK_CREATE, TASK_PROGRESS, TASK_TEST, TASK_DONE);
    }

    public static String[] getPhotoTypeList() {
        // 0-3分别代表四种照片：访谈人、座谈会议、敏感对象、地块现场
        return new String[]{INTERVIEWEE, MEETING, SENSITIVE_OBJECT, SITE_SCENE};
    }


    public static Map<Integer, String> getStatusCodeMap(){
        return ImmutableMap.<Integer, String>builder()
                .put(0, "请求成功")
                .put(1, "请求失败")
                .put(200, "请求成功")
                .put(400, "参数错误")
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
        return Lists.newArrayList(WGS84, GCJ02, bd09ll);
    }

    public static List<BitmapDescriptor> getTextureList(){
        List<BitmapDescriptor> textureList = new ArrayList<>();
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-brown.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-indigo.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-lightblue.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-lightgreen.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-orange.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-purple.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-red.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-teal.png"));
        textureList.add(BitmapDescriptorFactory.fromAsset("routeArrow-yellow.png"));
        return textureList;
    }
}
