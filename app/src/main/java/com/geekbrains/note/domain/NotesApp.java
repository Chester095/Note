package com.geekbrains.note.domain;

import android.app.Application;

import com.geekbrains.note.impl.NotesRepoImpl;

public class NotesApp extends Application {

    private final NotesRepoImpl notesRepo = new NotesRepoImpl();

    public NotesRepoImpl getAppNotesRepo(){
        return notesRepo;
    }
}
