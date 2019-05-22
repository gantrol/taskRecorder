package cn.com.wosuo.taskrecorder.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.TextViewField.UPDATE_LASTLY;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.TextViewField.WITHOUT_NOW;

public class DateUtil {
//    TODO: 4Move to DateConverter？
    public static Date unixTimestampToDate(int intTimestamp){
//        Timestamp timestamp = new Timestamp(intTimestamp * 1000L);
//        return new Date(timestamp.getTime());
        Date date = null;
        if (intTimestamp != 0){
            date = new Date(intTimestamp * 1000L);
        }
        return date;
    }

    public static String DateToChinaDateString(int style, Date date){
        DateFormat df;
//        int style = DateFormat.LONG;
        df = DateFormat.getDateInstance(style, Locale.CHINA);
        return df.format(date);
    }

    public static String unixTimestampToFullDateString(int intTimestamp){
        String result = WITHOUT_NOW;
        if (intTimestamp != 0){
            result = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(
                    unixTimestampToDate(intTimestamp));
        }
        return result;
    }

    public static String unixTimestampToChinaMiddleDateString(int intTimestamp){
        String result = WITHOUT_NOW;
        if (intTimestamp != 0){
            String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(
                    unixTimestampToDate(intTimestamp));
            result = UPDATE_LASTLY + dateString;

        }
        return result;
    }

    public static long getUnixTimestamp(){
        return System.currentTimeMillis() / 1000L;
    }
}
