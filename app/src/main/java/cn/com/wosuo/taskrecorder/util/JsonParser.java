package cn.com.wosuo.taskrecorder.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.wosuo.taskrecorder.vo.Task;
import cn.com.wosuo.taskrecorder.vo.ArrayResult;
import cn.com.wosuo.taskrecorder.vo.BigkeerResponse;
import cn.com.wosuo.taskrecorder.vo.Track;
import cn.com.wosuo.taskrecorder.vo.TrackData;
import cn.com.wosuo.taskrecorder.vo.User;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.ResourceField.LOGIN_SUCCESS;

public class JsonParser {
    public static final String TAG = "JsonParser";
    private static Gson mGson = null;

    public static Gson getGson(){
        if (mGson == null){
            mGson = new GsonBuilder()
//                    .registerTypeAdapter(User.class, new UserInstanceCreator())
                    .create();
        }
        return mGson;
    }

    public static List<User> parseGetExecutorJson(String responseBody){
        List<User> users = null;
        Gson gson = getGson();

        Type userType = new TypeToken<BigkeerResponse<List<User>>>() {}.getType();

        if (responseBody != null){
            BigkeerResponse<List<User>> userBigkeerResponse = gson.fromJson(responseBody, userType);
            int statusCode = userBigkeerResponse.getCode();
            if (statusCode == LOGIN_SUCCESS && userBigkeerResponse.getResult() != null) {
                users = userBigkeerResponse.getResult();
            }
        }
        if (users == null){
             users = new ArrayList<>();
        }
        return users;
    }

    @Deprecated
    public static int parseCreateTaskJson(String responseBody){
        int statusCode = -1;
        if (responseBody != null && !responseBody.isEmpty()){
            Type type = new TypeToken<BigkeerResponse<Integer>>(){}.getType();
            Gson gson = getGson();
            BigkeerResponse<Integer> response = gson.fromJson(responseBody, type);
            statusCode = response.getCode();
        }
        return statusCode;
    }

    public static Pair<Integer, String> parseChangeTaskStatusJson(String responseBody){
        int statusCode = -1;
        String message = null;
        if (responseBody != null && !responseBody.isEmpty()){
            Type type = new TypeToken<BigkeerResponse<String>>(){}.getType();
            Gson gson = getGson();
            BigkeerResponse<String> response = gson.fromJson(responseBody, type);
            statusCode = response.getCode();
            message = response.getMessage();
        }
        return new Pair<>(statusCode, message);
    }

    public static String generateTrackJson(List<TrackData> sTrackData, int coordinate){
        Gson gson = getGson();
//        TypeToken<List<TrackData>> token = new TypeToken<List<TrackData>>() {};
        Track track = new Track(coordinate, sTrackData);
        return gson.toJson(track);
    }

    /**
     *
     * @param responseData
     * @return static code and me;
     */
    @Deprecated
    public static Pair<Integer, User> parseLoginJson(String responseData) {
        int statusCode;
        User me = null;
        Gson gson = getGson();
        Type userType = new TypeToken<BigkeerResponse<User>>() {}.getType();
        BigkeerResponse<User> userResource = gson.fromJson(responseData, userType);
        statusCode = userResource.getCode();
        if (statusCode == LOGIN_SUCCESS && userResource.getResult() != null) {
            me = userResource.getResult();
//            me.setLocalStatus(1);
        }
        return new Pair<>(statusCode, me);
    }

    @Deprecated
    public static Pair<Integer,List<User>> parseAllUserJson(String JsonString){
        int statusCode;
        List<User> users;
        Gson gson = getGson();
        Type arrayResultType = new TypeToken<BigkeerResponse<ArrayResult<User>>>() {}.getType();
        BigkeerResponse<ArrayResult<User>> userResource = gson.fromJson(JsonString, arrayResultType);
        statusCode = userResource.getCode();
        users = userResource.getResult().getData();
        return new Pair<>(statusCode, users);
    }

    @Deprecated
    public static Pair<Integer, List<Task>> parseAllTaskJson(String JsonString){
        int statusCode;
        List<Task> data;
        Gson gson = getGson();
        Type type = new TypeToken<BigkeerResponse<ArrayResult<Task>>>(){}.getType();
        BigkeerResponse<ArrayResult<Task>> bigkeerResponse = gson.fromJson(JsonString, type);
        statusCode = bigkeerResponse.getResult().getCount();
        data = bigkeerResponse.getResult().getData();
        return new Pair<>(statusCode, data);
    }

}
