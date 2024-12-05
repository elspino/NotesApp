package com.polines.notesapp;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.polines.notesapp.adapter.NoteAdapter;
import com.polines.notesapp.database.Note;
import com.polines.notesapp.database.NoteDatabase;

import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadNotes();
    }

    private void loadNotes() {
        new Thread(() -> {
            List<Note> notes = NoteDatabase.getInstance(this).noteDao().getAllNotes();
            runOnUiThread(() -> {
                if (notes != null && !notes.isEmpty()) {
                    noteAdapter = new NoteAdapter(notes);
                    recyclerView.setAdapter(noteAdapter);
                }
            });
        }).start();
    }

}

