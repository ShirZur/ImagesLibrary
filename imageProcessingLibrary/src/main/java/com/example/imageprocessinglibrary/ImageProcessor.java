package com.example.imageprocessinglibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageProcessor {

    private static float currentRotation = 0f;

    public static Bitmap adjustBrightness(Bitmap bitmap, float brightness) {
        if (bitmap != null) {
            Bitmap adjustedBitmap = bitmap.copy(bitmap.getConfig(), true);

            ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                    brightness, 0, 0, 0, 0,
                    0, brightness, 0, 0, 0,
                    0, 0, brightness, 0, 0,
                    0, 0, 0, 1, 0
            });

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

            Canvas canvas = new Canvas(adjustedBitmap);
            canvas.drawBitmap(adjustedBitmap, 0, 0, paint);

            return adjustedBitmap;
        }
        return bitmap;
    }
    public static Bitmap applyBlur(Context context,Bitmap bitmap,  int radius) {
        if (bitmap != null && radius > 0) {
            Bitmap blurredBitmap = bitmap.copy(bitmap.getConfig(), true);
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, input.getElement());
            script.setRadius(Math.min(Math.max(radius, 0.1f), 25f));
            script.setInput(input);
            script.forEach(output);
            output.copyTo(blurredBitmap);
            rs.destroy();
            return blurredBitmap;
        }
        return bitmap;
    }

    public static Bitmap imageSharpening(Context context, Bitmap bitmap){
        if (bitmap != null) {
            Bitmap sharpenedBitmap = bitmap.copy(bitmap.getConfig(), true);
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, sharpenedBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());
            float[] sharpnessMatrix = {
                    0, -1, 0,
                    -1, 5, -1,
                    0, -1, 0
            };
            ScriptIntrinsicConvolve3x3 script = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
            script.setInput(input);
            script.setCoefficients(sharpnessMatrix);
            script.forEach(output);
            output.copyTo(sharpenedBitmap);

            rs.destroy();
            return sharpenedBitmap;
        }
        return bitmap;
    }

    public static Bitmap applyFilter(Bitmap originalBitmap, Bitmap currentBitmap, String filterType) {
        if (originalBitmap != null && currentBitmap != null) {
            Bitmap resetBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);

            ColorMatrix colorMatrix = new ColorMatrix();

            switch (filterType) {
                case "BLACK_WHITE":
                    colorMatrix.setSaturation(0);
                    break;
                case "SEPIA":
                    colorMatrix.setScale(1, 0.95f, 0.82f, 1);
                    break;
                case "NEGATIVE":
                    float[] negativeArray = {
                            -1.0f, 0, 0, 0, 255,
                            0, -1.0f, 0, 0, 255,
                            0, 0, -1.0f, 0, 255,
                            0, 0, 0, 1.0f, 0
                    };
                    colorMatrix.set(negativeArray);
                    break;
                case "VINTAGE":
                    colorMatrix.set(new float[]{
                            0.2f, 0.5f, 0.1f, 0, 0,
                            0.2f, 0.5f, 0.1f, 0, 0,
                            0.2f, 0.5f, 0.1f, 0, 0,
                            0, 0, 0, 1, 0
                    });
                    break;
            }

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

            return applyPaintToBitmap(resetBitmap, paint);
        }
        return currentBitmap;
    }

    private static Bitmap applyPaintToBitmap(Bitmap bitmap, Paint paint) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return resultBitmap;
    }

    public static void rotateImage(ImageView imageView, float degree) {
        currentRotation = (currentRotation + degree) % 360;
        imageView.setRotation(currentRotation);
    }

    public static void cropImage(Context context, Bitmap originalBitmap, CropOverlayView cropOverlayView) {
        if (originalBitmap != null) {
            cropOverlayView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(context, "No image to crop", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap finishCropping(Context context, Bitmap currentBitmap, CropOverlayView cropOverlayView, ImageView imageView) {
        if (currentBitmap != null && cropOverlayView != null) {
            Rect cropRect = cropOverlayView.getCropRect();

            // וידוא שה-cropRect לא חורג מגבולות ה-Bitmap
            if (cropRect.left < 0) cropRect.left = 0;
            if (cropRect.top < 0) cropRect.top = 0;
            if (cropRect.right > currentBitmap.getWidth()) cropRect.right = currentBitmap.getWidth();
            if (cropRect.bottom > currentBitmap.getHeight()) cropRect.bottom = currentBitmap.getHeight();

            if (cropRect.width() > 0 && cropRect.height() > 0) {
                Bitmap croppedBitmap = Bitmap.createBitmap(currentBitmap, cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
                imageView.setImageBitmap(croppedBitmap);
                cropOverlayView.setVisibility(View.GONE);
                return croppedBitmap;
            } else {
                Toast.makeText(context, "Invalid crop area", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No image to crop or CropOverlayView is missing", Toast.LENGTH_SHORT).show();
        }
        return currentBitmap;
    }







}
