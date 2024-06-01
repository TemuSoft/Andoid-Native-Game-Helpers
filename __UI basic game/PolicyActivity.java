package com.memofootball.memomem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PolicyActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isMute, soundMute, onVibrating;
    private WebView policy_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_policy);

        sharedPreferences = getSharedPreferences("nowayout78mice", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isMute = sharedPreferences.getBoolean("isMute", false);
        onVibrating = sharedPreferences.getBoolean("onVibrating", true);
        soundMute = sharedPreferences.getBoolean("soundMute", false);

        policy_view = (WebView) findViewById(R.id.policy_view);
        policy_view.loadUrl("https://doc-hosting.flycricket.io/memofootball-privacy-policy/8078529d-de2b-4887-a731-5c69dba90316/privacy");
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
}