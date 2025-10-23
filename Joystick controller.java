package com.CheeseAdventure.CA0710;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.Random;

public class GameView extends View{
    private SharedPreferences sharedPreferences;
    private int screenX, screenY;
    private Resources resources;
    private Random random;
    boolean isPlaying = true, game_over = false;
    long game_over_time;
    long game_start_time = System.currentTimeMillis(), game_pause_time;

    int game_score;
    private int xSpeed, ySpeed;
    private Context context;
    int CAT_CAUGHT_RAT = 0, FALLEN_KING_TRAP = 1, FALLEN_CAT_TRAP = 2;
    int game_over_reason = -1;

    Bitmap rat, cat;
    int cat_x, cat_y, cat_w, cat_h;
    int rat_x, rat_y, rat_w, rat_h;
    int active_rat;
    int joystick_radius, joystick_x, joystick_y;
    int move_w_h, move_x, move_y;
    boolean joystick_on_hold = false;
    float knob_x, knob_y;
    float last_dx = 0, last_dy = 0;
    float angle = 0;


    public GameView(Context mContext, int scX, int scY, Resources res, int level_amount) {
        super(mContext);
        screenX = scX;
        screenY = scY;
        resources = res;
        context = mContext;
        random = new Random();

        sharedPreferences = context.getSharedPreferences("tryMagi45PM071", context.MODE_PRIVATE);
        active_rat = sharedPreferences.getInt("active_rat", 0);

        int[] rats = new int[]{R.drawable.rat_0, R.drawable.rat_1, R.drawable.rat_2, R.drawable.rat_3};
        rat = BitmapFactory.decodeResource(res, rats[active_rat]);
        int w = rat.getWidth();
        int h = rat.getHeight();
        rat_w = screenX / 15;
        rat_h = h * rat_w / w;
        rat = Bitmap.createScaledBitmap(rat, rat_w, rat_h, false);

        joystick_radius = screenX / 10;
        joystick_x = screenX / 2 - joystick_radius;
        joystick_y = screenY - joystick_radius * 3;
        move_w_h = joystick_radius * 4;
        move_x = joystick_x - joystick_radius;
        move_y = joystick_y - joystick_radius;
        knob_x = joystick_x + joystick_radius;
        knob_y = joystick_y + joystick_radius;

        setSpeed();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.TRANSPARENT);

        // Draw joystick base
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(joystick_x + joystick_radius, joystick_y + joystick_radius, joystick_radius, paint);

        // Draw joystick knob
        paint.setColor(getResources().getColor(R.color.yellow));
        canvas.drawCircle(knob_x, knob_y, joystick_radius / 2, paint);

        // Draw rotated rat
        canvas.save();
        canvas.rotate(angle, rat_x + rat_w / 2f, rat_y + rat_h / 2f);
        canvas.drawBitmap(rat, rat_x, rat_y, paint);
        canvas.restore();
    }


    private void setSpeed() {
        xSpeed = screenX / 120;
        ySpeed = screenY / 120;
    }

    public void update() {
        if (joystick_on_hold) {
            float dx = knob_x - (joystick_x + joystick_radius);
            float dy = knob_y - (joystick_y + joystick_radius);
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance > joystick_radius * 0.2f) {
                float movementAngle = (float) Math.atan2(dy, dx);
                angle = (float) Math.toDegrees(movementAngle) + 90; // for drawing

                // Move in the direction of movementAngle (not angle)
                rat_x += Math.cos(movementAngle) * xSpeed;
                rat_y += Math.sin(movementAngle) * ySpeed;

                // Clamp to screen
                rat_x = Math.max(0, Math.min(rat_x, screenX - rat_w));
                rat_y = Math.max(0, Math.min(rat_y, screenY - rat_h));
            }
        }

        invalidate();
    }

    public Rect getJoystickCollision() {
        return new Rect(joystick_x, joystick_y, joystick_x + joystick_radius * 2, joystick_y + joystick_radius * 2);
    }
}



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameView.game_over) processActionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (gameView.joystick_on_hold) processActionMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (gameView.joystick_on_hold) processActionUp(x, y);
                break;
        }
        return true;
    }

    private void processActionDown(int x, int y) {
        Rect clicked = new Rect(x, y, x, y);

        if (Rect.intersects(clicked, gameView.getJoystickCollision())) {
            gameView.joystick_on_hold = true;
        }
    }

    private void processActionUp(int xp, int yp) {
        gameView.joystick_on_hold = false;
        gameView.knob_x = gameView.joystick_x + gameView.joystick_radius;
        gameView.knob_y = gameView.joystick_y + gameView.joystick_radius;
        gameView.last_dx = 0;
        gameView.last_dy = 0;
    }


    private void processActionMove(int x, int y) {
        float centerX = gameView.joystick_x + gameView.joystick_radius;
        float centerY = gameView.joystick_y + gameView.joystick_radius;

        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < gameView.joystick_radius) {
            gameView.knob_x = x;
            gameView.knob_y = y;
        } else {
            float angle = (float) Math.atan2(dy, dx);
            gameView.knob_x = centerX + gameView.joystick_radius * (float) Math.cos(angle);
            gameView.knob_y = centerY + gameView.joystick_radius * (float) Math.sin(angle);
        }

        gameView.last_dx = (gameView.knob_x - centerX) / gameView.joystick_radius;
        gameView.last_dy = (gameView.knob_y - centerY) / gameView.joystick_radius;
    }
