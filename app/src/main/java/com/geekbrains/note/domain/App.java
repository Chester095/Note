package com.geekbrains.note.domain;

import android.app.Application;

import com.geekbrains.note.impl.NotesRepoImpl;

public class App extends Application {
    private NotesRepoImpl notesRepo = new NotesRepoImpl();

    public NotesRepoImpl getNotesRepo(){
        return notesRepo;
    }
}
