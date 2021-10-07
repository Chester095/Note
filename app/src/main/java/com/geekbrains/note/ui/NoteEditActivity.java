package com.geekbrains.note.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

public class NoteEditActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState, NoteEntity item) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_note_edit, new NoteEditFragment())
                .addToBackStack(null)
                .commit();
    }
}