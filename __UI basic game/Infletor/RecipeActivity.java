package com.ttaastymmasterchefovver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    private ImageView close;
    private LinearLayout layout_vertical;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute;
    private boolean onVibrating;
    private String lang;
    private LayoutInflater inflate;
    private ArrayList<ImageView> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("ast6masterchef", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        onVibrating = sharedPreferences.getBoolean("onVibrating", false);
        lang = sharedPreferences.getString("lang", "");

        setContentView(R.layout.activity_recipe);

        inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        close = (ImageView) findViewById(R.id.close);

        layout_vertical = (LinearLayout) findViewById(R.id.layout_vertical);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                finish();
            }
        });

        process_favorite();
        for (int i = 0; i < recipes.size(); i++)
            recipes.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Player.button(soundMute);
                }
            });
    }

    private void process_favorite() {
        layout_vertical.removeAllViews();

        for (int i = 0; i < 5; i++) {
            View horizontal = inflate.inflate(R.layout.horizontal, null);
            LinearLayout horizontal_layout = (LinearLayout) horizontal.findViewById(R.id.layout_horizontal);
            for (int j = 0; j < 3; j++) {
                View recipe_card = inflate.inflate(R.layout.recipe_card, null);
                ImageView recipe = (ImageView) recipe_card.findViewById(R.id.recipe);

                recipes.add(recipe);
                horizontal_layout.addView(recipe_card);
            }

            layout_vertical.addView(horizontal);
        }
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