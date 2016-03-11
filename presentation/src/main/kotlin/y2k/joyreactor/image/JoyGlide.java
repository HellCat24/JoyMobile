package y2k.joyreactor.image;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import joy.reactor.data.enteties.Image;


/**
 * Created by Oleg on 24.02.2016.
 */
public class JoyGlide {

    private static int mDisplayWidth;

    public static void init(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mDisplayWidth = size.x;
    }

    public static void load(ImageView imageView, Image image) {
        String url = image.getUrl();

        GlideUrl glideUrl = null;

        if (url.contains("joy")) {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("Referer", "http://joyreactor.cc/")
                    .build());
        } else {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder().build());
        }

        if (image.isGif()) {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .fitCenter()
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .asBitmap()
                    .override(400, 0)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }
}
