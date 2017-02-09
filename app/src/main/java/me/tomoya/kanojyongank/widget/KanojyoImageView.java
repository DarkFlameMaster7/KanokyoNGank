package me.tomoya.kanojyongank.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Tomoya-Hoo on 2016/10/15.
 */

public class KanojyoImageView extends ImageView {
    private int currentWidth;
    private int currentHeight;

    public void setKanojyoSize(int currentWidth,int currentHeight) {
        this.currentWidth = currentWidth;
        this.currentHeight = currentHeight;
    }


    public KanojyoImageView(Context context) {
        super(context);
    }

    public KanojyoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KanojyoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
