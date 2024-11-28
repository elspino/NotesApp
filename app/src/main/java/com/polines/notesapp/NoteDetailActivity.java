package com.polines.notesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText noteText;
    private ImageView photoView;
    private TextView weatherText;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        noteText = findViewById(R.id.noteText);
        photoView = findViewById(R.id.photoView);
        weatherText = findViewById(R.id.weatherText);

        findViewById(R.id.takePhotoButton).setOnClickListener(v -> openCamera());

        findViewById(R.id.getWeatherButton).setOnClickListener(v -> getWeather());

        findViewById(R.id.saveNoteButton).setOnClickListener(v -> saveNote());
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 100);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            photoUri = data.getData();
            photoView.setImageURI(photoUri);
        }
    }

    private void getWeather() {
        // Вызов API погоды

    }

    private void saveNote() {
        String text = noteText.getText().toString();
    }
}
