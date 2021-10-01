package com.geekbrains.note.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;
import com.geekbrains.note.impl.NotesRepoImpl;

import java.io.Serializable;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    static final String NAME_EXTRA_KEY = "NAME_EXTRA_KEY";
    private NotesRepo notesRepo = new NotesRepoImpl();
    private NotesAdapter adapter = new NotesAdapter(); // сущность, которая "мапит" (отображает) значения. Превращает сущности во вьюшки.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        fillRepoByTestValues();
        initToolbar();
        initRecycler();
    }

    /*** Чтобы наш активити узнал о существовании меню.
     * Создание меню.
     * Инфлейтор заходит в notes_list_menu, пройдётся по ней
     * и для каждой создаст пункт меню и добавит в menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***Реакция на нажатие кнопки меню.
     * У нас есть элемент на который нажали. Проверяем тот ли это элемент.
     * И выполняем openNoteScreen.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_note_menu) {
            openNoteScreen(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*** Инициализация recyclerView
     * recyclerView состоит из сам recyclerView, адаптер и вьюшки
     */
    private void initRecycler() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // способ расположение заметок в recyclerView друг относительно друга (вертикальный список)
        recyclerView.setAdapter(adapter); // определяем адаптер
        adapter.setOnItemClickListener(this::onItemClick); // слушатель на нажатие говорит какой метод дальше использовать

        adapter.setData(notesRepo.getNotes()); // передаём данные из репозитория в адаптер
    }

    /*** Инициализация Toolbar
     *
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*** Реакция на нажатие элемента списка
     *
     * @param item - конкретная заметка, на которую нажали
     */
    private void onItemClick(NoteEntity item){
        openNoteScreen(item);
    }

    /*** Запуск NoteEditActivity активити
     *
     * @param item
     */
    private void openNoteScreen(@Nullable NoteEntity item) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(NAME_EXTRA_KEY, item);
        startActivity(intent);
    }


    /*** Временное наполнение репозитория заметками
     * создаём и тут же записываем в репозиторий
     */
    private void fillRepoByTestValues() {
        notesRepo.createNote(new NoteEntity("Заметка 1", "Заметка 1 какой-то длинный текст kjsdfl;khjsd;fklj;lkjsdf;lgkjkl;sdfjkdfочень тывалодлывоапо"));
        notesRepo.createNote(new NoteEntity("Заметка 2", "Заметка 2 какой-то длинный текст очень тывалодлывоапо"));
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