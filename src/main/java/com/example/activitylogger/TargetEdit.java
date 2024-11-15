package com.example.activitylogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TargetEdit extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText editTargetDistance, editWishName;
    private TextView editWishImage;
    private ImageView imageView;
    private Button saveButton, backButton;
    private Uri imageUri;

    private TargetDatabaseManager targetDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_edit);

        targetDatabaseManager = new TargetDatabaseManager(this);

        editTargetDistance = findViewById(R.id.editTargetDistance);
        editWishName = findViewById(R.id.editWishName);
        editWishImage = findViewById(R.id.editWishImage);
        saveButton = findViewById(R.id.saveButton);
        imageView = findViewById(R.id.imageView);
        backButton = findViewById(R.id.backButton);

        editWishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenImageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTargetRecord();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkPermissionAndOpenImageChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11以降
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE);
            } else {
                openImageChooser();
            }
        } else {
            // Android 10以前
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                openImageChooser();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "パーミッションが必要です", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            // 画像をストレージに保存し、そのパスを取得
            String imagePath = saveImageToStorage(imageUri);
            if (imagePath != null) {
                editWishImage.setText(imagePath);
            } else {
                Toast.makeText(this, "画像の保存に失敗しました", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private String saveImageToStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File storageDir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            } else {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            }

            if (!storageDir.exists() && !storageDir.mkdirs()) {
                return null;
            }

            String fileName = "image_" + System.currentTimeMillis() + ".png";
            File file = new File(storageDir, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.flush();
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void saveTargetRecord() {
        String target = editTargetDistance.getText().toString().trim();
        String wishName = editWishName.getText().toString().trim();
        String wishImage = editWishImage.getText().toString().trim();

        if (target.isEmpty() || wishName.isEmpty() || wishImage.isEmpty()) {
            Toast.makeText(this, "すべてのフィールドを入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        TargetRecord newRecord = new TargetRecord(
                -1,
                target,
                false,
                wishName,
                wishImage
        );

        targetDatabaseManager.addTargetRecord(newRecord);
        Toast.makeText(this, "ターゲットが保存されました", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TargetEdit.this, MainActivity.class);
        startActivity(intent);
    }
}