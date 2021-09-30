package com.geekbrains.note.domain;

import androidx.annotation.Nullable;

import java.util.List;

/*** Интерфейс для работы с заметками
 * описывает принципы работы с ними
 * реализует принцип CRUD
 */
public interface NotesRepo {
    List<NoteEntity> getNotes();
    @Nullable
    Integer createNote(NoteEntity note);
    boolean deleteNote(int id);
    boolean updateNote(int id, NoteEntity note);
}
