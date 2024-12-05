package com.polines.notesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.polines.notesapp.database.Note;
import com.polines.notesapp.database.NoteDatabase;
import com.polines.notesapp.service.WeatherService;

import java.io.File;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText noteText;
    private ImageView photoView;
    private TextView weatherText;
    private Uri photoUri;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && photoUri != null) {
                        photoView.setImageURI(photoUri);
                    }
                }
        );


        ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(NoteDetailActivity.this, "Permission to use the camera has not been received", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.takePhotoButton).setOnClickListener(v -> openCamera());
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        noteText = findViewById(R.id.noteText);
        photoView = findViewById(R.id.photoView);
        weatherText = findViewById(R.id.weatherText);

        getWeather();
        findViewById(R.id.saveNoteButton).setOnClickListener(v -> saveNote());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            photoUri = createImageFileUri();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraResultLauncher.launch(cameraIntent);
        }
    }


    private Uri createImageFileUri() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir != null && !storageDir.exists() && !storageDir.mkdirs()) {
            Log.e("NoteDetailActivity", "Failed to create directory: " + storageDir.getAbsolutePath());
            return null;
        }

        File imageFile = new File(storageDir, "photo_" + System.currentTimeMillis() + ".jpg");

        return FileProvider.getUriForFile(this,  "com.polines.notesapp.provider", imageFile);
    }



    private void getWeather() {
        WeatherService.getWeather(this, weatherData -> weatherText.setText(weatherData));
    }

    private void saveNote() {
        String text = noteText.getText().toString();
        String weather = weatherText.getText().toString();
        String photoUriString = (photoUri != null) ? photoUri.toString() : null;

        Note note = new Note();
        note.setText(text);
        note.setWeatherInfo(weather);
        note.setPhotoUri(photoUriString);

        new Thread(() -> {
            NoteDatabase.getInstance(this).noteDao().insert(note);
            finish();
        }).start();
    }

}
