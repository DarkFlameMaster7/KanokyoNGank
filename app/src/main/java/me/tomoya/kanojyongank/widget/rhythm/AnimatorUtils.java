package me.tomoya.kanojyongank.widget.rhythm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Tomoya-Hoo on 2016/10/26.
 */

public class AnimatorUtils {
    public static Animator showUpAndDownBounce(View view, int translationY, int animatorTime, boolean isStartAnimator, boolean isStartInterpolator) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", translationY);
        if (isStartInterpolator) {
            objectAnimator.setInterpolator(new OvershootInterpolator());
        }
        objectAnimator.setDuration(animatorTime);
        if (isStartAnimator) {
            objectAnimator.start();
        }
        return objectAnimator;
    }
    public static Animator moveScrollViewToX(View view,int toX,int time,int delayTime,boolean isStart) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view,"scrollX",new int[]{toX});
        objectAnimator.setDuration(time);
        objectAnimator.setInterpolator( new AccelerateDecelerateInterpolator());
        objectAnimator.setStartDelay(delayTime);
        if(isStart)
            objectAnimator.start();
        return objectAnimator;
    }

}
