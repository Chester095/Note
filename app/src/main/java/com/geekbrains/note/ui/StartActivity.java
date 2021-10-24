package com.geekbrains.note.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.geekbrains.note.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {
    public static final String LOG_TAG = "@@@";
    private BottomNavigationView bottomNavigationView;

    private final Map<Integer, Fragment> fragmentsMap = createFragments();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "StartActivity onCreate");
        setContentView(R.layout.activity_start);
        if (checkOrientation())
            initLandscapeOrientation();
        else {
            initPortraitOrientation();
            initBottomNavigation();
        }

    }


    private void initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_main, fragmentsMap.get(item.getItemId()))
                    .commit();

            return true;
        });
    }

    private Map<Integer, Fragment> createFragments() {
        Map<Integer, Fragment> fragmentsMap = new HashMap<>();
        fragmentsMap.put(R.id.menu_item_main, new NotesListFragment());
        fragmentsMap.put(R.id.menu_item_save_file, new SaveToFileFragment());
        fragmentsMap.put(R.id.menu_item_settings, new SettingsFragment());
        return fragmentsMap;
    }

    private void initLandscapeOrientation() {
        Log.d(LOG_TAG, "StartActivity.   initLandscapeOrientation");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, fragmentsMap.get(R.id.menu_item_main))
                .replace(R.id.fragment_container_note_edit, new NoteEditFragment())
                .commit();
    }

    private void initPortraitOrientation() {

        Log.d(LOG_TAG, "StartActivity.   initPortraitOrientation");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main,  fragmentsMap.get(R.id.menu_item_main))
                .commit();
    }

    private boolean checkOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}