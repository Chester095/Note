package com.geekbrains.note.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geekbrains.note.R;

public class StartActivity extends AppCompatActivity {
    public static final String LOG_TAG = "@@@";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "StartActivity onCreate");
        setContentView(R.layout.activity_start);
        if (checkOrientation())
            initLandscapeOrientation();
        else
            initPortraitOrientation();
    }

    private void initLandscapeOrientation() {
        Log.d(LOG_TAG, "StartActivity.   initLandscapeOrientation");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NotesListFragment())
                .replace(R.id.fragment_container_note_edit, new NoteEditFragment())
                .commit();
    }

    private void initPortraitOrientation() {
        Log.d(LOG_TAG, "StartActivity.   initPortraitOrientation");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NotesListFragment())
                .commit();
    }

    private boolean checkOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}