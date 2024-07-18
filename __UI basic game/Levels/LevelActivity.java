package com.cccol0risticssoverr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    private Button menu;
    private LinearLayout level_layouts;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute;
    private String lang;
    private int lastLevelActive, playLevel;
    private LayoutInflater inflate;
    private ArrayList<ArrayList<ImageView>> stars_levels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("ma5oofwderl0d", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        lang = sharedPreferences.getString("lang", "");
        lastLevelActive = sharedPreferences.getInt("lastLevelActive", 1);
        playLevel = sharedPreferences.getInt("playLevel", 1);

        setContentView(R.layout.activity_level);

        menu = (Button) findViewById(R.id.menu);
        inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        level_layouts = (LinearLayout) findViewById(R.id.level_layout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.button(soundMute);
                finish();
            }
        });

        load_UI_view();
    }


    private void load_UI_view() {
        level_layouts.removeAllViews();

        int counter = 1;
        for (int i = 0; i < 2; i++) {
            View horizontal = inflate.inflate(R.layout.horizontal, null);
            LinearLayout layout_horizontal = (LinearLayout) horizontal.findViewById(R.id.layout_horizontal);

            for (int j = 0; j < 5; j++) {
                View level_card = inflate.inflate(R.layout.level, null);
                Button level = (Button) level_card.findViewById(R.id.level);

                if (counter <= lastLevelActive){
                    level.setText(counter + "");
                    level.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Player.button(soundMute);

                            saveSelectedLevel(counter);
                        }
                    });
                }else {
                    level.setText("");
                    level.setBackgroundResource(R.drawable.lock);
                }

                layout_horizontal.addView(level_card);
            }

            level_layouts.addView(horizontal);
        }
    }


    private void saveSelectedLevel(int i) {
        editor.putInt("playLevel", i);
        editor.apply();
        intent = new Intent(LevelActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isMute)
            Player.all_screens.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isMute)
            Player.all_screens.start();
    }

    @Override
    public void onBackPressed() {
        return;
    }

}