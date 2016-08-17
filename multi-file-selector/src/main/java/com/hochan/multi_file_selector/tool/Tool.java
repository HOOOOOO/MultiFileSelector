package com.hochan.multi_file_selector.tool;

/**
 *
 * Created by hochan on 2016/5/20.
 */
public class Tool {

    public static String getDurationFormat(long source){
        int tmpsecond = (int) (source / 1000);
        int tmpminute = tmpsecond / 60;
        int hour = tmpminute / 60;
        int minute = tmpminute % 60;
        int second = tmpsecond % 60;
        if(hour > 0)
            return String.format("%s:%s:%s", hour, minute, second);
        else if(minute > 0)
            return String.format("%s:%s", minute, second);
        else{
            return String.format("%ss", second);
        }
    }
}
