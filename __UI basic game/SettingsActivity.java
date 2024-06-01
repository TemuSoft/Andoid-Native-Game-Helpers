package com.earthdefender.kaivse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private ImageView back;
    private Button sound_on, sound_off, music_on, music_off, vibration_on, vibration_off;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute;
    private boolean onVibrating;
    private String lang;
    private int volume_speed = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("e4thde7der", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isMute = sharedPreferences.getBoolean("isMute", false);
        soundMute = sharedPreferences.getBoolean("soundMute", false);
        onVibrating = sharedPreferences.getBoolean("onVibrating", false);
        lang = sharedPreferences.getString("lang", "");


        back = (ImageView) findViewById(R.id.back);


        sound_on = (Button) findViewById(R.id.sound_on);
        sound_off = (Button) findViewById(R.id.sound_off);
        music_on = (Button) findViewById(R.id.music_on);
        music_off = (Button) findViewById(R.id.music_off);
        vibration_on = (Button) findViewById(R.id.vibration_on);
        vibration_off = (Button) findViewById(R.id.vibration_off);

        if (!isMute){
            music_off.setAlpha(0.3F);
            music_off.setBackgroundResource(R.drawable.off);
            music_off.setTextColor(getResources().getColor(R.color.white));

            music_on.setAlpha(1F);
            music_on.setBackgroundResource(R.drawable.on);
            music_on.setTextColor(getResources().getColor(R.color.green));
        }else {
            music_on.setAlpha(0.3F);
            music_on.setBackgroundResource(R.drawable.off);
            music_on.setTextColor(getResources().getColor(R.color.white));

            music_off.setAlpha(1F);
            music_off.setBackgroundResource(R.drawable.on);
            music_off.setTextColor(getResources().getColor(R.color.green));
        }

        if (!soundMute){
            sound_off.setAlpha(0.3F);
            sound_off.setBackgroundResource(R.drawable.off);
            sound_off.setTextColor(getResources().getColor(R.color.white));

            sound_on.setAlpha(1F);
            sound_on.setBackgroundResource(R.drawable.on);
            sound_on.setTextColor(getResources().getColor(R.color.green));
        }else {
            sound_on.setAlpha(0.3F);
            sound_on.setBackgroundResource(R.drawable.off);
            sound_on.setTextColor(getResources().getColor(R.color.white));

            sound_off.setAlpha(1F);
            sound_off.setBackgroundResource(R.drawable.on);
            sound_off.setTextColor(getResources().getColor(R.color.green));
        }

        if (onVibrating){
            vibration_off.setAlpha(0.3F);
            vibration_off.setBackgroundResource(R.drawable.off);
            vibration_off.setTextColor(getResources().getColor(R.color.white));

            vibration_on.setAlpha(1F);
            vibration_on.setBackgroundResource(R.drawable.on);
            vibration_on.setTextColor(getResources().getColor(R.color.green));
        }else {
            vibration_on.setAlpha(0.3F);
            vibration_on.setBackgroundResource(R.drawable.off);
            vibration_on.setTextColor(getResources().getColor(R.color.white));

            vibration_off.setAlpha(1F);
            vibration_off.setBackgroundResource(R.drawable.on);
            vibration_off.setTextColor(getResources().getColor(R.color.green));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        music_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                if (isMute){
                    isMute = false;
                    music_off.setAlpha(0.3F);
                    music_off.setBackgroundResource(R.drawable.off);
                    music_off.setTextColor(getResources().getColor(R.color.white));

                    music_on.setAlpha(1F);
                    music_on.setBackgroundResource(R.drawable.on);
                    music_on.setTextColor(getResources().getColor(R.color.green));

                    Player.all_screens.start();

                    editor.putBoolean("isMute", isMute);
                    editor.apply();
                }
            }
        });

        music_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                if (!isMute){
                    isMute = true;

                    music_on.setAlpha(0.3F);
                    music_on.setBackgroundResource(R.drawable.off);
                    music_on.setTextColor(getResources().getColor(R.color.white));

                    music_off.setAlpha(1F);
                    music_off.setBackgroundResource(R.drawable.on);
                    music_off.setTextColor(getResources().getColor(R.color.green));

                    Player.StopAll();

                    editor.putBoolean("isMute", isMute);
                    editor.apply();
                }
            }
        });

        sound_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                if (soundMute){
                    soundMute = false;
                    sound_off.setAlpha(0.3F);
                    sound_off.setBackgroundResource(R.drawable.off);
                    sound_off.setTextColor(getResources().getColor(R.color.white));

                    sound_on.setAlpha(1F);
                    sound_on.setBackgroundResource(R.drawable.on);
                    sound_on.setTextColor(getResources().getColor(R.color.green));

                    editor.putBoolean("soundMute", soundMute);
                    editor.apply();
                }
            }
        });

        sound_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);
                if (!soundMute){
                    soundMute = true;

                    sound_on.setAlpha(0.3F);
                    sound_on.setBackgroundResource(R.drawable.off);
                    sound_on.setTextColor(getResources().getColor(R.color.white));

                    sound_off.setAlpha(1F);
                    sound_off.setBackgroundResource(R.drawable.on);
                    sound_off.setTextColor(getResources().getColor(R.color.green));

                    editor.putBoolean("soundMute", soundMute);
                    editor.apply();
                }
            }
        });

        vibration_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);

                if (!onVibrating){
                    vibration_off.setAlpha(0.3F);
                    vibration_off.setBackgroundResource(R.drawable.off);
                    vibration_off.setTextColor(getResources().getColor(R.color.white));

                    vibration_on.setAlpha(1F);
                    vibration_on.setBackgroundResource(R.drawable.on);
                    vibration_on.setTextColor(getResources().getColor(R.color.green));

                    onVibrating = true;
                    Player.vibrate(SettingsActivity.this, onVibrating, 500);
                    editor.putBoolean("onVibrating", onVibrating);
                    editor.apply();
                }
            }
        });

        vibration_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute);

                if (onVibrating){
                    vibration_on.setAlpha(0.3F);
                    vibration_on.setBackgroundResource(R.drawable.off);
                    vibration_on.setTextColor(getResources().getColor(R.color.white));

                    vibration_off.setAlpha(1F);
                    vibration_off.setBackgroundResource(R.drawable.on);
                    vibration_off.setTextColor(getResources().getColor(R.color.green));

                    onVibrating = false;
                    editor.putBoolean("onVibrating", onVibrating);
                    editor.apply();
                }
            }
        });

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