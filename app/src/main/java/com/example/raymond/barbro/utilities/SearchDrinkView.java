package com.example.raymond.barbro.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by alex on 4/14/17.
 */

public class SearchDrinkView extends View {

    Paint       mPaint;
    //MaskFilter  mEmboss;
    //MaskFilter  mBlur;
    Bitmap mBitmap;
    Canvas  mCanvas;
    Paint mBitmapPaint;
    Bitmap myBitmap;
    private int height;
    private int width;
    private int initialTop;
    private int initialBottom;
    private int initialRight;
    private int intialLeft;
    private RectF rect;


    public SearchDrinkView(Context context, Bitmap b) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setAntiAlias(true);

        mBitmapPaint = new Paint();
        mBitmapPaint.setARGB(128, 255, 255, 255);
        myBitmap = Bitmap.createBitmap(b);
        width = myBitmap.getWidth();
        height = myBitmap.getHeight();
        initialTop = 10;
        intialLeft = 10;
        initialRight = width - 10;
        initialBottom = initialTop + 100;
        rect = new RectF(intialLeft, initialTop, initialRight, initialBottom);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //mCanvas = new Canvas(mBitmap);

    }
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawRect(rect, mBitmapPaint);
        //canvas.drawPath(mPath, mPaint);
        invalidate();
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {

        mX = x;
        mY = y;
        Log.d("mx and my", mX + " " + mY);
    }
    private void touch_move(float x, float y) {
        float dx = x - mX;
        float dy = y - mY;
        //if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            rect.offset(dx, dy);
            mX = x;
            mY = y;
            Log.d("mx1 and my1", mX + " " + mY);
        //}
    }
    private void touch_up(float x, float y) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }
}
