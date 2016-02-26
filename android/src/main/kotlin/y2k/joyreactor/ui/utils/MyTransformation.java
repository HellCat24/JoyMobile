package y2k.joyreactor.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class MyTransformation extends BitmapTransformation {

    public MyTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap original, int width, int height) {

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        // If no matching Bitmap is in the pool, get will return null, so we should allocate.
        if (result == null) {
            float scaleWidth = ((float) 1080) / width;
            float newHeight = 1080 * height / width;
            float scaleHeight = newHeight / height;
            result = Bitmap.createBitmap((int) (width * scaleWidth), (int) (height * scaleHeight), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAlpha(128);
        canvas.drawBitmap(original, 0, 0, paint);
        return result;
    }

    @Override
    public String getId() {
        // Return some id that uniquely identifies your transformation.
        return "com.example.myapp.MyTransformation";
    }
}