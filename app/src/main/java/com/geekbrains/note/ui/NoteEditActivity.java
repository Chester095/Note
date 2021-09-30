package com.geekbrains.note.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

public class NoteEditActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initNoteEdit();

//        getIntent().get

        saveButton.setOnClickListener(view -> {
            NoteEntity noteEntity = new NoteEntity(
                    titleEditText.getText().toString(),
                    descriptionEditText.getText().toString()
            );
            setResult(RESULT_OK);
            // todo 02:14:00
        });
    }

    private void initNoteEdit() {
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        saveButton = findViewById(R.id.save_button);
    }
}