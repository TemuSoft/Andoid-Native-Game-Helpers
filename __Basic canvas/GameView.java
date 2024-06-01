package com.ballshot628;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private int screenX, screenY = 0;
    private Paint paint;
    private GameActivity activity;
    private Background bg;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isPlaying = false;
    private boolean isMute, soundMute, onVibrating;
    private Random random;
    private int score = 0;
    private String lang;
    private int xSpeed, ySpeed;
    private AlertDialog.Builder builder;
    private boolean on_tap_started = false;
    private Vibrator v;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        builder = new AlertDialog.Builder(activity);

        v = (Vibrator) activity.getSystemService(activity.VIBRATOR_SERVICE);

        this.screenX = screenX;
        this.screenY = screenY;

        random = new Random();

        sharedPreferences = activity.getSharedPreferences("ba1lshot68", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        lang = sharedPreferences.getString("lang", "");
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        onVibrating = sharedPreferences.getBoolean("onVibrating", false);

        bg = new Background(activity,screenX, screenY, getResources());

        paint = new Paint();
        setSpeed();
    }

    @Override
    public void run() {
         while (isPlaying) {
             draw ();
             sleep ();
             update();
         }
    }

    private void vibrate() {

    }

    private void setSpeed() {
        if (score / 5 > 40){
            xSpeed = screenX / 60;
            ySpeed = screenY / 60;
        }else {
            xSpeed = screenX / (90 - score / 5);
            ySpeed = screenY / (90 - score / 5);
        }
    }

    private void update(){
        if (on_tap_started) {
            setSpeed();

        }
    }

    private void draw () {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            paint = new Paint();

            canvas.drawBitmap(bg.background, 0, 0, paint);
            canvas.drawBitmap(bg.pause, bg.pX, bg.pY, paint);

            paint.setTextSize(screenX / 7);
            paint.setColor(getResources().getColor(R.color.white));
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(null);
            paint.setStrokeWidth(12);
            canvas.drawText(score + "", bg.sX + bg.sWidth / 2, bg.sY + bg.sHeight * 3 / 5, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep () {
         try {
             Thread.sleep(20);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
    }

    public void resume () {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause () {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processActionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                processActionMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                processActionUp(x, y);
                break;
        }
        return true;
    }

    private void processActionDown(int x, int y){
        //Player.button(soundMute);
        if (!on_tap_started) {
            on_tap_started = true;
        }
    }

    private void processActionMove(int x, int y){

    }

    private void processActionUp(int x, int y) {
        Rect clicked = new Rect(x, y, x ,y);

        if (Rect.intersects(clicked, bg.getPauseCollision())){
            processPause();
        }else {
            processActionTap(x, y);
        }

    }

    private void processActionTap(int x, int y) {

    }

    private void processPause() {
        pause();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.pause, null);
        builder.setView(view);

        final Button resume = (Button) view.findViewById(R.id.resume);
        final Button menu = (Button) view.findViewById(R.id.menu);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                resume();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LoadActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    private void game_over(){
        pause();
        editor.putInt("score", score);
        editor.apply();

        Player.leadboardDialog(activity, sharedPreferences, score);
    }
}
