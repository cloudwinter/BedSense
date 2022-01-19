package com.fenmenbielei.bedsense.uitls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  作者：chuang on 2016/12/23
 *  邮箱：844285775@qq.com
 *  
 */
public class TimeUtils {

    //获取系统时间并格式化
    public static  String getSystemTime(){
        long time=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date=new Date(time);
        String systemTime=format.format(date);
        return systemTime;
    }

    /**
     * 时间戳转为时间(年月日，时分秒)
     * @param strTime 时间戳
     * @return
     */
    public static String getStampToTime(String strTime) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        // 例如：cc_time=1291778220
        int i = Integer.parseInt(strTime);
        re_StrTime = sdf.format(new Date(i * 1000L));

        return re_StrTime;
    }

    public static String getStampToTime2(String strTime) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm:ss"
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        int i = Integer.parseInt(strTime);
        re_StrTime = sdf2.format(new Date(i * 1000L));
        return re_StrTime;
    }


    public static String getStampToTime3(String strTime) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm:ss"
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 例如：cc_time=1291778220
        int i = Integer.parseInt(strTime);
        re_StrTime = sdf3.format(new Date(i * 1000L));
        return re_StrTime;
    }

    /**
     * 时间转换为时间戳
     * @param timeStr 时间 例如: 2016-03-09 15:33:50
     * @return
     */
    public static long getTimeToStamp(String timeStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间转换为时间戳
     * @param date 日期
     * @return
     */
    public static long getTime(Date date){
        long timeStamp = date.getTime();
        return timeStamp/1000;
    }
}
