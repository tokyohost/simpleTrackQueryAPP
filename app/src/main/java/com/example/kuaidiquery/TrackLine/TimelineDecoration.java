package com.example.kuaidiquery.TrackLine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 快递路径时间线绘制
 *
 */

public class TimelineDecoration extends RecyclerView.ItemDecoration {


    private int width;//时间轴宽度
    private int top;//圆距离item顶部高度
    private Drawable goingDrawable;//绿色对勾圆
    private int goingDrawableSize;//绿色对勾圆的直径
    private int dividerHeight;//线条粗细

    private int lintColor = 0xff999999;//线条颜色
    private Paint mPaint;

    private int ovalRadius = 12;//灰色圆的半径

    private boolean isReceive = false; //是否收件


    public TimelineDecoration(int width, int top, Drawable goingDrawable, int goingDrawableSize, int dividerHeight,boolean isReceive) {
        this.width = width;
        this.top = top;
        this.goingDrawableSize = goingDrawableSize;
        this.goingDrawable = goingDrawable;
        this.dividerHeight = dividerHeight;
        this.isReceive = isReceive;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(width, 0, 0, dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int top = child.getTop();
            int bottom = child.getBottom();

            //竖直线
            int left = parent.getPaddingLeft() + width / 2;
            c.drawRect(left,
                    i==0?this.top+goingDrawableSize:top,//第一个item线有空一段
                    left + dividerHeight,
                    bottom + dividerHeight,
                    mPaint);

            //小圆点

            int ovalCenterX = top + this.top + ovalRadius;
            if (i == 0 && isReceive) { //仅签收时才显示对勾，其他显示圆点
                goingDrawable.setBounds(left-goingDrawableSize/2,top+this.top,left+goingDrawableSize/2,top+this.top+goingDrawableSize);
                goingDrawable.draw(c);
            } else {
                c.drawCircle(left, ovalCenterX, ovalRadius, mPaint);
            }


            //分割线
            mPaint.setColor(lintColor);
            c.drawRect(parent.getPaddingLeft() + width, bottom, parent.getWidth() - parent.getPaddingRight(), bottom + dividerHeight, mPaint);


        }

    }
}
