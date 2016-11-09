package com.tomoya.kanojyongank.util;

import android.content.Context;

import com.tomoya.kanojyongank.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomoya-Hoo on 2016/11/1.
 */

public class DateUtils {
    /**
     * 拆解对象为年月日
     * @param date
     * @return
     */
    public static int[] divideDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{year,month,day};
    }

    /**
     * 获取date中的星期 context.getResources().getStringArray(R.array.weekdays)
     * @param date
     * @return
     */
    public static String getWeekOfDate (Context context,Date date) {
        String[] weekday =context.getResources().getStringArray(R.array.weekdays);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (week < 0)
            week = 0;
        return weekday[week];
    }
    public static boolean isTheSameDay(Date one, Date another) {
        Calendar _one = Calendar.getInstance();
        _one.setTime(one);
        Calendar _another = Calendar.getInstance();
        _another.setTime(another);
        int oneDay = _one.get(Calendar.DAY_OF_YEAR);
        int anotherDay = _another.get(Calendar.DAY_OF_YEAR);

        return oneDay == anotherDay;
    }
    public static boolean isYesterday(Date one,Date another){
        Calendar _one = Calendar.getInstance();
        _one.setTime(one);
        Calendar _another = Calendar.getInstance();
        _another.setTime(another);
        int oneDay = _one.get(Calendar.DAY_OF_YEAR);
        int anotherDay = _another.get(Calendar.DAY_OF_YEAR);
        return Math.abs(oneDay - anotherDay)==1;
    }
}
