package me.tomoya.kanojyongank.widget.fresco;

import android.graphics.Matrix;
import android.graphics.Rect;
import com.facebook.drawee.drawable.ScalingUtils;

/**
 * Created by piper on 17-1-10.
 */

public class ImgScaleType implements ScalingUtils.ScaleType {
	@Override
	public Matrix getTransform(Matrix outTransform, Rect parentBounds, int childWidth,
			int childHeight, float focusX, float focusY) {
		final float scaleX = (float) parentBounds.width() / childWidth;
		final float scaleY = (float) parentBounds.height() / childHeight;
		float scale = Math.max(scaleX, scaleY);
		//outTransform.preTranslate(dx, dy);
		outTransform.preScale(scale, scale);
		return outTransform;
	}
}
