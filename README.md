# Image Processing Library

This Android library provides various image processing features, including cropping, brightness adjustment, blurring, sharpening, applying filters, and rotating images. It is designed to be straightforward and easy to integrate into any Android project.

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


