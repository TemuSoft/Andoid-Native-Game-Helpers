package com.n1mble.t0ad;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class GradientDrawer {
    public static void drawGradient(Canvas canvas, int startX, int startY, int endX, int endY, int startColor, int endColor){
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(startX, startY, endX, endY, startColor, endColor, Shader.TileMode.CLAMP));
        canvas.drawRect(startX, startY, endX, endY, paint);
    }
}
