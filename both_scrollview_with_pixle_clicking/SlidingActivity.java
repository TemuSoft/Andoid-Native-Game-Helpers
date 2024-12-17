package com.bat.city.ovr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    private LineView lineView;
    private FrameLayout map_container;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute;
    private String lang;
    private int available_coin;
    private int playLevel, lastLevelActive, active_player;
    private ArrayList<ArrayList<Integer>> levelCoordinates = new ArrayList<>();
    private int players[] = new int[]{
            R.drawable.player_0,
            R.drawable.player_1,
            R.drawable.player_2,
            R.drawable.player_3,
            R.drawable.player_4,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level);

        sharedPreferences = getSharedPreferences("m0ba45ity0", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        lang = sharedPreferences.getString("lang", "");
        available_coin = sharedPreferences.getInt("available_coin", 0);
        lastLevelActive = sharedPreferences.getInt("lastLevelActive", 1);
        playLevel = sharedPreferences.getInt("playLevel", 1);
        active_player = sharedPreferences.getInt("active_player", 0);

        lineView = new LineView(this, null);
        map_container = findViewById(R.id.map_container);
        map_container.addView(lineView, 0);
        lineView.setVisibility(View.VISIBLE);

        map_container.post(() -> {

            init_name_point();
            load_UI();
        });

    }

    private void init_name_point() {
        ArrayList<Integer> data = new ArrayList<>();
        data.add(calculate_right_position(1, 3)[0]);
        data.add(calculate_right_position(1, 3)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(2, 2)[0]);
        data.add(calculate_right_position(2, 2)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(3, 1)[0]);
        data.add(calculate_right_position(3, 1)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(4, 2)[0]);
        data.add(calculate_right_position(4, 2)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(3, 3)[0]);
        data.add(calculate_right_position(3, 3)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(2, 4)[0]);
        data.add(calculate_right_position(2, 4)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(1, 5)[0]);
        data.add(calculate_right_position(1, 5)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(2, 6)[0]);
        data.add(calculate_right_position(2, 6)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(3, 5)[0]);
        data.add(calculate_right_position(3, 5)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(4, 4)[0]);
        data.add(calculate_right_position(4, 4)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(5, 3)[0]);
        data.add(calculate_right_position(5, 3)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(6, 2)[0]);
        data.add(calculate_right_position(6, 2)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(7, 3)[0]);
        data.add(calculate_right_position(7, 3)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(6, 4)[0]);
        data.add(calculate_right_position(6, 4)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(5, 5)[0]);
        data.add(calculate_right_position(5, 5)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(4, 6)[0]);
        data.add(calculate_right_position(4, 6)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(3, 7)[0]);
        data.add(calculate_right_position(3, 7)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(5, 7)[0]);
        data.add(calculate_right_position(5, 7)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(6, 6)[0]);
        data.add(calculate_right_position(6, 6)[1]);
        levelCoordinates.add(data);

        data = new ArrayList<>();
        data.add(calculate_right_position(7, 5)[0]);
        data.add(calculate_right_position(7, 5)[1]);
        levelCoordinates.add(data);
    }

    private int[] calculate_right_position(int x, int y){
        int renderedWidth = map_container.getWidth();
        int renderedHeight = map_container.getHeight();
        int originalWidth = 1920;
        int originalHeight = 1494;
        int w = 1920 / 8;
        int h = 1494 / 8;
        float scaleX = (float) renderedWidth / originalWidth;
        float scaleY = (float) renderedHeight / originalHeight;
        int newX = (int) (x * w * scaleX);
        int newY = (int) (y * h * scaleY);

        return new int[]{newX, newY};
    }

    private void load_UI() {
        Context contextWithTheme = new ContextThemeWrapper(this, R.style.text0Theme);
        for (int i = 0; i < levelCoordinates.size(); i++) {
            int levelNumber = i + 1;

            Button level = new Button(contextWithTheme);
            level.setLayoutParams(new FrameLayout.LayoutParams(120, 120));
            level.setX(levelCoordinates.get(i).get(0));
            level.setY(levelCoordinates.get(i).get(1));
            level.setText("" + levelNumber);
            level.setPadding(0, 0, 0, 15);
            if (levelNumber <= lastLevelActive) {
                level.setBackgroundResource(R.drawable.level);
            }else {
                level.setBackgroundResource(R.drawable.locked);
                level.setEnabled(false);
            }

            if (levelNumber == playLevel){
                ImageView player = new ImageView(this);
                player.setLayoutParams(new FrameLayout.LayoutParams(300, 300));
                player.setX(levelCoordinates.get(i).get(0) - 150);
                player.setY(levelCoordinates.get(i).get(1) - 300);
                player.setImageResource(players[active_player]);
                map_container.addView(player);
            }

            level.setOnClickListener(v -> {
                Toast.makeText(LevelActivity.this, "Clicked on Level " + levelNumber, Toast.LENGTH_SHORT).show();
            });

            map_container.addView(level);

            if (i < levelCoordinates.size() - 1) {
                int nextX = levelCoordinates.get(i + 1).get(0);
                int nextY = levelCoordinates.get(i + 1).get(1);
                lineView.addLine(
                        levelCoordinates.get(i).get(0) + 60,
                        levelCoordinates.get(i).get(1) + 60,
                        nextX + 60,
                        nextY + 60
                );
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }
}