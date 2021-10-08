package com.geekbrains.note.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.geekbrains.note.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NotesListFragment())
                .commit();
    }
}