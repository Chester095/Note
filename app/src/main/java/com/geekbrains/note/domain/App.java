package com.geekbrains.note.domain;

import android.app.Application;

import com.geekbrains.note.impl.NotesRepoImpl;

public class App extends Application {
    private NotesRepo notesRepo = new NotesRepoImpl();

    public NotesRepo getNotesRepo(){
        return notesRepo;
    }
}
