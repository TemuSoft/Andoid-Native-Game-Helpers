package com.luckypuppyea.ter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Background {
    Random random;
    Bitmap background, pause;

    int screenX, screenY;
    int pX, pY, pWidth, pHeight;

    ArrayList<Bitmap> food = new ArrayList<>();
    ArrayList<Bitmap> dog = new ArrayList<>();

    Background(GameActivity activity, int scrX, int scrY, Resources res) {
        random = new Random();
        screenX = scrX;
        screenY = scrY;

        random = new Random();

        background = BitmapFactory.decodeResource(res, R.drawable.bg_play);
        pause = BitmapFactory.decodeResource(res, R.drawable.pause);

        pWidth = pause.getWidth();
        pHeight = pWidth;
        pX = screenX - pWidth - pWidth / 3;
        pY = pWidth / 3;

        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        pause = Bitmap.createScaledBitmap(pause, pWidth, pHeight, false);

        for (int i = 0; i < 8; i++) {
            int f = activity.getResources().getIdentifier("food_" + i, "drawable", activity.getPackageName());
            food.add(BitmapFactory.decodeResource(res, f));
        }

        for (int i = 0; i < 4; i++) {
            int d = activity.getResources().getIdentifier("dog_" + i, "drawable", activity.getPackageName());
            dog.add(BitmapFactory.decodeResource(res, d));
        }
    }

    public Rect getPauseCollision(){
        int m = 0;
        return  new Rect(pX - m, pY - m, pX + pWidth + m, pY + pHeight + m);
    }
}
