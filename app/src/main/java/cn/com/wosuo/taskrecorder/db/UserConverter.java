package cn.com.wosuo.taskrecorder.db;

import com.google.gson.Gson;

import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.vo.User;

public class UserConverter {

    public static String toJson(User user){
        Gson gson = JsonParser.getGson();
        return gson.toJson(user);
    }
    public static User toUserEntity(String userJson){
        Gson gson = JsonParser.getGson();
        return gson.fromJson(userJson, User.class);
    }
}
