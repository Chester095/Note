package com.geekbrains.note.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.Nullable;

/*** Класс с элементами заметки
 *
 */
public class NoteEntity implements Parcelable {
    @Nullable
    private Integer id;
    private String title;
    private String description;

    public NoteEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public NoteEntity(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    protected NoteEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<NoteEntity> CREATOR = new Creator<NoteEntity>() {
        @Override
        public NoteEntity createFromParcel(Parcel in) {
            return new NoteEntity(in);
        }

        @Override
        public NoteEntity[] newArray(int size) {
            return new NoteEntity[size];
        }
    };

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

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
