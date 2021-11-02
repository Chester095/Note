package com.geekbrains.note.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import com.geekbrains.note.io.IoAdapter
import com.geekbrains.note.io.SaveFile
import com.geekbrains.note.domain.NotesApp
import com.geekbrains.note.R
import com.geekbrains.note.domain.NoteEntity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.geekbrains.note.impl.NotesRepoImpl

class NotesListFragment : Fragment() {
    private lateinit var notesFragmentManager: FragmentManager
    private val adapter = NotesAdapter() // сущность, которая "мапит" (отображает) значения. Превращает сущности во вьюшки.
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initNoteRepoFromMyApp()
        if (app.appNotesRepo.notes.isEmpty()) {
            IoAdapter().readFromFile(SaveFile.readFromFile(requireActivity().applicationContext))
        }
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private fun initNoteRepoFromMyApp() {
        notesRepo = app.appNotesRepo
    }

    val app: NotesApp
        get() = requireActivity().application as NotesApp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(StartActivity.LOG_TAG, "onCreateView.   savedInstanceState = $savedInstanceState")
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onResume() {
        Log.d(StartActivity.LOG_TAG, "onResume.")
        notesFragmentManager = requireActivity().supportFragmentManager
        initToolbar()
        initRecycler()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(StartActivity.LOG_TAG, "onViewCreated.   savedInstanceState = $savedInstanceState")
        notesFragmentManager = requireActivity().supportFragmentManager
        initToolbar()
        initRecycler()
        dataFromNoteEditFragment()
        super.onViewCreated(view, savedInstanceState)
    }

    /*** Чтобы наш активити узнал о существовании меню.
     * Создание меню.
     * Инфлейтор заходит в notes_list_menu, пройдётся по ней
     * и для каждой создаст пункт меню и добавит в menu
     * @param menu
     * @param inflater
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /***Реакция на нажатие кнопки меню.
     * У нас есть элемент на который нажали. Проверяем тот ли это элемент.
     * И выполняем openNoteScreen.
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_note_menu) {
            openNoteScreen(null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /*** Запуск NoteEditFragment активити
     *
     * @param item
     */
    private fun openNoteScreen(item: NoteEntity?) {
        val container: Int = if (!checkOrientation()) {
            R.id.fragment_container_main
        } else {
            R.id.fragment_container_note_edit
        }
        Log.d(StartActivity.LOG_TAG, "NotesListFragment  openNoteScreen.$container")
        val result = Bundle()
        result.putParcelable(NoteEditFragment.IN_NOTE_ENTITY_KEY, item)
        notesFragmentManager.setFragmentResult(NoteEditFragment.IN_DATA_KEY, result)
        notesFragmentManager
                .beginTransaction()
                .replace(container, NoteEditFragment())
                .addToBackStack(null)
                .commit()
    }

    private fun checkOrientation(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun dataFromNoteEditFragment() {
        Log.d(StartActivity.LOG_TAG, "NotesListFragment  dataFromNoteEditFragment.")
        notesFragmentManager.setFragmentResultListener(NoteEditFragment.BACK_DATA_KEY, this, { _, result ->
            val noteEntity: NoteEntity? = result.getParcelable(NoteEditFragment.NOTE_ENTITY_KEY)
            val operationType = result.getInt(NoteEditFragment.TYPE_OPERATION_KEY)
            if (operationType == 1) saveNoteEntity(noteEntity) else if (operationType == 2) deleteNoteEntity(noteEntity)
        })
    }

    private fun saveNoteEntity(noteEntity: NoteEntity?) {
        if (noteEntity!!.id == 0) {
            app.appNotesRepo.createNote(noteEntity)
            SaveFile.writeToFile(IoAdapter().saveToFile(noteEntity.id, noteEntity.title, noteEntity.description), activity?.applicationContext, true)
        } else {
            app.appNotesRepo.updateNote(noteEntity.id, noteEntity)
            SaveFile.writeToFile(SaveFile.updateFile(), requireActivity().applicationContext, false)
        }
    }

    private fun deleteNoteEntity(noteEntity: NoteEntity?) {
        app.appNotesRepo.deleteNote(noteEntity!!.id)
        SaveFile.writeToFile(SaveFile.updateFile(), requireActivity().applicationContext, false)
    }

    /*** Инициализация recyclerView
     * recyclerView состоит из сам recyclerView, адаптер и вьюшки
     */
    private fun initRecycler() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // способ расположение заметок в recyclerView друг относительно друга (вертикальный список)
        recyclerView.adapter = adapter // определяем адаптер
        adapter.setOnItemClickListener { item: NoteEntity -> onItemClick(item) } // слушатель на нажатие говорит какой метод дальше использовать
        adapter.setOnItemClickListenerPopUpMenu { menuItem: MenuItem, noteEntity: NoteEntity? -> onPopupButtonClick(menuItem, noteEntity) }
        adapter.setData(app.appNotesRepo.notes) // передаём данные из репозитория в адаптер
        Log.d(StartActivity.LOG_TAG, "initRecycler.   notesRepo = " + app.appNotesRepo)
    }

    private fun onPopupButtonClick(menuItem: MenuItem, noteEntity: NoteEntity?) {
        when (menuItem.itemId) {
            R.id.popup_menu_item_delete -> {
                deleteNoteEntity(noteEntity)
                initRecycler()
            }
            R.id.popup_menu_item_duplicate -> Toast.makeText(context, "Дублирование заметки", Toast.LENGTH_SHORT).show()
            R.id.popup_menu_item_fil_repo -> {
                fillRepoByTestValues()
                Toast.makeText(context, "Данные записаны в файл. Перезапустите програму", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*** Инициализация Toolbar
     *
     */
    private fun initToolbar() {
        val toolbar: Toolbar = requireView().findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
    }

    /*** Реакция на нажатие элемента списка
     *
     * @param item - конкретная заметка, на которую нажали
     */
    private fun onItemClick(item: NoteEntity) {
        openNoteScreen(item)
    }

    /*** Временное наполнение репозитория заметками
     * создаём и тут же записываем в репозиторий
     */
    private fun fillRepoByTestValues() {
        SaveFile.writeToFile(IoAdapter().saveToFile(0, "Заметка 1", "Октябрь уж наступил — уж роща отряхает"), requireActivity().applicationContext, false)
        SaveFile.writeToFile(IoAdapter().saveToFile(1, "Заметка 2", "Последние листы с нагих своих ветвей;"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(2, "Заметка 3", "Дохнул осенний хлад — дорога промерзает."), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(3, "Заметка 4", "Журча еще бежит за мельницу ручей,"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(4, "Заметка 5", "Но пруд уже застыл; сосед мой поспешает"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(5, "Заметка 6", "В отъезжие поля с охотою своей,"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(6, "Заметка 7", "И страждут озими от бешеной забавы,"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(7, "Заметка 8", "И будит лай собак уснувшие дубравы."), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(8, "Заметка 9", "Теперь моя пора: я не люблю весны;"), requireActivity().applicationContext, true)
        SaveFile.writeToFile(IoAdapter().saveToFile(9, "Заметка 10", "Скучна мне оттепель; вонь, грязь — весной я болен;"), requireActivity().applicationContext, true)
        SaveFile.readFromFile(requireActivity().applicationContext)
    }

    companion object {
        @JvmField
        var notesRepo: NotesRepoImpl? = null
    }
}