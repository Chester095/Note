package com.geekbrains.note.io;

import com.geekbrains.note.domain.NoteEntity;
import com.geekbrains.note.ui.NotesListFragment;


public class IoAdapter extends NotesListFragment{



    public String saveToFile(int id, String title, String description) {
        return id + "\t|\t" + title + "\t|\t" + description + "\t|\r\n";
    }

    public void readFromFile(String res) {
        int firstPosition = 0;
        int id;
        String title;
        String description;
        while (firstPosition < res.length()) {
            id = Integer.valueOf(res.substring(firstPosition, res.indexOf("\t|\t", firstPosition)));
            firstPosition = res.indexOf("\t|\t", firstPosition) + 3;
            title = res.substring(firstPosition, res.indexOf("\t|\t", firstPosition));
            firstPosition = res.indexOf("\t|\t", firstPosition) + 3;
            description = res.substring(firstPosition, res.indexOf("\t|\r\n", firstPosition));
            firstPosition = res.indexOf("\t|\r\n", firstPosition) + 4;
            notesRepo.getNotesRepo().getNotes().add(new NoteEntity(id, title, description));
        }
    }
}
