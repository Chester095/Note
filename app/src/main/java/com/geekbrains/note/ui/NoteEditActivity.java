package com.geekbrains.note.ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.geekbrains.note.ui.NotesListFragment.BACK_NAME_EXTRA_KEY;
import static com.geekbrains.note.ui.NotesListFragment.NAME_EXTRA_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

/***
 *
 */
public class NoteEditActivity extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button deleteButton;
    private int id;
    private NoteEntity noteEntity1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initNoteEdit();

        noteEntity1 = getActivity().getIntent().getParcelableExtra(NAME_EXTRA_KEY);
        if (noteEntity1 != null) {
            id = noteEntity1.getId();
            titleEditText.setText(noteEntity1.getTitle());
            descriptionEditText.setText(noteEntity1.getDescription());
        }

        // создаём новый Entity и что с ней должны сделать
        saveButton.setOnClickListener(this::onClickSaveButton);
        deleteButton.setOnClickListener(this::onClickDeleteButton);
        super.onViewCreated(view, savedInstanceState);
    }


    private void initNoteEdit() {
        titleEditText = getView().findViewById(R.id.title_edit_text);
        descriptionEditText = getView().findViewById(R.id.description_edit_text);
        saveButton = getView().findViewById(R.id.save_button);
        deleteButton = getView().findViewById(R.id.delete_button);
    }

    private void onClickSaveButton(View view) {
        if (!titleEditText.getText().toString().equals("")) {
            NoteEntity noteEntity = new NoteEntity(id,
                    titleEditText.getText().toString(),
                    descriptionEditText.getText().toString()
            );
            Intent data = new Intent();
            data.putExtra(BACK_NAME_EXTRA_KEY, noteEntity);
            getActivity().setResult(RESULT_OK, data);
            getActivity().finish();
        }
    }

    private void onClickDeleteButton(View view) {
        if (noteEntity1 != null) {
            NoteEntity noteEntity = new NoteEntity(id,
                    titleEditText.getText().toString(),
                    descriptionEditText.getText().toString()
            );
            Intent data = new Intent();
            data.putExtra(BACK_NAME_EXTRA_KEY, noteEntity);
            getActivity().setResult(RESULT_CANCELED, data);
        }
        getActivity().finish();
    }
}