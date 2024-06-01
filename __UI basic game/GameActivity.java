package com.icecreamninja548;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener{
    private ImageView pause;
    private TextView score_v;
    private LinearLayout layout_canvas;
    private LayoutInflater inflate;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute;
    private Intent intent;
    private String lang;
    private AlertDialog.Builder builder;
    private Random random;
    private Handler handler;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("smashp78ucks489", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        lang = sharedPreferences.getString("lang", "");

        setContentView(R.layout.activity_game);

        builder = new AlertDialog.Builder(this);
        random = new Random();
        handler = new Handler();

        score_v = (TextView) findViewById(R.id.score);
        pause = (ImageView) findViewById(R.id.pause);

        layout_canvas = (LinearLayout) findViewById(R.id.layout_canvas);
        inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.button(soundMute);
                pauseDialog();
            }
        });

        layout_canvas.removeAllViews();
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        int w = point.x;
        int h = point.y;
        gameView = new GameView (this, w, h, getResources(), 0);
        gameView.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        layout_canvas.addView(gameView);

        layout_canvas.setOnTouchListener(this);
        reloading_UI();
    }


    private void reloading_UI(){
        Runnable r = new Runnable() {
            public void run() {
                if (gameView.isPlaying){
                    gameView.update();

                    score_v.setText(gameView.score + "");
                    reloading_UI();
                }
            }
        };
        handler.postDelayed(r, 20);
    }

    public void pauseDialog() {
        gameView.isPlaying = false;
        LayoutInflater inflater = LayoutInflater.from(this);
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
            public void onClick(View v) {
                Player.button(soundMute);
                alertDialog.dismiss();
                gameView.isPlaying = true;
                reloading_UI();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                Intent intent = new Intent(GameActivity.this, LoadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
	
	
    private void game_over(){
        pause();
        editor.putInt("score", score);
        editor.apply();
        save_best_score();

        Player.game_over(activity, sharedPreferences);
    }

    private void save_best_score() {
        if (sharedPreferences.getInt("score", 0) > sharedPreferences.getInt("best_score", 0)){
            editor.putInt("best_score", sharedPreferences.getInt("score", 0));
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.isPlaying = false;
        if (!isMute)
            Player.all_screens.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.isPlaying = true;
        isMute = sharedPreferences.getBoolean("isMute", false);
        if (!isMute)
            Player.all_screens.start();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        return;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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

    private void processActionDown(int x, int y) {

    }

    private void processActionUp(int xp, int yp) {
        Rect clicked = new Rect(xp, yp, xp ,yp);

    }

    private void processActionMove(int x, int y) {

    }
}