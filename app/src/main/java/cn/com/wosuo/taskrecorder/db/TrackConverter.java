package cn.com.wosuo.taskrecorder.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import cn.com.wosuo.taskrecorder.util.JsonParser;
import cn.com.wosuo.taskrecorder.vo.Track;

public class TrackConverter {
//TODO: Option 2 of
// https://stackoverflow.com/
// questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    @TypeConverter
    public static String toJson(Track tracks){
        Gson gson = JsonParser.getGson();
        return gson.toJson(tracks, Track.class);
    }

    @TypeConverter
    public static Track toTracks(String Json){
        Gson gson = JsonParser.getGson();
        return gson.fromJson(Json, Track.class);
    }
}
