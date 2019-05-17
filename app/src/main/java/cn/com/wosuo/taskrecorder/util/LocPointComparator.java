package cn.com.wosuo.taskrecorder.util;


import java.util.Comparator;

import cn.com.wosuo.taskrecorder.vo.TrackData;

public class LocPointComparator implements Comparator<TrackData> {
    @Override
    public int compare(TrackData o1, TrackData o2) {
        return Long.compare(o1.getTimestamp(), o2.getTimestamp());
    }
}
