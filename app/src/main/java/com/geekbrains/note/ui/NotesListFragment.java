package com.geekbrains.note.ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.domain.NotesRepo;
import com.geekbrains.note.impl.NotesRepoImpl;

public class NotesListFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FragmentManager fragmentManager;
    private NotesRepo notesRepo = new NotesRepoImpl();
    private NotesAdapter adapter = new NotesAdapter(); // сущность, которая "мапит" (отображает) значения. Превращает сущности во вьюшки.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fragmentManager = requireActivity().getSupportFragmentManager();
        fillRepoByTestValues();
        initToolbar();
        initRecycler();
        closeNoteScreen();
        super.onViewCreated(view, savedInstanceState);

    }


    /*** Чтобы наш активити узнал о существовании меню.
     * Создание меню.
     * Инфлейтор заходит в notes_list_menu, пройдётся по ней
     * и для каждой создаст пункт меню и добавит в menu
     * @param menu
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    /*** Запуск NoteEditFragment активити
     *
     * @param item
     */
    private void openNoteScreen(@Nullable NoteEntity item) {
        Bundle result = new Bundle();
        result.putParcelable(NoteEditFragment.IN_NOTE_ENTITY_KEY, item);
        fragmentManager.setFragmentResult(NoteEditFragment.IN_DATA_KEY, result);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_main, new NoteEditFragment())
                .addToBackStack(null)
                .commit();
    }

    private void closeNoteScreen() {
        fragmentManager.setFragmentResultListener(NoteEditFragment.BACK_DATA_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                NoteEntity noteEntity = result.getParcelable(NoteEditFragment.NOTE_ENTITY_KEY);
                int operationType = result.getInt(NoteEditFragment.TYPE_OPERATION_KEY);
                if (operationType == 1) saveNoteEntity(noteEntity);
                else if (operationType == 2) deleteNoteEntity(noteEntity);
            }
        });
        initRecycler();
    }


    private void saveNoteEntity(NoteEntity noteEntity) {
        if (noteEntity.getId() == 0)
            notesRepo.createNote(noteEntity);
        else
            notesRepo.updateNote(noteEntity.getId(), noteEntity);
    }

    private void deleteNoteEntity(NoteEntity noteEntity) {
        notesRepo.deleteNote(noteEntity.getId());
    }


    /*** Инициализация recyclerView
     * recyclerView состоит из сам recyclerView, адаптер и вьюшки
     */
    private void initRecycler() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // способ расположение заметок в recyclerView друг относительно друга (вертикальный список)
        recyclerView.setAdapter(adapter); // определяем адаптер
        adapter.setOnItemClickListener(this::onItemClick); // слушатель на нажатие говорит какой метод дальше использовать
        adapter.setData(notesRepo.getNotes()); // передаём данные из репозитория в адаптер
    }

    /*** Инициализация Toolbar
     *
     */
    private void initToolbar() {
        toolbar = getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }

    /*** Реакция на нажатие элемента списка
     *
     * @param item - конкретная заметка, на которую нажали
     */
    private void onItemClick(NoteEntity item) {
        openNoteScreen(item);
    }


    /*** Временное наполнение репозитория заметками
     * создаём и тут же записываем в репозиторий
     */
    private void fillRepoByTestValues() {
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