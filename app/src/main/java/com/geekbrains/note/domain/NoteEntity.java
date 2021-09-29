package com.geekbrains.note.domain;

import androidx.annotation.Nullable;

public class NoteEntity {
    @Nullable
    private Integer id;
    private String title;
    private String description;

    public NoteEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Nullable
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(@Nullable Integer id){
        this.id = id;
    }
}
