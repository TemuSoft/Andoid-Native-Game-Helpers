package com.isitfootbal.osetr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class Player {

    private static SharedPreferences sharedPreferences;
	private static boolean onVibrating;
    private static Vibrator v;
    private static LayoutInflater inflate;
    private static AlertDialog.Builder builder;
    public static MediaPlayer all_screens, button, music;

    public static void all_screens(Context context, int audio) {
        all_screens = MediaPlayer.create(context, audio);
        all_screens.setLooping(true);
    }

    public static void button(Context context, int audio) {
        button = MediaPlayer.create(context, audio);
        button.setLooping(false);
    }

    public static void music(Context context, int audio) {
        music = MediaPlayer.create(context, audio);
        music.setLooping(false);
    }


    public static void StopAll() {
        try {
            all_screens.pause();
            button.pause();
            music.pause();
        } catch (Exception e) {

        }
    }

    public static void button(boolean soundMute) {
        if (!soundMute)
            button.start();
    }

    public static void changeLanguage(Activity activity, SharedPreferences.Editor editor, String lang, boolean reload) {
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(lang);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, null);

        editor.putString("lang", lang);
        editor.apply();

        if (reload) {
            Intent intent = new Intent(activity, activity.getClass());
            activity.startActivity(intent);
            activity.finish();
        }
    }


    public static void vibrate(Activity activity, boolean onVibrating, int duration){
        v = (Vibrator) activity.getSystemService(activity.VIBRATOR_SERVICE);
        if (onVibrating) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(duration);
            }
        }
    }

    public static void settingDialog(Activity activity, SharedPreferences sharedPreferences) {
        builder = new AlertDialog.Builder(activity);

        final SharedPreferences.Editor[] editor = {sharedPreferences.edit()};
        final String[] lang = {sharedPreferences.getString("lang", "")};
        final boolean[] isMute = {sharedPreferences.getBoolean("isMute", false)};
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.setting, null);
        builder.setView(view);

        final ImageView sound = (ImageView) view.findViewById(R.id.sound);
        final ImageView music = (ImageView) view.findViewById(R.id.music);
        final Button language = (Button) view.findViewById(R.id.language);
        final Button back = (Button) view.findViewById(R.id.back);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        if (isMute[0])
            music.setImageResource(R.drawable.music_off);
        else
            music.setImageResource(R.drawable.music_on);

        if (soundMute[0])
            sound.setImageResource(R.drawable.sound_off);
        else
            sound.setImageResource(R.drawable.sound_on);

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                soundMute[0] = !soundMute[0];
                if (soundMute[0])
                    sound.setImageResource(R.drawable.sound_off);
                else
                    sound.setImageResource(R.drawable.sound_on);

                editor[0].putBoolean("soundMute", soundMute[0]);
                editor[0].apply();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.button(soundMute[0]);
                isMute[0] = !isMute[0];
                if (isMute[0]) {
                    music.setImageResource(R.drawable.music_off);
                    Player.StopAll();
                } else {
                    music.setImageResource(R.drawable.music_on);
                    Player.all_screens.start();
                }

                editor[0].putBoolean("isMute", isMute[0]);
                editor[0].apply();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                if (lang[0].equals("ru"))
                    lang[0] = "";
                else if (lang[0].equals("br"))
                    lang[0] = "ru";
                else
                    lang[0] = "br";

                Player.changeLanguage(activity, editor[0], lang[0], true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                alertDialog.dismiss();
            }
        });
    }

    public static void how_to_play_dialog(Activity activity, SharedPreferences sharedPreferences) {
        builder = new AlertDialog.Builder(activity);
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.questions, null);
        builder.setView(view);

        final Button back = (Button) view.findViewById(R.id.back);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                alertDialog.dismiss();
            }
        });
    }

    public static void game_over(Activity activity, SharedPreferences sharedPreferences) {
        builder = new AlertDialog.Builder(activity);
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.game_over, null);
        builder.setView(view);

        final Button restart = (Button) view.findViewById(R.id.restart);
        final Button home = (Button) view.findViewById(R.id.back);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                Intent intent = new Intent(activity, GameActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                Intent intent = new Intent(activity, LoadActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public static void game_done(Activity activity, SharedPreferences sharedPreferences) {
        builder = new AlertDialog.Builder(activity);
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.game_done, null);
        builder.setView(view);

        final Button next = (Button) view.findViewById(R.id.next);
        final Button home = (Button) view.findViewById(R.id.back);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                Intent intent = new Intent(activity, GameActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                Intent intent = new Intent(activity, LoadActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public static void levelDialog(Activity activity, SharedPreferences sharedPreferences) {
        builder = new AlertDialog.Builder(activity);

        final SharedPreferences.Editor[] editor = {sharedPreferences.edit()};
        final int lastLevelActive = sharedPreferences.getInt("lastLevelActive", 1);
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.levels, null);
        builder.setView(view);

        final Button level_1 = (Button) view.findViewById(R.id.level_1);
        final Button level_2 = (Button) view.findViewById(R.id.level_2);
        final Button level_3 = (Button) view.findViewById(R.id.level_3);
        final Button level_4 = (Button) view.findViewById(R.id.level_4);
        final Button level_5 = (Button) view.findViewById(R.id.level_5);
        final Button level_6 = (Button) view.findViewById(R.id.level_6);
        final Button level_7 = (Button) view.findViewById(R.id.level_7);
        final Button level_8 = (Button) view.findViewById(R.id.level_8);
        final Button level_9 = (Button) view.findViewById(R.id.level_9);
        final ArrayList<Button> levels = new ArrayList<>();
        levels.add(level_1);
        levels.add(level_2);
        levels.add(level_3);
        levels.add(level_4);
        levels.add(level_5);
        levels.add(level_6);
        levels.add(level_7);
        levels.add(level_8);
        levels.add(level_9);


        final Button back = (Button) view.findViewById(R.id.back);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        for (int i = 0; i < levels.size(); i++) {
            int finalI = i;
            levels.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSelectedLevel(finalI + 1, activity, sharedPreferences.edit());
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.button(soundMute[0]);
                alertDialog.dismiss();
            }
        });


        for (int i = 0; i < lastLevelActive; i++) {
            levels.get(i).setEnabled(true);
            levels.get(i).setBackgroundResource(R.drawable.on);
            levels.get(i).setTextColor(activity.getResources().getColor(R.color.gray));
        }
    }

    private static void saveSelectedLevel(int i, Activity activity, SharedPreferences.Editor editor) {
        editor.putInt("playLevel", i);
        editor.apply();
        Intent intent = new Intent(activity, GameActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
	
	
    public static void leadboardDialog(Activity activity, SharedPreferences sharedPreferences, int score) {
        builder = new AlertDialog.Builder(activity);
        inflate = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        final SharedPreferences.Editor[] editor = {sharedPreferences.edit()};
        final boolean[] soundMute = {sharedPreferences.getBoolean("soundMute", false)};

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.leader_board, null);
        builder.setView(view);

        final LinearLayout layout_vertical_main = (LinearLayout) view.findViewById(R.id.layout_vertical_main);
        final EditText name_temp = (EditText) view.findViewById(R.id.name_temp);
        final TextView score_t = (TextView) view.findViewById(R.id.score_v);
        final Button restart = (Button) view.findViewById(R.id.restart);
        final Button menu = (Button) view.findViewById(R.id.menu);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Log.e("", score + "");
        score_t.setText(score + "");

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button(soundMute[0]);
                String name_input = name_temp.getText().toString();

                for (int i = 0; i < 10; i++) {
                    String n = sharedPreferences.getString("name_" + i, "");
                    int s = sharedPreferences.getInt("score_" + i, -1);

                    if (score > s && !name_input.equals("")){
                        editor[0].putString("name_" + i, name_input);
                        editor[0].putInt("score_" + i, score);

                        int last = 9;

                        while (i < last){
                            editor[0].putString("name_" + last, sharedPreferences.getString("name_" + (last - 1), ""));
                            editor[0].putInt("score_" + last, sharedPreferences.getInt("score_" + (last - 1), -1));
                            last --;
                        }

                        editor[0].apply();
                        break;
                    }
                }

                Intent intent = new Intent(activity, activity.getClass());
                activity.startActivity(intent);
                activity.finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button(soundMute[0]);
                String name_input = name_temp.getText().toString();

                for (int i = 0; i < 10; i++) {
                    String n = sharedPreferences.getString("name_" + i, "");
                    int s = sharedPreferences.getInt("score_" + i, -1);

                    if (score > s && !name_input.equals("")){
                        editor[0].putString("name_" + i, name_input);
                        editor[0].putInt("score_" + i, score);

                        int last = 9;

                        while (i < last){
                            editor[0].putString("name_" + last, sharedPreferences.getString("name_" + (last - 1), ""));
                            editor[0].putInt("score_" + last, sharedPreferences.getInt("score_" + (last - 1), -1));
                            last --;
                        }

                        editor[0].apply();
                        break;
                    }
                }

                Intent intent = new Intent(activity, LoadActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        load_view(layout_vertical_main, sharedPreferences);
    }

    private static void load_view(LinearLayout layout_vertical_main, SharedPreferences sharedPreferences) {
        layout_vertical_main.removeAllViews();
        for (int i = 0; i < 10; i++) {
            View leader_board = inflate.inflate(R.layout.leader_board_one, null);
            final TextView name = (TextView) leader_board.findViewById(R.id.name);
            final TextView score = (TextView) leader_board.findViewById(R.id.score);

            String n = sharedPreferences.getString("name_" + i, "");
            int s = sharedPreferences.getInt("score_" + i, -1);

            if (s != -1) {
                name.setText(n);
                score.setText(s + "");

                layout_vertical_main.addView(leader_board);
            }else
                break;
        }
    }

}