package com.polines.notesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.polines.notesapp.database.Note;
import com.polines.notesapp.database.NoteDatabase;
import com.polines.notesapp.service.WeatherService;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText noteText;
    private ImageView photoView;
    private TextView weatherText;
    private Uri photoUri;
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;  // Добавляем этот Launcher

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Инициализация cameraResultLauncher
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            photoUri = imageUri;
                            photoView.setImageURI(imageUri);
                        }
                    }
                }
        );

        // Инициализация cameraPermissionLauncher для запроса разрешений
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();  // Если разрешение получено, открываем камеру
                    } else {
                        Toast.makeText(NoteDetailActivity.this, "Разрешение на использование камеры не получено", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Проверка наличия разрешения на использование камеры
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.takePhotoButton).setOnClickListener(v -> openCamera());  // Если разрешение есть, сразу можно использовать камеру
        } else {
            // Запрашиваем разрешение
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        noteText = findViewById(R.id.noteText);
        photoView = findViewById(R.id.photoView);
        weatherText = findViewById(R.id.weatherText);

        // Кнопка для получения погоды
        findViewById(R.id.getWeatherButton).setOnClickListener(v -> getWeather());

        // Кнопка для сохранения заметки
        findViewById(R.id.saveNoteButton).setOnClickListener(v -> saveNote());
    }

    // Открытие камеры
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraResultLauncher.launch(cameraIntent);  // Используем cameraResultLauncher для запуска камеры
        }
    }

    // Получение погоды
    private void getWeather() {
        WeatherService.getWeather(this, weatherData -> weatherText.setText(weatherData));
    }

    // Сохранение заметки
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
            finish();  // Закрытие активности после сохранения заметки
        }).start();
    }
}
