package cn.com.wosuo.taskrecorder.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;

import cn.com.wosuo.taskrecorder.vo.LocCenterPoint;
import cn.com.wosuo.taskrecorder.vo.TrackData;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.CoordinateType.GCJ02;
import static cn.com.wosuo.taskrecorder.util.FinalStrings.CoordinateType.WGS84;

public class CoordinateTypeUtil {

    private final static ArrayList<String> sCoorType = FinalMap.getCoorTypeList();

    public static LatLng toBaidull(TrackData trackData, int coordinate){
        double Y = trackData.getPointY();
        double X = trackData.getPointX();
        return toBaidull(Y, X, coordinate);
    }

    public static LatLng toBaidull(LocCenterPoint locCenterPoint){
        double Y = locCenterPoint.getPositionY();
        double X = locCenterPoint.getPositionX();
        int coordinate = locCenterPoint.getCoordinate();
        return toBaidull(Y, X, coordinate);
    }

    public static LatLng toBaidull(double Y, double X, int coordinate){
        LatLng GEO_TRACK = new LatLng(Y, X);
        if (coordinate == sCoorType.indexOf(WGS84)){
            CoordinateConverter converter = new CoordinateConverter()
                    .from(CoordinateConverter.CoordType.GPS)
                    .coord(GEO_TRACK);
            GEO_TRACK = converter.convert();
        } else if (coordinate == sCoorType.indexOf(GCJ02)){
            CoordinateConverter converter = new CoordinateConverter()
                    .from(CoordinateConverter.CoordType.COMMON)
                    .coord(GEO_TRACK);
            GEO_TRACK = converter.convert();
        }
        return GEO_TRACK;
    }


}
