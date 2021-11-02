package com.geekbrains.note.ui

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.geekbrains.note.R
import com.geekbrains.note.domain.NoteEntity

class NoteEditFragment : Fragment() {
    private var titleEditText: EditText? = null
    private var descriptionEditText: EditText? = null
    private var saveButton: Button? = null
    private var deleteButton: Button? = null
    private var noteId = 0
    private lateinit var noteEntityTemp: NoteEntity
    private lateinit var noteFragmentManager: FragmentManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  onCreateView.   inflater = $inflater   savedInstanceState = $savedInstanceState")
        return inflater.inflate(R.layout.fragment_note_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  onViewCreated.   notesRepo = ")
        noteFragmentManager = requireActivity().supportFragmentManager
        initNoteEdit()
        fillNoteData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun fillNoteData() {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  fillNoteData")
        noteFragmentManager.setFragmentResultListener(IN_DATA_KEY, this, { _: String?, result: Bundle ->
            if (result.getParcelable<Parcelable?>(IN_NOTE_ENTITY_KEY) != null) {
                noteEntityTemp = result.getParcelable(IN_NOTE_ENTITY_KEY)!!
                noteId = noteEntityTemp.id
                titleEditText?.setText(noteEntityTemp.title)
                descriptionEditText?.setText(noteEntityTemp.description)
            }
        })
    }

    private fun onClickSaveButton() {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  onClickSaveButton")
        if (titleEditText?.text.toString() != "") {
            val noteEntity = NoteEntity(noteId,
                    titleEditText?.text.toString(),
                    descriptionEditText?.text.toString()
            )
            val result = Bundle()
            result.putParcelable(NOTE_ENTITY_KEY, noteEntity)
            result.putInt(TYPE_OPERATION_KEY, CREATE_NOTE)
            noteFragmentManager.setFragmentResult(BACK_DATA_KEY, result)
        }
        closeNoteEditFragment()
    }

    private fun onClickDeleteButton() {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  onClickDeleteButton")
        val result = Bundle()
        val noteEntity = NoteEntity(noteId,
                titleEditText!!.text.toString(),
                descriptionEditText!!.text.toString()
        )
        result.putParcelable(NOTE_ENTITY_KEY, noteEntity)
        result.putInt(TYPE_OPERATION_KEY, DELETE_NOTE)
        noteFragmentManager.setFragmentResult(BACK_DATA_KEY, result)
        closeNoteEditFragment()
    }

    private fun closeNoteEditFragment() {
        noteFragmentManager.popBackStack()
    }

    private fun initNoteEdit() {
        Log.d(StartActivity.LOG_TAG, "NoteEditFragment  initNoteEdit")
        titleEditText = view?.findViewById(R.id.title_edit_text)
        descriptionEditText = view?.findViewById(R.id.description_edit_text)
        saveButton = view?.findViewById(R.id.save_button)
        deleteButton = view?.findViewById(R.id.delete_button)
        saveButton?.setOnClickListener { onClickSaveButton() }
        deleteButton?.setOnClickListener { onClickDeleteButton() }
    }

    companion object {
        const val IN_DATA_KEY = "IN_DATA_KEY"
        const val BACK_DATA_KEY = "BACK_DATA_KEY"
        const val IN_NOTE_ENTITY_KEY = "IN_NOTE_ENTITY_KEY"
        const val NOTE_ENTITY_KEY = "NOTE_ENTITY_KEY"
        const val TYPE_OPERATION_KEY = "TYPE_OPERATION_KEY"
        const val CREATE_NOTE = 1
        const val DELETE_NOTE = 2
    }
}