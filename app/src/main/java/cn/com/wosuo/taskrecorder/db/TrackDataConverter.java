package cn.com.wosuo.taskrecorder.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.com.wosuo.taskrecorder.vo.TrackData;

import static cn.com.wosuo.taskrecorder.util.JsonParser.getGson;

public class TrackDataConverter {
    @TypeConverter
    public static String toJson(List<TrackData> sTrackData){
        Gson gson = getGson();
        Type token = new TypeToken<List<TrackData>>() {}.getType();
        return gson.toJson(sTrackData, token);
    }

    @TypeConverter
    public static List<TrackData> toTrackDataList(String json){
        Gson gson = getGson();
        Type token = new TypeToken<List<TrackData>>() {}.getType();
        return gson.fromJson(json, token);
    }
}
