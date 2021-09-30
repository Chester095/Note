package com.geekbrains.note.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.note.R;
import com.geekbrains.note.domain.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteVh> {
    private List<NoteEntity> data = new ArrayList<>();
    private OnItemClickListener clickListener = null;

    public void setData(List<NoteEntity> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public NoteVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteVh(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVh holder, int position) {
        holder.bind(getItem(position));
    }

    private NoteEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(NoteEntity item);
    }
}
