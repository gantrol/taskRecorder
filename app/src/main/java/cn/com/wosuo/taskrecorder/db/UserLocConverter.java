package cn.com.wosuo.taskrecorder.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.vo.UserLocation;

public class UserLocConverter {

    @TypeConverter
    public static String toJson(UserLocation userLocation){
        Gson gson = JsonParser.getGson();
        return gson.toJson(userLocation);
    }

    @TypeConverter
    public static UserLocation toUserEntity(String userLocJson){
        Gson gson = JsonParser.getGson();
        return gson.fromJson(userLocJson, UserLocation.class);
    }
}
