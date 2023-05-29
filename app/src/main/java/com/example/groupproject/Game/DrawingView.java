package com.example.groupproject.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {
    private Paint paint;
    private Path path;
    private int brushSize;
    private int brushColor;

    private List<Path> paths;
    private List<Integer> colors;
    private List<Integer> brushSizes;

    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        brushSize = 5;
        brushColor = Color.BLACK;

        paint.setAntiAlias(true);
        paint.setStrokeWidth(brushSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paths = new ArrayList<>();
        colors = new ArrayList<>();
        brushSizes=new ArrayList<>();
    }

    public void setBrushSize(int size) {
        brushSize = size;
        paint.setStrokeWidth(brushSize);
    }

    public void setBrushColor(int color) {
        brushColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < paths.size(); i++) {
            paint.setColor(colors.get(i));
            paint.setStrokeWidth(brushSizes.get(i));
            canvas.drawPath(paths.get(i), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(x, y);
                paths.add(path);
                colors.add(brushColor);
                brushSizes.add((int) brushSize);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clear() {
        paths.clear();
        colors.clear();
        brushSizes.clear();
        invalidate();
    }
}




