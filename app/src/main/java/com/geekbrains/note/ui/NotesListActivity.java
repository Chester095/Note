package com.geekbrains.note.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;
import com.geekbrains.note.impl.NotesRepoImpl;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NotesRepo notesRepo = new NotesRepoImpl();
    private NotesAdapter adapter = new NotesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        fillRepoByTestValues();

        initToolbar();
        initRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_note_menu) {
            openNoteScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void openNoteScreen() {
        Intent intent = new Intent(this, NoteEditActivity.class);
        startActivity(intent);
    }

    private void fillRepoByTestValues() {
        notesRepo.createNote(new NoteEntity("Заметка 1", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 2", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 3", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 4", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 5", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 6", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 7", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 8", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 9", "какой-то длинный текст очень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 10", "какой-то длинный текст очень тывалодлывоапо"));
    }
}