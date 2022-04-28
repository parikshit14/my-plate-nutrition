package com.huskies.developer.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huskies.developer.MainActivity;
import com.huskies.developer.R;
import com.huskies.developer.listeners.ToWindowOnClick;

public class MyMenuActivity extends AppCompatActivity {
    public static boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyMenuActivity.isOpen = true;
        setContentView(R.layout.activity_my_menu);
        findViewById(R.id.from_menu_to_diary_menu).setOnClickListener(new ToWindowOnClick(this, DiaryActivity.class));
        findViewById(R.id.from_menu_to_profile).setOnClickListener(new ToWindowOnClick(this, ProfileActivity.class));
        findViewById(R.id.from_menu_to_dishes).setOnClickListener(new ToWindowOnClick(this, DishActivity.class));
        findViewById(R.id.from_menu_to_exercises).setOnClickListener(new ToWindowOnClick(this, ExerciseActivity.class));
        findViewById(R.id.from_menu_to_statistic).setOnClickListener(new ToWindowOnClick(this, StatisticActivity.class));
        findViewById(R.id.scan).setOnClickListener(new ToWindowOnClick(this, MainActivity.class));
    }

    public void onExitButton(final View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }
}
