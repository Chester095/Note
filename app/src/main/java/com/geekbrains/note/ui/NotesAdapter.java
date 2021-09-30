package com.geekbrains.note.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.geekbrains.note.domain.NoteEntity;

import java.util.ArrayList;
import java.util.List;

/*** Адаптер для получения данныч и передачи в конкретную вьюшку
 * Подробное описание 01:49:10
 * для работы с ViewHolder
 * напоминание: recyclerView состоит из сам recyclerView(вьюшка которая рисуется),
 *                                      ViewHolder(вьюшка конкретного элемента)
 *                                      и адаптер (превращает сущность данных в вьюшку)
 */
public class NotesAdapter extends RecyclerView.Adapter<NoteVh> {
    private List<NoteEntity> data = new ArrayList<>();
    private OnItemClickListener clickListener = null;

    /*** Получаем сюда данные для работы с ними
     * из активити. То что будем отрисовывать.
     * @param data
     */
    public void setData(List<NoteEntity> data) {
        this.data = data;
        notifyDataSetChanged(); // уведомляем адаптер, что изменились данные
    }

    /*** Создание конкретного ViewHolder
     *
     * @param parent - в какой вьюшке "раздувать" (из xml создавать реальные представления)
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public NoteVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteVh(parent, clickListener);
    }

    /*** Привязка
     *
     * @param holder - сам ViewHolder
     * @param position - "засунь туда данные"
     */
    @Override
    public void onBindViewHolder(@NonNull NoteVh holder, int position) {
        holder.bind(getItem(position));
    }

    /*** Возращает позицию элемента
     * сейчас из data. сделать из репозитория
     * @param position
     * @return
     */
    private NoteEntity getItem(int position) {
        return data.get(position);
    }

    /*** Кол-во всех элементов
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size(); // возращаем размер
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(NoteEntity item);
    }
}
