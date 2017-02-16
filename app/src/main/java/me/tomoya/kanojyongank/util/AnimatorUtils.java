package me.tomoya.kanojyongank.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Tomoya-Hoo on 2016/11/1.
 */

public class AnimatorUtils {

	public static Animator showUpAndDownBounce(View view, int translationY, int animatorTime,
			boolean isStartAnimator, boolean isStartInterpolator) {
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

	public static Animator moveScrollViewToX(View view, int toX, int time, int delayTime,
			boolean isStart) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "scrollX", new int[] {toX});
		objectAnimator.setDuration(time);
		objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		objectAnimator.setStartDelay(delayTime);
		if (isStart) {
			objectAnimator.start();
		}
		return objectAnimator;
	}

	public static Animator animFadeIn(View view) {
		return animFadeIn(view, 200L, null);
	}

	public static Animator animFadeIn(View view, long duration, Animator.AnimatorListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[] {0.0F, 1.0F});
		animator.setDuration(duration);
		if (listener != null) {
			animator.addListener(listener);
		}
		animator.start();
		return animator;
	}

	public static Animator animFadeIn(View view, int duration, ValueAnimator.AnimatorUpdateListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[] {0.0F, 1.0F});
		animator.setDuration(duration);
		if (listener != null) {
			animator.addUpdateListener(listener);
		}
		animator.start();
		return animator;
	}

	public static Animator animFadeOut(View view) {
		return animFadeOut(view, 200L, null);
	}

	public static Animator animFadeOut(View view, long duration, Animator.AnimatorListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[] {1.0F, 0.0F});
		animator.setDuration(duration);
		if (listener != null) {
			animator.addListener(listener);
		}
		animator.start();
		return animator;
	}

	public static void showBgColorAnimation(View view, int preColor, int currColor, int duration) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor",
				new int[] {preColor, currColor});
		objectAnimator.setDuration(duration);
		objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.start();
	}
}
