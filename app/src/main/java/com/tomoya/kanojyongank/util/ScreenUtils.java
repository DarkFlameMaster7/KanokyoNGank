package com.tomoya.kanojyongank.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by Tomoya-Hoo on 2016/11/16.
 */

public class ScreenUtils {
    /**
     * 获取屏幕宽高
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new int[] { displayMetrics.widthPixels, displayMetrics.heightPixels };
    }

    /**
     * 判断是否是平板
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources()
                       .getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
