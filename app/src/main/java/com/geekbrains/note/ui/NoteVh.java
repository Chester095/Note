package com.geekbrains.note.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

public class NoteVh extends RecyclerView.ViewHolder {

    private final TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    private final TextView descriptionTextView = itemView.findViewById(R.id.description_text_view);
    private NoteEntity note;


    public NoteVh(@NonNull ViewGroup parent, NotesAdapter.OnItemClickListener clickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
        itemView.setOnClickListener(view -> clickListener.onItemClick(note));
    }

    public void bind(NoteEntity note) {
        this.note = note;
        titleTextView.setText(note.getTitle());
        descriptionTextView.setText(note.getDescription());
    }
}
