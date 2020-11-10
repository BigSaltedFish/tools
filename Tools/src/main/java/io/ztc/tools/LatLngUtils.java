package io.ztc.tools;

import java.util.regex.Pattern;

public class LatLngUtils {
    /**只校验正数 0-90.000000 0-180.000000 范围内
     * 经纬度校验
     * 经度longitude: (?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6})
     * 纬度latitude：  (?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6})
     * @return boolean
     */
    public static boolean checkLoLa(String longitude,String latitude){
        String regLo = "(((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))|((?:[0-9]|[1-9][0-9]|1[0-7][0-9]))|(?:180))";
        String regLa = "(((?:[0-9]|[1-8][0-9]|90)\\.([0-9]{0,6}))|(?:[0-9]|[1-8][0-9]|90))";
        longitude = longitude.trim();
        latitude = latitude.trim();
        return longitude.matches(regLo) && latitude.matches(regLa);
    }

    public static boolean checkLoLa(double longitude,double latitude){
        return checkLat(latitude) && checkLng(longitude);
    }

    public static boolean checkLng(Double lng){
        return lng > -180.00 && lng < 180.00;
    }

    public static boolean checkLat(Double lat){
        return lat > -90 && lat < 90;
    }

    /**
     * 验证经度
     * @param lng 经度
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checklng(Double lng) {
        if(lng==null) {
            return false;
        }
        String regex = "^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,10})?)|180(([.][0]{1,10})?))$";
        return Pattern.matches(regex, lng.toString());
    }

    /**
     * 验证纬度
     * @param lat 纬度
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checklat(Double lat) {
        if(lat==null) {
            return false;
        }
        String regex = "^-?((0|[1-8]?[0-9]?)(([.][0-9]{1,10})?)|90(([.][0]{1,10})?))$";
        return Pattern.matches(regex, lat.toString());
    }

    /**只校验正数 0-90.000000 范围内
     * 纬度校验
     * 纬度latitude：  (?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6})
     * @return boolean
     */
    public static boolean checkLatitude(String latitude){
        String regLa ="(((?:[0-9]|[1-8][0-9]|90)\\.([0-9]{0,6}))|(?:[0-9]|[1-8][0-9]|90))";
        latitude = latitude.trim();
        return latitude.matches(regLa);
    }
    /**只校验正数 0-180.000000 范围内
     * 经度校验
     * 经度longitude: (?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6})
     * @return boolean
     */
    public static boolean checkLongitude(String longitude){
        String regLo = "(((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))|((?:[0-9]|[1-9][0-9]|1[0-7][0-9]))|(?:180))";
        longitude = longitude.trim();
        return longitude.matches(regLo);
    }

}
