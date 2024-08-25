# Image Processing Library

This Android library provides various image processing features, including cropping, brightness adjustment, blurring, sharpening, applying filters, and rotating images. It is designed to be straightforward and easy to integrate into any Android project.

## Demonstration Video
https://github.com/user-attachments/assets/1f4d983d-d3ac-4252-ac02-fcc951a456b4


## Features

- **Cut Image**: Crop any image to a specific size or aspect ratio.
- **Brightness Adjustment**: Increase or decrease the brightness of an image.
- **Blurring**: Apply a blur effect to the image with adjustable intensity.
- **Sharpening**: Enhance the sharpness of an image.
- **Filter Application**: Apply various filters such as Black & White, Sepia, Negative, and Vintage.
- **Image Rotation**: Rotate the image by a specified degree.

- ## Installation


# Usage
Here are some examples of how to use the library in your Android project:

### 1. Adjusting Brightness
```groovy
Bitmap adjustedBitmap = ImageProcessor.adjustBrightness(originalBitmap, 1.5f);
imageView.setImageBitmap(adjustedBitmap);
```

### 2.Applying a Blur Effect
```groovy
Bitmap blurredBitmap = ImageProcessor.applyBlur(context, originalBitmap, 10);
imageView.setImageBitmap(blurredBitmap);
```

### 3.Sharpening the Image
```groovy
Bitmap sharpenedBitmap = ImageProcessor.imageSharpening(context, originalBitmap);
imageView.setImageBitmap(sharpenedBitmap);
```

### 4.Applying Filters
```groovy
Bitmap sepiaBitmap = ImageProcessor.applyFilter(originalBitmap, currentBitmap, "SEPIA");
imageView.setImageBitmap(sepiaBitmap);
```

### 5.Rotating the Image
```groovy
ImageProcessor.rotateImage(imageView, 90f);
```

### 6. Cropping the Image with 'CropOverlayView'
**To allow users to crop images, you'll need to add the CropOverlayView to your layout:**
```groovy
ImageProcessor.rotateImage(imageView, 90f);
```
**In your activity, use the following code to start cropping and finalize the crop:**
```groovy
ImageProcessor.cropImage(context, originalBitmap, cropOverlayView);
Bitmap croppedBitmap = ImageProcessor.finishCropping(context, currentBitmap, cropOverlayView, imageView);
```

# Example Project
The library includes an example project demonstrating how to use these features in a typical Android application. Simply clone this repository and open the project in Android Studio to explore the example.



