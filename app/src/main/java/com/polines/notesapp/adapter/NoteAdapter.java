package com.polines.notesapp.adapter;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polines.notesapp.R;
import com.polines.notesapp.database.Note;
import com.polines.notesapp.database.NoteDatabase;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> notes;

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

        holder.deleteButton.setOnClickListener(v -> new Thread(() -> {
            NoteDatabase.getInstance(holder.itemView.getContext()).noteDao().deleteById(note.getId());

            notes.remove(position);

            new Handler(Looper.getMainLooper()).post(() -> {
                notifyItemRemoved(position);
                Toast.makeText(holder.itemView.getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            });
        }).start());
    }


    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView textTextView;
        private final ImageView photoImageView;
        private final TextView weatherTextView;
        private final Button deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTextView = itemView.findViewById(R.id.textTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            weatherTextView = itemView.findViewById(R.id.weatherTextView);
            deleteButton = itemView.findViewById(R.id.deleteNoteButtonInList);
        }

        public void bind(Note note) {
            textTextView.setText(note.getText());

            if (note.getPhotoUri() != null && !note.getPhotoUri().isEmpty()) {
                photoImageView.setImageURI(Uri.parse(note.getPhotoUri()));
            } else {
                photoImageView.setImageResource(R.drawable.placeholder);
            }

            weatherTextView.setText(note.getWeatherInfo());
        }
    }
}
