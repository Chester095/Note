package com.geekbrains.note.impl;

import androidx.annotation.Nullable;

import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;

import java.util.ArrayList;
import java.util.List;

public class NotesRepoImpl implements NotesRepo {
    private final ArrayList<NoteEntity> cache = new ArrayList<>();
    private int counter = 0;


    @Override
    public List<NoteEntity> getNotes() {
        return new ArrayList<>(cache);
    }

    @Nullable
    @Override
    public Integer createNote(NoteEntity note) {
        int newId = ++counter;
        note.setId(newId);
        cache.add(note);
        return newId;
    }

    @Override
    public boolean deleteNote(int id) {
        for (int i = 0; i < cache.size(); i++) {
            if (id == cache.get(i).getId()) {
                cache.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateNote(int id, NoteEntity note) {
        deleteNote(id);
        note.setId(id);
        cache.add(note);
        return true;
    }
}
