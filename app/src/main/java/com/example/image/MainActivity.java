package com.example.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.imageprocessinglibrary.CropOverlayView;
import com.example.imageprocessinglibrary.ImageProcessor;


public class MainActivity extends AppCompatActivity {


    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private ImageView imageView;
    private Uri imageUri;
    private float currentRotation = 0f;
    private float brightness = 1f;
    private Bitmap originalBitmap;
    private Bitmap currentBitmap;
    private Bitmap tempCurrent;
    private CropOverlayView cropOverlayView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        androidx.appcompat.widget.AppCompatButton selectImageButton = findViewById(R.id.select_image_button);
        androidx.appcompat.widget.AppCompatButton rotateLeftButton = findViewById(R.id.rotate_left_button);
        androidx.appcompat.widget.AppCompatButton rotateRightButton = findViewById(R.id.rotate_right_button);
        SeekBar brightnessSeekBar = findViewById(R.id.brightness_seekbar);
        SeekBar blurSeekBar = findViewById(R.id.blur_seekbar);
        androidx.appcompat.widget.AppCompatButton sharpenButton = findViewById(R.id.sharpen_button);
        androidx.appcompat.widget.AppCompatButton filterBWButton = findViewById(R.id.filter_bw_button);
        androidx.appcompat.widget.AppCompatButton filterSepiaButton = findViewById(R.id.filter_sepia_button);
        androidx.appcompat.widget.AppCompatButton filterNegativeButton = findViewById(R.id.filter_negative_button);
        androidx.appcompat.widget.AppCompatButton filterVintageButton = findViewById(R.id.filter_vintage_button);

        cropOverlayView = findViewById(R.id.crop_overlay);
        androidx.appcompat.widget.AppCompatButton cropButton = findViewById(R.id.crop_button);
        androidx.appcompat.widget.AppCompatButton doneButton = findViewById(R.id.done_button);
        cropButton.setOnClickListener(view -> cropAndDisplayImage());
        doneButton.setOnClickListener(view -> {
            currentBitmap = ImageProcessor.finishCropping(MainActivity.this, currentBitmap, cropOverlayView, imageView);
        });

        selectImageButton.setOnClickListener(view -> openGallery());

        rotateLeftButton.setOnClickListener(view -> ImageProcessor.rotateImage(imageView, -90f));
        rotateRightButton.setOnClickListener(view -> ImageProcessor.rotateImage(imageView, 90f));

        brightnessSeekBar.setMax(200);
        brightnessSeekBar.setProgress(100);

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress / 100f;
                if (currentBitmap != null) {
                    currentBitmap = ImageProcessor.adjustBrightness(tempCurrent, brightness);
                    imageView.setImageBitmap(currentBitmap);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        blurSeekBar.setMax(25);
        blurSeekBar.setProgress(0);
        blurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                applyBlur(progress);
                currentBitmap = ImageProcessor.applyBlur(MainActivity.this, originalBitmap, progress);
                imageView.setImageBitmap(currentBitmap);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sharpenButton.setOnClickListener(view -> applySharpen());


        filterBWButton.setOnClickListener(view -> applyFilterAndDisplay("BLACK_WHITE"));
        filterSepiaButton.setOnClickListener(view -> applyFilterAndDisplay("SEPIA"));
        filterNegativeButton.setOnClickListener(view -> applyFilterAndDisplay("NEGATIVE"));
        filterVintageButton.setOnClickListener(view -> applyFilterAndDisplay("VINTAGE"));
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void cropAndDisplayImage() {
        ImageProcessor.cropImage(MainActivity.this, tempCurrent, cropOverlayView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                currentBitmap = originalBitmap;
                tempCurrent = originalBitmap;
                imageView.setImageBitmap(originalBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void applySharpen() {
        currentBitmap = ImageProcessor.imageSharpening(MainActivity.this, currentBitmap);
        imageView.setImageBitmap(currentBitmap);
    }


    private void applyFilterAndDisplay(String filterType) {
        currentBitmap = ImageProcessor.applyFilter(originalBitmap, currentBitmap, filterType);
        imageView.setImageBitmap(currentBitmap);
    }


}