package com.tomoya.kanojyongank.widget.fresco;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by piper on 17-1-10.
 */

public class KanojyoProgressBar extends Drawable {
    @Override protected boolean onLevelChange(int level) {
        // level is on a scale of 0-10,000
        // where 10,000 means fully downloaded

        // your app's logic to change the drawable's
        // appearance here based on progress
        return super.onLevelChange(level);
    }

    @Override public void draw(Canvas canvas) {

    }

    @Override public void setAlpha(int alpha) {

    }

    @Override public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
