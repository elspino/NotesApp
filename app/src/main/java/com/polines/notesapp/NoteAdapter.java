package com.polines.notesapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polines.notesapp.database.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView textTextView;
        private final ImageView photoImageView;
        private final TextView weatherTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTextView = itemView.findViewById(R.id.textTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            weatherTextView = itemView.findViewById(R.id.weatherTextView);
        }

        public void bind(Note note) {
            // Устанавливаем текст заметки
            textTextView.setText(note.getText());

            // Устанавливаем фото, если оно есть
            if (note.getPhotoUri() != null && !note.getPhotoUri().isEmpty()) {
                photoImageView.setImageURI(Uri.parse(note.getPhotoUri()));
            } else {
                photoImageView.setImageResource(R.drawable.placeholder);  // Можно добавить изображение-заполнитель
            }

            // Устанавливаем информацию о погоде
            weatherTextView.setText(note.getWeatherInfo());
        }
    }
}
