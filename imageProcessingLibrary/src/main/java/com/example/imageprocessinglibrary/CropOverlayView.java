package com.example.imageprocessinglibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CropOverlayView extends View {
    private Paint borderPaint;
    private Rect cropRect;
    private float startX, startY;

    public CropOverlayView(Context context) {
        this(context, null);
    }

    public CropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(0xFFFFFFFF);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);
        cropRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cropRect != null) {
            canvas.drawRect(cropRect, borderPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                cropRect.set((int) startX, (int) startY, (int) startX, (int) startY);
                break;

            case MotionEvent.ACTION_MOVE:
                cropRect.right = (int) event.getX();
                cropRect.bottom = (int) event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public Rect getCropRect() {
        return cropRect;
    }
}