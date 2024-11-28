package com.polines.notesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Кнопка для перехода на экран создания новой заметки
        findViewById(R.id.addNoteButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent);
        });

        // Кнопка для перехода на экран списка всех заметок
        findViewById(R.id.viewNotesButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
            startActivity(intent);
        });
    }
}
