package com.geekbrains.note.listeners;

import android.view.MenuItem;

import com.geekbrains.note.domain.NoteEntity;

public interface OnItemPopUpMenuClickListener {

    void onClickItemPopUpMenu(MenuItem menuItem, NoteEntity noteEntity);

}
