package cn.com.wosuo.taskrecorder.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.UPDATE_LASTLY;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.WITHOUT_NOW;

@Deprecated
public class DateUtil {
//    TODO: 4Move to DateConverter？
    public static Date intTimestampToDate(int intTimestamp){
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

    public static String intTimestampToFullDateString(int intTimestamp){
        String result = WITHOUT_NOW;
        if (intTimestamp != 0){
            result = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(
                    intTimestampToDate(intTimestamp));
        }
        return result;
    }

    public static String intTimestampToChinaMiddleDateString(int intTimestamp){
        String result = WITHOUT_NOW;
        if (intTimestamp != 0){
            String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(
                    intTimestampToDate(intTimestamp));
            result = UPDATE_LASTLY + dateString;

        }
        return result;
    }
}
