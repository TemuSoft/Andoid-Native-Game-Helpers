package com.epicaaaoverrdventuressss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity{
    private TextView loading;
    private SharedPreferences sharedPreferences;
    private LinearLayout loading_main, loading_child;
    private int angle = 0;
    private Handler handler;
    private int width, speed, new_width;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("eaverr5dtssss", MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        handler = new Handler();

        loading = (TextView) findViewById(R.id.loading);
        loading_main = (LinearLayout) findViewById(R.id.loading_main);
        loading_child = (LinearLayout) findViewById(R.id.loading_child);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        width = point.x * 1 / 3;
        speed = width / 100;

        // Set the width of the LinearLayout
        ViewGroup.LayoutParams params = loading_main.getLayoutParams();
        params.width = width; // Set the width programmatically
        loading_main.setLayoutParams(params);

        rotate_loading_UI();
    }

    private void rotate_loading_UI(){
        Runnable r = new Runnable() {
            public void run() {
                new_width += speed;
                counter ++;

                int ttt = counter / 10;

                if (ttt % 4 == 3)
                    loading.setText(getResources().getString(R.string.loading) + "...");
                else if (ttt % 4 == 2)
                    loading.setText(getResources().getString(R.string.loading) + "..");
                else if (ttt % 4 == 1)
                    loading.setText(getResources().getString(R.string.loading) + ".");
                else
                    loading.setText(getResources().getString(R.string.loading) + "");

                // Set the width of the LinearLayout
                ViewGroup.LayoutParams params = loading_child.getLayoutParams();
                params.width = new_width; // Set the width programmatically
                loading_child.setLayoutParams(params);

                if (new_width > width){
                    Intent intent = new Intent(MainActivity.this, LoadActivity.class);
                    startActivity(intent);
                    finish();
                }else
                    rotate_loading_UI();
            }
        };
        handler.postDelayed(r, 30);

    }

}