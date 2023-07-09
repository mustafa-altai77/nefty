package org.sudadev.nefty.common;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.sudadev.nefty.BuildConfig;
import org.sudadev.nefty.R;
import org.sudadev.nefty.ui.custom.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageChooserDialog {
    private int GALLERY_REQUEST_CODE = 9878;
    private int CAMERA_REQUEST_CODE = 2545;
    private int FILE_PICKER_REQUEST_CODE = 1874;
    private final int ACCESS_PERMISSION_REQUEST_CODE = 8754;

    private Context context;
    private String mCurrentPhotoPath;
    private boolean isFromFragment = false;
    private Fragment fragment;

    public ImageChooserDialog(Context context) {
        this.context = context;
    }

    public ImageChooserDialog(Fragment fragment, Context context, boolean isFromFragment) {
        this.context = context;
        this.fragment = fragment;
        this.isFromFragment = isFromFragment;
    }

    public File getFileFromUri(Uri uri) throws IOException {
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    String result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("MSG", "cursor.getString: " +  result);
                    return new File(uri.getPath() + "/" +  result);
                }
            } finally {
                cursor.close();
            }
        }
        else {
            Log.d("MSG", uri.getPath());
            File file = new File(uri.getPath());
            return file;
        }

        return null;
    }

    public File getImageFromUri(Uri uri) throws IOException {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return new File(resizeImage(cursor.getString(idx), 400, 300));
        }
        return new File(resizeImage(uri.getPath(), 400, 300));
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void checkPermission(int layoutResourceId,
                                int galleryRequestCode, int cameraRequestCode, int fileRequestCode) {
        this.GALLERY_REQUEST_CODE = galleryRequestCode;
        this.CAMERA_REQUEST_CODE = cameraRequestCode;
        this.FILE_PICKER_REQUEST_CODE = fileRequestCode;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            showSnackbar(layoutResourceId);
            //askPermissions();

        }
        else {
            if (fileRequestCode != 0) {
                chooseFile();
            }
            else {
                //showImageSelectionDialog();
                if (isFromFragment) ImagePicker.create(fragment).start();
                else ImagePicker.create((BaseActivity) context).start();
            }
        }
    }

    private void chooseFile() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            final String[] ACCEPT_MIME_TYPES = {
                    "application/pdf",
                    "image/*"
            };

            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile.setAction(Intent.ACTION_GET_CONTENT);
            chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");

            if (isFromFragment) {
                fragment.startActivityForResult(chooseFile, FILE_PICKER_REQUEST_CODE);
            } else {
                ((Activity) context).startActivityForResult(chooseFile, FILE_PICKER_REQUEST_CODE);
            }
        } else {
            Toast.makeText(context, R.string.file_access_permission, Toast.LENGTH_LONG).show();
        }
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.chooseSource);

        builder.setPositiveButton(R.string.Galary, (dialog, which) -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (isFromFragment) {
                    fragment.startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
                } else {
                    ((Activity) context).startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
                }
                dialog.dismiss();
            } else {
                dialog.dismiss();
                Toast.makeText(context, R.string.file_access_permission, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton(R.string.Camera, (dialog, which) -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
                dialog.dismiss();
            } else {
                dialog.dismiss();
                Toast.makeText(context, R.string.no_camera_permission, Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
                    photoURI = Uri.fromFile(photoFile);
                } else {
                    photoURI = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID +".fileprovider", photoFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (isFromFragment) {
                    fragment.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                } else {
                    ((Activity) context).startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String resizeImage(String path, int reqWidth, int reqHeight) throws IOException {
        int orientation;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap photo = BitmapFactory.decodeFile(path, options);

        ExifInterface exif = new ExifInterface(path);

        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        Matrix m = new Matrix();
        if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
            m.postRotate(180);
            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), m, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            m.postRotate(90);
            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), m, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            m.postRotate(270);
            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), m, true);
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(path);
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        return path;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void showSnackbar(int layoutResourceId) {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(layoutResourceId),
                R.string.no_camera_file_permission, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.allow_permission, view -> {
            askPermissions();
        });
        snackbar.show();
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(((Activity) context),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ACCESS_PERMISSION_REQUEST_CODE);
        }
    }
}


