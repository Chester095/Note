package com.geekbrains.note.ui;

import static com.geekbrains.note.ui.NotesListActivity.BACK_NAME_EXTRA_KEY;
import static com.geekbrains.note.ui.NotesListActivity.NAME_EXTRA_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

/***
 *
 */
public class NoteEditActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button deleteButton;
    private int id;
    private NoteEntity noteEntity1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initNoteEdit();


        noteEntity1 = getIntent().getParcelableExtra(NAME_EXTRA_KEY);
        if (noteEntity1 != null) {
            id = noteEntity1.getId();
            titleEditText.setText(noteEntity1.getTitle());
            descriptionEditText.setText(noteEntity1.getDescription());
        }

        // создаём новый Entity и что с ней должны сделать
        saveButton.setOnClickListener(this::onClickSaveButton);
        deleteButton.setOnClickListener(this::onClickDeleteButton);
    }

    private void initNoteEdit() {
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);
    }

    private void onClickSaveButton(View view) {
        if (!titleEditText.getText().toString().equals("")) {
            NoteEntity noteEntity = new NoteEntity(id,
                    titleEditText.getText().toString(),
                    descriptionEditText.getText().toString()
            );
            Intent data = new Intent();
            data.putExtra(BACK_NAME_EXTRA_KEY, noteEntity);
            setResult(RESULT_OK, data);
            finish();
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
            setResult(RESULT_CANCELED, data);
        }
        finish();
    }
}