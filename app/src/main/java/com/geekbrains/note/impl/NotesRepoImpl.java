package com.geekbrains.note.impl;

import androidx.annotation.Nullable;

import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;

import java.util.ArrayList;
import java.util.List;


/*** Реализация интерфейса NotesRepo (создание репозитория)
 * здесь нужно делать хранение данных
 */
public class NotesRepoImpl implements NotesRepo {
    private final ArrayList<NoteEntity> cache = new ArrayList<>(); // хранение данных
    private int counter = 0; // счётчик для списка cache


    /*** возращает данные
     *
     * @return
     */
    @Override
    public List<NoteEntity> getNotes() {
        return new ArrayList<>(cache);
    }

    /*** добавляем заметку в коллекцию
     *
     * @param note
     * @return newId - новый id
     */
    @Nullable
    @Override
    public Integer createNote(NoteEntity note) {
        int newId = ++counter;
        note.setId(newId);
        cache.add(note);
        return newId;
    }

    /*** Удаление конкретной заметки
     *
     * @param id
     * @return
     */
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

    /*** Обновляем конкретную заметку
     * Через удаление и создание заново
     * @param id
     * @param note
     * @return
     */
    @Override
    public boolean updateNote(int id, NoteEntity note) {
        deleteNote(id);
        note.setId(id); // на какой-то "всякий случай"
        cache.add(note);
        return true;
    }
}
