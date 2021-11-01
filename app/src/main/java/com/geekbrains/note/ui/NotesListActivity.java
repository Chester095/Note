package com.geekbrains.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;
import com.geekbrains.note.impl.NotesRepoImpl;

public class NotesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    static final String NAME_EXTRA_KEY = "NAME_EXTRA_KEY";
    static final String BACK_NAME_EXTRA_KEY = "BACK_NAME_EXTRA_KEY";
    static final int CREATE_NOTE = 1;
    static final int UPDATE_NOTE = 2;

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
    private void onItemClick(NoteEntity item) {
        openNoteScreen(item);
    }

    /*** Запуск NoteEditActivity активити
     *
     * @param item
     */
    private void openNoteScreen(@Nullable NoteEntity item) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(NAME_EXTRA_KEY, item);
        if (item == null)
            startActivityForResult(intent, CREATE_NOTE);
        else
            startActivityForResult(intent, UPDATE_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            NoteEntity noteEntity = data.getParcelableExtra(BACK_NAME_EXTRA_KEY);
            if (requestCode == CREATE_NOTE) {
                notesRepo.createNote(noteEntity);
            } else if (requestCode == UPDATE_NOTE) {
                notesRepo.updateNote(noteEntity.getId(), noteEntity);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == UPDATE_NOTE) {
                NoteEntity noteEntity = data.getParcelableExtra(BACK_NAME_EXTRA_KEY);
                notesRepo.deleteNote(noteEntity.getId());
            }
        }
        initRecycler();
    }


    /*** Временное наполнение репозитория заметками
     * создаём и тут же записываем в репозиторий
     */
    private void fillRepoByTestValues
    () {
        notesRepo.createNote(new NoteEntity("Заметка 1", "Октябрь уж наступил — уж роща отряхает"));
        notesRepo.createNote(new NoteEntity("Заметка 2", "Последние листы с нагих своих ветвей;"));
        notesRepo.createNote(new NoteEntity("Заметка 3", "Дохнул осенний хлад — дорога промерзает."));
        notesRepo.createNote(new NoteEntity("Заметка 4", "Журча еще бежит за мельницу ручей,"));
        notesRepo.createNote(new NoteEntity("Заметка 5", "Но пруд уже застыл; сосед мой поспешает"));
        notesRepo.createNote(new NoteEntity("Заметка 6", "В отъезжие поля с охотою своей,"));
        notesRepo.createNote(new NoteEntity("Заметка 7", "И страждут озими от бешеной забавы,"));
        notesRepo.createNote(new NoteEntity("Заметка 8", "И будит лай собак уснувшие дубравы."));
        notesRepo.createNote(new NoteEntity("Заметка 9", "Теперь моя пора: я не люблю весны;"));
        notesRepo.createNote(new NoteEntity("Заметка 10", "Скучна мне оттепель; вонь, грязь — весной я болен;"));
    }
}