package cn.com.wosuo.taskrecorder.db;

import androidx.room.TypeConverter;
import java.util.Date;

public class DateConverter {
    @Deprecated
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @Deprecated
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}