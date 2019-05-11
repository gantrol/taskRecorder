package cn.com.wosuo.taskrecorder.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArrayResult<T> {
    int count;
    @SerializedName(value = "data", alternate = {"users"})
    List<T> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
