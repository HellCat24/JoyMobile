package y2k.joyreactor.ui.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.Display;

import com.squareup.picasso.Transformation;

public class ScaleTransform implements Transformation {

    private int screenWidth;

    public ScaleTransform(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        float scaleWidth = ((float) screenWidth) / width;
        float newHeight = screenWidth * height / width;
        float scaleHeight = newHeight / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(source, 0, 0, width, height - 20, matrix, false);
        if (resizedBitmap != source) {
            source.recycle();
        }
        return resizedBitmap;

    }

    @Override
    public String key() {
        return "resize";
    }
}