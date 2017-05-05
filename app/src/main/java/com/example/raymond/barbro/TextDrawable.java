package com.example.raymond.barbro;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by raymond on 4/28/17.
 */

public class TextDrawable extends Drawable {
    private String mText;
    private Paint paint;

    public TextDrawable(String text){
        mText = text;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40f);

    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(mText, 50, 50, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
