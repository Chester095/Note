package com.geekbrains.note.ui;

import static com.geekbrains.note.ui.StartActivity.LOG_TAG;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.geekbrains.note.io.IoAdapter;
import com.geekbrains.note.io.SaveFile;

public class NotesListFragment extends Fragment {

    private FragmentManager fragmentManager;

    public static final NotesRepo notesRepo = new NotesRepoImpl();
    private final NotesAdapter adapter = new NotesAdapter(); // сущность, которая "мапит" (отображает) значения. Превращает сущности во вьюшки.


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (notesRepo.getNotes().isEmpty()) {
            new IoAdapter().readFromFile(SaveFile.readFromFile(requireActivity().getApplicationContext()));
        }
        Log.d(LOG_TAG, "onCreate.   savedInstanceState = " + savedInstanceState
                + "      notesRepo.getNotes().isEmpty() = " + notesRepo.getNotes().isEmpty());
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView.   savedInstanceState = " + savedInstanceState);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume.");
        fragmentManager = requireActivity().getSupportFragmentManager();
        initToolbar();
        initRecycler();

        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onViewCreated.   savedInstanceState = " + savedInstanceState);
        fragmentManager = requireActivity().getSupportFragmentManager();
        initToolbar();
        initRecycler();
        dataFromNoteEditFragment();
        super.onViewCreated(view, savedInstanceState);

    }


    /*** Чтобы наш активити узнал о существовании меню.
     * Создание меню.
     * Инфлейтор заходит в notes_list_menu, пройдётся по ней
     * и для каждой создаст пункт меню и добавит в menu
     * @param menu
     * @param inflater
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
        int container;
        if (!checkOrientation()) {
            container = R.id.fragment_container_main;
        } else {
            container = R.id.fragment_container_note_edit;
        }
        Log.d(LOG_TAG, "NotesListFragment  openNoteScreen." + container);
        Bundle result = new Bundle();
        result.putParcelable(NoteEditFragment.IN_NOTE_ENTITY_KEY, item);
        fragmentManager.setFragmentResult(NoteEditFragment.IN_DATA_KEY, result);
        fragmentManager
                .beginTransaction()
                .replace(container, new NoteEditFragment())
                .addToBackStack(null)
                .commit();
    }

    private boolean checkOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void dataFromNoteEditFragment() {
        Log.d(LOG_TAG, "NotesListFragment  dataFromNoteEditFragment.");
        fragmentManager.setFragmentResultListener(NoteEditFragment.BACK_DATA_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                NoteEntity noteEntity = result.getParcelable(NoteEditFragment.NOTE_ENTITY_KEY);
                int operationType = result.getInt(NoteEditFragment.TYPE_OPERATION_KEY);
                if (operationType == 1)
                    saveNoteEntity(noteEntity);
                else if (operationType == 2)
                    deleteAlertInfo(noteEntity);
            }
        });
    }


    private void saveNoteEntity(NoteEntity noteEntity) {
        if (noteEntity.getId() == 0) {
            notesRepo.createNote(noteEntity);
            SaveFile.writeToFile(new IoAdapter().saveToFile(noteEntity.getId(), noteEntity.getTitle(), noteEntity.getDescription()), getActivity().getApplicationContext(), true);
        } else {
            notesRepo.updateNote(noteEntity.getId(), noteEntity);
            SaveFile.writeToFile(SaveFile.updateFile(), requireActivity().getApplicationContext(), false);
        }
    }


    /*** Инициализация recyclerView
     * recyclerView состоит из сам recyclerView, адаптер и вьюшки
     */
    private void initRecycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // способ расположение заметок в recyclerView друг относительно друга (вертикальный список)
        recyclerView.setAdapter(adapter); // определяем адаптер
        adapter.setOnItemClickListener(NotesListFragment.this::onItemClick); // слушатель на нажатие говорит какой метод дальше использовать
        adapter.setOnItemClickListenerPopUpMenu(this::onPopupButtonClick);
        adapter.setData(notesRepo.getNotes()); // передаём данные из репозитория в адаптер
        Log.d(LOG_TAG, "initRecycler.   notesRepo = " + notesRepo);
    }

    private void deleteNoteEntity(NoteEntity noteEntity) {
        notesRepo.deleteNote(noteEntity.getId());
        SaveFile.writeToFile(SaveFile.updateFile(), requireActivity().getApplicationContext(), false);
        initRecycler();
    }


    private void deleteAlertInfo(NoteEntity noteEntity) {
        new AlertDialog.Builder(getContext())
                .setTitle("Точно хотите удалить?")
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton("Да", (dialog, which) -> {
                    deleteNoteEntity(noteEntity);
                })
                .setNegativeButton("Нет", ((dialog, which) -> Toast.makeText(getContext(), "Хорошо не будем )", Toast.LENGTH_SHORT).show()))
                .setCancelable(true)
                .show();
    }

    @SuppressLint("NonConstantResourceId")
    public void onPopupButtonClick(MenuItem menuItem, NoteEntity noteEntity) {
        switch (menuItem.getItemId()) {
            case R.id.popup_menu_item_delete:
                deleteAlertInfo(noteEntity);
                break;
            case R.id.popup_menu_item_duplicate:
                Toast.makeText(getContext(), "Дублирование заметки", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup_menu_item_fil_repo:
                fillRepoByTestValues();
                Toast.makeText(getContext(), "Данные записаны в файл. Перезапустите програму", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*** Инициализация Toolbar
     *
     */
    private void initToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
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
        SaveFile.writeToFile(new IoAdapter().saveToFile(0, "Заметка 1", "Октябрь уж наступил — уж роща отряхает"), requireActivity().getApplicationContext(), false);
        SaveFile.writeToFile(new IoAdapter().saveToFile(1, "Заметка 2", "Последние листы с нагих своих ветвей;"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(2, "Заметка 3", "Дохнул осенний хлад — дорога промерзает."), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(3, "Заметка 4", "Журча еще бежит за мельницу ручей,"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(4, "Заметка 5", "Но пруд уже застыл; сосед мой поспешает"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(5, "Заметка 6", "В отъезжие поля с охотою своей,"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(6, "Заметка 7", "И страждут озими от бешеной забавы,"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(7, "Заметка 8", "И будит лай собак уснувшие дубравы."), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(8, "Заметка 9", "Теперь моя пора: я не люблю весны;"), requireActivity().getApplicationContext(), true);
        SaveFile.writeToFile(new IoAdapter().saveToFile(9, "Заметка 10", "Скучна мне оттепель; вонь, грязь — весной я болен;"), requireActivity().getApplicationContext(), true);

        SaveFile.readFromFile(requireActivity().getApplicationContext());

    }


}