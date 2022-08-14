package com.mysl.api.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 坐标工具类
 * @author Ivan Su
 * @date 2022/8/14
 */
@Slf4j
public class CoordUtil {
    // 地球半径 6378.137km
    private static final double EARTH_RADIUS = 6378.137;

    // 计算距离，单位：米
    public static double getDistance(double longitude1, double latitude1,
                               double longitude2, double latitude2) {
        log.info("lng1: {}, lat1:{}, lng2: {}, lat: {}", longitude1, latitude1, longitude2, latitude2);
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // 有小数的情况; 注意这里的10000d中的“d”
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
