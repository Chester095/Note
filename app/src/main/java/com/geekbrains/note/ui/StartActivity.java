package com.geekbrains.note.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.geekbrains.note.R;

public class StartActivity extends AppCompatActivity {
    public static final String LOG_TAG = "@@@";
    public static boolean firstOnCreate=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)  firstOnCreate = false;
        Log.d(LOG_TAG, "onCreateStartActivity.   savedInstanceState = " + savedInstanceState);
        setContentView(R.layout.activity_start);
        if (checkOrientation()) initLandscapeOrientation();
        else initPortraitOrientation();

    }

    private void initLandscapeOrientation() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NotesListFragment())
                .replace(R.id.fragment_container_note_edit, new NoteEditFragment())
                .commit();
    }

    private void initPortraitOrientation() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NotesListFragment())
                .commit();
    }

    private boolean checkOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}