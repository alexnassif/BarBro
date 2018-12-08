package com.alexnassif.mobile.barbro;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by mobile on 4/28/17.
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

        int x = 0, y = 50;
        for (String line: mText.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += 45;
        }


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
