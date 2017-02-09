package me.tomoya.kanojyongank.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Tomoya-Hoo on 2016/11/1.
 */

public class AnimatorUtils {
    public static Animator animFadeIn(View view){
        return animFadeIn(view, 200L, null);
    }
    public static Animator animFadeIn(View view,long duration,Animator.AnimatorListener listener){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0F, 1.0F});
        animator.setDuration(duration);
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
        return animator;
    }
    public static Animator animFadeOut(View view){
        return animFadeOut(view, 200L, null);
    }
    public static Animator animFadeOut(View view,long duration,Animator.AnimatorListener listener){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0F, 0.0F});
        animator.setDuration(duration);
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
        return animator;
    }
    public static void showBgColorAnimation(View view ,int preColor,int currColor,int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", new int[]{preColor, currColor});
        objectAnimator.setDuration(duration);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.start();
    }
}
