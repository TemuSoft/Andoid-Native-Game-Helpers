package com.WormsThief.WT2306;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileAccess {
    boolean file_selected;
    String fileName;
    private final AppCompatActivity activity;
    private ImageView imageView;
    private Uri cameraImageUri;
    private File photoFile;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    public FileAccess(AppCompatActivity activity, ImageView imageView) {
        this.activity = activity;
        this.imageView = imageView;
    }

    public void showImagePickerDialog() {
        String[] options = {"Choose from Gallery", "Take Photo"};
        new AlertDialog.Builder(activity).setTitle("Select Option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        pickImageFromGallery();
                    } else {
                        requestCameraPermission();
                    }
                }).show();
    }

    private void pickImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (hasPermission(Manifest.permission.READ_MEDIA_IMAGES)) {
                openGallery();
            } else {
                requestPermission(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery();
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void requestCameraPermission() {
        if (hasPermission(Manifest.permission.CAMERA)) {
            takePhotoFromCamera();
        } else {
            requestPermission(Manifest.permission.CAMERA);
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, 101);
    }

    private void takePhotoFromCamera() {
        try {
            photoFile = createImageFile();
            cameraImageUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider", photoFile);
            takePictureLauncher.launch(cameraImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    private void saveImage(Bitmap bitmap) {
        File directory = new File(activity.getFilesDir(), "saved_images");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory, fileName + ".jpg");

        if (file.exists())
            delete_profile(fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerLaunchers(AppCompatActivity activity) {
        imagePickerLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                            bitmap = rotateBitmapIfRequired(bitmap, selectedImageUri);
                            imageView.setImageBitmap(bitmap);
                            saveImage(bitmap);
                            file_selected = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        takePictureLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.TakePicture(), result -> {
                    if (result) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(cameraImageUri));
                            bitmap = rotateBitmapIfRequired(bitmap, cameraImageUri);
                            imageView.setImageBitmap(bitmap);
                            saveImage(bitmap);
                            file_selected = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public Bitmap loadImageFromInternalStorage(String file_name) {
        File directory = new File(activity.getFilesDir(), "saved_images");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory, file_name + ".jpg");
        String path = file.getAbsolutePath();
        Bitmap bitmap = null;
        try {
            file = new File(path);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void delete_profile(String file_name) {
        File directory = new File(activity.getFilesDir(), "saved_images");
        File file = new File(directory, file_name + ".jpg");
        if (file.exists())
            file.delete();
    }


    private Bitmap rotateBitmapIfRequired(Bitmap bitmap, Uri imageUri) throws IOException {
        InputStream input = activity.getContentResolver().openInputStream(imageUri);
        ExifInterface exif = new ExifInterface(input);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        input.close();

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
